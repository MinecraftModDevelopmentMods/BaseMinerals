# Minecraft 1.15.2 port

The baseline is released Base Minerals 2.5.2.114041, MMD merge e30ddcb.
The target is 2.5.2.115021, Forge 31.2.57, MCP snapshot 20200514-1.15.1
(config 20200515.085601), Java 8. Gradle 9.6.1/ForgeGradle 7.0.34,
Renamer 1.1.5 and Foojay 1.0.0 remain; Gradle uses Java 17 and Mavenizer Java 25.

## Decisions

- Keep all 17 block, 41 item and four fluid IDs, native recipes/loot, translations,
  bucket identities, material properties and the exact Fortune powder formula.
  Keep the seven ore defaults and their stable OreSpawn rule/material/channel
  IDs. No new minerals, terrain, retrogen, equipment or content-mode policy.
- Retain the additive hammer whitelist contributions and canonical Forge tags.
  Qualification must exercise normal mining progress and actual client/server
  removal, drops and durability, including salt with the iron Crack Hammer.
- Register ore and molten render layers in client setup through Forge 31's
  RenderTypeLookup. Keep the overlays, native fluid attributes, bucket layers
  and colour handlers. Resource pack format is 5. No Forge blockstate extension
  or legacy render-layer method is carried forward.
- Forge 31's ChunkLoader owns an IOWorker instead of extending RegionFileCache.
  Capture that loader's constructor directory in its own injected field/getter
  and use it to locate the world-specific numeric mineral registry during data
  fixing. Keep the mapping sidecar for later unloaded chunks. Do not derive the
  world from a global current-save variable or a single shared numeric map.
- Continue to enqueue the typed schema-5 provider through OreSpawn's public
  API. Pin public OreSpawn 4.1.7.115021 and optional Base Metals 3.0.3.115021 by
  SHA-256. OreSpawn owns placement and saved-profile merging.
- No matching Modern Metals 1.15 artifact or checkout was found at port start.
  Do not load its 1.14 jar. Keep the optional crushing bridge and future local
  pin support, but qualify the currently available standalone/Base Metals paths.
- The existing material-settings screen and Tinkers limitations remain. This
  port does not introduce substitutes for integrations absent on the target.

References: [Forge 1.15 porting notes](https://docs.minecraftforge.net/en/1.15.x/legacy/porting/),
the [linked rendering primer](https://gist.github.com/williewillus/30d7e3f775fe93c503bddf054ef3f93e),
[Minecraft 1.15.2 release notes](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-15-2),
the exact mapped Forge 31.2.57 classes, and
[OreSpawn's public 4.1.7.115021 release](https://github.com/MinecraftModDevelopmentMods/OreSpawn/releases/tag/4.1.7.115021).

The local receipt records the tested artifact hashes, actual gates and any
remaining limits. Manual acceptance and publication are separate steps.
