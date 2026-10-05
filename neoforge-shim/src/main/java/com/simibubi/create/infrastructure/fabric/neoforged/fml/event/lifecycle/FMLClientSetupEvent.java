package com.simibubi.create.infrastructure.fabric.neoforged.fml.event.lifecycle;

/**
 * Fired from Create's client entrypoint, after {@link FMLCommonSetupEvent}.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code FMLClientSetupEvent} for Fabric (PORTING.md D7).
 */
public class FMLClientSetupEvent extends ParallelDispatchEvent {}
