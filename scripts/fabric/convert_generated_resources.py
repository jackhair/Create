#!/usr/bin/env python3
"""Rewrite NeoForge-specific keys in Create's data JSON to their Fabric equivalents.

See PORTING.md decision D4: until datagen runs on Fabric, we ship upstream's
generated resources passed through this script. It is idempotent, so re-run it
after every upstream merge.

  neoforge:conditions        -> fabric:load_conditions
  neoforge:mod_loaded        -> fabric:all_mods_loaded
  neoforge:not/and/or/true   -> fabric:not/and/or/true
  neoforge:tag_empty         -> fabric:not + fabric:tags_populated (not(tag_empty) collapses to tags_populated)
  neoforge:item_exists       -> fabric:registry_contains
  neoforge:single/tag/components (fluid ingredients) -> create:fluid/fluid_tag/fluid_components
  neoforge:block_tag         -> create:block_tag
  neoforge:cures             -> removed (vanilla effect codec ignores it; milk/totem behaviour is vanilla)

Fluid amounts stay in mB, per decision D3; Create's fluid ingredient codecs convert to droplets on load.
Files under data/neoforge/ and data/*/neoforge/ are left in place and reported, since
the code registers their contents on Fabric.

Usage: scripts/fabric/convert_generated_resources.py [--dry-run] [--check] [resource_dir ...]
  --check  exit 1 if anything would change (for CI)
"""

import json
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
DEFAULT_DIRS = [REPO / "src/generated/resources/data", REPO / "src/main/resources/data"]

INGREDIENT_TYPES = {
	"neoforge:single": "create:fluid",
	"neoforge:tag": "create:fluid_tag",
	"neoforge:components": "create:fluid_components",
	"neoforge:block_tag": "create:block_tag",
}
DROPPED_KEYS = {"neoforge:cures"}


class UnknownCondition(Exception):
	pass


def condition(c: dict) -> dict:
	kind = c.get("type")
	if kind == "neoforge:mod_loaded":
		return {"condition": "fabric:all_mods_loaded", "values": [c["modid"]]}
	if kind == "neoforge:tag_empty":
		return {"condition": "fabric:not", "value": tags_populated(c)}
	if kind == "neoforge:not":
		inner = c["value"]
		if inner.get("type") == "neoforge:tag_empty":
			return tags_populated(inner)
		return {"condition": "fabric:not", "value": condition(inner)}
	if kind in ("neoforge:and", "neoforge:or"):
		return {"condition": "fabric:" + kind.split(":")[1], "values": [condition(v) for v in c["values"]]}
	if kind == "neoforge:true":
		return {"condition": "fabric:true"}
	if kind == "neoforge:false":
		return {"condition": "fabric:not", "value": {"condition": "fabric:true"}}
	if kind == "neoforge:item_exists":
		return {"condition": "fabric:registry_contains", "registry": "minecraft:item", "values": [c["item"]]}
	if "condition" in c and str(c["condition"]).startswith("fabric:"):
		return c  # already converted
	raise UnknownCondition(json.dumps(c))


def tags_populated(tag_empty: dict) -> dict:
	out = {"condition": "fabric:tags_populated", "values": [tag_empty["tag"]]}
	if "registry" in tag_empty:
		out["registry"] = tag_empty["registry"]
	return out


def convert(node):
	if isinstance(node, list):
		return [convert(v) for v in node]
	if not isinstance(node, dict):
		return node
	out = {}
	for key, value in node.items():
		if key in DROPPED_KEYS:
			continue
		if key == "neoforge:conditions":
			out["fabric:load_conditions"] = [condition(c) for c in value]
		elif key == "type" and value in INGREDIENT_TYPES:
			out[key] = INGREDIENT_TYPES[value]
		else:
			out[key] = convert(value)
	return out


def main() -> int:
	flags = {a for a in sys.argv[1:] if a.startswith("--")}
	dirs = [Path(a) for a in sys.argv[1:] if not a.startswith("--")] or DEFAULT_DIRS
	write = not ({"--dry-run", "--check"} & flags)

	changed, failed, loader_dirs = [], [], set()
	for root in dirs:
		for path in sorted(root.rglob("*.json")):
			if ".cache" in path.parts:
				continue
			if "neoforge" in path.relative_to(root).parts:
				loader_dirs.add(path.parent)
				continue
			text = path.read_text(encoding="utf-8")
			if "neoforge:" not in text:
				continue
			try:
				new = json.dumps(convert(json.loads(text)), indent=2, ensure_ascii=False)
			except UnknownCondition as e:
				failed.append(f"{path}: unknown condition {e}")
				continue
			if text.endswith("\n"):
				new += "\n"
			if "neoforge:" in new:
				failed.append(f"{path}: unhandled neoforge key remains")
			if new != text:
				changed.append(path)
				if write:
					path.write_text(new, encoding="utf-8")

	verb = "Converted" if write else "Would convert"
	print(f"{verb} {len(changed)} files.")
	if loader_dirs:
		print(f"\nLeft in place (contents must be registered in code on Fabric):")
		for d in sorted(loader_dirs):
			print(f"  {d.relative_to(REPO) if REPO in d.parents else d}")
	if failed:
		print(f"\n{len(failed)} files need attention:")
		for f in failed:
			print(f"  {f}")
		return 1
	return 1 if "--check" in flags and changed else 0


if __name__ == "__main__":
	sys.exit(main())
