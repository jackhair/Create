// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.wrapper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandler;

public class RecipeWrapper implements RecipeInput {
    protected final IItemHandler inv;

    public RecipeWrapper(IItemHandler inv) {
        this.inv = inv;
    }

    /**
     * Returns the size of this inventory.
     */
    @Override
    public int size() {
        return inv.getSlots();
    }

    /**
     * Returns the stack in this slot. This stack should be a modifiable reference, not a copy of a stack in your inventory.
     */
    @Override
    public ItemStack getItem(int slot) {
        return inv.getStackInSlot(slot);
    }
}
