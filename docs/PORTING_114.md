# Minecraft 1.14.4 port

The starting point is released Base Minerals 2.5.2.113021, commit 6206d8a6.
The target is Forge 28.2.26, MCP snapshot 20190719-1.14.3/config
20190829.143755, Java 8, ForgeGradle 7.0.34 and Gradle 9.6.1. Gradle uses
Java 17 and Mavenizer uses Java 25.

## Target decisions

- Keep the baseminerals namespace, 17 block IDs, 41 item IDs, four fluid IDs,
  translations and all existing recipe IDs. No equipment or Fantasy policy
  is added. Niter and saltpeter remain distinct; silicon has no generated ore.
- Move block drops to native loot tables. Lithium returns one ore block;
  the other six ores return a uniform 2..4+Fortune powders. A small registered
  loot function preserves that exact distribution. Silk Touch returns one
  ore, weak tools return nothing, and hammers use the shared crushing recipe.
- Keep the restored hardness, mining tiers, effective blast resistance and
  storage-block beacon eligibility from 2.5.2.
- Use Forge 28 recipe serializers and native recipe types. Base Metals owns
  shared crushing when installed; otherwise Modern Metals supplies it. The
  contribution IDs stay under baseminerals. The seven existing smelts also
  work in the blast furnace at half the cooking time with the same outputs/XP.
- Register fluids through Forge's fluid registry after constructing the pairs
  in the sequential block event. Native fluid attributes replace Forge 25's
  renderer patch. Keep molten light, colour, flow properties, dedicated
  buckets and heat. The generic bucket mask/overlay artwork is reused from
  the MMD Modern Metals sibling; existing ore artwork stays unchanged.
- Map the level-two ingot offers to armorer, toolsmith and weaponsmith, with
  native restocking and villager experience.
- Preserve world-specific numeric mappings in baseminerals-legacy-registry.dat.
  Hook the target SaveFormat and ChunkLoader before vanilla data fixing; the
  chunk loader's own region directory selects the correct world's mapping.
  The numeric-state lookup remains scoped to the conversion thread, and
  upgraded completed chunks use 1.14's full status. Forge 25's leaves-fixer
  repair and fluid-renderer hooks are not part of the Forge 28 port.
- Keep the same schema-5 OreSpawn provider, seven stable rule IDs, quantities,
  shapes, heights, material groups and standard placement channel. OreSpawn
  owns generation; the mod contributes no terrain, strata or fallback engine.
- Pin the public OreSpawn 4.1.7.114041 and Base Metals 3.0.2.114041 artifacts
  by checksum. Modern Metals 1.14 is a separately fingerprinted local test
  fixture and remains optional. Do not present it as a published dependency.

References: [Forge's porting notes](https://docs.minecraftforge.net/en/1.14.x/legacy/porting1214/),
[Forge registries](https://docs.minecraftforge.net/en/1.14.x/concepts/registries/),
[Minecraft 1.14.4 notes](https://www.minecraft.net/en-us/article/minecraft-java-1-14-4-released),
the exact mapped Forge/Minecraft sources, and the public
[OreSpawn 4.1.7.114041 release](https://github.com/MinecraftModDevelopmentMods/OreSpawn/releases/tag/4.1.7.114041).

The local verification receipt records actual tests and limitations. Manual
gameplay/visual acceptance and any publication are separate steps.
