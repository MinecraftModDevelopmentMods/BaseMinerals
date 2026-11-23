# Manual testing on 1.14.4

Refresh the inner BaseMinerals Gradle project in Eclipse. Start with
runClientWithBoth.launch to load the pinned local Base Metals and Modern Metals
fixes. Run pinLocalCompanions and regenerate launches after rebuilding those
siblings. runClient.launch is standalone;
runClientWithBaseMetals.launch and runClientWithModernMetals.launch test the
two companions separately. Each profile has its own run directory.

Use a disposable world for these checks:

1. Look at all seven ores in terrain and in the inventory. Check their stone
   layer, transparent overlays and names. Mine them with ordinary picks,
   Fortune, Silk Touch and the installed Crack Hammers.
2. Check lithium and silicon smelting in a furnace and blast furnace, storage
   crafting and crushing, and the two molten buckets. Place and collect each
   fluid, check its appearance and light, then save and reopen the world.
3. Open a copy of an older save with the matching companion mods installed.
   Check familiar mineral deposits, stored items, placed molten blocks and
   buckets. Save and reopen the copy. Keep its saved OreSpawn profile and
   baseminerals-legacy-registry.dat together. See WORLD_UPGRADES.md for the
   expected Forge 25 vanilla-registry prompts.

The older material settings screens and Tinkers integration remain unavailable,
as on the released 1.13.2 branch. Change generation through OreSpawn. Automated
checks cover the main content and migration paths; visual and ordinary gameplay
acceptance are still needed before release.
