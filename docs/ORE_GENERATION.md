# Ore generation

Base Minerals supplies one native schema-5 declaration through OreSpawn's public
API. OreSpawn owns baking and execution. This mod adds no fallback generator,
rocks, terrain, biomes, strata or fluid deposits, and needs no Mineralogy.

## Fresh-world defaults

Ranges are inclusive. Frequency is expected attempts per chunk; quantity is a
requested block budget, so veins can yield less when hosts are unavailable.

| Mineral | Y | Attempts/chunk | Quantity | Shape |
| --- | --- | ---: | --- | --- |
| Lithium | 8..47 | 4 | 12..18 | Vein |
| Niter | 16..63 | 5 | 8..16 | Clusters |
| Salt | 24..79 | 2.5 | 16..32 | Flat clusters |
| Sulfur | 4..31 | 3 | 12..18 | Clusters |
| Phosphorus | 16..63 | 3 | 12..18 | Clusters |
| Potash | 8..47 | 2.5 | 12..24 | Flat clusters |
| Saltpeter | 24..79 | 3.75 | 8..16 | Clusters |

Lithium and phosphorus use a triangular height curve; sulfur favors the bottom
of its range; the others use uniform heights. Each mineral retains its previous
expected requested budget while changing depth and shape. These are gameplay
approximations rather than geological simulations.

Lithium uses vein length 8 and node size 3. Cluster horizontal spread, vertical
spread and node size are respectively niter `(4,2,3)`, salt `(8,0,8)`, sulfur
`(4,2,4)`, phosphorus `(6,1,6)`, potash `(6,0,6)` and saltpeter `(4,1,3)`.
The balance draws on [lithium pegmatites](https://www.usgs.gov/data/lithium-deposits-united-states),
[evaporite potash](https://www.usgs.gov/publications/potash-a-global-overview-evaporate-related-potash-resources-including-spatial)
and [sedimentary phosphate](https://pubs.usgs.gov/bul/b1693/html/bull74kl.htm).

Vanilla stone, including metadata variants, is eligible. Additional hosts are
intrusive igneous rocks for lithium; sedimentary rocks for nitrates, salt, potash
and phosphorus; sedimentary/volcanic rocks for sulfur. There are no hard biome
restrictions. Rules cover ordinary dimensions except vanilla Nether/End.
Silicon and charcoal do not generate.

## Saved settings and shared ores

Rule IDs remain `baseminerals:legacy/<mineral>_ore`. Canonical materials use
`orespawn:<mineral>`, with the `orespawn:standard` placement channel. Niter and
saltpeter remain separate. Sulfur/Sulphur aliases refer to the same material.

OreSpawn's reviewed source policies can share one placement budget across
equivalent outputs. Sulfur priority is Mineralogy, Base Minerals, then Electric
Advantage; lithium priority is Base Minerals then Electric Advantage. Only actual
loaded/managed sources qualify for automatic consolidation. An external generator
does not become controllable merely through an Ore Dictionary alias.
An existing profile choice remains authoritative even after sources become
manageable. Review the resulting source groups in OreSpawn before changing them.

Native global overrides use `config/baseminerals-orespawn.json`. Existing world
profiles and native overrides take precedence over legacy imports. Supported
OS1/OS3 custom and disabled rules retain their settings rather than adopting this
balance. The explicit Overworld rule overrides the ordinary-dimension fallback;
these are alternative dimension declarations, not two generation passes. Existing
ore entries are kept as whole entries, including older selector-only rules. Missing entries follow OreSpawn's normal provider merge behavior.
Retrogen stays disabled; generated chunks are unchanged. Keep legacy files until
the importer report and resulting settings have been reviewed.
The pinned engine currently reinserts a native rule after an OS1-imported world
reload; that migration gate fails. See [qualification limits](KNOWN_ISSUES.md).
