# Known issues for Minecraft 1.10.2

The build and fresh-world generation checks pass. Two compatibility problems
remain with the pinned dependencies.

## Moving an OS1-imported world can add a second lithium rule

Pinned public OreSpawn 4.1.2.110021 converts an OS1 lithium entry to
`baseminerals:legacy/baseminerals_lithium_ore_0`. First import retains its custom
frequency 1.5, quantity 4 and Y=8..39. When only the saved world is copied into a
fresh instance without the original global OS1 file, provider merging adds
`baseminerals:legacy/lithium_ore` with the fresh defaults alongside it. Both
remain active in separate placement channels. A disposable 256-chunk later
sample contained 15,759 lithium blocks, rather than preserving the imported
budget alone.

This needs a fix in OreSpawn's import and provider merging. Its published API
has no rule-ID migration alias or way to edit an active profile. Turning off
all provider merging would also stop missing rules from being added. Base
Minerals does not rewrite world profiles or call OreSpawn's internal classes.

Keeping the original global OS1 file lets the importer suppress the extra rule
again on reload; that control passes. A saved world profile alone fails to retain
its budget. The packaged suite now asserts one lithium rule after profile-only
reload. The standalone reproduction fails on this pin, and
`run_continuity_checks.py` also checks that the rule IDs for each imported output
stay the same. The earlier checks verified settings but missed the extra rule.
OS3 imports with matching rule IDs and saved selector-only profiles pass these
checks.

## Historical Electric Advantage dependencies

Public Electric Advantage 2.2.0 / Power Advantage 2.3.0 fail before world load
against required Base Metals 2.5.1.110021: Power Advantage references missing
`cyano.basemetals.init.Materials`. The combined Mineralogy case fails too.
Synthetic source-policy tests do not clear this public integration gate.

Public Mineralogy 6.0.1.110021 loads and supports reviewed sulfur consolidation.
Its fresh profile retains separate sulfur placement; automatic default
consolidation is not claimed.
