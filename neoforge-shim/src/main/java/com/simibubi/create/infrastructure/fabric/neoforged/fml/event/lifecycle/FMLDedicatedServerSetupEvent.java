package com.simibubi.create.infrastructure.fabric.neoforged.fml.event.lifecycle;

/**
 * Fired from Create's server entrypoint, after {@link FMLCommonSetupEvent}.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code FMLDedicatedServerSetupEvent} for Fabric (PORTING.md D7).
 */
public class FMLDedicatedServerSetupEvent extends ParallelDispatchEvent {}
