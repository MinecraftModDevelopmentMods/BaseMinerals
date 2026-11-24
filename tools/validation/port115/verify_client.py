"""Run a generated Eclipse profile with the isolated client save/reopen probe.

The Eclipse DebugPlugin parser must be available in --parser-classpath.
No player run directory or save is used.
"""
import argparse
import hashlib
import json
import os
from pathlib import Path
import shutil
import subprocess
from xml.etree import ElementTree as ET


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for key in ('java', 'parser-java', 'project', 'launch', 'probe', 'output'):
        parser.add_argument('--' + key, required=True, type=Path)
    parser.add_argument('--parser-classpath', required=True)
    parser.add_argument('--mapped-mods', type=Path)
    args = parser.parse_args()
    args.output.mkdir(parents=True)
    mods = args.output / 'mods'
    mods.mkdir()
    shutil.copy2(args.probe, mods / args.probe.name)
    if args.mapped_mods:
        for jar in args.mapped_mods.glob('*.jar'):
            shutil.copy2(jar, mods / jar.name)
    launch = ET.parse(args.launch).getroot()
    attrs = {e.attrib['key']: e.attrib['value'] for e in launch.findall('stringAttribute')}
    cp = [e.attrib['path'] for e in ET.parse(args.project / '.classpath').getroot().findall('classpathentry')
          if e.attrib.get('kind') == 'lib']
    for entry in launch.findall('listAttribute/listEntry'):
        value = ET.fromstring(entry.attrib['value']).attrib
        if 'externalArchive' in value:
            cp.append(value['externalArchive'])
    cp = list(dict.fromkeys(cp))
    assert all(Path(p).is_file() for p in cp), 'Missing Eclipse classpath entry'
    parsed = subprocess.check_output([args.parser_java, '-cp', args.parser_classpath,
        'ParseEclipseArguments', args.launch]).decode().strip('\0').split('\0')
    env = os.environ.copy()
    env.update({e.attrib['key']: e.attrib['value'] for e in launch.findall('mapAttribute/mapEntry')})
    inputs = dict(launch=str(args.launch), arguments=parsed, classpath=cp,
                  staged_mod_classes=env['MOD_CLASSES'], mods={
                      p.name: hashlib.sha256(p.read_bytes()).hexdigest() for p in mods.glob('*.jar')})
    main_jar = args.project / 'build/libs/BaseMinerals-2.5.2.115021.jar'
    inputs['candidate_sha256'] = hashlib.sha256(main_jar.read_bytes()).hexdigest()
    (args.output / 'inputs.json').write_text(json.dumps(inputs, indent=2))
    (args.output / 'options.txt').write_text('pauseOnLostFocus:false\nrenderDistance:4\nmaxFps:60\n')
    with (args.output / 'console.log').open('wb') as stream:
        result = subprocess.run([args.java, '-Xmx2500m', '-cp', os.pathsep.join(cp),
            attrs['org.eclipse.jdt.launching.MAIN_TYPE'], *parsed], cwd=args.output, env=env,
            stdout=stream, stderr=subprocess.STDOUT, timeout=240,
            creationflags=subprocess.CREATE_NO_WINDOW)
    report = json.loads((args.output / 'client-probe.json').read_text())
    log = (args.output / 'console.log').read_text(errors='replace')
    unexpected = [line for line in log.splitlines() if ('/ERROR]' in line or '/FATAL]' in line)
        and not any(allowed in line for allowed in ('Received empty payload on channel fml:handshake',
                  'Forge config just got changed on the file system!'))]
    assert not unexpected, unexpected
    assert result.returncode == 0 and report['passed'] and report['worldReopened'], report
    print(args.output.name, 'PASS', report['checks'], 'checks; fresh world and save/reopen', flush=True)


if __name__ == '__main__':
    main()
