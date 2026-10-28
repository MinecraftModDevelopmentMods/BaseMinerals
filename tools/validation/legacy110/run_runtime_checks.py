"""Run the installed Base Minerals jar in disposable packaged Forge servers."""
import argparse
import hashlib
import json
import re
import shutil
import subprocess
import time
from pathlib import Path

ROOT = Path(__file__).resolve().parents[3]
FORGE = 'forge-1.10.2-12.18.3.2511-universal.jar'

def write_json(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, indent=2)+'\n', encoding='utf-8')

def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()

def dependency(spec, mirror=None):
    group, artifact, version, *classifier = spec['coordinate'].split(':')
    filename = f'{artifact}-{version}'+('-'+classifier[0] if classifier else '')+'.jar'
    path = (mirror or ROOT/'.gradle/legacy-dependencies')/group.replace('.', '/')/artifact/version/filename
    if digest(path).upper() != spec['sha256']:
        raise AssertionError(f'Dependency checksum mismatch: {spec["id"]}')
    return path

def prepare(directory, template, jars, world=None):
    directory.mkdir(parents=True, exist_ok=False)
    for name in (FORGE, 'minecraft_server.1.10.2.jar'):
        shutil.copy2(template/name, directory/name)
    shutil.copytree(template/'libraries', directory/'libraries')
    (directory/'mods').mkdir()
    for jar in jars:
        shutil.copy2(jar, directory/'mods'/jar.name)
    for name in ('banned-players.json','banned-ips.json','ops.json','whitelist.json'):(directory/name).write_text('[]\n',encoding='utf-8')
    write_json(directory/'input-jars.json',{jar.name:digest(directory/'mods'/jar.name) for jar in jars})
    (directory/'eula.txt').write_text('eula=true\n', encoding='utf-8')
    (directory/'server.properties').write_text(
        'level-name=world\nserver-ip=127.0.0.1\nserver-port=0\nonline-mode=false\n'
        'level-seed=8675309\nview-distance=2\nmax-tick-time=-1\nspawn-protection=0\n'
        'generate-structures=false\nlevel-type=DEFAULT\n', encoding='utf-8')
    if world:
        if directory.resolve().is_relative_to(world.resolve()):
            raise ValueError('Disposable target is inside the source world')
        shutil.copytree(world, directory/'world')

def run(java, directory, phase, properties=None, expect_failure=False):
    properties = {'minerals.phase':phase, **(properties or {})}
    command = [str(java), '-Xms256M', '-Xmx2G']+[f'-D{k}={v}' for k,v in properties.items()]
    command += ['-jar', FORGE, 'nogui']
    log = directory/f'{phase}-console.log'
    started = time.time_ns()
    with log.open('wb') as stream:
        result = subprocess.run(command, cwd=directory, stdout=stream, stderr=subprocess.STDOUT, timeout=900)
    report_path = directory/f'{phase}-minerals.json'
    if not report_path.is_file() or report_path.stat().st_mtime_ns < started:
        raise AssertionError(f'No fresh runtime report: {log}')
    report = json.loads(report_path.read_text(encoding='utf-8'))
    if result.returncode or not report['passed']:
        raise AssertionError(f'Packaged checks failed: {report_path}; {log}')
    text = log.read_text(errors='replace')
    if not expect_failure and re.search(r"Couldn't load[^\r\n]*for baseminerals", text):
        raise AssertionError(f'An optional integration failed: {log}')
    print(directory.name, phase, 'PASS', flush=True)
    return report
