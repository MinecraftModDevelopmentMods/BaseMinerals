# Known issues for Minecraft 1.11.2

## OS1 profiles with an edited lithium rule

[OreSpawn 4.1.6.111021](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9524249)
keeps the imported lithium rule when a world moves without its original
configuration. It backs up affected profiles before removing an untouched
duplicate. If the added rule or its source policy was edited, OreSpawn
preserves the edits and flags the rule for review.

## Historical Electric Advantage dependencies

The historical Electric Advantage 2.2.0 and Power Advantage 2.3.0 jars target
Minecraft 1.10.2. In the 1.11.2 test, Power Advantage fails before world load
while linking `FluidDrainTileEntity`. That also blocks the combined case.

[Mineralogy 3.3.0.6](https://www.curseforge.com/minecraft/mc-mods/minecraft-mineralogy/files/2561871)
loads and passes the chunk census. OreSpawn reports its sulfur as an external
source. The synthetic source-policy fixture verifies managed output choices,
including source removal and restoration.
