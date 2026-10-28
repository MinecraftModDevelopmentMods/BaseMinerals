"""Required regression gate: an OS1 import must not gain native placement on reload."""
import argparse,json
from pathlib import Path
from run_runtime_checks import ROOT,dependency,prepare,run,write_json

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    for name in ('java','template','output'):parser.add_argument('--'+name,type=Path,required=True)
    args=parser.parse_args()
    specs=json.loads((ROOT/'gradle/dependencies.json').read_text())
    jars=[dependency(s) for s in specs if s.get('runtime')]+[ROOT/'build/libs/BaseMinerals-2.5.1.110021.jar',ROOT/'build/test-mods/baseminerals-probe-packaged.jar']
    args.output.mkdir(parents=True,exist_ok=False)
    initial=args.output/'initial';prepare(initial,args.template,jars)
    source=initial/'config/orespawn/baseminerals.json'
    write_json(source,{'dimensions':[{'dimension':'0','ores':[{'blockID':'baseminerals:lithium_ore','size':4,'variation':0,'frequency':1.5,'minHeight':8,'maxHeight':40}]}]})
    first=run(args.java,initial,'first')
    before={k:v for k,v in first['profile']['ores'].items() if v.get('block')=='baseminerals:lithium_ore'}
    assert len(before)==1,before
    # Copy only the saved world, preserving the original world and OS1 file.
    # A moved world must not need the old global file to preserve its budget.
    target=args.output/'profile-only';prepare(target,args.template,jars,initial/'world')
    reload=run(args.java,target,'later',{'minerals.sample':'true','minerals.width':'16'})
    after={k:v for k,v in reload['profile']['ores'].items() if v.get('block')=='baseminerals:lithium_ore'}
    passed=set(before)==set(after)
    write_json(args.output/'required-migration-gate.json',{'passed':passed,'before':before,'after':after,'counts':reload['dimensions']['0']['counts']})
    assert passed,('OS1 reload adds native placement alongside the imported rule',list(before),list(after))
    print('BASE_MINERALS_OS1_RELOAD_PASS',flush=True)

if __name__=='__main__':main()
