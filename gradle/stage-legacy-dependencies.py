"""Build a checksum-verified Maven mirror from downloads or an existing Gradle cache."""

import argparse
import hashlib
import json
import time
import urllib.error
import urllib.request
from pathlib import Path


LEGACYDEV_VERSION = "0.2.3.1"
LEGACYDEV_SHA256 = "5A906209C2DC429DCCD9AA4B208100D4E2E07A6B9FA1D8A48D436236F4F02642"
FORGE_RELEASES = (
    "https://maven.minecraftforge.net/releases",
    "https://maven.minecraftforge.net",
    "https://maven.creeperhost.net",
)


def download(relative, repositories):
    for repository in repositories:
        url = repository.rstrip("/") + "/" + relative.as_posix()
        request = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
        for attempt in range(3):
            try:
                with urllib.request.urlopen(request, timeout=30) as response:
                    return response.read()
            except (OSError, urllib.error.URLError) as error:
                if attempt == 2:
                    last_error = error
                    break
                print(f"Retrying {url}", flush=True)
                time.sleep(2)
    raise last_error


def stage_legacydev(destination, cache):
    module = Path("net/minecraftforge/legacydev")
    relative = module / LEGACYDEV_VERSION / f"legacydev-{LEGACYDEV_VERSION}-fatjar.jar"
    target = destination / relative
    cached = cache / "caches/minecraftforge/forgegradle/mavenizer/caches/maven/forge" / relative if cache else None
    if target.is_file():
        data = target.read_bytes()
    elif cached and cached.is_file():
        data = cached.read_bytes()
    else:
        data = download(relative, FORGE_RELEASES)
    if hashlib.sha256(data).hexdigest().upper() != LEGACYDEV_SHA256:
        raise ValueError(f"Checksum mismatch for LegacyDev {LEGACYDEV_VERSION}")
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_bytes(data)
    target.with_suffix(".jar.sha1").write_text(hashlib.sha1(data).hexdigest(), encoding="ascii")
    # Mavenizer follows the legacy userdev version range. Offer only the tested build.
    (destination / module / "maven-metadata.xml").write_text(f"""<metadata>
  <groupId>net.minecraftforge</groupId>
  <artifactId>legacydev</artifactId>
  <versioning>
    <latest>{LEGACYDEV_VERSION}</latest>
    <release>{LEGACYDEV_VERSION}</release>
    <versions><version>{LEGACYDEV_VERSION}</version></versions>
  </versioning>
</metadata>
""", encoding="utf-8")
    print("legacydev", LEGACYDEV_SHA256, flush=True)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("destination", type=Path)
    parser.add_argument("--cache", type=Path, help="Existing Gradle user home; matching files are verified before reuse")
    args = parser.parse_args()
    specs = json.loads(Path(__file__).with_name("dependencies.json").read_text(encoding="utf-8"))
    destination = args.destination.resolve()
    stage_legacydev(destination, args.cache)

    for spec in specs:
        group, artifact, version, *classifier = spec["coordinate"].split(":")
        filename = f"{artifact}-{version}" + ("-" + classifier[0] if classifier else "") + ".jar"
        relative = Path(group.replace(".", "/")) / artifact / version / filename
        target = destination / relative
        target.parent.mkdir(parents=True, exist_ok=True)

        if not target.exists():
            cached = list((args.cache / "caches/modules-2/files-2.1" / group / artifact / version).glob("*/" + filename)) if args.cache else []
            if cached:
                data = cached[0].read_bytes()
            else:
                data = download(relative, (spec["repository"],))
            if hashlib.sha256(data).hexdigest().upper() != spec["sha256"]:
                raise ValueError(f"Checksum mismatch: {spec['coordinate']}")
            target.write_bytes(data)

        if hashlib.sha256(target.read_bytes()).hexdigest().upper() != spec["sha256"]:
            raise ValueError(f"Checksum mismatch in mirror: {target}")
        print(spec["id"], spec["sha256"], flush=True)


if __name__ == "__main__":
    main()
