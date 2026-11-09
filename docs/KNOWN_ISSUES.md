# Known issues for Minecraft 1.10.2

## OS1 profiles with an edited lithium rule

[OreSpawn 4.1.6.110021](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9518063)
keeps the imported lithium rule when a world moves without its original
configuration. It backs up an affected profile before removing an untouched
duplicate. The packaged tests cover the move and the repair of a copied 4.1.2
profile. If the added rule or its source policy was edited, OreSpawn preserves
the edits and flags the rule for review.

## Historical Electric Advantage dependencies

Electric Advantage 2.2.0 and Power Advantage 2.3.0 fail before world load with
Base Metals 2.6.0.110021 because Power Advantage references the old
`cyano.basemetals.init.Materials` class. The same failure occurs with
Mineralogy installed.

Base Electric 2.2.6.110021 and Base Power 2.4.4.110021 passed the fresh and
reload shared-ore checks with the pinned Base Metals and OreSpawn builds.
See [ore generation](ORE_GENERATION.md) for the tested defaults.
