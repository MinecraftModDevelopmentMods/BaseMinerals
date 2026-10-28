"""Test the OS4 managed-source contract using an explicitly synthetic sibling."""
import argparse,copy,json
from pathlib import Path
from run_runtime_checks import ROOT,dependency,prepare,run,write_json
from run_packaged_suite import fingerprint

def main():
    parser=argparse.ArgumentParser()
    for name in ('java','template','output'):parser.add_argument('--'+name,type=Path,required=True)
    parser.add_argument('--mirror',type=Path)
    args=parser.parse_args();args.output.mkdir(parents=True,exist_ok=False)
    specs=json.loads((ROOT/'gradle/dependencies.json').read_text())
    jars=[dependency(s,args.mirror) for s in specs if s.get('runtime')]+[ROOT/'build/libs/BaseMinerals-2.5.1.110021.jar',ROOT/'build/test-mods/baseminerals-probe-packaged.jar']
    fixture=ROOT/'build/test-mods/synthetic-source-policy-fixture-packaged.jar'
    properties={'minerals.sample':'true','minerals.shared':'true','minerals.width':'16'}
    balanced=args.output/'balanced';prepare(balanced,args.template,jars+[fixture])
    result=run(args.java,balanced,'first',properties)
    policies=result['profile']['ore_source_policies'];keys=['orespawn:'+m+'|minecraft:overworld' for m in ('sulfur','lithium')]
    for key in keys:
        policy=policies[key];assert policy['mode']=='consolidated' and policy['output_mode']=='balanced',policy
        assert len(policy['outputs'])==2 and len(policy['placement_sources'])==1,policy
        assert next(iter(policy['placement_sources'].values())).startswith('baseminerals:'),policy
    source=balanced/'world';before=fingerprint(source);results={'balanced':result}
    for label,mode,owner in [('single-base','single','baseminerals'),('single-sibling','single','electricadvantage'),('custom','custom','electricadvantage'),('weighted','custom','electricadvantage'),('original','custom','baseminerals')]:
        target=args.output/label;prepare(target,args.template,jars+[fixture],source)
        path=target/'world/serverconfig/orespawn-worldgen.json';profile=json.loads(path.read_text())
        for key in keys:
            policy=profile['ore_source_policies'][key]
            selected=next(c['source_id'] for c in policy['candidates'] if c['owner']==owner)
            policy.update(mode='keep_separate' if label=='original' else 'consolidated',output_mode=mode,outputs={selected:1},review_required=False)
            if label=='weighted':
                policy['outputs']={c['source_id']:3 if c['owner']=='electricadvantage' else 1 for c in policy['candidates']}
        write_json(path,profile);report=run(args.java,target,'later',properties);results[label]=report
        outputs=report['dimensions']['0']['sharedOutputs']
        if label not in ('original','weighted'):
            assert any(key.startswith(owner+':') and count>0 for key,count in outputs.items()),outputs
            assert not any(not key.startswith(owner+':') and count>0 for key,count in outputs.items()),outputs
    for material,foreign in [('lithium','li'),('sulfur','sulfur')]:
        def total(label):
            values=results[label]['dimensions']['0']['sharedOutputs']
            return values.get('baseminerals:'+material+'_ore',0)+values.get('electricadvantage:'+foreign+'_ore',0)
        assert total('single-base')==total('single-sibling')==total('original'),material
        weighted=results['weighted']['dimensions']['0']['sharedOutputs']
        own=weighted.get('baseminerals:'+material+'_ore',0);sibling=weighted.get('electricadvantage:'+foreign+'_ore',0)
        assert own>0 and 2.5<sibling/own<3.5,(material,own,sibling)
    # Provider settings survive loss/reappearance of an output source. A missing
    # Minecraft block itself is not promised physical restoration after removal.
    choice=args.output/'single-sibling/world';choice_before=fingerprint(choice)
    target=args.output/'missing-source';prepare(target,args.template,jars,choice)
    missing=run(args.java,target,'reload',{'fml.queryResult':'confirm'})
    results['missing-source']=missing
    restored=args.output/'restored-source';prepare(restored,args.template,jars+[fixture],target/'world')
    report=run(args.java,restored,'later',properties);results['restored-source']=report
    for key in keys:
        policy=report['profile']['ore_source_policies'][key]
        assert any(c['owner']=='electricadvantage' and c['loaded'] for c in policy['candidates'])
        selected=results['single-sibling']['profile']['ore_source_policies'][key]
        assert policy['output_mode']==selected['output_mode'] and policy['outputs']==selected['outputs'],policy
    assert fingerprint(source)==before
    assert fingerprint(choice)==choice_before
    write_json(args.output/'results.json',results)
    print('BASE_MINERALS_SYNTHETIC_POLICY_CONTRACT_PASS',len(results),'runs',flush=True)

if __name__=='__main__':main()
