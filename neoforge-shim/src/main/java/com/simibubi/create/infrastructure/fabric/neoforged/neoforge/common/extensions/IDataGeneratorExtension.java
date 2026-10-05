package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

/**
 * The parts of NeoForge's DataGenerator patches Create's datagen uses, injected into {@link DataGenerator}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IDataGeneratorExtension {
	private DataGenerator self() {
		return (DataGenerator) this;
	}

	default PackOutput getPackOutput() {
		return self().vanillaPackOutput;
	}

	default <T extends DataProvider> T addProvider(boolean run, T provider) {
		return self().getVanillaPack(run).addProvider(output -> provider);
	}

	/** Moves another generator's providers into this one. */
	default void merge(DataGenerator other) {
		self().providersToRun.putAll(other.providersToRun);
		self().allProviderIds.addAll(other.allProviderIds);
		other.providersToRun.clear();
	}
}
