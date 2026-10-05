// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.wrapper;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandler;

/**
 * Exposes the hands inventory of an {@link LivingEntity} as an {@link IItemHandler} using {@link LivingEntity#getItemBySlot(EquipmentSlot)} and
 * {@link LivingEntity#setItemSlot(EquipmentSlot, ItemStack)}.
 */
public class EntityHandsInvWrapper extends EntityEquipmentInvWrapper {
    public EntityHandsInvWrapper(LivingEntity entity) {
        super(entity, EquipmentSlot.Type.HAND);
    }
}
