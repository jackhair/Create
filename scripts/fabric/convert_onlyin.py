#!/usr/bin/env python3
"""Convert NeoForge @OnlyIn/Dist annotations to Fabric @Environment/EnvType.

Idempotent: safe to re-run after merging upstream changes.

Only the annotation form is rewritten. Files that still use `Dist` for other
purposes (@EventBusSubscriber, @Mod, DistExecutor, ...) keep their Dist import
and are listed at the end for manual conversion.

Usage: scripts/fabric/convert_onlyin.py [--dry-run] [src_dir]
"""

import re
import sys
from pathlib import Path

ONLYIN_IMPORT = "import net.neoforged.api.distmarker.OnlyIn;"
DIST_IMPORT = "import net.neoforged.api.distmarker.Dist;"
ENV_IMPORT = "import net.fabricmc.api.Environment;"
ENVTYPE_IMPORT = "import net.fabricmc.api.EnvType;"

ANNOTATION = re.compile(r"@OnlyIn\(\s*(?:value\s*=\s*)?Dist\.(CLIENT|DEDICATED_SERVER)\s*\)")
ENV_NAMES = {"CLIENT": "CLIENT", "DEDICATED_SERVER": "SERVER"}


def convert(text: str) -> tuple[str, bool]:
	"""Returns the converted text and whether Dist is still referenced."""
	text = ANNOTATION.sub(lambda m: f"@Environment(EnvType.{ENV_NAMES[m.group(1)]})", text)

	body = "\n".join(l for l in text.splitlines() if not l.startswith("import "))
	dist_still_used = re.search(r"\bDist\b", body) is not None

	lines = text.splitlines(keepends=True)
	out = []
	have_env = ENV_IMPORT in text
	have_envtype = ENVTYPE_IMPORT in text
	for line in lines:
		stripped = line.strip()
		if stripped == ONLYIN_IMPORT:
			if not have_env:
				out.append(ENV_IMPORT + "\n")
				have_env = True
			continue
		if stripped == DIST_IMPORT:
			if not have_envtype:
				out.append(ENVTYPE_IMPORT + "\n")
				have_envtype = True
			if dist_still_used:
				out.append(line)
			continue
		out.append(line)
	return "".join(out), dist_still_used


def main() -> int:
	args = [a for a in sys.argv[1:] if not a.startswith("--")]
	dry_run = "--dry-run" in sys.argv
	root = Path(args[0]) if args else Path(__file__).resolve().parents[2] / "src"

	changed, manual = [], []
	for path in sorted(root.rglob("*.java")):
		text = path.read_text(encoding="utf-8")
		if "distmarker" not in text:
			continue
		new_text, dist_still_used = convert(text)
		if dist_still_used:
			manual.append(path)
		if new_text != text:
			changed.append(path)
			if not dry_run:
				path.write_text(new_text, encoding="utf-8")

	print(f"{'Would convert' if dry_run else 'Converted'} {len(changed)} files.")
	if manual:
		print(f"\n{len(manual)} files still use Dist and need manual conversion:")
		for path in manual:
			print(f"  {path.relative_to(root.parent) if root.parent in path.parents else path}")
	return 0


if __name__ == "__main__":
	sys.exit(main())
