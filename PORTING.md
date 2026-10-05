# Create → Fabric 26.3 Porting Plan

Branch: `mc26.3/fabric/dev` (fork: `jackhair/Create`)
Base: upstream `Creators-of-Create/Create` `mc1.21.1/dev` @ `a92700863` (Create 6.0.11, NeoForge 21.1.219, MC 1.21.1)
Status: **Phase 1b in progress (shim layer)**. Last updated 2026-10-04.

## Goal

Ship Create for **Fabric on Minecraft 26.3**, built from the official Create codebase, without Porting Lib, in a way that keeps upstream Create merges tractable.

## Remotes

| Remote | Repo | Use |
|---|---|---|
| `origin` | `jackhair/Create` | our fork, push here |
| `upstream` | `Creators-of-Create/Create` | source of truth for content and bugfixes (`mc1.21.1/dev`) |
| `fabricators` | `Fabricators-of-Create/Create` | **reference only**: `mc1.20.1/fabric/dev` (shipped Fabric 6.0.8.1), `mc1.21.1/fabric/dev` (abandoned Mar 2025, does not compile) |

## Where things stand (research summary)

**Upstream codebase:** 2,017 Java files, ~275k lines. 701 files import NeoForge. 230 of those only use `@OnlyIn`/`Dist` (a mechanical swap), so ~470 files have real dependencies. The baseline compiles cleanly on NeoForge with JDK 21.

**Already loader-neutral, thanks to Catnip (bundled in Ponder):**
- Networking: 114 packets, all sent through `CatnipServices.NETWORK`.
- Config: `ConfigBase` and `AllConfigs`.
- Most client-only dispatch: `CatnipServices.PLATFORM`.

**Dependency availability for Fabric** (checked 2026-10-04):

| Dependency | 1.21.1 | 26.1.2 | 26.3 |
|---|---|---|---|
| Fabric API | 0.116.17 | 0.155.3 | 0.161.0 |
| Flywheel / Vanillin | 1.0.7-48 / 1.1.4-48 | 1.0.6-mc26.1-9 / 1.1.3-mc26.1-9 | **none** |
| Ponder + Catnip | 1.0.87 | 1.0.50 | **none** |
| Registrate (Fabric fork) | 1.3.34 (needs Porting Lib) | none | none |
| Porting Lib | 3.1.0-beta.91 (snapshots) | none | none |
| Forge Config API Port | 21.1.6 | 26.1.5 | 26.3.1 |
| JEI / REI / EMI | yes / yes / yes | yes / yes / no | yes / yes / no |
| Sodium / Iris | yes | yes | alpha / yes |
| CC: Tweaked | yes | yes | yes (alpha-tagged) |
| Farmer's Delight Refabricated, JourneyMap, Xaero | yes | yes | yes |
| Trinkets / Accessories (Curios replacement) | yes | none | none |
| FTB Library / Chunks | yes | yes | none |

**Hard blockers at 26.3:**
- Flywheel, Vanillin and Ponder/Catnip stop at 26.1.2. We port them, or wait for upstream.
- No Registrate exists for Fabric on 26.x.
- Porting Lib has nothing published after 1.21.1.

## Strategy

1. **Change one axis at a time.** Do the loader port on 1.21.1, where every dependency exists. Then do the version jumps. Doing both at once is how the Fabricators' 1.21.1 attempt ended up with 3,600+ compile errors.
2. **No Porting Lib.** Put a small, Create-owned platform layer in `com.simibubi.create.infrastructure.fabric`. Seed it from the Fabricators' 1.21.1 shims (`infrastructure/fabric/{transfer,item,block,util}`). Use Catnip services wherever they already cover something.
3. **Minimize the upstream diff.**
   - Keep package and class structure identical.
   - Mark every port change with a `// fabric:` comment (the Fabricators' convention).
   - Prefer adapting at choke points over rewriting call sites.
4. **Break the build deliberately.** Take NeoForge off the classpath, then fix compile errors one subsystem at a time, committing per subsystem.
5. **Use upstream's generated data as a test oracle.** Once datagen runs on Fabric, diff its output against upstream's committed `src/generated`. Any difference that isn't a deliberate loader key is a bug.

## Decisions

| # | Decision | Recommendation | Status |
|---|---|---|---|
| D1 | Phase order | Fabric on 1.21.1 first, then 26.1.2, then 26.3 | **accepted** 2026-10-04 |
| D2 | Registration | Vendor Registrate (MPL-2.0) as an in-repo subproject, ported to Fabric without Porting Lib. Stub its datagen until Phase 1d. Use upstream Registrate's 26.1/26.2 releases as the reference for each version jump. | **accepted** 2026-10-04 |
| D3 | Fluid units | Droplets (81,000 per bucket) internally, matching the Fabric ecosystem. Recipe codecs read upstream's mB values and multiply by 81, so data JSON stays identical to upstream. Display in mB. | **accepted** 2026-10-04 |
| D4 | Datagen | Phase 1: ship upstream's generated resources, run through a script that rewrites `neoforge:` keys. Phase 1d: port datagen to Fabric and verify parity. Required before Phase 2, because the 26.x data formats change. | proposed |
| D5 | Compat | Compile against the Fabric builds of JEI, CC:T, Sodium, FD, FTB, JourneyMap, Xaero from the start, since compat code is woven into core (CC:T is referenced from 60 core files, JEI from 42), but don't load them at runtime until each is ported. NeoForge-only integrations are excluded from the source set and their call sites removed. Original plan: gate every compat module off at first. Re-enable after the core works: JEI/REI, CC:T, Sodium/Iris, FD, JourneyMap/Xaero. Drop Curios at 26.x unless Trinkets or Accessories gets ported. Drop NeoForge-only mods (TConstruct, FramedBlocks, DynamicTrees, StorageDrawers, etc.). | proposed |
| D6 | Mod id / distribution | Mod id stays `create`, private builds only. Assets are All Rights Reserved; a public release needs the Create team's permission. | proposed |
| D7 | NeoForge API shim layer | Create-owned copies of the NeoForge APIs Create uses, under `com.simibubi.create.infrastructure.fabric.neoforged.*` in `neoforge-shim/` (mirroring NeoForge packages). A script rewrites imports, so call sites stay identical to upstream. Plain-Java NeoForge classes are **vendored** (LGPL-2.1, kept in their own folder): item handlers, datagen model builders, registries. Loader-facing pieces are **re-implemented** on Fabric API: registries on Fabric attributes and DynamicRegistries; capabilities on BlockApiLookup, bridged to the Transfer API. Both NeoForge **event buses** are emulated: `SimpleEventBus` with NeoForge dispatch semantics; `@EventBusSubscriber` classes come from a build-time index, and each game event gets a small adapter that fires it from a Fabric callback or mixin. NeoForge **extension methods** become shim interfaces injected into vanilla classes (Loom interface injection) plus mixins. Fluid amounts are long droplets (D3), so fluid call sites still change, and compile errors flag each one. | **in progress** (bus, FML and registry shims done) |

## Phases

### Phase 0 — Preparation (done)
- [x] Fork, add remotes, create the `mc26.3/fabric/dev` branch
- [x] Confirm the upstream NeoForge baseline compiles (JDK 21)
- [x] Inventory NeoForge usage, Fabric dependency matrix, Fabricators' techniques
- [x] Sign off decisions D1–D3 (D4–D6 still proposed)
- [x] NeoForge gametest baseline: **all 64 required tests pass** (1.46 s). List in `docs/porting/neoforge-gametest-baseline.txt`
- [ ] Reference test world with the smoke-test builds, made on NeoForge in a client. **Needs Jack**, since it's hands-on play; optional but useful for side-by-side checks
- [x] `scripts/fabric/convert_onlyin.py`: converts all 311 `@OnlyIn` in 230 files (idempotent). 20 files that use `Dist` for `@EventBusSubscriber`/`@Mod`/`DistExecutor` are flagged for manual work
- [x] `scripts/fabric/convert_generated_resources.py`: rewrites 584 data files (idempotent, has a `--check` mode). Leaves `data/neoforge/data_maps/*` and `data/create/neoforge/biome_modifier/*` in place for code-side registration
  - Fluid ingredient type names it emits, which the 1c codecs must register: `create:fluid`, `create:fluid_tag`, `create:fluid_components`; plus `create:block_tag` for items
- [x] CI: deferred. Add GitHub Actions (build + gametests + `convert_generated_resources.py --check`) once Phase 1 compiles

Both scripts are first run as part of 1a, so the branch stays buildable on NeoForge until the toolchain switch.

### Phase 1 — Fabric on MC 1.21.1
Exit criteria:
- Compiles with no NeoForge on the classpath
- Client and dedicated server boot
- All gametests pass
- Smoke-test checklist passes

**1a. Build and bootstrap**: build switched (`dd4826ccf`). Remaining: entrypoints and Mods/FML replacements, which move into 1b
- [x] Gradle 9.8.0, fabric-loom 1.18.2 (remapping), Mojmap + Parchment; Gradle runs on JDK 25, target Java 21
- [x] `fabric.mod.json` template; `create.accesswidener` generated by `scripts/fabric/at_to_aw.py` (41 entries; 4 stale upstream AT entries skipped)
- [x] Vendored Registrate in `registrate/`, compiled with Create (Lombok)
- [x] Conversion scripts run (`0cdad91e1`)
- [x] First compile: **3,021 errors in 591 files** (lower bound, since javac stops before deeper analysis while imports are unresolved)

**Progress metric:** compile errors and files with errors, logged at each milestone in `docs/porting/progress.md`.

- Replace ModDevGradle with Loom. 1.21.1 is obfuscated, so this needs the remapping plugin: Mojmap + Parchment 2024.11.17.
  - Run Gradle on JDK 25. The target stays Java 21.
- Write `fabric.mod.json` (from `neoforge.mods.toml`), entrypoints for `Create`/`CreateClient`, and the mixin config.
- Convert `accesstransformer.cfg` (41 entries) to `create.accesswidener` (`-f` becomes `mutable`).
- Dependencies: Fabric API, Flywheel/Vanillin fabric-1.21.1, Ponder/Catnip fabric, Forge Config API Port 21.1.x.
- Run `scripts/fabric/convert_onlyin.py` and `convert_generated_resources.py`, then hand-convert the 20 flagged `Dist` files; `compat/Mods.java` → `FabricLoader.isModLoaded`; `FMLEnvironment`/`FMLPaths`/`ModList` replacements.

**1b. Core platform layer** (`neoforge-shim/`, per D7)
- [x] Tooling: `scripts/fabric/shim.py` (vendor / imports / missing), `scripts/fabric/javac_check.sh` (fast error summary)
- [x] Item handlers, datagen model builders, utils (vendored)
- [x] Event buses (`SimpleEventBus`), FML environment, `ModList`/`ModContainer` (configs via Forge Config API Port), lifecycle events
- [x] Registries: `DeferredHolder`/`DeferredRegister`/`RegisterEvent` (vendored), `RegistryBuilder`, `NewRegistryEvent`, `DataPackRegistryEvent`, `RegistrationPhase` (NeoForge's order)
- [ ] Capabilities on `BlockApiLookup`/`ItemApiLookup`/`EntityApiLookup`, bridged both ways to `ItemStorage.SIDED`/`FluidStorage` (1c)
- [ ] Fluids: `FluidStack`/`IFluidHandler`/`FluidTank` with long droplets, `FluidType` on `FluidVariantAttributes` (1c)
- [ ] `ModelData`/`BakedModelWrapper`, client extensions (1d)
- [ ] Game-event adapters for the 73 event types; `@EventBusSubscriber` index (Gradle task)
- [ ] Extension-method interfaces + interface injection (`ILevelExtension#getCapability` and others)
- [ ] Port Registrate onto the shims; Fabric entrypoints (`CreateFabric`, client) running `RegistrationPhase` and lifecycle events

Original plan text:
- **Registration (D2):**
  - Vendored Registrate.
  - `DeferredRegister` (14 files) → `Registry.register`.
  - The 12 custom registries in `CreateBuiltInRegistries` → `FabricRegistryBuilder`.
  - Datapack registry → `DynamicRegistries`.
  - `CreateDataMaps` (blaze burner fuels) → a custom reload listener.
- **Events:**
  - Wire FAPI callbacks in `CommonEvents`/`ClientEvents`/`InputEvents` through one `register()` each.
  - Create's own events (`BlockEntityBehaviourEvent`, `TrackGraphMergeEvent`, `PipeCollisionEvent`, `DeployerRecipeSearchEvent`) become Fabric `Event<T>`.
  - Events FAPI lacks (Living drops/knockback/damage/target, EntityEvent.Size, render hand/arm, fog, etc.) each get a Create-owned event fired from a mixin. Roughly 25 of them.
- **Extension-method hooks** (~90 files) get Create-local marker interfaces plus mixins: `shouldCheckWeakPower`, `canConnectRedstone`, `getPistonPushReaction`, landing/running/destroy/hit effects, `onDestroyedByPlayer`, `getCloneItemStack`, `onItemUseFirst`, `IEntityWithComplexSpawn`, and so on. Use FAPI registries where they exist: flammable, path node types, `FabricItem`, `EquipmentSlotProvider`.
- **Misc:**
  - `FakePlayer` → FAPI `FakePlayer`
  - `AttachmentType` → Data Attachment API
  - `Tags.*` → `c:` conventional tags
  - Biome modifiers → `BiomeModifications`

**1c. Transfer and fluids** (largest chunk, ~150 + ~100 files)
- Shims: `ItemStackHandler`/`SlottedStackStorage`, `FluidStack` (droplets, with finished codecs), `FluidTank`, `TransferUtil`, cached lookup (the `StorageProvider` equivalent).
- Choke points first:
  - `CapManipulationBehaviourBase` / `InvManipulationBehaviour` / `TankManipulationBehaviour`
  - `ItemHelper`, `SmartInventory`, `FluidHelper`, `SmartFluidTank(Behaviour)`, `CombinedTankWrapper`
  - The `MountedItemStorage` and `MountedFluidStorage` APIs
- Replace `RegisterCapabilitiesEvent` (42 block entities, all routed through `CommonEvents.registerCapabilities`) with `ItemStorage.SIDED` / `FluidStorage.SIDED` registrations.
- Fluids:
  - `FluidType` (9 files) → `FluidVariantAttributeHandler` + `FluidRenderHandlerRegistry`
  - Our own milk fluid
  - `FluidInteractionRegistry` → mixin
- Recipes: `SizedFluidIngredient` → a Create fluid ingredient; `ICustomIngredient` → `fabric-recipe-api` `CustomIngredient`; `IBrewingRecipe` → FAPI brewing.

**1d. Client**
- Models: `BakedModelWrapperWithData`/`ModelData` (28 files) → FRAPI `emitBlockQuads` + `RenderDataBlockEntity`. Choke points: `CTModel`, copycat models, `BeltModel`, `TrackModel`, `PipeAttachmentModel`, `FluidTankModel`, `ModelSwapper`.
- **43 `neoforge:obj` + 7 `neoforge:composite` models.** Fabric has neither loader. Options: vendor a small OBJ loader, or bake the OBJs to JSON once. Decide during 1d.
- Item renderers: `SimpleCustomRenderer`/`CustomRenderedItems` → `BuiltinItemRendererRegistry`.
- Other client APIs:
  - GUI layers → `HudRenderCallback`
  - `KeyModifier` key mappings → `KeyBindingHelper`
  - Particles → `ParticleFactoryRegistry`
  - Item decorators → mixin
  - Config screen → Mod Menu

**1e. Datagen (D4), gametests, compat**
- `CreateDatagen` → FAPI `DataGeneratorEntrypoint`. Run the parity diff against upstream `src/generated`.
- Port gametests to Fabric's gametest API; all 64 from the NeoForge baseline must pass.
- Re-enable compat per D5.

**Smoke-test checklist (Phases 1–3):**
- Kinetics: water wheel → shaft → press/mixer/basin recipes.
- Items and fluids: belts, funnels, chutes; fluid pipes and tanks (unit display).
- Contraptions: mechanical bearing, piston, gantry, minecart contraption.
- Trains: track placement, station, schedule.
- Deployer.
- Logistics: packager, stock ticker, frogport.
- Blocks: copycats, connected textures.
- Ponder UI; JEI/REI categories.
- Dedicated server + client join.

### Phase 2 — MC 1.21.1 → 26.1.2
Dependencies all exist: Flywheel/Vanillin/Ponder/Catnip 26.1.2, FAPI 0.155.3. Registrate is our vendored copy, ported using upstream Registrate MC26.1-1.5.7 as the reference.
- **Toolchain:**
  - Switch to the non-remapping `net.fabricmc.fabric-loom`; `modImplementation` becomes `implementation`.
  - Java 25, Gradle 9.8.
  - FAPI Mojang-name renames, using Fabric's IDEA migration map.
- **Vanilla changes, by version:**
  - 1.21.2: registry key must be set in block/item settings; recipes become server-only (use FAPI `RecipeSynchronization` for JEI/REI and Create's client-side recipe needs); entity render states; merged `InteractionResult`.
  - 1.21.4: item model definitions (`assets/*/items/`); item tints in JSON.
  - 1.21.5: `Optional`-returning NBT getters; `BlockEntity#onBlockReplaced` ordering.
  - 1.21.6: `RenderPipeline` instead of `RenderSystem` (28 files use `RenderSystem`); `ValueInput`/`ValueOutput` block-entity saving; GUI render state.
  - 1.21.9: render command queue for block entity, entity and particle renderers; `BlockEntityRenderState`.
  - 26.1: `ItemStackTemplate`; codec-only `RecipeSerializer`; `FluidModel` replaces `FluidRenderHandler`; automatic chunk layers.
- Regenerate all data with Fabric datagen (this is why D4 must finish in Phase 1).
- Check the NeoForged primers for each version as the detailed checklist.

### Phase 3 — 26.1.2 → 26.3
- **Dependencies first:** if upstream hasn't published 26.3 builds, fork and port Flywheel, Vanillin and Ponder/Catnip (Fabric modules only).
- **26.2:**
  - Experimental Vulkan backend. Raw GL must go through Blaze3D; 4 Create files use GL directly.
  - `BlockIds`/`ItemIds` split; datagen `valueLookupBuilder` removed.
  - `gui.setScreen`.
- **26.3:**
  - Recipes and advancements become reloadable dynamic registries.
  - Fuel and compost become item components.
  - FAPI drops `FuelRegistry`, Strippable, and similar registries (`BlockTransformerHelper` replaces them).
  - Block codecs removed (60 overrides to delete).
  - GLFW replaced by SDL (13 files use GLFW).
  - `FluidVariantAttributes` API change.

## Risk register

| Risk | Impact | Mitigation |
|---|---|---|
| Flywheel/Ponder never reach 26.3 upstream | Blocks Phase 3 | Budget for porting them ourselves; reassess when Phase 2 completes |
| Upstream Create ships its own 26.1 port mid-way | Merge pain or wasted work | `// fabric:` markers, small diffs, choke-point adapters; re-evaluate rebasing onto their 26.1 branch when it appears |
| Transfer API semantics (transactions vs simulate/execute) | Subtle duplication or voiding bugs | Gametests plus the logistics part of the smoke test; port the Fabricators' proven patterns |
| FRAPI / model API churn after 1.21.4 and in 26.1 | Model rework twice | Keep model logic separate from API glue; accept rework |
| Droplet/mB conversion edge cases | Lossy amounts | Droplets internally; convert only at data load and display |
| Assets are All Rights Reserved | Can't distribute publicly | Private builds; seek permission before any release |

## Reference material

- Fabricators' Fabric 6.0.8 diff, file by file: `git diff mc1.20.1-6.0.8 fabricators/mc1.20.1/fabric/dev -- <path>`. Grep for `// fabric:` there.
- Fabricators' 1.21.1 shims: `git show fabricators/mc1.21.1/fabric/dev:src/main/java/com/simibubi/create/infrastructure/fabric/...`
- Fabric blog posts 1.21.2 → 26.3: https://fabricmc.net/blog/
- Ponder/Catnip 26.1: `Creators-of-Create/Ponder` `mc26.1/dev` (Catnip services: platform, fluids, config, networking)
- Community NeoForge 26.2 port (unreviewed, reference only): `doscarchase/Create` `codex/create-mc26.2`
