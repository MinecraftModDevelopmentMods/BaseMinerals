"""Parse resources and validate exported native declarations against the pinned engine."""
import argparse,json,re,zipfile
from pathlib import Path
from referencing import Registry,Resource
from jsonschema import Draft202012Validator
import yaml
from run_runtime_checks import ROOT,dependency

def main():
    parser=argparse.ArgumentParser();parser.add_argument('--mirror',type=Path);args=parser.parse_args()
    count=0
    resources=ROOT/'build/resources/main'
    for path in resources.rglob('*'):
        if path.is_file() and (path.suffix in ('.json','.mcmeta') or path.name=='mcmod.info'):
            json.loads(path.read_text(encoding='utf-8-sig'));count+=1
    assert json.loads((resources/'pack.mcmeta').read_text())['pack']['pack_format']==3
    specs=json.loads((ROOT/'gradle/dependencies.json').read_text())
    with zipfile.ZipFile(dependency(next(s for s in specs if s['id']=='orespawn'),args.mirror)) as jar:
        schemas=[json.loads(jar.read(n)) for n in jar.namelist() if n.startswith('META-INF/orespawn/docs/schemas/') and n.endswith('.json')]
    registry=Registry().with_resources((s['$id'],Resource.from_contents(s)) for s in schemas)
    schema=next(s for s in schemas if s['$id'].endswith('/orespawn-provider.schema.json'))
    provider=json.loads((ROOT/'build/reports/native-provider.json').read_text())
    Draft202012Validator(schema,registry=registry).validate(provider)
    assert len(provider['ores'])==7
    assert all(not provider.get(section) for section in ['rocks','terrain_dimensions','geomes','fluid_deposits','biome_palettes'])
    assets=resources/'assets/baseminerals'
    source_textures=ROOT/'src/main/resources/assets/baseminerals/textures/blocks'
    for ore in provider['ores'].values():
        name=ore['block'].split(':',1)[1]
        state=json.loads((assets/'blockstates'/f'{name}.json').read_text())
        item=json.loads((assets/'models/item'/f'{name}.json').read_text())
        model=json.loads((assets/'models/block'/f'{name}.json').read_text())
        assert state['variants']['normal']['model']==f'baseminerals:{name}',name
        assert item['parent']==f'baseminerals:block/{name}',name
        textures=assets/'textures/blocks'
        if (textures/'ore_overlays'/f'{name}.png').is_file():
            assert model['parent']=='baseminerals:block/ore_overlay',name
            assert (assets/'models/block/ore_overlay.json').is_file(),name
            assert model['textures']=={
                'particle':'minecraft:blocks/stone','base':'minecraft:blocks/stone',
                'overlay':f'baseminerals:blocks/ore_overlays/{name}'
            },name
            if not (source_textures/f'{name}.png').is_file():
                assert not (textures/f'{name}.png').exists(),f'{name}: stale solid texture'
        else:
            assert (textures/f'{name}.png').is_file(),f'{name}: missing solid texture'
            assert model['parent']=='block/cube_all',name
            assert model['textures']=={'all':f'baseminerals:blocks/{name}'},name
    assert {'en_us.lang','de_de.lang'} <= {p.name for p in (assets/'lang').glob('*.lang')}
    language=dict(line.split('=',1) for line in (assets/'lang/en_us.lang').read_text(encoding='utf-8').splitlines() if '=' in line and not line.startswith('#'))
    for name in ['charcoal','lithium','niter','phosphorus','potash','salt','saltpeter','silicon','sulfur']:
        assert 'config.baseminerals.enable_'+name in language,name
    for name in ['lithium','silicon']:
        assert 'config.baseminerals.fluid_'+name in language,name
    for path in (ROOT/'.github/workflows').glob('*.yml'):
        workflow=yaml.load(path.read_text(),Loader=yaml.BaseLoader);assert workflow.get('on') and workflow.get('jobs'),path
        for job in workflow['jobs'].values():
            for step in job.get('steps',[]):
                if 'uses' in step:assert re.fullmatch(r'[^@]+@[0-9a-f]{40}',step['uses']),step['uses']
    release=(ROOT/'.github/workflows/deploy-release.yml').read_text()
    assert all(value in release for value in [
        'confirm_live_publication','270972','112021) minecraft_version=1.12.2; branch=master-1.12',
        '261744(required)','240967(optional)','240967(required)','245586(required)'
    ])
    print(f'BASE_MINERALS_RESOURCES_PASS json={count} provider_schema=5 workflows=5')

if __name__=='__main__':main()
