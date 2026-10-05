#!/usr/bin/env python3
"""Tools for the NeoForge API shim layer (PORTING.md decision D7).

Shim classes live in neoforge-shim/src/main/java under SHIM_PREFIX, mirroring NeoForge's
package layout: net.neoforged.neoforge.items.IItemHandler becomes
com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandler.

  vendor <fqcn|package.*>...  Copy classes from the NeoForge sources jar into the shim tree,
                              renaming packages. References to other NeoForge classes are
                              rewritten only if that class is already shimmed (or vendored in
                              the same call), so compile errors show what's still missing.
  imports [--check]           Rewrite net.neoforged references in Create and Registrate to the
                              shim package, for every class the shim tree provides. Idempotent;
                              re-run after adding shims and after upstream merges.
  missing                     List NeoForge classes still referenced, by use count.
  tidy                        In the shim tree, drop imports of unshimmed NeoForge classes that only
                              javadoc mentions, turning those links into {@code} text. Run by vendor.
"""

from __future__ import annotations

import os
import re
import sys
import zipfile
from collections import Counter
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
SHIM_ROOT = REPO / "neoforge-shim/src/main/java"
SHIM_PREFIX = "com.simibubi.create.infrastructure.fabric.neoforged"
NEO_PREFIX = "net.neoforged"
NEO_VERSION = "21.1.219"
SOURCE_DIRS = [REPO / "src/main/java", REPO / "registrate/src/main/java", SHIM_ROOT]
SOURCES_JAR_GLOB = f"~/.gradle/caches/modules-2/files-2.1/net.neoforged/neoforge/{NEO_VERSION}/*/neoforge-{NEO_VERSION}-sources.jar"

# NeoForge classes that a Fabric dependency already provides under their original names.
CLASSPATH_JARS = ["~/.gradle/caches/modules-2/files-2.1/fuzs.forgeconfigapiport/forgeconfigapiport-fabric/*/*/forgeconfigapiport-fabric-*.jar"]


def classpath_provided() -> set[str]:
	import glob
	found = set()
	for pattern in CLASSPATH_JARS:
		for jar in glob.glob(os.path.expanduser(pattern)):
			if jar.endswith("-sources.jar"):
				continue
			for name in zipfile.ZipFile(jar).namelist():
				if name.startswith("net/neoforged/") and name.endswith(".class") and "$" not in name:
					found.add(name[:-6].replace("/", "."))
	return found


REF = re.compile(r"\bnet\.neoforged((?:\.[A-Za-z_$][\w$]*)+)")
HEADER = f"// fabric: vendored from NeoForge {NEO_VERSION} (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7\n"


def shim_classes() -> set[str]:
	"""Top-level class names (NeoForge FQCN form) that the shim tree provides."""
	base = SHIM_ROOT / SHIM_PREFIX.replace(".", "/")
	if not base.exists():
		return set()
	return {NEO_PREFIX + "." + str(p.relative_to(base).with_suffix("")).replace(os.sep, ".")
			for p in base.rglob("*.java") if p.name != "package-info.java"}


def resolve(dotted: str, provided: set[str]) -> str | None:
	"""Longest prefix of `net.neoforged...` that is a provided top-level class (handles nested refs)."""
	parts = dotted.split(".")
	for end in range(len(parts), 2, -1):
		candidate = ".".join(parts[:end])
		if candidate in provided:
			return candidate
	return None


def rewrite(text: str, provided: set[str]) -> str:
	def sub(m: re.Match) -> str:
		full = "net.neoforged" + m.group(1)
		if resolve(full, provided):
			return SHIM_PREFIX + full[len(NEO_PREFIX):]
		# package wildcard / package reference whose classes are all shimmed is left alone on purpose
		return full
	return REF.sub(sub, text)


def cmd_imports(check: bool) -> int:
	provided = shim_classes()
	changed = []
	for root in SOURCE_DIRS:
		for path in root.rglob("*.java"):
			text = path.read_text(encoding="utf-8")
			if "net.neoforged" not in text:
				continue
			new = rewrite(text, provided)
			if new != text:
				changed.append(path)
				if not check:
					path.write_text(new, encoding="utf-8")
	print(f"{'Would rewrite' if check else 'Rewrote'} {len(changed)} files ({len(provided)} shim classes).")
	return 1 if check and changed else 0


def cmd_missing() -> int:
	provided = shim_classes() | classpath_provided()
	counts: Counter[str] = Counter()
	for root in SOURCE_DIRS:
		for path in root.rglob("*.java"):
			for m in REF.finditer(path.read_text(encoding="utf-8")):
				full = "net.neoforged" + m.group(1)
				if resolve(full, provided):
					continue
				# trim to the top-level class: first segment starting with an uppercase letter
				parts = full.split(".")
				for i, part in enumerate(parts):
					if part[:1].isupper():
						full = ".".join(parts[:i + 1])
						break
				counts[full] += 1
	for name, n in counts.most_common():
		print(f"{n:5d} {name}")
	return 0


def sources_jar() -> zipfile.ZipFile:
	import glob
	jars = glob.glob(os.path.expanduser(SOURCES_JAR_GLOB))
	if not jars:
		raise SystemExit(f"NeoForge {NEO_VERSION} sources jar not found in the Gradle cache")
	return zipfile.ZipFile(jars[0])


def cmd_vendor(targets: list[str]) -> int:
	jar = sources_jar()
	names = jar.namelist()
	wanted = []
	for t in targets:
		path = t.replace(".", "/")
		if t.endswith(".*"):
			pkg = path[:-2] + "/"
			wanted += [n for n in names if n.startswith(pkg) and n.endswith(".java") and "/" not in n[len(pkg):]]
		elif path + ".java" in names:
			wanted.append(path + ".java")
		else:
			raise SystemExit(f"{t} not found in NeoForge sources")
	provided = shim_classes() | {n[:-5].replace("/", ".") for n in wanted}
	for name in wanted:
		dest = SHIM_ROOT / SHIM_PREFIX.replace(".", "/") / name[len("net/neoforged/"):]
		if dest.exists():
			print(f"skip (exists): {dest.relative_to(REPO)}")
			continue
		text = jar.read(name).decode("utf-8")
		text = re.sub(r"^package net\.neoforged\.", "package " + SHIM_PREFIX + ".", rewrite(text, provided), count=1, flags=re.M)
		dest.parent.mkdir(parents=True, exist_ok=True)
		dest.write_text(HEADER + text, encoding="utf-8")
		print(f"vendored: {dest.relative_to(REPO)}")
	return 0


def cmd_tidy() -> int:
	changed = 0
	for path in SHIM_ROOT.rglob("*.java"):
		text = path.read_text(encoding="utf-8")
		imports = re.findall(r"^import (net\.neoforged\.[\w.]+)\.(\w+);\n", text, re.M)
		if not imports:
			continue
		code = "\n".join(l for l in text.splitlines() if not re.match(r"\s*(\*|/\*\*|//|import )", l))
		new = text
		for pkg, name in imports:
			if re.search(r"\b" + re.escape(name) + r"\b", code):
				continue
			new = new.replace(f"import {pkg}.{name};\n", "")
			new = re.sub(r"\{@link(?:plain)? " + re.escape(name) + r"((?:[#.][^}\s]*)?)(?: [^}]*)?\}", lambda m: "{@code " + name + m.group(1) + "}", new)
			new = re.sub(r"@see " + re.escape(name) + r"\b", f'@see "NeoForge {name}"', new)
		if new != text:
			path.write_text(new, encoding="utf-8")
			changed += 1
	print(f"Tidied {changed} files.")
	return 0


def main() -> int:
	if len(sys.argv) < 2:
		print(__doc__)
		return 1
	cmd, args = sys.argv[1], sys.argv[2:]
	if cmd == "imports":
		return cmd_imports("--check" in args)
	if cmd == "missing":
		return cmd_missing()
	if cmd == "vendor":
		result = cmd_vendor(args)
		cmd_tidy()
		return result
	if cmd == "tidy":
		return cmd_tidy()
	print(__doc__)
	return 1


if __name__ == "__main__":
	sys.exit(main())
