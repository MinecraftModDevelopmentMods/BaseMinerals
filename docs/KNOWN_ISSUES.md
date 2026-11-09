# Known issues for Minecraft 1.12.2

## Moving an OreSpawn 1 configuration

The 1.12.2 importer reads OreSpawn 2 and 3 configuration files. For an OreSpawn
1 configuration, use OreSpawn 4 on 1.10.2 or 1.11.2 to import it before
moving the world. Carry the resulting `serverconfig/orespawn-worldgen.json`
with the save.

[OreSpawn 4.1.6.112021](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9527784)
keeps the saved imported lithium rule. It backs up affected profiles before
removing an untouched duplicate. If the added rule or its source policy was
edited, OreSpawn preserves the edits and flags the rule for review.

## MMDLib version in Forge

The [published MMDLib 1.0.0 RC3 jar](https://www.curseforge.com/minecraft/mc-mods/mmdlib/files/9414723)
reports `1.0.0-rc2` in the Forge Mods list. Base Minerals builds and tests against
the exact RC3 download and accepts the version reported by its metadata.
