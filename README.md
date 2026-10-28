# Base Minerals

Base Minerals adds lithium, niter, phosphorus, potash, salt, saltpeter, silicon
and sulfur to Base Metals. It provides powders, storage blocks and, for some
materials, ingots, blends and molten forms. Charcoal uses the material supplied
by Base Metals. Silicon is made from sand and coal or charcoal dust; it does
not generate as ore.

This branch targets Minecraft 1.10.2 and Forge 12.18.3.2511 or later. It requires
Base Metals 2.5.1.110021 and OreSpawn 4.1.2.110021, or compatible versions in the
declared ranges. Mineralogy is optional. Tinkers' Construct adds melting and
casting for lithium and silicon.

Material switches remain in `config/BaseMinerals.cfg` and require a full restart.
Open the same screen from Forge Mods or OreSpawn's Mods directory. Configure ore
placement and shared ore outputs in OreSpawn. New worlds use different depths
and deposit shapes for each mineral. Vanilla stone and its variants are valid
hosts, and Base Minerals does not replace terrain.

Existing registry names and configuration keys are kept. Saved OreSpawn profiles
and OS3 imports keep their settings, and generated chunks are not rewritten.
OS1 imports have a known issue when a world is moved without its original global
import file; see the [known issues](docs/KNOWN_ISSUES.md). Defaults for new worlds
can be changed in `config/baseminerals-orespawn.json`.

See [ore generation](docs/ORE_GENERATION.md), [building](docs/BUILDING.md),
[verification](tools/validation/legacy110/README.md) and
[release preparation](docs/RELEASING.md).

The published Electric Advantage and Power Advantage versions also have a
compatibility problem with the required Base Metals version. The
[known issues](docs/KNOWN_ISSUES.md) describe both problems and the test results.
