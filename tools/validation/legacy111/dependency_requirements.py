"""Reject missing/old public dependencies before any disposable world is loaded."""
import hashlib
import json
import subprocess
import urllib.request
import argparse
from pathlib import Path
from run_runtime_checks import ROOT, FORGE, dependency, prepare, write_json

OLD = {
    'base':('base-metals-240967','2533178','abdf704023100ad6bb70351ce72c31a07943ad5bce8308155564194a25089ccc'),
    'os':('mmd-orespawn-245586','2533159','05cc48e59c7b20ed9b7f29ae014f5e2e5b2f16f289ef7d6f274f017b5e2a6f77'),
    'os4':('mmd-orespawn-245586','8840593','51ff282d41e3c566db2541ccad53968030ea12e985a5cee7307162bbf560c99a')
}

def verify(java,template,output,base,orespawn,minerals):
    fixtures=output/'dependency-fixtures';fixtures.mkdir()
    old={}
    for key,(artifact,version,expected) in OLD.items():
        target=fixtures/f'{artifact}-{version}.jar'
        request=urllib.request.Request(f'https://www.cursemaven.com/curse/maven/{artifact}/{version}/{target.name}',headers={'User-Agent':'Mozilla/5.0'})
        with urllib.request.urlopen(request,timeout=45) as response:target.write_bytes(response.read())
        assert hashlib.sha256(target.read_bytes()).hexdigest()==expected,'Public fixture checksum mismatch'
        old[key]=target
    scenarios={
        'missing-base':([orespawn,minerals],'basemetals','2.5.1.111021'),
        'old-base':([old['base'],orespawn,minerals],'basemetals','2.5.1.111021'),
        'missing-orespawn':([base,minerals],'orespawn','4.1.6.111021'),
        'old-orespawn':([base,old['os'],minerals],'orespawn','4.1.6.111021'),
        'old-orespawn4':([base,old['os4'],minerals],'orespawn','4.1.6.111021')
    }
    result={}
    for name,(jars,modid,version) in scenarios.items():
        target=output/name;prepare(target,template,jars)
        log=target/'rejection-console.log'
        with log.open('wb') as stream:
            subprocess.run([str(java),'-Xmx1G','-jar',FORGE,'nogui'],cwd=target,stdout=stream,stderr=subprocess.STDOUT,timeout=90)
        text=log.read_text(errors='replace')
        rejected = ('MissingModsException' in text or 'Missing mandatory dependencies' in text
                    or 'MultipleModsErrored' in text)
        assert rejected and f'{modid}@[{version},' in text,log
        assert not (target/'world/level.dat').exists(),'Incompatible dependencies reached world loading'
        result[name]={'rejected_before_world':True,'required_mod':modid,'minimum':version}
        print(name,'PASS',flush=True)
    write_json(output/'dependency-requirements.json',result)

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    for name in ('java','template','output'):parser.add_argument('--'+name,type=Path,required=True)
    parser.add_argument('--mirror',type=Path);args=parser.parse_args();args.output.mkdir(parents=True,exist_ok=False)
    specs=json.loads((ROOT/'gradle/dependencies.json').read_text())
    selected={s['id']:dependency(s,args.mirror) for s in specs if s['id'] in ('basemetals','orespawn')}
    verify(args.java,args.template,args.output,selected['basemetals'],selected['orespawn'],ROOT/'build/libs/BaseMinerals-2.5.1.111021.jar')

if __name__=='__main__':main()
