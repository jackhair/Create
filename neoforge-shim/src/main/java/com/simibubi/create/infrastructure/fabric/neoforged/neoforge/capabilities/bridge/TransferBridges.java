package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.BlockCapability;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.Capabilities;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.ICapabilityProvider;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.NeoForgeMod;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.IFluidHandler;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandler;

/**
 * Connects NeoForge's standard capabilities to Fabric's Transfer API so Create interoperates with other Fabric
 * mods:
 * <ul>
 * <li>providers registered for {@link Capabilities.ItemHandler#BLOCK} / {@link Capabilities.FluidHandler#BLOCK}
 * are also registered on {@link ItemStorage#SIDED} / {@link FluidStorage#SIDED}, wrapped as storages;</li>
 * <li>capability lookups fall back to the Fabric lookups (vanilla containers, other mods), wrapped as handlers;</li>
 * <li>{@link Capabilities.FluidHandler#ITEM} falls back to {@link FluidStorage#ITEM}.</li>
 * </ul>
 * Create code (PORTING.md D7), not a NeoForge class.
 */
public final class TransferBridges {
	private static boolean initialized;

	private TransferBridges() {}

	/** Called once by Create's Fabric entrypoint after registration. */
	public static void init() {
		if (initialized)
			return;
		initialized = true;

		Capabilities.ItemHandler.BLOCK.lookup().registerFallback((level, pos, state, be, side) ->
			StorageItemHandler.of(ItemStorage.SIDED.find(level, pos, state, be, side)));
		Capabilities.FluidHandler.BLOCK.lookup().registerFallback((level, pos, state, be, side) ->
			StorageFluidHandler.of(FluidStorage.SIDED.find(level, pos, state, be, side)));
		Capabilities.FluidHandler.ITEM.lookup().registerFallback((stack, context) -> ItemContextFluidHandler.of(stack));

		if (NeoForgeMod.MILK.isBound()) {
			// fabric: vanilla's milk bucket isn't a fluid container on Fabric; NeoForge makes it one when milk is enabled
			FluidVariant milk = FluidVariant.of(NeoForgeMod.MILK.value());
			FluidStorage.combinedItemApiProvider(Items.MILK_BUCKET).register(context ->
				new FullItemFluidStorage(context, Items.BUCKET, milk, FluidConstants.BUCKET));
			FluidStorage.combinedItemApiProvider(Items.BUCKET).register(context ->
				new EmptyItemFluidStorage(context, Items.MILK_BUCKET, NeoForgeMod.MILK.value(), FluidConstants.BUCKET));
		}
	}

	@SuppressWarnings("unchecked")
	public static <T, C, BE extends BlockEntity> void exportBlockEntity(BlockCapability<T, C> capability, BlockEntityType<BE> type, ICapabilityProvider<BE, C, T> provider) {
		if (capability == Capabilities.ItemHandler.BLOCK) {
			ICapabilityProvider<BE, C, IItemHandler> items = (ICapabilityProvider<BE, C, IItemHandler>) provider;
			ItemStorage.SIDED.registerForBlockEntity((be, side) -> {
				IItemHandler handler = items.getCapability(be, (C) side);
				return handler == null ? null : ItemHandlerStorage.of(handler);
			}, type);
		} else if (capability == Capabilities.FluidHandler.BLOCK) {
			ICapabilityProvider<BE, C, IFluidHandler> fluids = (ICapabilityProvider<BE, C, IFluidHandler>) provider;
			FluidStorage.SIDED.registerForBlockEntity((be, side) -> {
				IFluidHandler handler = fluids.getCapability(be, (C) side);
				return handler == null ? null : FluidHandlerStorage.of(handler);
			}, type);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T, C> void exportBlock(BlockCapability<T, C> capability, IBlockCapabilityProvider<T, C> provider, Block... blocks) {
		if (capability == Capabilities.ItemHandler.BLOCK) {
			ItemStorage.SIDED.registerForBlocks((level, pos, state, be, side) -> {
				IItemHandler handler = (IItemHandler) provider.getCapability(level, pos, state, be, (C) side);
				return handler == null ? null : ItemHandlerStorage.of(handler);
			}, blocks);
		} else if (capability == Capabilities.FluidHandler.BLOCK) {
			FluidStorage.SIDED.registerForBlocks((level, pos, state, be, side) -> {
				IFluidHandler handler = (IFluidHandler) provider.getCapability(level, pos, state, be, (C) side);
				return handler == null ? null : FluidHandlerStorage.of(handler);
			}, blocks);
		}
	}
}
