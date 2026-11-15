# Building Base Minerals 1.13.2

Use Java 17 for Gradle, Java 25 for Forge's Mavenizer, and Java 8 for compilation
and Minecraft. The wrapper is Gradle 9.6.1 with ForgeGradle 7.0.34, Renamer 1.1.5,
Forge 25.0.223 and MCP snapshot 20180921-1.13.

Select one Gradle cache for Eclipse and the terminal. Stage the checksum-pinned
public OreSpawn and Base Metals jars, then build with Java 17 as JAVA_HOME.
Base Metals is pinned to the published MMD Maven release 3.0.3.113021 and
checked by SHA-256. It remains optional when playing.

    python gradle/stage-legacy-dependencies.py <mirror>
    .\gradlew.bat check build genEclipseRuns eclipse "-PlegacyDependencyVerificationRepository=<mirror>"

On a configured machine, add --offline to rebuild from the same cache.
The reobfuscated player jar is build/libs/BaseMinerals-4.0.0.113021.jar.
Development output lives under build/libs-dev. Sources and Javadocs are separate.

Import the inner checkout as an Eclipse Gradle project. The generated client
and server launch files use the Java 8 game toolchain and staged mod output.
For a terminal launch, use runClient or runServer. Use the published Base Metals
jar with the packaged Forge profile when checking Crack Hammer integration.

The test runners in tools/validation/legacy113 use an independently installed
Forge server template. Install requirements.txt from that directory for the
Anvil and NBT checks.
