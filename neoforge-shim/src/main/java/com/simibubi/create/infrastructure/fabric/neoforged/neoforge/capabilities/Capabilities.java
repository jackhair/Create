package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.IFluidHandler;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandler;

/**
 * NeoForge's standard capabilities, under NeoForge's names. {@code bridge.TransferBridges} connects them
 * to Fabric's Transfer API in both directions.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code Capabilities} for Fabric (PORTING.md D7).
 */
public final class Capabilities {
	private Capabilities() {}

	public static final class ItemHandler {
		public static final BlockCapability<IItemHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(id("item_handler"), IItemHandler.class);
		public static final EntityCapability<IItemHandler, @Nullable Void> ENTITY = EntityCapability.createVoid(id("item_handler"), IItemHandler.class);
		public static final EntityCapability<IItemHandler, @Nullable Direction> ENTITY_AUTOMATION = EntityCapability.createSided(id("item_handler_automation"), IItemHandler.class);
		public static final ItemCapability<IItemHandler, @Nullable Void> ITEM = ItemCapability.createVoid(id("item_handler"), IItemHandler.class);

		private ItemHandler() {}
	}

	public static final class FluidHandler {
		public static final BlockCapability<IFluidHandler, @Nullable Direction> BLOCK = BlockCapability.createSided(id("fluid_handler"), IFluidHandler.class);
		public static final EntityCapability<IFluidHandler, @Nullable Direction> ENTITY = EntityCapability.createSided(id("fluid_handler"), IFluidHandler.class);
		public static final ItemCapability<IFluidHandlerItem, @Nullable Void> ITEM = ItemCapability.createVoid(id("fluid_handler"), IFluidHandlerItem.class);

		private FluidHandler() {}
	}

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath("neoforge", path);
	}
}
