# Base Minerals for Minecraft 1.21.1

Base Minerals adds lithium, niter, phosphorus, potash, salt, saltpeter, sulfur
and silicon. Niter, phosphorus, potash, salt, saltpeter and sulfur ores drop 2
to 4 powders, more with Fortune; lithium ore drops itself and needs crushing.
Store the powders as blocks, and smelt silicon into ingots. Saltpeter or potash
with sulfur and coal makes gunpowder. Lithium and silicon also come as molten fluids in buckets.

## Installation

Use Java 21 and NeoForge **21.1.265 or later for Minecraft 1.21.1** on both
client and server. Base Minerals needs:

- [MMDLib](https://www.curseforge.com/minecraft/mc-mods/mmdlib) 2.0.2 or later.
  The minerals, their items and blocks are MMDLib materials.
- [MMD OreSpawn](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn)
  4.1.1.121012 or a later 4.x release, which places the ores.

Base Metals is optional. Its crackhammers and crushers use MMDLib's shared
`mmdlib:crushing` recipes: each ore gives two powders, a storage block nine,
and lithium and silicon ingots and nuggets give a powder and a small powder.

## Upgrading a world

Copy the world before you open it. Worlds from Base Minerals 2.5.1 for 1.13.2
upgrade directly: block, item and fluid names are unchanged. Worlds from 1.12.2
or earlier must first be upgraded with Base Minerals 2.5.1.113021 on 1.13.2.
[World upgrade details](docs/WORLD_UPGRADES.md)

See [building](docs/BUILDING.md), [ore generation](docs/ORE_GENERATION.md),
[releasing](docs/RELEASING.md) and [known limits](docs/KNOWN_ISSUES.md).
