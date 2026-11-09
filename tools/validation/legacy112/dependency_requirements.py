"""Reject missing/old public dependencies before any disposable world is loaded."""
import hashlib
import json
import subprocess
import urllib.request
import argparse
from pathlib import Path
from run_runtime_checks import ROOT, FORGE, dependency, prepare, write_json

OLD = {
    'mmdlib':('mmdlib-261744','2699843','c60917d368629976afe439aa15dce15b46345c1e473f0b04e901db2a47cadf37'),
    'os':('mmd-orespawn-245586','9343209','5b5929ccdeb055679fd6d7b9629bf94f06085a6ecd791fd08d0500f666c079dd')
}

def verify(java,template,output,mmdlib,orespawn,minerals):
    fixtures=output/'dependency-fixtures';fixtures.mkdir()
    old={}
    for key,(artifact,version,expected) in OLD.items():
        target=fixtures/f'{artifact}-{version}.jar'
        request=urllib.request.Request(f'https://www.cursemaven.com/curse/maven/{artifact}/{version}/{target.name}',headers={'User-Agent':'Mozilla/5.0'})
        with urllib.request.urlopen(request,timeout=45) as response:target.write_bytes(response.read())
        assert hashlib.sha256(target.read_bytes()).hexdigest()==expected,'Public fixture checksum mismatch'
        old[key]=target
    scenarios={
        'missing-mmdlib':([orespawn,minerals],'mmdlib','1.0.0-rc2'),
        'old-mmdlib':([old['mmdlib'],orespawn,minerals],'mmdlib','1.0.0-rc2'),
        'missing-orespawn':([mmdlib,minerals],'orespawn','4.1.6.112021'),
        'old-orespawn':([mmdlib,old['os'],minerals],'orespawn','4.1.6.112021')
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
    selected={s['id']:dependency(s,args.mirror) for s in specs if s['id'] in ('mmdlib','orespawn')}
    verify(args.java,args.template,args.output,selected['mmdlib'],selected['orespawn'],ROOT/'build/libs/BaseMinerals-2.5.1.112021.jar')

if __name__=='__main__':main()
