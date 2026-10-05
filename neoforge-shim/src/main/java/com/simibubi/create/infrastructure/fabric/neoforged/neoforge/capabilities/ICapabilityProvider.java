package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import org.jetbrains.annotations.Nullable;

/**
 * Create-owned re-implementation of NeoForge's {@code ICapabilityProvider} for Fabric (PORTING.md D7).
 */
@FunctionalInterface
public interface ICapabilityProvider<O, C, T> {
	@Nullable
	T getCapability(O object, C context);
}
