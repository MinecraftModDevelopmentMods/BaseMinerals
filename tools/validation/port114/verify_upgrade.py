"""Check mineral states and tagged inventories across the real Forge 1.14 upgrade."""
import argparse
from collections import Counter
from hashlib import sha256
from io import BytesIO
import json
from pathlib import Path
import struct
import zlib

import nbtlib
from nbtlib import Byte, ByteArray, Compound, Int, List, Short, String
import numpy as np

from run_forge import prepare, run

MOD = 'baseminerals:'
SECTOR = 4096
PROBE_CHUNK = (64, 64)


def digest(path):
    return sha256(path.read_bytes()).hexdigest()


def fingerprint(world):
    return {p.relative_to(world).as_posix(): digest(p) for p in sorted(world.rglob('*')) if p.is_file()}


def saved_mineral_rules(world):
    path = world / 'serverconfig/orespawn-worldgen.json'
    if not path.is_file():
        return {}
    ores = json.loads(path.read_text(encoding='utf-8'))['ores']
    return {key: value for key, value in ores.items() if value.get('block', '').startswith(MOD)}


def legacy_registry(world, registry):
    root = nbtlib.load(world / 'level.dat')
    ids = root['FML']['Registries']['minecraft:' + registry]['ids']
    return {int(entry['V']): str(entry['K']) for entry in ids}


def chunk_data(region, slot):
    offset = int.from_bytes(region[slot * 4:slot * 4 + 3], 'big') * SECTOR
    length, compression = struct.unpack_from('>IB', region, offset)
    if compression not in (1, 2):
        raise ValueError(f'Unsupported region compression {compression}')
    data = zlib.decompress(region[offset + 5:offset + 4 + length], 31 if compression == 1 else 15)
    return nbtlib.File.parse(BytesIO(data))


def nibble_array(tag):
    raw = np.frombuffer(tag.tobytes(), dtype=np.uint8)
    result = np.empty(len(raw) * 2, dtype=np.uint8)
    result[::2] = raw & 15
    result[1::2] = raw >> 4
    return result


def legacy_section(section, ids):
    blocks = np.frombuffer(section['Blocks'].tobytes(), dtype=np.uint8).astype(np.uint16)
    if 'Add' in section:
        blocks |= nibble_array(section['Add']).astype(np.uint16) << 8
    positions = np.flatnonzero(np.isin(blocks, list(ids)))
    if not len(positions):
        return
    metadata = nibble_array(section['Data'])
    for index in positions:
        block = ids[int(blocks[index])]
        properties = (('level', str(metadata[index])),) if block in (MOD + 'lithium', MOD + 'silicon') else ()
        yield int(index), block, properties


def flattened_section(section):
    palette = section.get('Palette', [])
    targets = {i: (str(entry['Name']), tuple(sorted((k, str(v)) for k, v in entry.get('Properties', {}).items())))
               for i, entry in enumerate(palette) if str(entry['Name']).startswith(MOD)}
    if not targets:
        return
    bits = max(4, (len(palette) - 1).bit_length())
    words = [int(word) & ((1 << 64) - 1) for word in section.get('BlockStates', [])]
    for index in range(4096):
        start = index * bits
        word, shift = divmod(start, 64)
        value = words[word] >> shift if words else 0
        if shift + bits > 64 and words:
            value |= words[word + 1] << (64 - shift)
        state = targets.get(value & ((1 << bits) - 1))
        if state:
            yield index, *state


def normalized_stack(stack, legacy):
    name = str(stack['id'])
    tag = stack.get('tag', Compound()).unpack()
    if name == 'forge:bucketfilled':
        fluid = tag.get('FluidName', '').removeprefix(MOD)
        if fluid not in ('lithium', 'silicon'):
            return None
        name = MOD + fluid + '_bucket'
        del tag['FluidName'], tag['Amount']
    if not name.startswith(MOD):
        return None
    display = tag.get('display', {})
    if 'Name' in display:
        value = display['Name']
        display['Name'] = {'text': value} if legacy else json.loads(value)
    return name, int(stack['Count']), tag


def census(world, ids, coverage=None, require_flattened=False):
    chunks = set()
    blocks = {}
    chests = {}
    for path in sorted(world.rglob('r.*.*.mca')):
        dimension = path.parent.parent.relative_to(world).as_posix()
        region = path.read_bytes()
        for slot in range(1024):
            if not region[slot * 4:slot * 4 + 3].strip(b'\0'):
                continue
            root = chunk_data(region, slot)
            legacy = int(root.get('DataVersion', -1)) < 1451
            level = root['Level']
            cx, cz = int(level['xPos']), int(level['zPos'])
            chunk = dimension, cx, cz
            if coverage is not None and chunk not in coverage:
                continue
            if require_flattened:
                assert int(root.get('DataVersion', -1)) >= 1976, f'Chunk did not reach 1.14.4: {chunk}'
            chunks.add(chunk)
            for section in level.get('Sections', []):
                if 'Blocks' in section:
                    assert not require_flattened, f'Numeric block array survived in {chunk}'
                    states = legacy_section(section, ids)
                else:
                    states = flattened_section(section)
                for index, name, properties in states:
                    x, y, z = index & 15, (index >> 8) + int(section['Y']) * 16, (index >> 4) & 15
                    blocks[dimension, 16 * cx + x, y, 16 * cz + z] = name, properties
            for entity in level.get('TileEntities', []):
                if 'Items' not in entity:
                    continue
                items = {int(stack['Slot']): result for stack in entity['Items']
                         if (result := normalized_stack(stack, legacy))}
                if items:
                    chests[dimension, int(entity['x']), int(entity['y']), int(entity['z'])] = items
    return chunks, blocks, chests


def signed(value):
    return value if value < 128 else value - 256


def set_nibble(array, index, value):
    shift = (index & 1) * 4
    prior = int(array[index >> 1]) & 255
    array[index >> 1] = signed((prior & ~(15 << shift)) | value << shift)


def add_fixture(world, probe_chunk=PROBE_CHUNK):
    cx, cz = probe_chunk
    region_path = world / 'region' / f'r.{cx // 32}.{cz // 32}.mca'
    slot = (cx & 31) + (cz & 31) * 32
    region = bytearray(region_path.read_bytes())
    chunk = chunk_data(region, slot)
    level = chunk['Level']
    assert int(level['xPos']) == cx and int(level['zPos']) == cz
    y = 224
    assert all(int(s['Y']) != y // 16 for s in level['Sections'])
    section = Compound(Y=Byte(y // 16), Blocks=ByteArray([0] * 4096),
                       Data=ByteArray([0] * 2048), Add=ByteArray([0] * 2048),
                       BlockLight=ByteArray([0] * 2048), SkyLight=ByteArray([-1] * 2048))
    level['Sections'].append(section)
    solids = sorted((name, id_) for id_, name in legacy_registry(world, 'blocks').items() if name.startswith(MOD))

    def put(index, id_, meta=0):
        section['Blocks'][index] = signed(id_ & 255)
        set_nibble(section['Add'], index, id_ >> 8)
        set_nibble(section['Data'], index, meta)

    for index, (name, id_) in enumerate(solids, 32):
        put(index, id_)
    fluid_ids = {name: id_ for name, id_ in solids if name in (MOD + 'lithium', MOD + 'silicon')}
    for row, id_ in enumerate(fluid_ids.values(), 4):
        for level_value in range(1, 16):
            put(row * 16 + level_value, id_, level_value)
    item_names = sorted(name for name in legacy_registry(world, 'items').values() if name.startswith(MOD))
    stacks = [Compound(id=String(name), Count=Byte(index % 3 + 1), Damage=Short(0),
                       tag=Compound(MigrationTest=String(name)))
              for index, name in enumerate(item_names)]
    for fluid in ('lithium', 'silicon'):
        stacks.append(Compound(id=String('forge:bucketfilled'), Count=Byte(1), Damage=Short(0),
                               tag=Compound(FluidName=String(fluid), Amount=Int(1000), MigrationTest=String(fluid))))
    entities = List[Compound](level.get('TileEntities', []))
    level['TileEntities'] = entities
    for index, start in enumerate(range(0, len(stacks), 27)):
        chest_x, chest_z = index * 3 + 1, 8
        put(chest_z * 16 + chest_x, 54, 2)
        inventory = List[Compound](stacks[start:start + 27])
        for slot_id, stack in enumerate(inventory):
            stack['Slot'] = Byte(slot_id)
        entities.append(Compound(id=String('minecraft:chest'), x=Int(cx * 16 + chest_x),
                                 y=Int(y), z=Int(cz * 16 + chest_z), Items=inventory))
    raw = BytesIO()
    chunk.write(raw)
    compressed = zlib.compress(raw.getvalue())
    payload = struct.pack('>IB', len(compressed) + 1, 2) + compressed
    sectors = (len(payload) + SECTOR - 1) // SECTOR
    offset = len(region) // SECTOR
    assert len(region) % SECTOR == 0 and sectors < 256
    region[slot * 4:slot * 4 + 4] = offset.to_bytes(3, 'big') + bytes([sectors])
    region.extend(payload.ljust(sectors * SECTOR, b'\0'))
    region_path.write_bytes(region)
    return len(solids), len(stacks)


def compare(before, after):
    original_chunks, original_blocks, original_chests = before
    chunks, blocks, chests = after
    difference = {position: [original_blocks.get(position), blocks.get(position)]
                  for position in original_blocks.keys() | blocks.keys()
                  if original_blocks.get(position) != blocks.get(position)}
    if difference:
        preview = list(sorted(difference.items()))[:12]
        raise AssertionError(f'{len(difference)} mineral coordinates or states changed. First changes {preview}')
    assert original_chests == chests, 'Mineral chest items, counts or tags changed'
    assert chunks == original_chunks, f'{len(original_chunks - chunks)} original chunks are missing'
    return {'chunks': len(chunks), 'blocks': dict(sorted(Counter(b[0] for b in blocks.values()).items())),
            'chests': len(chests), 'stacks': sum(map(len, chests.values())),
            'coordinate_sha256': sha256(repr(sorted(blocks.items())).encode()).hexdigest()}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ('java', 'runtime', 'output', 'source'):
        parser.add_argument('--' + name, required=True, type=Path)
    parser.add_argument('--mods', required=True, nargs='+', type=Path)
    parser.add_argument('--probe-chunk', nargs=2, type=int, default=PROBE_CHUNK)
    parser.add_argument('--minimum-blocks', type=int, default=1000)
    parser.add_argument('--natural-open', action='store_true',
                        help='Open normally before converting the remaining unloaded chunks')
    args = parser.parse_args()
    before_source = fingerprint(args.source)
    prepare(args.runtime, args.output, args.mods, args.source)
    target = args.output / 'world'
    ids = {id_: name for id_, name in legacy_registry(target, 'blocks').items() if name.startswith(MOD)}
    forms = add_fixture(target, args.probe_chunk)
    before = census(target, ids)
    rules = saved_mineral_rules(target)
    assert len(before[1]) >= args.minimum_blocks and len(ids) > 0, 'No meaningful source mineral census'
    source_manifest = {'source': str(args.source), 'fingerprint': before_source, 'registry': ids,
                       'added_forms': forms, 'jars': {p.name: digest(p) for p in args.mods}}
    (args.output / 'sources.json').write_text(json.dumps(source_manifest, indent=2))
    result = {}
    try:
        if args.natural_open:
            run(args.java, args.output, 'natural-open')
            result['natural-open'] = compare(before, census(target, ids, before[0]))
            if rules:
                assert saved_mineral_rules(target) == rules, 'Saved OreSpawn mineral rules changed'
        for phase in ('first', 'reload'):
            run(args.java, args.output, phase, upgrade=True)
            result[phase] = compare(before, census(target, ids, before[0], require_flattened=True))
            if rules:
                assert saved_mineral_rules(target) == rules, 'Saved OreSpawn mineral rules changed'
            result[phase]['saved_rules'] = len(rules)
            print(f"{phase} census PASS {result[phase]}", flush=True)
            (args.output / 'results.json').write_text(json.dumps(result, indent=2))
    finally:
        assert before_source == fingerprint(args.source), 'Source fixture changed'


if __name__ == '__main__':
    main()
