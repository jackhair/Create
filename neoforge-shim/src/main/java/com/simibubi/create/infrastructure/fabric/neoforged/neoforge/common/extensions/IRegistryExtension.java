package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.core.Registry;

/**
 * The parts of NeoForge's {@code IRegistryExtension} Create uses, injected into {@link Registry}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IRegistryExtension<T> {
	@SuppressWarnings("unchecked")
	default boolean containsValue(T value) {
		return ((Registry<T>) this).getKey(value) != null;
	}
}
