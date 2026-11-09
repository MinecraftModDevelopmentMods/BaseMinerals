# Base Minerals 1.12 qualification

Probes are separate mods under `build/test-mods`. Exclude them from distribution
and ordinary Eclipse profiles. All game directories/worlds are disposable.
Suites refuse to reuse output directories and fingerprint copied fixtures.

`test` checks the provider settings, missing blocks, startup material
and fluid switches, restart latching, charcoal ownership and namespace relinking.
`verifyProjectResources` validates JSON and the exported provider against schemas
inside the pinned OreSpawn jar, configuration labels and workflow pins.

```text
python tools/validation/legacy112/run_packaged_suite.py --java <java8> --template <clean Forge template> --mirror <verified mirror> --output <new directory>
```

The template contains only Forge 1.12.2-14.23.5.2859, vanilla server and libraries.
The suite covers all Y levels across 256 interior chunks per dimension for four
fixed seeds, with a populated border. It checks Nether and End exclusion, repeat
and reload digests, later chunks, disabled materials and generation, native
overrides, a saved OS1 import, direct OS3 imports, saved profiles and stacks, and Tinkers molten
forms. It also checks negative hosts, records attempts, quantities and selected
heights, and runs three paired timing comparisons.
The later-chunk case also recenses the original area. Every individual disabled
material is tested through actual generation, rather than registration alone.
Seed zero needs explicit fresh metadata because server.properties treats `0`
as random. `--quick` is only an early smoke.

The packaged suite uses a saved lithium rule extracted from an OreSpawn 4
world on Minecraft 1.11.2. For the complete-world check, run
`reproduce_os1_reload.py` with `--source-world` pointing to an already imported
1.10.2 or 1.11.2 save, plus `--java`, `--template`, `--mirror` and a new
`--output`. The runner works on a copy and fingerprints its source. See
[known issues](../../../docs/KNOWN_ISSUES.md) for raw OS1 files and edited profiles.

`run_overlap_suite.py` stages the public 1.12.2 Base Electric, Base Power and
Mineralogy jars pinned in `overlap_dependencies.json` and captures their source
policies on fresh and reopened worlds. Use `--sample --with-base-metals` to
census 256 chunks per dimension with the pinned Base Metals jar installed.
The runner preserves failures per integration and continues to collect the
other cases, then exits unsuccessfully if any public integration failed.

`packagedSourcePolicyTest` runs eight cases with a clearly synthetic output-only
sibling. It checks Balanced, Single, Custom weights, Keep Original, physical
placement-budget equality and saved choices across removal/restoration.

After the full suite, `run_continuity_checks.py` accepts `--suite` and
`--selector-fixture` in addition to `--java`, `--template`, `--output`. It checks
copied OS1/OS3 worlds, saved selector-only profiles, all six vanilla stone
metadata hosts, bedrock/air/water/obsidian exclusions and decoded pattern settings.
`run_mineralogy_policy.py` tests reviewed sulfur output choices on copied worlds
when the fixture supplies an OreSpawn-managed Mineralogy source.

`run_packaged_client.py` exercises both configuration entries, save/cancel/restart,
baked models and integrated-world fresh/reload checks against the installed
obfuscated client. It verifies assets in a disposable directory, saves screenshots
and rejects texture errors. Supply `--java`, `--cache`, `--installer`, `--template`,
`--mirror`, `--output` and `--phase first|reload`. Add `--with-base-metals` to
the first launch to include the pinned Base Metals jar.

Inspect complete logs as well as JSON reports. Forge's unsigned-distribution
warning, the pinned Base Metals fingerprint warning, offline Realms checks and normal
sound shutdown messages are distinct from loading/generation errors. Manual
visual acceptance, hosted CI and publication remain separate gates.
Tinkers' historical Base Metals registrations emit alternative-prefix warnings
for its own molten alloys. Missing-source tests deliberately exercise Forge's
missing-mod and mapping warnings. A Forge-only control reproduces the vanilla
Nether population warnings seen while the census probe loads chunks.
