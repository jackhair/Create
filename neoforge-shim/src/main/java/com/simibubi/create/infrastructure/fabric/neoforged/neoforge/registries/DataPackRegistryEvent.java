package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;

/**
 * Create-owned re-implementation of NeoForge's {@code DataPackRegistryEvent} on Fabric's
 * {@link DynamicRegistries} (PORTING.md D7).
 */
public abstract class DataPackRegistryEvent extends Event implements IModBusEvent {
	public static final class NewRegistry extends DataPackRegistryEvent {
		NewRegistry() {}

		public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec) {
			dataPackRegistry(registryKey, codec, null);
		}

		public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec, @Nullable Codec<T> networkCodec) {
			if (networkCodec == null)
				DynamicRegistries.register(registryKey, codec);
			else
				DynamicRegistries.registerSynced(registryKey, codec, networkCodec);
		}

		/** The builder consumer is ignored: Fabric's dynamic registries don't support its options. */
		public <T> void dataPackRegistry(ResourceKey<Registry<T>> registryKey, Codec<T> codec, @Nullable Codec<T> networkCodec, Consumer<RegistryBuilder<T>> consumer) {
			dataPackRegistry(registryKey, codec, networkCodec);
		}
	}
}
