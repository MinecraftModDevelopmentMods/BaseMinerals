# Ore generation

Base Minerals supplies one native schema-5 declaration through OreSpawn's public
API. OreSpawn owns baking and execution. This mod adds no fallback generator,
rocks, terrain, biomes, strata or fluid deposits, and needs no Mineralogy.

## Fresh-world defaults

Ranges are inclusive. Frequency is expected attempts per chunk. Quantity is a
requested block budget, so veins can yield less when hosts are unavailable.
The tuned ranges below apply to fresh Overworld defaults. Other ordinary
dimensions retain the previous fallback ranges and budgets.

| Mineral | Y | Attempts/chunk | Quantity | Shape |
| --- | --- | ---: | --- | --- |
| Lithium | -32..56 | 3 | 15 | Vein |
| Niter | 32..88 | 1.5 | 12 | Clusters |
| Salt | 16..80 | 0.55 | 20 | Flat clusters |
| Sulfur | -32..64 | 1.5 | 15 | Clusters |
| Phosphorus | -16..64 | 1.5 | 15 | Clusters |
| Potash | 8..64 | 0.8 | 18 | Flat clusters |
| Saltpeter | 40..96 | 1.25 | 12 | Clusters |

Lithium and phosphorus use a triangular height curve. Sulfur favors the bottom
of its range, and the others use uniform heights. The new Overworld defaults reduce excessive soluble-mineral supply while
retaining the previous shapes and the fallback budgets in other ordinary dimensions. These are gameplay
approximations rather than geological simulations.

Lithium uses vein length 8 and node size 3. Cluster horizontal spread, vertical
spread and node size are respectively niter `(4,2,3)`, salt `(8,0,8)`, sulfur
`(4,2,4)`, phosphorus `(6,1,6)`, potash `(6,0,6)` and saltpeter `(4,1,3)`.
The balance draws on [lithium pegmatites](https://www.usgs.gov/data/lithium-deposits-united-states),
[evaporite potash](https://www.usgs.gov/publications/potash-a-global-overview-evaporate-related-potash-resources-including-spatial)
and [sedimentary phosphate](https://pubs.usgs.gov/bul/b1693/html/bull74kl.htm).

Fresh Overworld rules accept exact natural stone/deepslate fallbacks, not polished
blocks. Other ordinary dimensions keep their historical fallback hosts. Geological hosts are
intrusive igneous rocks for lithium, sedimentary rocks for nitrates, salt, potash
and phosphorus, and sedimentary or volcanic rocks for sulfur. There are no hard biome restrictions. Salt, potash and nitrates favour dry basins;
phosphorus favours sedimentary provinces and lithium favours intrusive settings. Rules cover ordinary dimensions except vanilla Nether/End.
Silicon and charcoal do not generate.

## Saved settings and shared ores

Rule IDs remain `baseminerals:legacy/<mineral>_ore`. Canonical materials use
`orespawn:<mineral>`, with the `orespawn:standard` placement channel. Niter and
saltpeter remain separate. Sulfur/Sulphur aliases refer to the same material.

OreSpawn's reviewed source policies can share one placement budget across
equivalent outputs. Sulfur priority is Mineralogy, Base Minerals, then Base
Electric. Lithium priority is Base Minerals then Base Electric. Only actual
loaded/managed sources qualify for automatic consolidation. An external generator
does not become controllable merely through a shared tag.
An existing profile choice remains authoritative even after sources become
manageable. Review the resulting source groups in OreSpawn before changing them.

Native global overrides use `config/baseminerals-orespawn.json`. Existing world
profiles and native overrides take precedence over these defaults. The explicit
Overworld rule overrides the ordinary-dimension fallback, so each chunk receives
a single placement budget. Retrogen stays disabled.
