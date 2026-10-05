package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.ItemStack;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.ItemCapability;

/**
 * The parts of NeoForge's {@code IItemStackExtension} Create uses, injected into {@link ItemStack}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IItemStackExtension {
	private ItemStack self() {
		return (ItemStack) this;
	}

	@Nullable
	default <T, C> T getCapability(ItemCapability<T, C> capability, C context) {
		return capability.getCapability(self(), context);
	}

	@Nullable
	default <T> T getCapability(ItemCapability<T, @Nullable Void> capability) {
		return capability.getCapability(self(), null);
	}
}
