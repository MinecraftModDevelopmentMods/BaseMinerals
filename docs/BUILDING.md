# Building Base Minerals 1.10.2

Use Gradle 9.6.1, ForgeGradle 7.0.34, Renamer 1.1.5 and Foojay 1.0.0. Gradle runs
on Java 17; Mavenizer uses Java 25; compilation, bytecode and Minecraft use Java 8.
Forge is 12.18.3.2511 with stable-29 mappings. Release pins are Temurin 8u502-b07,
17.0.1+12 and 25.0.3+9-LTS. Install them or pass their homes with
`org.gradle.java.installations.paths`. Do not launch Gradle on Java 8.
On the command line use `-Dorg.gradle.java.installations.paths=<homes>` and
`-Dorg.gradle.java.installations.auto-detect=false` to keep offline runs on the
same physical JDK installations. Gradle 9 deprecates the older `-P` form.

With `JAVA_HOME`/`PATH` selecting Java 17:

```powershell
$env:GRADLE_USER_HOME='<one workspace cache shared with Eclipse>'
python gradle/stage-legacy-dependencies.py .gradle/legacy-dependencies
python -m pip install -r tools/validation/requirements.txt
.\gradlew.bat clean check build javadoc verifyReleaseArtifacts verifyProjectResources prepareEclipse
```

Use `-PpythonExecutable=<python executable>` if Python is not named `python3`.
`gradle/dependencies.json` pins production downloads and SHA-256. Staging creates
a verified Maven mirror; `-PlegacyDependencyVerificationRepository=<mirror>` selects
another location. Original jars stay unchanged; development copies are remapped
and stripped of invalid signatures. No floating mod versions, JCenter or
`mavenLocal()` are used. A normal build never publishes.

`build/libs/BaseMinerals-2.5.1.110021.jar` is the installed jar. Sources/Javadocs
are separate. Test probes are isolated from all release archives.

## Eclipse

Run `prepareEclipse` and import the inner Git/Gradle project using Buildship. Use
Java 17 for Gradle, a Java 8 project JRE and one physical cache for Eclipse and the
terminal. Production profiles contain only Base Minerals, Base Metals and OreSpawn;
optional APIs are compilation dependencies. Test profiles are separate. Regenerate
profiles when dependency/toolchain locations change. FG7 uses `genEclipseRuns` and
`prepareEclipse`; `setupDecompWorkspace` no longer applies.

Gradle launches merge production classes and processed resources into one mod
directory. Eclipse uses `bin/main`, with resources from `build/resources/main` so
metadata is not duplicated. `verifyEclipseProductionClasspath` checks isolation.
ForgeGradle's `source-sets-not-merged-for-eclipse` diagnostic refers to its own
automatic merge. This project deliberately merges production output itself;
the generated profiles, Buildship model and Java 8 Eclipse compiler are audited.

## Verification

`test` checks native declarations, configuration latching and namespace compatibility.
`verifyProjectResources` validates resources and the pinned engine schema. Packaged server checks require a clean Forge template:

```text
gradlew packagedForgeRuntimeTest -PpackagedForgeRuntimeRoot=<clean Forge template> -PpackagedForgeRuntimeOutput=<new disposable directory>
gradlew packagedSourcePolicyTest -PpackagedForgeRuntimeRoot=<clean Forge template> -PpackagedSourcePolicyOutput=<another new directory>
```

The output directory must not already exist. See the validation README for exact
client commands, migration scenarios and limitations.
