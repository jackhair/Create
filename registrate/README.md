# Vendored Registrate

Source: [ithundxr/Registrate](https://github.com/ithundxr/Registrate) branch `1.21/dev` @ `1304409`
(the fork upstream Create 6.0.11 pins as `com.tterrag.registrate:Registrate:MC1.21-1.3.0+67`).

Licensed under MPL-2.0 (see `LICENSE`); modified files keep that license. It's vendored so Create's
call sites stay identical to upstream while we port it to Fabric without Porting Lib (PORTING.md, decision D2).

It's compiled as part of Create's main source set (see `build.gradle`).

References for porting:
- Fabric equivalents: [Fabricators-of-Create/Registrate-Refabricated](https://github.com/Fabricators-of-Create/Registrate-Refabricated) `fabric/1.21.1`
- Later Minecraft versions: [tterrag1098/Registrate](https://github.com/tterrag1098/Registrate) branches `1.21.5/dev`, `1.21.8/dev`, `26.1/dev`, `26.2/dev`

Mark every Fabric change with a `// fabric:` comment, as in Create.
