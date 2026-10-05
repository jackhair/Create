# NeoForge API shim layer

Create-owned copies and re-implementations of the NeoForge APIs Create uses, so Create's call
sites stay close to upstream (PORTING.md, decision D7). Classes mirror NeoForge's package layout under
`com.simibubi.create.infrastructure.fabric.neoforged`: for example
`net.neoforged.neoforge.items.IItemHandler` becomes
`com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandler`.

- **Vendored** classes (plain Java over vanilla types) are copied from NeoForge 21.1.219 sources with
  `scripts/fabric/shim.py vendor <class>`. They keep NeoForge's LGPL-2.1-only license (`LICENSE.txt`) and
  start with a `// fabric: vendored from NeoForge` line. Changes are marked `// fabric:`.
- **Re-implemented** classes (registries, capabilities and anything else that hooks the loader) are
  written for Fabric API with NeoForge's public shape. They're Create code (MIT) and say so in their
  javadoc.

After adding a shim, run `scripts/fabric/shim.py imports` to point Create's and Registrate's imports at
it. `scripts/fabric/shim.py missing` lists the NeoForge classes still referenced.

Compiled as part of Create's main source set (see `build.gradle`).
