"""Qualify installed Base Minerals in disposable Forge servers; never use a played world."""
import argparse, copy, gzip, hashlib, json, math, shutil, struct
from pathlib import Path
from run_runtime_checks import ROOT, dependency, prepare, run, write_json, digest

PREFIX='baseminerals:legacy/'
SELECTOR='orespawn:all_except_nether_end'
NAMES=['lithium','niter','salt','sulfur','phosphorus','potash','saltpeter']
def fingerprint(path):
    return {str(p.relative_to(path)):digest(p) for p in path.rglob('*') if p.is_file()}
def seed_zero(directory):
    # A dedicated server treats level-seed=0 as random. Supply valid fresh world
    # metadata with RandomSeed=0 so this test actually exercises seed zero.
    def name(text):
        value=text.encode();return struct.pack('>H',len(value))+value
    def tag(kind,key,value):return bytes([kind])+name(key)+value
    data=tag(4,'RandomSeed',struct.pack('>q',0))+tag(8,'generatorName',name('default'))
    for key,value in [('generatorVersion',1),('GameType',1),('version',19133),('SpawnX',0),('SpawnY',64),('SpawnZ',0)]:data+=tag(3,key,struct.pack('>i',value))
    data+=tag(8,'LevelName',name('Base Minerals seed zero'))+tag(1,'MapFeatures',b'\0')+tag(1,'initialized',b'\x01')+b'\0'
    world=directory/'world';world.mkdir(exist_ok=True)
    with gzip.open(world/'level.dat','wb') as stream:stream.write(b'\x0a\0\0'+tag(10,'Data',data)+b'\0')
def placement(ore):return ore['dimension_selectors'][SELECTOR]
def placements(ore):return [placement(ore)]+list(ore.get('dimensions',{}).values())
def subset(expected,actual):
    if isinstance(expected,dict):
        for key,value in expected.items():assert key in actual,key;subset(value,actual[key])
    else:assert expected==actual,(expected,actual)

def main():
    parser=argparse.ArgumentParser(description=__doc__)
    for name in ('java','template','output'):parser.add_argument('--'+name,type=Path,required=True)
    parser.add_argument('--mirror',type=Path);parser.add_argument('--quick',action='store_true')
    parser.add_argument('--prior-suite',type=Path,help='Reuse 33 completed census/compatibility reports with identical input jars; revalidate recording and run timings in a new output directory.')
    args=parser.parse_args();args.output.mkdir(parents=True,exist_ok=False)
    specs=json.loads((ROOT/'gradle/dependencies.json').read_text())
    base=[dependency(s,args.mirror) for s in specs if s.get('runtime')]
    base += [ROOT/'build/libs/BaseMinerals-2.5.1.110021.jar',ROOT/'build/test-mods/baseminerals-probe-packaged.jar']
    native=json.loads((ROOT/'build/reports/native-provider.json').read_text())
    write_json(args.output/'source-fixtures.json',{str(p.relative_to(ROOT)):digest(p) for p in [Path(__file__).resolve(),ROOT/'build/reports/native-provider.json',ROOT/'tools/validation/legacy110/fixtures/orespawn3-baseminerals.json']})
    results={}
    def setup(name,seed=8675309,extra=(),world=None,override=None,config=None):
        directory=args.output/name;prepare(directory,args.template,base+list(extra),world)
        properties=directory/'server.properties';text=properties.read_text().replace('level-seed=8675309','level-seed='+str(seed));properties.write_text(text)
        if seed==0 and world is None:seed_zero(directory)
        if override is not None:write_json(directory/'config/baseminerals-orespawn.json',override)
        if config is not None:
            path=directory/'config/BaseMinerals.cfg';path.parent.mkdir(parents=True,exist_ok=True);path.write_text(config)
        return directory
    def check(directory,phase='first',properties=None):
        result=run(args.java,directory,phase,properties or {})
        results[directory.name+':'+phase]=result
        write_json(args.output/'results.json',results)
        return result
    if args.prior_suite:
        results=json.loads((args.prior_suite/'results.json').read_text())
        assert len(results)==33 and all(r['passed'] for r in results.values()),'Prior suite must contain the 33 completed pre-benchmark reports'
        expected={p.name:digest(p) for p in base}
        for directory in args.prior_suite.iterdir():
            if directory.is_dir() and (directory/'input-jars.json').exists():
                inputs=json.loads((directory/'input-jars.json').read_text())
                assert all(inputs.get(name)==sha for name,sha in expected.items()),('Changed input jar',directory)
        source=args.prior_suite/'seed-8675309/world';before=fingerprint(source)
        overworld=results['recorded-overworld:first'];fixture=results['recorded-host-fixture:first']
        write_json(args.output/'reused-evidence.json',{'path':str(args.prior_suite),'results_sha256':digest(args.prior_suite/'results.json'),'source_files':before})
        # Reusing old reports cannot bypass the subsequently discovered OS1 reload gate.
        imported=setup('os1-reload',world=args.prior_suite/'os1-import/world')
        reloaded=check(imported,'later',{'minerals.sample':'true','minerals.width':'16'})
        assert len([o for o in reloaded['profile']['ores'].values() if o.get('block')=='baseminerals:lithium_ore'])==1,'OS1 reload reinserted the native lithium budget'
    else:
        width=3 if args.quick else 16
        properties={'minerals.sample':'true','minerals.width':str(width),'minerals.saved':'true'}
        for seed in ([8675309] if args.quick else [0,1,8675309,123456789]):
            directory=setup('seed-'+str(seed),seed)
            first=check(directory,properties=properties)
            assert first['seed']==seed,('Wrong world seed',seed,first['seed'])
            subset(native['ores'],first['profile']['ores'])
            reload=check(directory,'reload',properties)
            assert {d:v['digest'] for d,v in first['dimensions'].items()}=={d:v['digest'] for d,v in reload['dimensions'].items()},'Reload changed ore census'
            repeat=check(setup('repeat-'+str(seed),seed),properties=properties)
            assert {d:v['digest'] for d,v in first['dimensions'].items()}=={d:v['digest'] for d,v in repeat['dimensions'].items()},'Seed is not deterministic'
        if args.quick:
            print('BASE_MINERALS_QUICK_SUITE_PASS',flush=True);return
        source=args.output/'seed-8675309'/'world';before=fingerprint(source)
        later=setup('later-chunks',world=source);check(later,'later',properties)
        unchanged=check(later,'reload',properties)
        original=results['seed-8675309:first']
        assert {d:v['digest'] for d,v in original['dimensions'].items()}=={d:v['digest'] for d,v in unchanged['dimensions'].items()},'Later generation changed old ore coordinates'
        assert fingerprint(source)==before,'Copied source fixture was modified'
        override=copy.deepcopy(native)
        for ore in override['ores'].values():ore['enabled']=False
        empty=check(setup('generation-disabled',override=override),properties={'minerals.sample':'true','minerals.noOres':'true','minerals.width':'16'})
        assert all(not value['counts'] for value in empty['dimensions'].values())
        for name in ['all']+NAMES+['silicon','charcoal']:
            disabled=NAMES+['silicon','charcoal'] if name=='all' else [name]
            config='Minerals {\n'+''.join(' B:Enable'+n.capitalize()+'=false\n' for n in disabled)+'}\n'
            check(setup('disabled-'+name,config=config),properties={'minerals.sample':'true','minerals.width':'16','minerals.noOres':str(name=='all').lower()})
        optional=[dependency(s,args.mirror) for s in specs if s['id'] in ['tconstruct','mantle']]
        check(setup('tinkers',extra=optional))
        # Imported OS3 rules must retain historical defaults/custom values, not rebalance.
        legacy=json.loads((ROOT/'tools/validation/legacy110/fixtures/orespawn3-baseminerals.json').read_text())
        legacy['spawns']['lithium_ore']['parameters'].update(size=5,variation=0,frequency=2.75,minHeight=10,maxHeight=50)
        legacy['spawns']['sulfur_ore']['enabled']=False
        imported=setup('os3-import');write_json(imported/'config/orespawn3/baseminerals.json',legacy)
        old_hash=digest(imported/'config/orespawn3/baseminerals.json')
        result=check(imported)
        p=placement(result['profile']['ores'][PREFIX+'lithium_ore']);subset({'min_y':10,'max_y':49,'frequency':2.75,'quantity':5},p)
        assert not result['profile']['ores'][PREFIX+'sulfur_ore']['enabled']
        assert digest(imported/'config/orespawn3/baseminerals.json')==old_hash
        check(imported,'reload')
        os1=setup('os1-import');write_json(os1/'config/orespawn/baseminerals.json',{'dimensions':[{'dimension':'0','ores':[{'blockID':'baseminerals:lithium_ore','size':4,'variation':0,'frequency':1.5,'minHeight':8,'maxHeight':40}]}]})
        result=check(os1);matches=[o for o in result['profile']['ores'].values() if o.get('block')=='baseminerals:lithium_ore'];assert len(matches)==1
        assert '1.5' in json.dumps(matches)
        # Saved world settings must remain authoritative without the old global import file.
        reloaded=check(setup('os1-profile-reload',world=os1/'world'),'later',{'minerals.sample':'true','minerals.width':'16'})
        assert len([o for o in reloaded['profile']['ores'].values() if o.get('block')=='baseminerals:lithium_ore'])==1,'OS1 reload reinserted the native lithium budget'
        existing=setup('saved-profile-precedence',world=source)
        profile=existing/'world/serverconfig/orespawn-worldgen.json';value=json.loads(profile.read_text())
        for p in placements(value['ores'][PREFIX+'salt_ore']):p['frequency']=1.25
        write_json(profile,value)
        override=copy.deepcopy(native)
        for p in placements(override['ores'][PREFIX+'salt_ore']):p['frequency']=9
        write_json(existing/'config/baseminerals-orespawn.json',override);write_json(existing/'config/orespawn3/baseminerals.json',legacy)
        result=check(existing);assert placement(result['profile']['ores'][PREFIX+'salt_ore'])['frequency']==1.25
        future=check(setup('native-precedence',override=override));assert placement(future['profile']['ores'][PREFIX+'salt_ore'])['frequency']==9
        # Record the actual baked attempts while delegating shape placement to OS4.
        recorded=copy.deepcopy(native)
        for name in NAMES:
            for p in placements(recorded['ores'][PREFIX+name+'_ore']):
                settings={k:p[k] for k in ('spread','vertical_spread','node_size')}
                settings['length']=8;p['pattern']={'type':'basemineralsprobe:'+name,'settings':settings}
        overworld=check(setup('recorded-overworld',override=recorded),properties={'minerals.record':'true','minerals.sample':'true','minerals.width':'16'})
        fixture=check(setup('recorded-host-fixture',override=recorded),properties={'minerals.custom':'true','minerals.record':'true','minerals.sample':'true','minerals.negativeHosts':'true','minerals.width':'16'})
    for name in NAMES:
        p=placement(native['ores'][PREFIX+name+'_ore']);rate=p['frequency'];fraction=rate%1
        tolerance=5*math.sqrt(256*fraction*(1-fraction)) if fraction else 0
        # Both exact-domain and selector routes receive one placement budget.
        # Different dimension population paths need not consume identical RNG.
        for report in (overworld,fixture):
            stats=report['recording'][name]
            assert abs(stats['attempts']-256*rate)<=tolerance,(name,stats['attempts'],rate)
            assert p['min_quantity']<=stats['minQuantity']<=stats['maxQuantity']<=p['max_quantity']
            assert not any(n for y,n in enumerate(stats['heights']) if y<p['min_y'] or y>p['max_y'])
            if p['height_distribution']=='triangle':
                low,high=p['min_y'],p['max_y'];middle=(high-low+1)//3
                assert sum(stats['heights'][low+middle:high-middle+1])>sum(stats['heights'][low:low+middle]),name
            if p['height_distribution']=='bottom_triangle':
                midpoint=(p['min_y']+p['max_y'])//2
                assert sum(stats['heights'][:midpoint+1])>sum(stats['heights'][midpoint+1:]),name
    # Same engine/JVM/seed, three isolated runs for both sets of declarations.
    historical=copy.deepcopy(native)
    for name in NAMES:
        for p in placements(historical['ores'][PREFIX+name+'_ore']):p.update(min_y=0,max_y=31 if name=='sulfur' else 95,frequency=8 if name in ('lithium','niter','salt') else 6,min_quantity=4,max_quantity=11,pattern='default',height_distribution='uniform')
    timings={}
    import statistics
    for label,definition in [('historical',historical),('rebalanced',native)]:
        times=[]
        for index in range(3):
            result=check(setup('timing-'+label+'-'+str(index),override=definition),properties={'minerals.custom':'true','minerals.sample':'true','minerals.width':'16'})
            times.append(result['dimensions']['42']['generationNanos'])
        timings[label]={'runs':times,'median':statistics.median(times)}
    timings['ratio']=timings['rebalanced']['median']/timings['historical']['median'];write_json(args.output/'timings.json',timings)
    assert timings['ratio']<=1.2,('Generation regression needs investigation',timings)
    assert fingerprint(source)==before
    print('BASE_MINERALS_PACKAGED_SUITE_PASS',len(results),'runs',flush=True)

if __name__=='__main__':main()
