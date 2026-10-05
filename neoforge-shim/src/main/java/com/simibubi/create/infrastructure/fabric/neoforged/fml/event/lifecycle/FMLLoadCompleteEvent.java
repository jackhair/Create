package com.simibubi.create.infrastructure.fabric.neoforged.fml.event.lifecycle;

/**
 * Fired when the client has started or the server is starting, once every mod has initialized.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code FMLLoadCompleteEvent} for Fabric (PORTING.md D7).
 */
public class FMLLoadCompleteEvent extends ParallelDispatchEvent {}
