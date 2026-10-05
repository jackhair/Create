package com.simibubi.create.infrastructure.fabric.neoforged.fml.event;

/**
 * Marks events posted on the mod bus rather than {@code NeoForge.EVENT_BUS}; used to route
 * {@code @EventBusSubscriber} methods. Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IModBusEvent {}
