const assert = require('node:assert/strict');
const test = require('node:test');
const fs = require('node:fs');
const path = require('node:path');
const root = path.join(__dirname, '../src/main/resources');
const minerals = ['lithium', 'niter', 'phosphorus', 'potash', 'salt', 'saltpeter', 'silicon', 'sulfur'];
// The accepted 1.13 registry contract: eight stores, seven ores, powders, two metals and buckets.
const active = new Set(minerals.flatMap(m => [m + '_block', m + '_powder', m + '_smallpowder']));
minerals.filter(m => m !== 'silicon').forEach(m => active.add(m + '_ore'));
for (const m of ['lithium', 'silicon']) for (const suffix of ['', '_ingot', '_nugget', '_bucket', '_molten']) active.add(m + suffix);
active.add('silicon_blend'); active.add('silicon_smallblend'); active.add('ore_overlay');
function files(dir) {
  return fs.readdirSync(dir, {withFileTypes: true}).flatMap(e => e.isDirectory()
    ? files(path.join(dir, e.name)) : [path.join(dir, e.name)]);
}
test('source JSON parses and registered-content model textures resolve', () => {
  let count = 0;
  for (const file of files(root).filter(f => /\.(json|mcmeta)$/.test(f))) {
    const value = JSON.parse(fs.readFileSync(file, 'utf8'));
    count++;
    if (!file.includes(path.join('assets', 'baseminerals', 'models'))) continue;
    // The inherited tree also contains unused models for content never registered by this mod.
    if (!active.has(path.basename(file, '.json'))) continue;
    for (const texture of Object.values(value.textures || {})) {
      if (!texture.startsWith('baseminerals:')) continue;
      const png = path.join(root, 'assets/baseminerals/textures', texture.split(':')[1] + '.png');
      assert.ok(fs.existsSync(png), `${file} references missing ${texture}`);
    }
  }
  assert.ok(count > 250);
});

test('canonical Forge block and item tags include every ore and storage block', () => {
  function members(kind, id, seen = new Set()) {
    assert.ok(!seen.has(id), `Tag cycle at ${id}`);
    const next = new Set([...seen, id]);
    const [namespace, name] = id.split(':');
    const tag = JSON.parse(fs.readFileSync(path.join(root, 'data', namespace, 'tags', kind, name + '.json'), 'utf8'));
    assert.equal(tag.replace, false, `Tag must merge with companion mods: ${id}`);
    return tag.values.flatMap(value => value.startsWith('#') ? members(kind, value.slice(1), next) : [value]);
  }
  for (const kind of ['blocks', 'items']) {
    const ores = members(kind, 'forge:ores');
    const storage = members(kind, 'forge:storage_blocks');
    for (const mineral of minerals) {
      if (mineral !== 'silicon') assert.ok(ores.includes(`baseminerals:${mineral}_ore`), `${kind}: ${mineral} missing from forge:ores`);
      assert.ok(storage.includes(`baseminerals:${mineral}_block`), `${kind}: ${mineral} missing from forge:storage_blocks`);
    }
  }
  for (const owner of ['basemetals', 'modernmetals']) {
    const crushable = members('blocks', owner + ':crackhammer_crushable');
    for (const mineral of minerals) {
      if (mineral !== 'silicon') assert.ok(crushable.includes(`baseminerals:${mineral}_ore`), `${owner}: ${mineral} ore is not hammer crushable`);
      assert.ok(crushable.includes(`baseminerals:${mineral}_block`), `${owner}: ${mineral} storage is not hammer crushable`);
    }
  }
});
