# Base Minerals 1.13.2 upgrade checks

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
molten block. Each original chunk must use the flattened 1.13.2 format.

    python tools/validation/legacy113/verify_upgrade.py --java <java8> --runtime <forge-server-template> --source <old-world> --output <new-run-directory> --mods <baseminerals.jar> <orespawn.jar> [basemetals.jar]

The default probe chunk is (64, 64). Supply --probe-chunk X Z to use a different
generated chunk outside the spawn area. Add --natural-open to check a normal
first open before converting the remaining chunks. Use verify_generation.py
with the same Java, template, output and mod arguments to census new ores.

Keep the original world and its mod set together. Forge and any companion mods
own conversion of their own blocks, items and saved data.
