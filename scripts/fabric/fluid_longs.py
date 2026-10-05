#!/usr/bin/env python3
"""One-off helper for the D3 droplet migration (PORTING.md): widens fluid amounts from int to long in Create
sources that use the shim's fluid types. Only touches NeoForge fluid-handler signatures and int locals
assigned from fluid-amount calls; everything else is left to the compiler and review.
Usage: scripts/fabric/fluid_longs.py [--dry-run] [files...]
"""
import re
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
FLUID_IMPORT = re.compile(r"import com\.simibubi\.create\.infrastructure\.fabric\.neoforged\.neoforge\.fluids\.")

SIGNATURES = [
	(r"\bpublic int fill\(FluidStack", "public long fill(FluidStack"),
	(r"\bint fill\(FluidStack", "long fill(FluidStack"),
	(r"\bpublic int getTankCapacity\(int", "public long getTankCapacity(int"),
	(r"\bint getTankCapacity\(int", "long getTankCapacity(int"),
	(r"\bFluidStack drain\(int (\w+)", r"FluidStack drain(long \1"),
	(r"\bpublic int getFluidAmount\(\)", "public long getFluidAmount()"),
	(r"\bpublic int getSpace\(\)", "public long getSpace()"),
]
# int locals initialised from a fluid amount
AMOUNT_CALL = r"(?:\.getAmount\(\)|\.getFluidAmount\(\)|\.getTankCapacity\(|\.fill\(|\.amount\(\)|\.getSpace\(\)|forceFill\()"
LOCAL = re.compile(r"(?m)^(\s*(?:final\s+)?)int(\s+\w+\s*=\s*[^;]*" + AMOUNT_CALL + r")")


def convert(text):
	for pattern, replacement in SIGNATURES:
		text = re.sub(pattern, replacement, text)
	text = LOCAL.sub(r"\1long\2", text)
	return text


def main():
	dry = "--dry-run" in sys.argv
	args = [a for a in sys.argv[1:] if not a.startswith("--")]
	files = [Path(a) for a in args] or list((REPO / "src/main/java").rglob("*.java"))
	changed = 0
	for path in files:
		text = path.read_text(encoding="utf-8")
		if not FLUID_IMPORT.search(text):
			continue
		new = convert(text)
		if new != text:
			changed += 1
			if not dry:
				path.write_text(new, encoding="utf-8")
	print(f"{'Would change' if dry else 'Changed'} {changed} files")


if __name__ == "__main__":
	main()
