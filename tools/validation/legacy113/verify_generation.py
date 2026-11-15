"""Census the new world after first boot and reload in packaged Forge."""
import argparse
from collections import Counter
import json
from pathlib import Path

from run_forge import prepare, run
from verify_upgrade import census

HEIGHTS = {
    'lithium': (8, 47), 'niter': (16, 63), 'salt': (24, 79), 'sulfur': (4, 31),
    'phosphorus': (16, 63), 'potash': (8, 47), 'saltpeter': (24, 79),
}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ('java', 'runtime', 'output'):
        parser.add_argument('--' + name, required=True, type=Path)
    parser.add_argument('--mods', required=True, nargs='+', type=Path)
    args = parser.parse_args()
    prepare(args.runtime, args.output, args.mods)
    world = args.output / 'world'
    run(args.java, args.output, 'first')
    chunks, blocks, _ = census(world, {})
    histograms = {f'baseminerals:{name}_ore': Counter() for name in HEIGHTS}
    for (dimension, x, y, z), (name, props) in blocks.items():
        assert dimension == '.' and name in histograms, (dimension, x, y, z, name)
        minimum, maximum = HEIGHTS[name.split(':')[1].removesuffix('_ore')]
        assert minimum <= y <= maximum, (name, y)
        histograms[name][y] += 1
    assert all(histograms.values()), 'One or more mineral ores did not generate'
    run(args.java, args.output, 'reload')
    assert census(world, {}, chunks)[1] == blocks, 'Mineral coordinates changed on reload'
    summary = {'chunks': len(chunks), 'blocks': len(blocks), 'height_histograms': histograms}
    (args.output / 'generation.json').write_text(json.dumps(summary, indent=2))
    print('Fresh generation and reload PASS', {k: sum(v.values()) for k, v in histograms.items()})


if __name__ == '__main__':
    main()
