# Building Base Minerals for Minecraft 1.21.1

The build follows the MMD mod template: Gradle 9.8 with NeoForge's
ModDevGradle, Java 21 and Parchment mappings. Gradle downloads Java 21 itself
if it isn't installed.

```sh
./gradlew build
```

On Windows, use `gradlew.bat`. The game jar is
`build/libs/BaseMinerals-4.0.1.121012-development.jar`; a release tag sets the
version instead (see [releasing](RELEASING.md)). `./gradlew runClient` and
`./gradlew runServer` start the game from `run/`.

MMDLib and OreSpawn come from the MMD Maven repository at
`https://maven.moddev.zone/releases`. Their versions are in `gradle.properties`.

Compilation fails on any use of deprecated or removal-marked API, in the mod
and its tests alike. Use the replacement the deprecation names; NeoForge's are
usually extension methods that take the level and position.

## Where things live

- `src/main/resources/mmdlib/material/` holds one MMDLib material per mineral:
  its forms, hardness, mining tier and block sound. MMDLib registers the
  powders, ingots, nuggets, blends, storage blocks and ores from these files.
- `src/main/resources/mmdlib/item/` adds Base Minerals' own forms:
  `baseminerals:powder`, which keeps the `<mineral>_powder` IDs, and
  `baseminerals:bucket`, which fills the molten fluids.
- Molten lithium and silicon are registered in `content/ModContent.java`, so
  their fluid and block IDs match older saves.
- `src/main/resources/assets/` holds the textures, models and language files.
  Ore models are generated at build time by `gradle/ore-models.gradle` from the
  overlay textures in `textures/block/ore_overlays/`.
- `src/main/resources/data/` holds the recipes, loot tables and tags.

## Tests

`./gradlew check` runs both suites:

- `src/test/` holds JUnit tests that run with the game, MMDLib, OreSpawn and
  Base Minerals loaded. They check that every 1.13.2 block, item and fluid ID is
  still registered, that the recipes, tags and loot tables load in a server,
  the OreSpawn declarations, the molten fluids and buckets, and that every
  block and item has its models, textures and English name.
- `src/gametest/` holds game tests, run by `./gradlew runGameTestServer` in a
  fresh world: molten minerals set entities on fire; ores drop powder (or
  raw lithium) without experience, with the Fortune and Silk Touch counts; and blocks sound like stone and resist explosions. The source set is
  not part of the published jar.

Use a fresh test world when checking the OreSpawn defaults. See
[ore generation](ORE_GENERATION.md) for the placement rules and overrides.
