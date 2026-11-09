# Building Base Minerals 1.12.2

Use Gradle 9.6.1, ForgeGradle 7.0.34, Renamer 1.1.5 and Foojay 1.0.0. Gradle runs
on Java 17 and Mavenizer uses Java 25. Compilation, bytecode and Minecraft use Java 8.
Forge is 14.23.5.2859 with stable-39 mappings. Release pins are Temurin 8u502-b07,
17.0.1+12 and 25.0.3+9-LTS. Install them or pass their homes with
`org.gradle.java.installations.paths`. Do not launch Gradle on Java 8.
On the command line use `-Dorg.gradle.java.installations.paths=<homes>` and
`-Dorg.gradle.java.installations.auto-detect=false` to keep offline runs on the
same physical JDK installations. Gradle 9 deprecates the older `-P` form.

The staging script downloads and verifies MMDLib, OreSpawn and the optional
compatibility jars using `gradle/dependencies.json`. The exact MMDLib pin is the
published 1.0.0 RC3 jar. Its embedded Forge metadata still reports `1.0.0-rc2`,
which is why that version appears in the runtime dependency range.

Select Java 17 with `JAVA_HOME` and `PATH`, then stage the dependencies and build.

```powershell
$env:GRADLE_USER_HOME='<one workspace cache shared with Eclipse>'
$mirror = '<path to the staged 1.12.2 Maven mirror>'
python gradle/stage-legacy-dependencies.py $mirror
python -m pip install -r tools/validation/requirements.txt
.\gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyProjectResources prepareEclipse "-PlegacyDependencyVerificationRepository=$mirror"
```

Use `-PpythonExecutable=<python executable>` if Python is not named `python3`.
`gradle/dependencies.json` pins production downloads and SHA-256. Staging creates
a verified Maven mirror. `-PlegacyDependencyVerificationRepository=<mirror>` selects
its location. The build remaps development copies and strips invalid signatures
from those copies. No floating mod versions, JCenter or
`mavenLocal()` are used. A normal build never publishes.

`build/libs/BaseMinerals-2.5.1.112021.jar` is the installed jar. Sources/Javadocs
are separate. Test probes are isolated from all release archives.

## Eclipse

Run `prepareEclipse` and import the inner Git/Gradle project using Buildship. Use
Java 17 for Gradle, a Java 8 project JRE and one physical cache for Eclipse and the
terminal. The standard client and server profiles load Base Minerals, MMDLib and OreSpawn.
Base Metals and Tinkers are optional compatibility dependencies. Regenerate
profiles when dependency/toolchain locations change. FG7 uses `genEclipseRuns` and
`prepareEclipse`.

Use `runClientWithBaseMetals` in Eclipse to test Base Metals tools against the
Base Minerals ores. It loads the pinned Base Metals jar and keeps its saves and
configuration under `run/with-base-metals`.

Gradle launches merge production classes and processed resources into one mod
directory. Eclipse uses `bin/main`, with resources from `build/resources/main` so
metadata is not duplicated. `verifyEclipseProductionClasspath` checks isolation.
ForgeGradle's `source-sets-not-merged-for-eclipse` diagnostic refers to its own
automatic merge. This project merges production output explicitly and audits
the generated profiles, Buildship model and Java 8 Eclipse compiler.

## Ore models

`generateOreModels` writes the ore block models to `build/generated/ore-models`.
The normal build includes them in the game and sources jars. Blockstates and
inventory models use the existing model names.

The seven ores use KiriCattus's transparent PNGs in
`src/main/resources/assets/baseminerals/textures/blocks/ore_overlays/`. The build
layers each PNG over Minecraft stone for the block and inventory models.
An ore with only a full texture in `textures/blocks/` uses that texture on a
single cube. To switch it to an overlay, put a PNG with the same name in
`ore_overlays/` and rebuild.

## Verification

`test` checks native declarations, configuration latching and namespace compatibility.
`verifyProjectResources` validates resources and the pinned engine schema. Packaged server checks require a clean Forge template:

```text
gradlew packagedForgeRuntimeTest -PpackagedForgeRuntimeRoot=<clean Forge template> -PpackagedForgeRuntimeOutput=<new disposable directory>
gradlew packagedSourcePolicyTest -PpackagedForgeRuntimeRoot=<clean Forge template> -PpackagedSourcePolicyOutput=<another new directory>
```

The output directory must not already exist. See the validation README for exact
client commands, migration scenarios and limitations.
