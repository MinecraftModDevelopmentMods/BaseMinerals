"""Inspect real public sibling jars before asserting source-policy behavior."""
import argparse,hashlib,json,urllib.request
from pathlib import Path
from run_runtime_checks import ROOT,dependency,prepare,run,write_json

def main():
    parser=argparse.ArgumentParser()
    for name in ('java','template','output'):parser.add_argument('--'+name,type=Path,required=True)
    parser.add_argument('--cases',nargs='+',choices=['alone','electric','mineralogy','combined'],default=['alone','electric','mineralogy','combined'])
    parser.add_argument('--sample',action='store_true',help='Census 256 chunks in each vanilla dimension.')
    args=parser.parse_args();args.output.mkdir(parents=True,exist_ok=False)
    specs=json.loads((ROOT/'gradle/dependencies.json').read_text())
    jars=[dependency(s) for s in specs if s.get('runtime')]+[ROOT/'build/libs/BaseMinerals-2.5.1.110021.jar',ROOT/'build/test-mods/baseminerals-probe-packaged.jar']
    dependencies={};receipts=[]
    for spec in json.loads((ROOT/'tools/validation/legacy110/overlap_dependencies.json').read_text()):
        _,artifact,version=spec['coordinate'].split(':');name=artifact+'-'+version+'.jar'
        target=args.output/'dependencies'/name;target.parent.mkdir(parents=True,exist_ok=True)
        url=spec['repository']+'/curse/maven/'+artifact+'/'+version+'/'+name
        with urllib.request.urlopen(url,timeout=60) as response:target.write_bytes(response.read())
        assert hashlib.sha256(target.read_bytes()).hexdigest().upper()==spec['sha256']
        dependencies[spec['id']]=target;receipts.append({**spec,'url':url})
    write_json(args.output/'public-dependency-receipts.json',receipts)
    results={};failures={}
    for name,selected in [('alone',[]),('electric',['electricadvantage','poweradvantage']),('mineralogy',['mineralogy']),('combined',['electricadvantage','poweradvantage','mineralogy'])]:
        if name not in args.cases:continue
        directory=args.output/name;prepare(directory,args.template,jars+[dependencies[key] for key in selected])
        try:
            result=run(args.java,directory,'first',{'minerals.sample':'true','minerals.width':'16'} if args.sample else {})
        except AssertionError as failure:
            failures[name]=str(failure);write_json(args.output/'failures.json',failures)
            print('BASE_MINERALS_PUBLIC_INTEGRATION_FAILED',name,str(failure),flush=True)
            continue
        results[name]=result;write_json(args.output/'results.json',results)
        print('BASE_MINERALS_OVERLAP_PROFILE',name,json.dumps({key:value for key,value in result['profile'].get('ore_source_policies',{}).items() if key.startswith(('orespawn:sulfur|','orespawn:lithium|'))}),flush=True)
    if failures:raise AssertionError(f'Public integration gates failed: {failures}')

if __name__=='__main__':main()
