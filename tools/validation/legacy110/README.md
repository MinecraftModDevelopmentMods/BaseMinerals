# Base Minerals 1.10 qualification

Probes are separate mods under `build/test-mods`; exclude them from distribution
and ordinary Eclipse profiles. All game directories/worlds are disposable.
Suites refuse to reuse output directories and fingerprint copied fixtures.

`test` checks the provider settings, missing blocks, startup material
and fluid switches, restart latching, charcoal ownership and namespace relinking.
`verifyProjectResources` validates JSON and the exported provider against schemas
inside the pinned OreSpawn jar, configuration labels and workflow pins.

```text
python tools/validation/legacy110/run_packaged_suite.py --java <java8> --template <clean Forge template> --output <new directory>
```

The template contains only Forge 1.10.2-12.18.3.2511, vanilla server and libraries.
The suite covers four fixed seeds; 256 interior chunks per dimension with a
populated border; all Y levels; Nether/End exclusion; repeat/reload digests;
later chunks; disabled materials/generation; overrides; OS1/OS3 imports; saved
profiles/stacks; Tinkers molten forms; negative hosts; recorded attempts,
quantities and height selection; and three paired timing runs.
The later-chunk case also recenses the original area. Every individual disabled
material is tested through actual generation, rather than registration alone.
Seed zero needs explicit fresh metadata because server.properties treats `0`
as random. `--quick` is only an early smoke.

The full packaged suite currently fails when it loads an OS1-imported world
without the original global import file: the pinned engine adds a second native
lithium rule. Run `reproduce_os1_reload.py` with `--java`, `--template` and a new
`--output` directory for the shorter reproduction. See the
[known issues](../../../docs/KNOWN_ISSUES.md). This failure is kept in the suite.

`run_overlap_suite.py` stages public sibling jars pinned in
`overlap_dependencies.json` and captures their real source policies. Its Electric
Advantage/Power Advantage jars are historical public releases, rather than the
unpublished modern native-provider implementations.
Failures are preserved per integration; the runner continues to collect the
other cases, then exits unsuccessfully if any public integration failed.

`packagedSourcePolicyTest` runs eight cases with a clearly synthetic output-only
sibling. It checks Balanced, Single, Custom weights, Keep Original, physical
placement-budget equality and saved choices across removal/restoration. It does
not claim that the historical public Electric Advantage jar loads.

After the full suite, `run_continuity_checks.py` accepts `--suite` and
`--selector-fixture` in addition to `--java`, `--template`, `--output`. It checks
copied OS1/OS3 worlds, saved selector-only profiles, all six vanilla stone
metadata hosts, bedrock/air/water/obsidian exclusions and decoded pattern settings.
`run_mineralogy_policy.py` accepts the successful public Mineralogy run directory
as `--integration`; it tests reviewed sulfur output choices on copied worlds.

`run_packaged_client.py` exercises both configuration entries, save/cancel/restart,
baked models and integrated-world fresh/reload checks against the installed
obfuscated client. It verifies assets in a disposable directory, saves screenshots
and rejects texture errors. Supply `--java`, `--cache`, `--installer`, `--template`,
`--output` and `--phase first|reload`.

Inspect complete logs as well as JSON reports. Forge's unsigned-distribution
warning, public Base Metals' fingerprint warning, offline Realms checks and normal
sound shutdown messages are distinct from loading/generation errors. Manual
visual acceptance, hosted CI and publication remain separate gates.
Tinkers' historical Base Metals registrations also emit alternative-prefix
warnings for its own molten alloys. Keep that dependency warning separate from
new Base Minerals registry warnings. Missing-source tests deliberately exercise
Forge's missing-mod/mapping warnings; restored policy choices do not promise to
restore blocks already removed by Minecraft.
