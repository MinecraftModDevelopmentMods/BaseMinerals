# Base Minerals 1.15.2 upgrade checks

The older saves store a Forge registry snapshot alongside their numeric chunks.
Two saves may assign different numbers to the same mineral block. The upgrade
uses the registry in the save being opened, supplies named states only during
that chunk's flattening pass, and keeps the mapping for later reloads.
Ordinary mineral blocks have one state. The two molten blocks map the old
metadata nibble to the named level property. Existing item names carry forward,
and Forge's old universal buckets become named lithium and silicon buckets.

The packaged test runner copies the source world and fingerprints every file.
It checks every original chunk after conversion and reload. It compares exact
mineral coordinates and states, chest inventory slots, stack sizes, custom
names, unrelated NBT tags and the saved OreSpawn mineral rules. A test chest
and placed blocks extend coverage to every item and block in the source
registry. The fixture also covers all 16 fluid levels for each registered
molten block. Each original chunk must reach the flattened 1.15.2 format
(DataVersion 2230 or later). Already flattened 1.13.2 and 1.14.4 saves are checked with
the separate named-world runner, including their inventories and saved rules.

    python tools/validation/port115/verify_upgrade.py --java <java8> --runtime <forge-server-runtime> --source <old-world> --output <new-run-directory> --mods <baseminerals.jar> <orespawn.jar> [basemetals.jar]

Use verify_named_upgrade.py with the same arguments for a 1.13.2 or 1.14.4 source. It
does not insert the numeric fixture or alter the source world. Keep
baseminerals-legacy-registry.dat with any previously flattened save: chunks
that were never loaded may still need its original registry mapping.

The default probe chunk is (64, 64). Supply --probe-chunk X Z to use a different
generated chunk outside the spawn area. Add --natural-open to check a normal
first open before converting the remaining chunks. Use verify_census.py with
--seed and --probe to check new ores across all three vanilla dimensions.

Keep the original world and its mod set together. Forge and any companion mods
own conversion of their own blocks, items and saved data.

Forge 31 may ask for confirmation when opening a Forge 25 world because the
old villager-profession registry was removed and the vanilla signs and three
dye IDs were renamed. The disposable runner accepts only those exact vanilla
lists and records the prompts. It stops for any unknown missing content.
Do not use blanket confirmation on a player world; inspect its missing list.
