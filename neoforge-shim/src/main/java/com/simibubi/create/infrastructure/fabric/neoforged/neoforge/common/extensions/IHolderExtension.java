package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Holder;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.DataMapLoader;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.datamaps.DataMapType;

/**
 * The parts of NeoForge's {@code IHolderExtension} Create uses, injected into {@link Holder}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IHolderExtension<T> {
	@Nullable
	@SuppressWarnings("unchecked")
	default <A> A getData(DataMapType<T, A> type) {
		return DataMapLoader.getData(type, (Holder<T>) this);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	default net.minecraft.resources.ResourceKey<T> getKey() {
		return ((Holder<T>) this).unwrapKey().orElse(null);
	}
}
