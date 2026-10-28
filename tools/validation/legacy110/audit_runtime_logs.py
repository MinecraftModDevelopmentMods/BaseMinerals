"""Audit full successful-run logs, with narrow, documented legacy exceptions."""
import argparse,collections,json,re
from pathlib import Path
from run_runtime_checks import write_json,digest

def category(line,path):
    if 'FML appears to be missing any signature data.' in line:return 'unsigned Forge distribution'
    if 'Invalid fingerprint detected!' in line and any('['+m+']' in line for m in ('basemetals','mineralogy')):return 'public dependency fingerprint'
    if any(t in line for t in ('SERVER IS RUNNING IN OFFLINE/INSECURE MODE','server will make no attempt to authenticate','While this makes the game possible','To change this, set "online-mode"')):return 'isolated offline server'
    if '[Client thread/WARN]: Author: Paul Lamb' in line:return 'legacy sound shutdown'
    if 'client-' in str(path) and "Can't keep up!" in line:return 'automated client startup tick delay'
    if 'tinkers' in str(path) and ('[FML]: ***' in line or '[FML]: *  at ' in line or ('Dangerous alternative prefix `tconstruct`' in line and 'expected `basemetals`' in line)):return 'public Base Metals Tinkers registration'
    if 'missing-source' in str(path) and any(t in line for t in ('Ignoring invalid OreSpawn-managed ore \'electricadvantage:', 'saved with mod electricadvantage', 'unidentified mappings', 'Unidentified block: electricadvantage:', 'Unidentified item: electricadvantage:', 'mappings that may cause world breakage')):return 'intentional missing-source fixture'
    return None

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--directories',type=Path,nargs='+',required=True);parser.add_argument('--output',type=Path,required=True)
    args=parser.parse_args();counts=collections.Counter();unknown=[];files={}
    for directory in args.directories:
        for path in directory.rglob('*-console.log'):
            text=path.read_text(errors='replace');files[str(path)]={'sha256':digest(path),'bytes':path.stat().st_size}
            for line in text.splitlines():
                if re.search(r'\[(?:[^\]]*/)?(?:WARN|ERROR|FATAL)\]',line):
                    kind=category(line,path)
                    if kind:counts[kind]+=1
                    else:unknown.append({'path':str(path),'line':line})
            for marker in ('BASE_MINERALS_RUNTIME_FAIL','Exception in server tick loop','Exception caught during firing event','java.lang.NullPointerException','[TEXTURE ERRORS]'):
                if marker in text:unknown.append({'path':str(path),'fatal_marker':marker})
    result={'passed':not unknown,'logs':files,'documented_messages':dict(counts),'unclassified':unknown};write_json(args.output,result)
    assert not unknown,('Unclassified runtime diagnostics',unknown[:10])
    print('BASE_MINERALS_FULL_LOG_AUDIT_PASS',len(files),'logs',flush=True)

if __name__=='__main__':main()
