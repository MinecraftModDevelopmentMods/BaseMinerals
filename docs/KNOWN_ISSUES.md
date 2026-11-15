# Known limits for Minecraft 1.21.1

The 1.12 material and molten-form switches in BaseMinerals.cfg and their
settings screens have not been ported. Use OreSpawn to change ore generation.
The 1.12 Tinkers' Construct integration does not carry over.

Worlds from 1.12.2 and earlier must be upgraded on 1.13.2 first; see
[world upgrades](WORLD_UPGRADES.md).

Ore depths are the 1.13.2 ranges, all above Y 4. The deeper 1.18+ caves below
Y 0 don't hold Base Minerals ores unless an OreSpawn profile changes them.

Lithium ore, and mineral ores taken with Silk Touch, become powders only
through the `mmdlib:crushing` recipes, which need a crackhammer or crusher from
another MMD mod such as Base Metals. A crackhammer crushes a mined ore whole,
without Fortune, so it gives two powders where a pickaxe gives two to four.
