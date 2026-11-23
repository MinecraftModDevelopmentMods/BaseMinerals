# Building Base Minerals 1.14.4

Use Java 17 for Gradle, Java 25 for Mavenizer, and Java 8 for compilation and
Minecraft. The wrapper uses Gradle 9.6.1, ForgeGradle 7.0.34 and Renamer 1.1.5.
The target is Forge 28.2.26 with MCP snapshot 20190719-1.14.3 and MCP config
20190829.143755.

Select one Gradle cache for Eclipse and the terminal. Stage the checksum-pinned
public OreSpawn 4.1.7.114041, Base Metals 3.0.2.114041 and Modern Metals
3.0.0.114041 jars, then build with Java 17 as JAVA_HOME. Both metal mods remain
optional when playing.

```powershell
python gradle/stage-legacy-dependencies.py .gradle/legacy-dependencies
.\gradlew.bat check build javadoc verify114ReleaseArtifacts genEclipseRuns eclipse
```

Set org.gradle.java.installations.paths to the three JDK directories when
toolchain discovery is disabled. In this workspace GRADLE_USER_HOME is
D:/BaseMetals/.gradle-verify-cache. Add --offline to rebuild from the same cache.
The player jar is build/libs/BaseMinerals-2.5.2.114041.jar. Development output
is under build/libs-dev; sources and Javadocs are separate.

Import the inner checkout as an Eclipse Gradle project. The generated client
and server launches use Java 8 and staged main output. Gradle refresh preserves
quoted paths for directories containing spaces. Use runClient.launch alone,
runClientWithBaseMetals.launch, runClientWithModernMetals.launch or
runClientWithBoth.launch. The four profiles use separate run directories and
the pinned public metal jars by default. Terminal runClient accepts
-PwithBaseMetals=true and -PwithModernMetals=true.

To test the current local Base Metals and Modern Metals fixes, build those
sibling checkouts, then run these as separate commands:

```powershell
.\gradlew.bat pinLocalCompanions
.\gradlew.bat check genEclipseRuns eclipse
```

The ignored local-basemetals.json and local-modernmetals.json files pin both
player and mapped jars by SHA-256. Gradle checks the source hashes and the exact
copies staged in each profile. Old managed companion copies are retained under
.gradle/retired-compatibility-jars; worlds, settings and unrelated mods are kept.
After rebuilding either sibling, repin it with pinLocalBaseMetals or
pinLocalModernMetals, then regenerate the launches.

With the local pins present, runClientWithBaseMetals.launch and
runClientWithBoth.launch load the fixed local Base Metals build. The latter
also loads the pinned local Modern Metals build. runClientWithModernMetals.launch
uses Modern Metals alone. Terminal runClient accepts -PwithBaseMetals=true and
-PwithModernMetals=true. Use -PuseLocalBaseMetals=false to test the published
Base Metals baseline. Release builds (-PreleaseBuild=true) select published
dependencies and reject explicitly enabled local overrides.

## Local gameplay checks

The tools under tools/validation/port114 use an independently installed
Forge 1.14.4-28.2.26 server directory containing its launcher and libraries.
Install tools/validation/port114/requirements.txt for the Anvil/NBT checks.
Each runner creates a new disposable output directory and retains its logs.
Never use a player save or install a probe in a normal game.

```powershell
.\gradlew.bat packagedMiningProbeJar packagedGenerationProbeJar clientProbeJar
python tools/validation/port114/verify_mining.py --help
python tools/validation/port114/verify_census.py --help
python tools/validation/port114/verify_upgrade.py --help
python tools/validation/port114/verify_named_upgrade.py --help
python tools/validation/port114/verify_client.py --help
```

The mining probe checks survival tools, Fortune, Silk Touch, shared hammers,
native cooking and crushing, block properties, fluid light and smith offers.
Use --processing and run it alone, with Base Metals, with Modern Metals and
with both. Hammer checks use the native start/tick/stop digging path and assert
mining speed as well as drops and durability. The client probe additionally
holds mining through the real client controller and network packets, checks
server/client block agreement and verifies Base Metals' fixed ore overlay gap.
The generation probe populates a margin around 256 interior chunks
in each vanilla dimension, scans Y=0..255, checks mineral heights and repeats
the census after later generation and reload. It handles actual seed zero
through an empty explicitly seeded save. The client probe checks models,
translations, ore layers, bucket tint and fluid atlas sprites, then saves and
reopens a disposable integrated world. It uses the actual Eclipse Windows
argument parser. Compile ParseEclipseArguments.java into a disposable helper
directory and include that directory and the installed Eclipse DebugPlugin
bundles in --parser-classpath; use the matching Java runtime as --parser-java.
None of these probes is included in the player jar.
