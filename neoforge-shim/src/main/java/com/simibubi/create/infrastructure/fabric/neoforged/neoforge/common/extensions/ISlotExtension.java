package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.world.inventory.Slot;

/**
 * The parts of NeoForge's {@code ISlotExtension} Create uses, injected into {@link Slot}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface ISlotExtension {
	default int getSlotIndex() {
		return ((Slot) this).getContainerSlot();
	}
}
