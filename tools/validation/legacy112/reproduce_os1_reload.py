"""Move a world with an already imported OS1 profile from 1.10.2 or 1.11.2."""
import argparse,json
from pathlib import Path
from run_runtime_checks import ROOT,dependency,prepare,run,write_json
from run_packaged_suite import fingerprint,subset

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    for name in ('java','template','output','source-world'):parser.add_argument('--'+name,type=Path,required=True)
    parser.add_argument('--mod-jar',type=Path,default=ROOT/'build/libs/BaseMinerals-2.5.1.112021.jar')
    parser.add_argument('--probe-jar',type=Path,default=ROOT/'build/test-mods/baseminerals-probe-packaged.jar')
    parser.add_argument('--with-base-metals',action='store_true')
    parser.add_argument('--mirror',type=Path);args=parser.parse_args()
    specs=json.loads((ROOT/'gradle/dependencies.json').read_text())
    jars=[dependency(s,args.mirror) for s in specs if s.get('runtime')
          or (args.with_base_metals and s['id']=='basemetals')]+[args.mod_jar,args.probe_jar]
    args.output.mkdir(parents=True,exist_ok=False)
    source_files=fingerprint(args.source_world)
    profile=json.loads((args.source_world/'serverconfig/orespawn-worldgen.json').read_text())
    before={k:v for k,v in profile['ores'].items() if v.get('block')=='baseminerals:lithium_ore'}
    assert len(before)==1 and next(iter(before.values())).get('legacy_import')=='os1',before
    write_json(args.output/'source-fixtures.json',{'path':str(args.source_world),'sha256':source_files})
    target=args.output/'profile-only';prepare(target,args.template,jars,args.source_world)
    reload=run(args.java,target,'later',{'minerals.sample':'true','minerals.width':'16'})
    after={k:v for k,v in reload['profile']['ores'].items() if v.get('block')=='baseminerals:lithium_ore'}
    passed=set(before)==set(after)
    write_json(args.output/'required-migration-gate.json',{'passed':passed,'before':before,'after':after,'counts':reload['dimensions']['0']['counts']})
    assert passed,('OS1 reload adds native placement alongside the imported rule',list(before),list(after))
    subset(before,after)
    assert fingerprint(args.source_world)==source_files,'Copied source world was modified'
    print('BASE_MINERALS_OS1_RELOAD_PASS',flush=True)

if __name__=='__main__':main()
