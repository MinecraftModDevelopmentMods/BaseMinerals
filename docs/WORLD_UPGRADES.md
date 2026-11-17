# Upgrading Base Minerals worlds to 1.21.1

1. Close Minecraft or stop the server. Copy the whole world directory before
   upgrading, and keep the copy until you're happy with the result.
2. Install Base Minerals 4.0.0 or later, MMDLib 2.0.2 or later and MMD OreSpawn 4.1.1 or
   later on NeoForge for 1.21.1, plus 1.21.1 versions of the other mods that
   placed blocks or items in the world.
3. Open the world. Use Optimize World or the server's `--forceUpgrade`
   option to convert every region at once; otherwise chunks convert as you
   visit them.

## From 1.13.2

Base Minerals 2.5.1.113021 worlds upgrade directly. Every block, item and
fluid keeps its ID, and Minecraft converts their states and item stacks.

- Placed molten lithium and silicon keep their level. The molten block items
  that 1.13.2 registered still exist, outside the creative tab, so stacks of
  them in inventories survive.
- The OreSpawn rules keep their `baseminerals:legacy/<mineral>_ore` IDs.
- Tags moved from `forge:` to `c:`, so a data pack or script that names
  `forge:dusts/sulfur` should now name `c:dusts/sulfur`.
- `baseminerals-legacy-registry.dat`, written by the 1.13.2 upgrade, is no
  longer read and can be deleted once the world has been upgraded.

## From 1.12.2 and earlier

Upgrade the world on 1.13.2 with Base Minerals 2.5.1.113021 first. That
version reads the old Forge registry and keeps the minerals through Minecraft's
1.13 flattening, including molten fluid levels and Forge's old filled buckets;
1.21.1 cannot do this. Then follow the 1.13.2 steps above.
