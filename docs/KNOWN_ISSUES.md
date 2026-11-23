# Known limits for Minecraft 1.14.4

The 1.12 material and molten-form switches in BaseMinerals.cfg and their settings
screens have not been ported. The registries keep the old content available
for saved blocks and inventories. Use OreSpawn to change ore generation.
The 1.12 Tinkers' Construct integration does not carry over to Forge 28.

Modern Metals 1.14 is currently tested using a frozen local candidate. Its
compatibility launch is available after staging that fixture; this does not
make Modern Metals a published or required dependency.

The development profiles also pin the locally fixed Base Metals bars and ore
overlays. That jar has the same version as the published build but a different
hash. Publish those companion fixes and update the public pins before treating
the local combined profile as release-dependency qualification.

OreSpawn 4.1.7.114041 writes empty dimensions or dimension_selectors objects
for some vanilla and companion-mod rules. Its published JSON schema rejects
those empty objects, although the engine loads and runs them. The Base Minerals
rules validate against that schema. This needs an OreSpawn serialization/schema
correction before claiming full saved-profile schema validation; the local
receipt retains the failures.

OreSpawn 1 settings need to be imported on a supported older installation before
moving the world. Carry the resulting saved profile with the world.

An already-converted world whose numeric registry or original chunks were
discarded requires the original backup for lossless conversion.
