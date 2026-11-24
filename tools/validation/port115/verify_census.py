"""Run the full-height generation probe, then compare a saved-world reload."""
import argparse
import hashlib
import json
from pathlib import Path
import shutil
import subprocess
from run_forge import prepare


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ('java', 'runtime', 'output', 'probe'):
        parser.add_argument('--' + name, required=True, type=Path)
    parser.add_argument('--mods', required=True, nargs='+', type=Path)
    parser.add_argument('--seed', required=True, type=int)
    parser.add_argument('--compare', type=Path, help='Independent same-seed generation report')
    parser.add_argument('--provider-override', type=Path)
    parser.add_argument('--world-profile', type=Path)
    parser.add_argument('--disabled', default='', help='Expected disabled minerals, comma-separated, or all')
    args = parser.parse_args()
    jars = [*args.mods, args.probe]
    prepare(args.runtime, args.output, jars)
    (args.output / 'input-jars.json').write_text(json.dumps({
        p.name: hashlib.sha256(p.read_bytes()).hexdigest() for p in jars}, indent=2))
    properties = args.output / 'server.properties'
    properties.write_text(properties.read_text().replace('level-seed=8675309', 'level-seed=' + str(args.seed)))
    for source, relative in ((args.provider_override, 'config/baseminerals-orespawn.json'),
                             (args.world_profile, 'world/serverconfig/orespawn-worldgen.json')):
        if source:
            destination = args.output / relative
            destination.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(source, destination)
    if args.seed == 0:
        # DedicatedServer replaces a textual zero with a random seed. An empty,
        # explicitly seeded save exercises actual seed zero without editing chunks.
        from nbtlib import File, Compound, String, Long, Int, Byte
        world = args.output / 'world'
        world.mkdir(exist_ok=True)
        initial = File({'Data': Compound({'RandomSeed': Long(0), 'LevelName': String('world'),
            'DataVersion': Int(2230), 'version': Int(19133), 'GameType': Int(0),
            'generatorName': String('default'), 'generatorVersion': Int(1), 'MapFeatures': Byte(1),
            'initialized': Byte(0), 'allowCommands': Byte(1),
            'Version': Compound({'Id': Int(2230), 'Name': String('1.15.2'), 'Snapshot': Byte(0)})})})
        initial.save(world / 'level.dat', gzipped=True)
        (args.output / 'seed-zero-fixture.json').write_text(json.dumps(dict(
            description='Empty native save; explicit RandomSeed=0; no pre-generated chunks',
            sha256=hashlib.sha256((world / 'level.dat').read_bytes()).hexdigest()), indent=2))
    reports = []
    for phase in ('fresh', 'reload'):
        with (args.output / (phase + '-console.log')).open('wb') as stream:
            result = subprocess.run([str(args.java), '-Xmx3G', '-Dmineralprobe.disabled=' + args.disabled, '-jar',
                json.loads((args.output / 'forge-runtime.json').read_text())['launcher'], 'nogui'],
                cwd=args.output, stdout=stream, stderr=subprocess.STDOUT, timeout=900)
        report = json.loads((args.output / 'generation-results.json').read_text())
        shutil.copy2(args.output / 'generation-results.json', args.output / (phase + '-results.json'))
        assert result.returncode == 0 and report['passed'] and report['seed'] == args.seed, report
        text = (args.output / (phase + '-console.log')).read_text(errors='replace')
        unexpected = [line for line in text.splitlines() if ('/ERROR]' in line or '/FATAL]' in line)
            and 'Forge config just got changed on the file system!' not in line]
        assert not unexpected, unexpected
        reports.append(report)
        print(args.output.name, phase, 'PASS', round(report['seconds'], 2), 'seconds', flush=True)
    def digests(report):
        return {key: value['sorted_coordinate_sha256'] for key, value in report['dimensions'].items()}
    assert digests(reports[0]) == digests(reports[1]), 'Saved ores changed on reload'
    if args.compare:
        assert digests(reports[0]) == digests(json.loads(args.compare.read_text())), 'Independent generation differs'
    (args.output / 'qualification.json').write_text(json.dumps(dict(passed=True, seed=args.seed,
        reload_unchanged=True, repeated_generation_matches=bool(args.compare)), indent=2))


if __name__ == '__main__':
    main()
