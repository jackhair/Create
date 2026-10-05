// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.entity;

import java.util.Map;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;

/**
 * EntityAttributeCreationEvent.<br>
 * Use this event to register attributes for your own EntityTypes.
 * This event is fired after registration and before common setup.
 * <br>
 * Fired on the Mod bus {@link IModBusEvent}.<br>
 **/
public class EntityAttributeCreationEvent extends Event implements IModBusEvent {
    private final Map<EntityType<? extends LivingEntity>, AttributeSupplier> map;

    public EntityAttributeCreationEvent(Map<EntityType<? extends LivingEntity>, AttributeSupplier> map) {
        this.map = map;
    }

    public void put(EntityType<? extends LivingEntity> entity, AttributeSupplier map) {
        if (DefaultAttributes.hasSupplier(entity))
            throw new IllegalStateException("Duplicate DefaultAttributes entry: " + entity);
        this.map.put(entity, map);
    }
}
