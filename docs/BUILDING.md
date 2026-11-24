# Building Base Minerals 1.15.2

Use Java 17 for Gradle, Java 25 for Mavenizer, and Java 8 for compilation and
Minecraft. The wrapper uses Gradle 9.6.1, ForgeGradle 7.0.34 and Renamer 1.1.5.
The target is Forge 31.2.57 with MCP snapshot 20200514-1.15.1 and MCP config
20200515.085601.

Select one Gradle cache for Eclipse and the terminal. Stage the checksum-pinned
public OreSpawn 4.1.7.115021 and Base Metals 3.0.3.115021 jars, then build with
Java 17 as JAVA_HOME. Base Metals remains optional when playing.

```powershell
python gradle/stage-legacy-dependencies.py .gradle/legacy-dependencies
.\gradlew.bat check build javadoc verify115ReleaseArtifacts genEclipseRuns eclipse
```

Set org.gradle.java.installations.paths to the three JDK directories when
toolchain discovery is disabled. In this workspace GRADLE_USER_HOME is
D:/BaseMetals/.gradle-verify-cache. Add --offline to rebuild from the same cache.
The player jar is build/libs/BaseMinerals-2.5.2.115021.jar. Development output
is under build/libs-dev; sources and Javadocs are separate.

Import the inner checkout as an Eclipse Gradle project. The generated client
and server launches use Java 8 and staged main output. Gradle refresh preserves
quoted paths for directories containing spaces. Use runClient.launch alone or
runClientWithBaseMetals.launch for hammer and companion testing. The profiles
have separate run directories. Terminal runClient accepts -PwithBaseMetals=true.

The four Eclipse client profiles cover standalone, Base Metals 3.0.3.115021, Modern Metals 3.0.0.115021 and both. To use a local companion build, run `pinLocalCompanions`, then regenerate Eclipse in a separate invocation. Gradle checks the player and mapped jars and retains replaced copies under `.gradle/retired-compatibility-jars`. Release builds use the published dependencies.

## Local gameplay checks

The tools under tools/validation/port115 use an independently installed
Forge 1.15.2-31.2.57 server directory containing its launcher and libraries.
Install tools/validation/port115/requirements.txt for the Anvil/NBT checks.
Each runner creates a new disposable output directory and retains its logs.
Never use a player save or install a probe in a normal game.

```powershell
.\gradlew.bat packagedMiningProbeJar packagedGenerationProbeJar clientProbeJar
python tools/validation/port115/verify_mining.py --help
python tools/validation/port115/verify_census.py --help
python tools/validation/port115/verify_upgrade.py --help
python tools/validation/port115/verify_named_upgrade.py --help
python tools/validation/port115/verify_client.py --help
```

The mining probe checks survival tools, Fortune, Silk Touch, shared hammers,
native cooking and crushing, block properties, fluid light and smith offers.
Use --processing and run it alone, with published Base Metals and with the
local model-fix candidate. Hammer checks use the native start/tick/stop digging path and assert
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
