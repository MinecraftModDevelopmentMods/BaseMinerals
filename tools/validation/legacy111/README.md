# Base Minerals 1.11 qualification

Probes are separate mods under `build/test-mods`. Exclude them from distribution
and ordinary Eclipse profiles. All game directories/worlds are disposable.
Suites refuse to reuse output directories and fingerprint copied fixtures.

`test` checks the provider settings, missing blocks, startup material
and fluid switches, restart latching, charcoal ownership and namespace relinking.
`verifyProjectResources` validates JSON and the exported provider against schemas
inside the pinned OreSpawn jar, configuration labels and workflow pins.

```text
python tools/validation/legacy111/run_packaged_suite.py --java <java8> --template <clean Forge template> --mirror <verified mirror> --output <new directory>
```

The template contains only Forge 1.11.2-13.20.1.2588, vanilla server and libraries.
The suite covers all Y levels across 256 interior chunks per dimension for four
fixed seeds, with a populated border. It checks Nether and End exclusion, repeat
and reload digests, later chunks, disabled materials and generation, native
overrides, OS1 and OS3 imports, saved profiles and stacks, and Tinkers molten
forms. It also checks negative hosts, records attempts, quantities and selected
heights, and runs three paired timing comparisons.
The later-chunk case also recenses the original area. Every individual disabled
material is tested through actual generation, rather than registration alone.
Seed zero needs explicit fresh metadata because server.properties treats `0`
as random. `--quick` is only an early smoke.

The packaged suite requires one imported lithium rule when an OS1 world moves
without its global import file. Run `reproduce_os1_reload.py` with `--java`,
`--template`, `--mirror` and a new `--output` directory for the shorter check.
See [known issues](../../../docs/KNOWN_ISSUES.md) for edited profiles.

`run_overlap_suite.py` stages public sibling jars pinned in
`overlap_dependencies.json` and captures their real source policies. Its Electric
Advantage and Power Advantage jars are the historical 1.10.2 public releases.
Mineralogy is the 1.11.2 public 3.3.0.6 release, whose sulfur is an external
source in OreSpawn.
The runner preserves failures per integration and continues to collect the
other cases, then exits unsuccessfully if any public integration failed.

`packagedSourcePolicyTest` runs eight cases with a clearly synthetic output-only
sibling. It checks Balanced, Single, Custom weights, Keep Original, physical
placement-budget equality and saved choices across removal/restoration. It does
not claim that the historical public Electric Advantage jar loads.

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
`--mirror`, `--output` and `--phase first|reload`.

Inspect complete logs as well as JSON reports. Forge's unsigned-distribution
warning, the pinned Base Metals fingerprint warning, offline Realms checks and normal
sound shutdown messages are distinct from loading/generation errors. Manual
visual acceptance, hosted CI and publication remain separate gates.
Tinkers' historical Base Metals registrations emit alternative-prefix warnings
for its own molten alloys. Missing-source tests deliberately exercise Forge's
missing-mod and mapping warnings. A Forge-only control reproduces the vanilla
Nether population warnings seen while the census probe loads chunks.
