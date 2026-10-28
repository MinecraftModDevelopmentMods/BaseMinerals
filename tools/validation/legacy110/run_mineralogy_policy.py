"""Review real public Mineralogy sulfur policies on disposable world copies."""
import argparse,json
from pathlib import Path
from run_runtime_checks import ROOT,dependency,prepare,run,write_json,digest
from run_packaged_suite import fingerprint

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    for name in ('java','template','output','integration'):parser.add_argument('--'+name,type=Path,required=True)
    args=parser.parse_args();args.output.mkdir(parents=True,exist_ok=False)
    specs=json.loads((ROOT/'gradle/dependencies.json').read_text())
    jars=[dependency(s) for s in specs if s.get('runtime')]+[ROOT/'build/libs/BaseMinerals-2.5.1.110021.jar',ROOT/'build/test-mods/baseminerals-probe-packaged.jar']
    mineralogy=next(p for p in (args.integration/'mods').glob('*.jar') if 'mineralogy' in p.name.lower())
    pin=next(s for s in json.loads((ROOT/'tools/validation/legacy110/overlap_dependencies.json').read_text()) if s['id']=='mineralogy')
    assert digest(mineralogy).upper()==pin['sha256'];jars.append(mineralogy)
    source=args.integration/'world';before=fingerprint(source);results={}
    key='orespawn:sulfur|minecraft:overworld'
    for label in ('balanced','single-base','single-mineralogy','weighted'):
        target=args.output/label;prepare(target,args.template,jars,source)
        path=target/'world/serverconfig/orespawn-worldgen.json';profile=json.loads(path.read_text());policy=profile['ore_source_policies'][key]
        candidates={c['owner']:c['source_id'] for c in policy['candidates'] if c['loaded'] and not c['external']}
        assert set(candidates)=={'baseminerals','mineralogy'},candidates
        outputs={candidates['baseminerals']:1,candidates['mineralogy']:3 if label=='weighted' else 1}
        if label.startswith('single-'):outputs={candidates['baseminerals' if label=='single-base' else 'mineralogy']:1}
        policy.update(mode='consolidated',output_mode='balanced' if label=='balanced' else 'custom' if label=='weighted' else 'single',outputs=outputs,
                      placement_sources={'orespawn:standard':candidates['mineralogy']},review_required=False)
        write_json(path,profile)
        report=run(args.java,target,'later',{'minerals.sample':'true','minerals.shared':'true','minerals.width':'16'})
        active=report['profile']['ore_source_policies'][key]
        assert active['mode']=='consolidated' and active['placement_sources']==policy['placement_sources'],active
        physical=report['dimensions']['0']['sharedOutputs'];own=physical.get('baseminerals:sulfur_ore',0);other=physical.get('mineralogy:sulfur_ore',0)
        assert own+other>0,physical
        if label=='single-base':assert own>0 and other==0
        if label=='single-mineralogy':assert own==0 and other>0
        if label=='balanced':assert own>0 and other>0 and 0.7<other/own<1.3
        if label=='weighted':assert own>0 and 2.5<other/own<3.5
        results[label]=report
    totals=[sum(v['dimensions']['0']['sharedOutputs'].get(k,0) for k in ('baseminerals:sulfur_ore','mineralogy:sulfur_ore')) for v in results.values()]
    assert len(set(totals))==1,('Output choices changed the placement budget',totals)
    assert len({v['dimensions']['0']['sharedDigest'] for v in results.values()})==1,'Output choices moved the shared ore positions'
    assert fingerprint(source)==before
    write_json(args.output/'source-fixture.json',{'path':str(source),'files':before,'mineralogy':pin})
    write_json(args.output/'results.json',results)
    print('BASE_MINERALS_PUBLIC_MINERALOGY_POLICIES_PASS',len(results),'runs',flush=True)

if __name__=='__main__':main()
