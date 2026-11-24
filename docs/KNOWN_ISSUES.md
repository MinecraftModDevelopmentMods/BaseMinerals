# Known limits for Minecraft 1.15.2

The 1.12 material and molten-form switches in BaseMinerals.cfg and their settings
screens have not been ported. The registries keep the old content available
for saved blocks and inventories. Use OreSpawn to change ore generation.
The 1.12 Tinkers' Construct integration does not carry over to Forge 31.

OreSpawn 4.1.7.115021 writes empty dimensions or dimension_selectors objects
for some vanilla and companion-mod rules. Its published JSON schema rejects
those empty objects, although the engine loads and runs them. The Base Minerals
rules validate against that schema. This needs an OreSpawn serialization/schema
correction before claiming full saved-profile schema validation; the local
receipt retains the failures.

OreSpawn 1 settings need to be imported on a supported older installation before
moving the world. Carry the resulting saved profile with the world.

An already-converted world whose numeric registry or original chunks were
discarded requires the original backup for lossless conversion.
