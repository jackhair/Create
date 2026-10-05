package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Lifecycle;

import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.fabricmc.fabric.api.event.registry.RegistryAttributeHolder;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

/**
 * Builds a custom built-in registry. {@link #create()} doesn't add it to the root registry; register it with
 * {@link NewRegistryEvent#register} or directly, as NeoForge does.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code RegistryBuilder} on Fabric registry attributes
 * (PORTING.md D7). {@code maxId} isn't enforced.
 */
public class RegistryBuilder<T> {
	private final ResourceKey<? extends Registry<T>> registryKey;
	private final List<AddCallback<T>> addCallbacks = new ArrayList<>();
	private final List<BakeCallback<T>> bakeCallbacks = new ArrayList<>();
	@Nullable
	private ResourceLocation defaultKey;
	private boolean sync;
	private boolean intrusiveHolders;

	public RegistryBuilder(ResourceKey<? extends Registry<T>> registryKey) {
		this.registryKey = registryKey;
	}

	public RegistryBuilder<T> defaultKey(ResourceLocation key) {
		this.defaultKey = key;
		return this;
	}

	public RegistryBuilder<T> defaultKey(ResourceKey<T> key) {
		return defaultKey(key.location());
	}

	public RegistryBuilder<T> maxId(int maxId) {
		return this;
	}

	public RegistryBuilder<T> sync(boolean sync) {
		this.sync = sync;
		return this;
	}

	public RegistryBuilder<T> withIntrusiveHolders() {
		this.intrusiveHolders = true;
		return this;
	}

	public RegistryBuilder<T> onAdd(AddCallback<T> callback) {
		addCallbacks.add(callback);
		return this;
	}

	public RegistryBuilder<T> onBake(BakeCallback<T> callback) {
		bakeCallbacks.add(callback);
		return this;
	}

	public RegistryBuilder<T> disableRegistrationCheck() {
		return this;
	}

	public Registry<T> create() {
		List<BakeCallback<T>> bake = List.copyOf(bakeCallbacks);
		MappedRegistry<T> registry;
		if (defaultKey != null) {
			registry = new DefaultedMappedRegistry<>(defaultKey.toString(), registryKey, Lifecycle.stable(), intrusiveHolders) {
				@Override
				public Registry<T> freeze() {
					Registry<T> frozen = super.freeze();
					bake.forEach(c -> c.onBake(this));
					return frozen;
				}
			};
		} else {
			registry = new MappedRegistry<>(registryKey, Lifecycle.stable(), intrusiveHolders) {
				@Override
				public Registry<T> freeze() {
					Registry<T> frozen = super.freeze();
					bake.forEach(c -> c.onBake(this));
					return frozen;
				}
			};
		}

		RegistryAttributeHolder attributes = RegistryAttributeHolder.get(registryKey);
		attributes.addAttribute(RegistryAttribute.MODDED);
		if (sync)
			attributes.addAttribute(RegistryAttribute.SYNCED);

		for (AddCallback<T> callback : addCallbacks)
			RegistryEntryAddedCallback.event(registry).register((rawId, id, value) -> callback.onAdd(registry, rawId, ResourceKey.create(registryKey, id), value));

		return registry;
	}

	@FunctionalInterface
	public interface AddCallback<T> {
		void onAdd(Registry<T> registry, int id, ResourceKey<T> key, T value);
	}

	@FunctionalInterface
	public interface BakeCallback<T> {
		void onBake(Registry<T> registry);
	}
}
