#!/usr/bin/env python3
"""Write Loom's "loom:injected_interfaces" into src/main/resources/fabric.mod.json.

Reads neoforge-shim/injected-interfaces.json (Mojang class names, readable) and translates the
keys to intermediary names using Loom's cached mappings, since Loom requires intermediary keys.
Run `./gradlew help` once first so the mappings exist. Usage: injected_interfaces.py [--check]
"""
from __future__ import annotations

import glob
import json
import os
import sys
from collections import OrderedDict
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
SOURCE = REPO / "neoforge-shim/injected-interfaces.json"
FMJ = REPO / "src/main/resources/fabric.mod.json"
MAPPINGS = "~/.gradle/caches/fabric-loom/{mc}/loom.mappings.*layered*/mappings.tiny"


def minecraft_version() -> str:
	for line in (REPO / "gradle.properties").read_text().splitlines():
		if line.split("=")[0].strip() == "minecraft_version":
			return line.split("=", 1)[1].strip()
	raise SystemExit("minecraft_version not found")


def named_to_intermediary() -> dict[str, str]:
	files = glob.glob(os.path.expanduser(MAPPINGS.format(mc=minecraft_version())))
	if not files:
		raise SystemExit("Loom mappings not found; run ./gradlew help first")
	result = {}
	with open(files[0]) as f:
		header = f.readline().rstrip("\n").split("\t")
		inter, named = header.index("intermediary") - 3, header.index("named") - 3
		for line in f:
			if line.startswith("c\t"):
				cols = line.rstrip("\n").split("\t")[1:]
				result[cols[named]] = cols[inter]
	return result


def main() -> int:
	source = json.loads(SOURCE.read_text())
	mapping = named_to_intermediary()
	injected = OrderedDict()
	for named, interfaces in source.items():
		if named.startswith("_"):
			continue
		if named not in mapping:
			raise SystemExit(f"Unknown class {named}")
		injected[mapping[named]] = interfaces

	fmj = json.loads(FMJ.read_text(), object_pairs_hook=OrderedDict)
	custom = fmj.setdefault("custom", OrderedDict())
	if custom.get("loom:injected_interfaces") == injected:
		print("fabric.mod.json injected interfaces are up to date")
		return 0
	if "--check" in sys.argv:
		print("fabric.mod.json injected interfaces are out of date; run scripts/fabric/injected_interfaces.py")
		return 1
	custom["loom:injected_interfaces"] = injected
	FMJ.write_text(json.dumps(fmj, indent=2) + "\n")
	print(f"Wrote {len(injected)} injected interface entries")
	return 0


if __name__ == "__main__":
	sys.exit(main())
