package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries;

import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.BuiltInRegistries;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;

/**
 * Fired on the mod bus before {@link RegisterEvent}s so custom registries exist in time.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code NewRegistryEvent} for Fabric (PORTING.md D7).
 */
public class NewRegistryEvent extends Event implements IModBusEvent {
	NewRegistryEvent() {}

	public <T> Registry<T> create(RegistryBuilder<T> builder) {
		Registry<T> registry = builder.create();
		register(registry);
		return registry;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> void register(Registry<T> registry) {
		((WritableRegistry) BuiltInRegistries.REGISTRY).register(registry.key(), registry, RegistrationInfo.BUILT_IN);
	}
}
