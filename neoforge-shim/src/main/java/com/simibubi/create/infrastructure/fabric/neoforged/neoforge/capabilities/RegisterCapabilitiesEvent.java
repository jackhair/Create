package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import java.util.Objects;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge.TransferBridges;

/**
 * Registers capability providers as Fabric API Lookup providers. Providers for the standard item and fluid
 * capabilities are also exposed to Fabric's Transfer API (see {@link TransferBridges}). Fired once on the
 * mod bus by Create's Fabric entrypoint, after registration.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code RegisterCapabilitiesEvent} for Fabric (PORTING.md D7).
 */
public class RegisterCapabilitiesEvent extends Event implements IModBusEvent {
	public RegisterCapabilitiesEvent() {}

	public <T, C> void registerBlock(BlockCapability<T, C> capability, IBlockCapabilityProvider<T, C> provider, Block... blocks) {
		Objects.requireNonNull(provider);
		capability.lookup().registerForBlocks(provider::getCapability, blocks);
		TransferBridges.exportBlock(capability, provider, blocks);
	}

	@SuppressWarnings("unchecked")
	public <T, C, BE extends BlockEntity> void registerBlockEntity(BlockCapability<T, C> capability, BlockEntityType<BE> blockEntityType, ICapabilityProvider<? super BE, C, T> provider) {
		Objects.requireNonNull(provider);
		capability.lookup().registerForBlockEntity((be, context) -> provider.getCapability(be, context), blockEntityType);
		TransferBridges.exportBlockEntity(capability, blockEntityType, (ICapabilityProvider<BE, C, T>) provider);
	}

	public <T, C> void registerItem(ItemCapability<T, C> capability, ICapabilityProvider<ItemStack, C, T> provider, ItemLike... items) {
		Objects.requireNonNull(provider);
		capability.lookup().registerForItems(provider::getCapability, items);
	}

	public <T, C, E extends Entity> void registerEntity(EntityCapability<T, C> capability, EntityType<E> entityType, ICapabilityProvider<? super E, C, T> provider) {
		Objects.requireNonNull(provider);
		capability.lookup().registerForType((entity, context) -> provider.getCapability(entity, context), entityType);
	}

	public boolean isBlockRegistered(BlockCapability<?, ?> capability, Block block) {
		return capability.lookup().getProvider(block) != null;
	}

	public boolean isItemRegistered(ItemCapability<?, ?> capability, ItemLike item) {
		return capability.lookup().getProvider(item.asItem()) != null;
	}
}
