"""Census copied imported/saved profiles and explicit positive/negative hosts."""
import argparse,copy,json
from pathlib import Path
from run_runtime_checks import ROOT,dependency,prepare,run,write_json
from run_packaged_suite import PREFIX,NAMES,placements,fingerprint,subset

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    for name in ('java','template','output','suite','selector-fixture'):parser.add_argument('--'+name,type=Path,required=True)
    args=parser.parse_args();args.output.mkdir(parents=True,exist_ok=False)
    specs=json.loads((ROOT/'gradle/dependencies.json').read_text())
    jars=[dependency(s) for s in specs if s.get('runtime')]+[ROOT/'build/libs/BaseMinerals-2.5.1.110021.jar',ROOT/'build/test-mods/baseminerals-probe-packaged.jar']
    results={};fixtures={};failures={}
    for name,source in [('os1',args.suite/'os1-import/world'),('os3',args.suite/'os3-import/world'),('saved-selector',args.selector_fixture)]:
        before=fingerprint(source);target=args.output/name;prepare(target,args.template,jars,source)
        original=json.loads((source/'serverconfig/orespawn-worldgen.json').read_text())
        if name=='saved-selector':
            for ore in original['ores'].values():
                if ore.get('source_mod')=='baseminerals':assert not ore.get('dimensions'),ore
            override=json.loads((ROOT/'build/reports/native-provider.json').read_text())
            for p in placements(override['ores'][PREFIX+'salt_ore']):p['frequency']=9
            write_json(target/'config/baseminerals-orespawn.json',override)
        report=run(args.java,target,'later',{'minerals.sample':'true','minerals.width':'16'})
        for block in {ore.get('block') for ore in original['ores'].values() if ore.get('source_mod')=='baseminerals'}:
            old_ids={key for key,ore in original['ores'].items() if ore.get('block')==block}
            new_ids={key for key,ore in report['profile']['ores'].items() if ore.get('block')==block}
            if old_ids!=new_ids:failures[name]={'block':block,'original_ids':sorted(old_ids),'reloaded_ids':sorted(new_ids)}
        for key,ore in original['ores'].items():
            if ore.get('source_mod')=='baseminerals':
                actual=report['profile']['ores'][key];subset(ore,actual)
                for section in ('dimensions','dimension_selectors'):
                    assert ore.get(section,{})==actual.get(section,{}),('Saved dimension declarations changed',name,key,section)
        assert fingerprint(source)==before,'Source fixture changed'
        results[name]=report;fixtures[name]={'path':str(source),'files':before}
    native=json.loads((ROOT/'build/reports/native-provider.json').read_text())
    for name,properties in [('stone-variants',{'minerals.variantHosts':'true'}),('negative-hosts',{'minerals.negativeHosts':'true'}),('decoded-shapes',{'minerals.record':'true'})]:
        target=args.output/name;prepare(target,args.template,jars)
        if name=='decoded-shapes':
            definition=copy.deepcopy(native)
            for material in NAMES:
                for p in placements(definition['ores'][PREFIX+material+'_ore']):
                    settings={k:p[k] for k in ('spread','vertical_spread','node_size')};settings['length']=8
                    p['pattern']={'type':'basemineralsprobe:'+material,'settings':settings}
            write_json(target/'config/baseminerals-orespawn.json',definition)
        report=run(args.java,target,'first',{'minerals.custom':'true','minerals.sample':'true','minerals.width':'16',**properties})
        if name=='decoded-shapes':
            for material,stats in report['recording'].items():
                p=placements(native['ores'][PREFIX+material+'_ore'])[0]
                subset({**{k:p[k] for k in ('spread','vertical_spread','node_size')},'length':8},stats['settings'])
        results[name]=report
    write_json(args.output/'source-fixtures.json',fixtures)
    write_json(args.output/'results.json',results)
    write_json(args.output/'continuity-gates.json',{'passed':not failures,'failures':failures})
    assert not failures,('Imported rules gained an extra placement budget',failures)
    print('BASE_MINERALS_CONTINUITY_PASS',len(results),'runs',flush=True)

if __name__=='__main__':main()
