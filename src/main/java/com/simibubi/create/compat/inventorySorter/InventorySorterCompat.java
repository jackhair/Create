package com.simibubi.create.compat.inventorySorter;

import com.simibubi.create.compat.Mods;
import com.simibubi.create.content.logistics.redstoneRequester.RedstoneRequesterMenu.SorterProofSlot;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.IEventBus;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.InterModComms;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

/**
 * Compatibility with cpw's InventorySorter.
 * We need to stop it from interfering with scrolling in the Redstone Requester's screen.
 */
public class InventorySorterCompat {
	public static final String SLOT_BLACKLIST = "slotblacklist";

	public static void init(IEventBus bus) {
		bus.addListener(InventorySorterCompat::sendImc);
	}

	private static void sendImc(InterModEnqueueEvent event) {
		InterModComms.sendTo(Mods.INVENTORYSORTER.id(), SLOT_BLACKLIST, SorterProofSlot.class::getName);
	}
}
