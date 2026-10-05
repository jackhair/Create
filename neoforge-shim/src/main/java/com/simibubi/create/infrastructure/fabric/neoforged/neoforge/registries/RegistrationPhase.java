package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.IEventBus;

/**
 * Fires NeoForge's registration events on a mod bus, in NeoForge's order: {@link NewRegistryEvent},
 * {@link DataPackRegistryEvent.NewRegistry}, then one {@link RegisterEvent} per registry. Registries go
 * attributes, data components and armor materials first, then the rest in creation order (NeoForge's
 * "vanilla order", then modded registries).
 * <p>
 * Create code (PORTING.md D7), not a NeoForge class. Called from Create's Fabric entrypoint.
 */
public final class RegistrationPhase {
	private RegistrationPhase() {}

	public static void run(IEventBus modBus) {
		NewRegistryEvent newRegistries = new NewRegistryEvent();
		NeoForgeRegistries.registerAll(newRegistries);
		modBus.post(newRegistries);
		modBus.post(new DataPackRegistryEvent.NewRegistry());
		for (ResourceLocation id : registrationOrder()) {
			ResourceKey<? extends Registry<?>> key = ResourceKey.createRegistryKey(id);
			Registry<?> registry = Objects.requireNonNull(BuiltInRegistries.REGISTRY.get(id));
			modBus.post(new RegisterEvent(key, registry));
		}
	}

	public static Set<ResourceLocation> registrationOrder() {
		Set<ResourceLocation> ordered = new LinkedHashSet<>();
		ordered.add(Registries.ATTRIBUTE.location());
		ordered.add(Registries.DATA_COMPONENT_TYPE.location());
		ordered.add(Registries.ARMOR_MATERIAL.location());
		ordered.addAll(BuiltInRegistries.REGISTRY.keySet());
		return ordered;
	}
}
