"""Start a packaged Forge world in a disposable directory and save it after boot."""
import argparse
import json
import hashlib
from pathlib import Path
import queue
import shutil
import subprocess
import threading
import time

LAUNCHER = 'forge-1.14.4-28.2.26.jar'


def prepare(runtime, target, mods, source=None):
    if source and target.resolve().is_relative_to(source.resolve()):
        raise ValueError('Disposable output must be outside the source world')
    target.mkdir(parents=True)
    launcher = runtime.resolve() / LAUNCHER
    assert launcher.is_file() and (runtime / 'libraries').is_dir(), 'Incomplete official Forge runtime'
    (target / 'forge-runtime.json').write_text(json.dumps(dict(launcher=str(launcher),
        sha256=hashlib.sha256(launcher.read_bytes()).hexdigest()), indent=2))
    (target / 'mods').mkdir()
    for mod in mods:
        shutil.copy2(mod, target / 'mods' / mod.name)
    if source:
        shutil.copytree(source, target / 'world')
    (target / 'eula.txt').write_text('eula=true\n')
    (target / 'server.properties').write_text(
        'level-name=world\nlevel-seed=8675309\nonline-mode=false\nserver-ip=127.0.0.1\n'
        'server-port=0\nview-distance=3\nmax-tick-time=0\n'
    )


def run(java, target, phase, upgrade=False):
    command = [str(java), '-Xms256M', '-Xmx2G']
    if upgrade:
        command.append('-Dbaseminerals.debugUpgrade=true')
    command.extend(['-jar', json.loads((target / 'forge-runtime.json').read_text())['launcher'], 'nogui'])
    if upgrade:
        command.append('--forceUpgrade')
    log = target / (phase + '-console.log')
    messages = queue.Queue()
    with log.open('w', encoding='utf-8') as stream:
        process = subprocess.Popen(command, cwd=target, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                   stderr=subprocess.STDOUT, encoding='utf-8', errors='replace')

        def collect():
            for line in process.stdout:
                stream.write(line)
                stream.flush()
                messages.put(line)
            messages.put(None)

        thread = threading.Thread(target=collect, daemon=True)
        thread.start()
        deadline = time.monotonic() + 480
        started = False
        query_lines = []
        try:
            while time.monotonic() < deadline:
                try:
                    line = messages.get(timeout=1)
                except queue.Empty:
                    continue
                if line is None:
                    break
                query_lines.append(line.strip())
                if 'Run the command /fml confirm' in line:
                    prompt = '\n'.join(query_lines[-24:])
                    # Only the retired vanilla registry and the six vanilla
                    # sign/dye renames are expected across Forge 25 -> 28.
                    # Never blanket-confirm unknown mod content.
                    names = sorted(s for s in query_lines[-24:] if s.startswith('minecraft:') and ' ' not in s)
                    registry = ('There are 1 missing registries in this save.' in prompt
                                and names == ['minecraft:villagerprofessions'])
                    renames = ('There are 6 missing entries in this save.' in prompt and names == sorted([
                        'minecraft:sign', 'minecraft:wall_sign', 'minecraft:sign', 'minecraft:rose_red',
                        'minecraft:cactus_green', 'minecraft:dandelion_yellow']))
                    assert registry or renames, prompt
                    process.stdin.write('fml confirm\n')
                    process.stdin.flush()
                    receipt = target / (phase + '-registry-confirmation.json')
                    confirmations = json.loads(receipt.read_text()) if receipt.exists() else []
                    confirmations.append(dict(prompt=prompt, confirmed=names))
                    receipt.write_text(json.dumps(confirmations, indent=2))
                if 'Done (' in line and 'For help, type' in line:
                    started = True
                    process.stdin.write('stop\n')
                    process.stdin.flush()
                    break
                if 'Encountered an unexpected exception' in line:
                    break
            if not started:
                raise RuntimeError(f'Forge never reached a playable world. See {log}')
            if process.wait(timeout=120):
                raise RuntimeError(f'Forge shutdown failed. See {log}')
        finally:
            if process.poll() is None:
                process.kill()
                process.wait()
            thread.join(timeout=10)
    text = log.read_text(encoding='utf-8')
    failures = ('Encountered an unexpected exception', 'Error occurred applying transform of coremod',
                "Couldn't read loot table baseminerals:", "Couldn't parse loot table baseminerals:", 'Parsing error loading recipe baseminerals:')
    if any(failure in text for failure in failures):
        raise RuntimeError(f'Forge reported a migration or resource error. See {log}')
    print(f'{target.name} {phase} PASS', flush=True)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ('java', 'runtime', 'output'):
        parser.add_argument('--' + name, required=True, type=Path)
    parser.add_argument('--mods', required=True, type=Path, nargs='+')
    parser.add_argument('--source', type=Path)
    parser.add_argument('--force-upgrade', action='store_true')
    args = parser.parse_args()
    prepare(args.runtime, args.output, args.mods, args.source)
    run(args.java, args.output, 'first', args.force_upgrade)
    run(args.java, args.output, 'reload', args.force_upgrade)


if __name__ == '__main__':
    main()
