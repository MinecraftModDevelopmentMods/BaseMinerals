"""Run the isolated survival-mining probe against a packaged Base Minerals jar."""
import argparse
import hashlib
import json
from pathlib import Path
import subprocess

from run_forge import LAUNCHER, prepare


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ("java", "runtime", "output", "probe"):
        parser.add_argument("--" + name, type=Path, required=True)
    parser.add_argument("--mods", nargs="+", type=Path, required=True)
    parser.add_argument("--processing", action="store_true")
    args = parser.parse_args()
    jars = [*args.mods, args.probe]
    prepare(args.runtime, args.output, jars)
    (args.output / "input-jars.json").write_text(json.dumps({
        jar.name: hashlib.sha256(jar.read_bytes()).hexdigest() for jar in jars
    }, indent=2))
    with (args.output / "console.log").open("wb") as stream:
        command = [str(args.java), "-Xmx2G"]
        if args.processing:
            command.append("-Dmineralprobe.processing=true")
        result = subprocess.run([*command, "-jar", json.loads((args.output / "forge-runtime.json").read_text())["launcher"], "nogui"],
                                cwd=args.output, stdout=stream, stderr=subprocess.STDOUT, timeout=240)
    report = json.loads((args.output / "mining-results.json").read_text())
    log = (args.output / "console.log").read_text(encoding="utf-8", errors="replace")
    for failure in ("Encountered an unexpected exception", "Error occurred applying transform of coremod",
                    "Couldn't read loot table baseminerals:", "Parsing error loading recipe baseminerals:"):
        assert failure not in log, f"Forge startup error: {failure}"
    print(args.output.name, "PASS" if report["passed"] else "FAIL", report["checks"], "checks", flush=True)
    assert result.returncode == 0 and report["passed"], report["failures"][:12]


if __name__ == "__main__":
    main()
