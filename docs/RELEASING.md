# Release preparation

Normal builds never publish. Main, sources, Javadocs and SHA-256 form one audited
candidate. Gradle enforces Java 8 bytecode, deterministic archives, processed
metadata, dependency hashes and exclusion of tests, local context and shared
library copies. Prepared bundles contain exactly those three jars, `SHA256SUMS`
and `CHANGELOG.txt`. `verifyPreparedReleaseArtifacts` audits the prepared files.

CI has a cold bootstrap/offline rebuild, build/tests, resource/schema checks,
packaged server qualification, Eclipse generation and clean reproducibility check.
CodeQL/wrapper checks are separate. Actions use full commit hashes.

The shared dispatcher on MMD's default branch, `master-1.12`, picks the branch
and CurseForge dependencies from the version. For 1.12.2, supply the
`Major.Minor.Patch.112021` version, full merged `master-1.12` SHA, successful
upstream push CI run ID and SHA-256 of the tested player jar. Run verification
with publication confirmation off before uploading.

CurseForge project **270972** should list MMDLib **261744** and MMD OreSpawn
**245586** as required, with Base Metals **240967** optional. The workflow needs
access to `CURSEFORGE_TOKEN`. Check the public file and its download checksum
after upload. If an upload times out, check its status before trying again.
