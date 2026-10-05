package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.Entity;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.EntityCapability;

/**
 * The parts of NeoForge's {@code IEntityExtension} Create uses, injected into {@link Entity}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IEntityExtension {
	private Entity self() {
		return (Entity) this;
	}

	@Nullable
	default <T, C> T getCapability(EntityCapability<T, C> capability, C context) {
		return capability.getCapability(self(), context);
	}

	@Nullable
	default <T> T getCapability(EntityCapability<T, @Nullable Void> capability) {
		return capability.getCapability(self(), null);
	}
}
