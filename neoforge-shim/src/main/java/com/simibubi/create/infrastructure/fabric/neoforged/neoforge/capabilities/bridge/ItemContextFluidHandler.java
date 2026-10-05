package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.world.item.ItemStack;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * A NeoForge {@link IFluidHandlerItem} for any item with a Fabric {@code FluidStorage.ITEM} storage (buckets,
 * bottles, other mods' tanks). The item lives in a single-slot {@link ContainerItemContext}; {@link #getContainer()}
 * returns whatever that slot holds after the operations so far, matching NeoForge's contract.
 * <p>
 * Create code (PORTING.md D7), not a NeoForge class.
 */
public final class ItemContextFluidHandler extends StorageFluidHandler implements IFluidHandlerItem {
	private final ContainerItemContext context;

	private ItemContextFluidHandler(ContainerItemContext context, Storage<FluidVariant> storage) {
		super(storage);
		this.context = context;
	}

	@Nullable
	public static IFluidHandlerItem of(ItemStack stack) {
		if (stack.isEmpty())
			return null;
		ContainerItemContext context = ContainerItemContext.withInitial(stack);
		Storage<FluidVariant> storage = context.find(FluidStorage.ITEM);
		return storage == null ? null : new ItemContextFluidHandler(context, storage);
	}

	@Override
	public ItemStack getContainer() {
		return context.getItemVariant().toStack((int) context.getAmount());
	}
}
