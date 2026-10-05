// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.wrappers;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.material.Fluids;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.NeoForgeMod;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidStack;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidType;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

/**
 * Wrapper for vanilla and forge buckets.
 * Swaps between empty bucket and filled bucket of the correct type.
 */
public class FluidBucketWrapper implements IFluidHandlerItem {
    protected ItemStack container;

    public FluidBucketWrapper(ItemStack container) {
        this.container = container;
    }

    @Override
    public ItemStack getContainer() {
        return container;
    }

    public boolean canFillFluidType(FluidStack fluid) {
        if (fluid.is(Fluids.WATER) || fluid.is(Fluids.LAVA)) {
            return true;
        }
        return !fluid.getFluidType().getBucket(fluid).isEmpty();
    }

    public FluidStack getFluid() {
        Item item = container.getItem();
        if (item instanceof BucketItem) {
            return new FluidStack(((BucketItem) item).content, FluidType.BUCKET_VOLUME);
        } else if (item instanceof MilkBucketItem && NeoForgeMod.MILK.isBound()) {
            return new FluidStack(NeoForgeMod.MILK.get(), FluidType.BUCKET_VOLUME);
        } else {
            return FluidStack.EMPTY;
        }
    }

    protected void setFluid(FluidStack fluidStack) {
        if (fluidStack.isEmpty())
            container = new ItemStack(Items.BUCKET);
        else
            container = getFilledBucket(fluidStack); // fabric: FluidUtil isn't shimmed
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public long getTankCapacity(int tank) {
        return FluidType.BUCKET_VOLUME;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    @Override
    public long fill(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.getAmount() < FluidType.BUCKET_VOLUME || container.getItem() instanceof MilkBucketItem || !getFluid().isEmpty() || !canFillFluidType(resource)) {
            return 0;
        }

        if (action.execute()) {
            setFluid(resource);
        }

        return FluidType.BUCKET_VOLUME;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (container.getCount() != 1 || resource.getAmount() < FluidType.BUCKET_VOLUME) {
            return FluidStack.EMPTY;
        }

        FluidStack fluidStack = getFluid();
        if (!fluidStack.isEmpty() && FluidStack.isSameFluidSameComponents(fluidStack, resource)) {
            if (action.execute()) {
                setFluid(FluidStack.EMPTY);
            }
            return fluidStack;
        }

        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(long maxDrain, FluidAction action) {
        if (container.getCount() != 1 || maxDrain < FluidType.BUCKET_VOLUME) {
            return FluidStack.EMPTY;
        }

        FluidStack fluidStack = getFluid();
        if (!fluidStack.isEmpty()) {
            if (action.execute()) {
                setFluid(FluidStack.EMPTY);
            }
            return fluidStack;
        }

        return FluidStack.EMPTY;
    }

    // fabric: from NeoForge's FluidUtil#getFilledBucket
    private static ItemStack getFilledBucket(FluidStack fluidStack) {
        if (fluidStack.getComponents().isEmpty()) {
            if (fluidStack.is(net.minecraft.world.level.material.Fluids.WATER))
                return new ItemStack(net.minecraft.world.item.Items.WATER_BUCKET);
            if (fluidStack.is(net.minecraft.world.level.material.Fluids.LAVA))
                return new ItemStack(net.minecraft.world.item.Items.LAVA_BUCKET);
        }
        return fluidStack.getFluidType().getBucket(fluidStack);
    }
}
