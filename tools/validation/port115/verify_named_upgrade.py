"""Check an already flattened source save without modifying its original fixture."""
import argparse
import json
from pathlib import Path
from run_forge import prepare, run
from verify_upgrade import census, compare, digest, fingerprint, saved_mineral_rules


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ('java', 'runtime', 'source', 'output'):
        parser.add_argument('--' + name, required=True, type=Path)
    parser.add_argument('--mods', required=True, nargs='+', type=Path)
    args = parser.parse_args()
    original = fingerprint(args.source)
    before = census(args.source, {})
    assert len(before[1]) > 1000 and len(before[2]) > 0, 'Insufficient named source coverage'
    rules = saved_mineral_rules(args.source)
    prepare(args.runtime, args.output, args.mods, args.source)
    (args.output / 'sources.json').write_text(json.dumps(dict(source=str(args.source),
        fingerprint=original, jars={p.name: digest(p) for p in args.mods}), indent=2))
    result = {}
    try:
        for phase in ('natural-open', 'first', 'reload'):
            run(args.java, args.output, phase, upgrade=phase != 'natural-open')
            result[phase] = compare(before, census(args.output / 'world', {}, before[0],
                require_flattened=phase != 'natural-open'))
            assert saved_mineral_rules(args.output / 'world') == rules, 'Saved mineral rules changed'
            result[phase]['saved_rules'] = len(rules)
            (args.output / 'results.json').write_text(json.dumps(result, indent=2))
            print(args.output.name, phase, 'PASS', result[phase], flush=True)
    finally:
        assert fingerprint(args.source) == original, 'Original named source changed'


if __name__ == '__main__':
    main()
