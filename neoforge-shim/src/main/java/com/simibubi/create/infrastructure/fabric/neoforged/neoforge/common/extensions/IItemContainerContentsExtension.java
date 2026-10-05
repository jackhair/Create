package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * The parts of NeoForge's {@code IItemContainerContentsExtension} Create uses, injected into {@link ItemContainerContents}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IItemContainerContentsExtension {
	default int getSlots() {
		return ((ItemContainerContents) this).items.size();
	}

	default ItemStack getStackInSlot(int slot) {
		return ((ItemContainerContents) this).items.get(slot).copy();
	}
}
