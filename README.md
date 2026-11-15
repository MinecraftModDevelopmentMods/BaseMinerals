# Base Minerals

Base Minerals adds lithium, niter, phosphorus, potash, salt, saltpeter, sulfur
and silicon. The seven ores use KiriCattus's overlays. OreSpawn handles
placement, including the saved rules and shared ore sources.

This branch targets Minecraft 1.13.2 and Forge 25.0.223. Install
[OreSpawn 4.1.7.113021](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9611746).
[Base Metals 3.0.3.113021](https://github.com/MinecraftModDevelopmentMods/BaseMetals/releases/tag/3.0.3.113021)
is optional in new worlds and supplies the Crack Hammer. Keep Base Metals
installed when upgrading a world that already contains its blocks or items.
Use 3.0.3.113021 or a later compatible 1.13.2 build when installing the two together.

Base Minerals shares its ore crushing recipes with Base Metals when installed.
If Base Metals is absent, Modern Metals crackhammers can crush the ores instead.

## Upgrading a world

Copy the whole save folder before opening it on 1.13.2, including level.dat
and serverconfig. Start the copy with Forge, Base Minerals, OreSpawn and the
1.13.2 versions of any other mods whose content it uses. Check existing blocks
and inventories, save, then reopen the copy.

Base Minerals reads the old Forge registry from the world and saves the
mineral ID mapping alongside it. Keep baseminerals-legacy-registry.dat with the
save because previously unloaded chunks may still use the old numeric IDs.
Placed molten lithium and silicon keep their fluid level. The old Forge filled
buckets become the matching named buckets.

See [building](docs/BUILDING.md), [ore generation](docs/ORE_GENERATION.md),
[upgrade checks](docs/WORLD_UPGRADES.md) and [known limits](docs/KNOWN_ISSUES.md).
