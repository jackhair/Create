package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.common;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidType;

/**
 * Registers client extensions for blocks, items and fluid types. Fired once by Create's Fabric client
 * entrypoint after registration.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code RegisterClientExtensionsEvent} for Fabric (PORTING.md D7).
 */
public class RegisterClientExtensionsEvent extends Event implements IModBusEvent {
	public RegisterClientExtensionsEvent() {}

	public void registerBlock(IClientBlockExtensions extensions, Block... blocks) {
		for (Block block : blocks)
			ClientExtensionsManager.BLOCK_EXTENSIONS.put(block, extensions);
	}

	public boolean isBlockRegistered(Block block) {
		return ClientExtensionsManager.BLOCK_EXTENSIONS.containsKey(block);
	}

	public void registerItem(IClientItemExtensions extensions, Item... items) {
		for (Item item : items)
			ClientExtensionsManager.ITEM_EXTENSIONS.put(item, extensions);
	}

	public boolean isItemRegistered(Item item) {
		return ClientExtensionsManager.ITEM_EXTENSIONS.containsKey(item);
	}

	public void registerFluidType(IClientFluidTypeExtensions extensions, FluidType... fluidTypes) {
		for (FluidType type : fluidTypes)
			ClientExtensionsManager.FLUID_TYPE_EXTENSIONS.put(type, extensions);
	}

	public boolean isFluidTypeRegistered(FluidType type) {
		return ClientExtensionsManager.FLUID_TYPE_EXTENSIONS.containsKey(type);
	}
}
