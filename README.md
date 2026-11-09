# Base Minerals

Base Minerals adds lithium, niter, phosphorus, potash, salt, saltpeter, silicon
and sulfur to Base Metals. It provides powders, storage blocks and, for some
materials, ingots, blends and molten forms. Charcoal uses the material supplied
by Base Metals. Make silicon from sand and coal or charcoal dust.

The seven ores use KiriCattus's overlays on Minecraft's stone texture.

This branch targets Minecraft 1.11.2 and Forge 13.20.1.2588 or later. It requires
Base Metals 2.5.1.111021 and OreSpawn 4.1.6.111021, or compatible versions in the
declared ranges. Mineralogy is optional. Tinkers' Construct adds melting and
casting for lithium and silicon.

Material switches remain in `config/BaseMinerals.cfg` and require a full restart.
Open the same screen from Forge Mods or OreSpawn's Mods directory. Configure ore
placement and shared ore outputs in OreSpawn. New worlds use different depths
and deposit shapes for each mineral. Vanilla stone and its variants are valid
hosts, and Base Minerals does not replace terrain.

Existing registry names and configuration keys are kept. Saved OreSpawn profiles
and OS1 and OS3 imports keep their settings. OreSpawn 4.1.6 preserves the
imported lithium rule when an OS1 world moves without its original config.
Defaults for new worlds can be changed in `config/baseminerals-orespawn.json`.

See [ore generation](docs/ORE_GENERATION.md), [building](docs/BUILDING.md),
[verification](tools/validation/legacy111/README.md) and
[release preparation](docs/RELEASING.md).

See the [known issues](docs/KNOWN_ISSUES.md) for historical mod compatibility.
