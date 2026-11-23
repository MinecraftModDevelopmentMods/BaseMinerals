# Base Minerals

Base Minerals adds lithium, niter, phosphorus, potash, salt, saltpeter, sulfur
and silicon. The seven ores use KiriCattus's overlays. OreSpawn handles
placement, including the saved rules and shared ore sources.

This branch targets Minecraft 1.14.4 and Forge 28.2.26. Install
[OreSpawn 4.1.7.114041](https://github.com/MinecraftModDevelopmentMods/OreSpawn/releases/tag/4.1.7.114041).
[Base Metals 3.0.2.114041](https://github.com/MinecraftModDevelopmentMods/BaseMetals/releases/tag/3.0.2.114041)
is optional in new worlds and supplies the Crack Hammer. Keep Base Metals
installed when upgrading a world that already contains its blocks or items.
Use 3.0.2.114041 or a later compatible 1.14.4 build when installing the two together.

Base Minerals shares its ore crushing recipes with Base Metals when installed.
If Base Metals is absent,
[Modern Metals 3.0.0.114041](https://github.com/MinecraftModDevelopmentMods/ModernMetals/releases/tag/3.0.0.114041)
crackhammers can crush the ores instead. Both metal mods remain optional.

Mining lithium returns one ore block. Niter, phosphorus, potash, salt,
saltpeter and sulfur drop 2–4 powders, with Fortune increasing the upper
limit by its level. Silk Touch returns one ore block. A Crack Hammer gives
two powders per ore, as on the earlier branches.

Lithium and silicon smelting also works in the blast furnace, with the same
outputs and experience at half the cooking time. The old smith ingot trades
are available from level-two armorers, toolsmiths and weaponsmiths.

## Upgrading a world

Copy the whole save folder before opening it on 1.14.4, including level.dat
and serverconfig. Start the copy with Forge, Base Minerals, OreSpawn and the
1.14.4 versions of any other mods whose content it uses. Check existing blocks
and inventories, save, then reopen the copy.

Base Minerals reads the old Forge registry from the world and saves the
mineral ID mapping alongside it. Keep baseminerals-legacy-registry.dat with the
save because previously unloaded chunks may still use the old numeric IDs.
Placed molten lithium and silicon keep their fluid level. The old Forge filled
buckets become the matching named buckets.

See [building](docs/BUILDING.md), [ore generation](docs/ORE_GENERATION.md),
[upgrade checks](docs/WORLD_UPGRADES.md), [port decisions](docs/PORTING_114.md)
and [known limits](docs/KNOWN_ISSUES.md).
