// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.tick;

import net.minecraft.world.entity.Entity;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.ICancellableEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.entity.EntityEvent;

/**
 * Base class of the two entity tick events.
 * 
 * @see EntityTickEvent.Pre
 * @see EntityTickEvent.Post
 */
public abstract class EntityTickEvent extends EntityEvent {
    protected EntityTickEvent(Entity entity) {
        super(entity);
    }

    /**
     * {@link EntityTickEvent.Pre} is fired once per game tick, per entity, before the entity performs work for the current tick.
     * <p>
     * This event fires on both the logical server and logical client.
     */
    public static class Pre extends EntityTickEvent implements ICancellableEvent {
        public Pre(Entity entity) {
            super(entity);
        }

        /**
         * Cancels this event, preventing the current tick from being executed for the entity.
         * <p>
         * Additionally, if this event is canceled, then {@link EntityTickEvent.Post} will not be fired for the current tick.
         */
        @Override
        public void setCanceled(boolean canceled) {
            ICancellableEvent.super.setCanceled(canceled);
        }
    }

    /**
     * {@link EntityTickEvent.Post} is fired once per game tick, per entity, after the entity performs work for the current tick.
     * <p>
     * If {@link EntityTickEvent.Pre} was canceled for the current tick, this event will not fire.
     * <p>
     * This event fires on both the logical server and logical client.
     */
    public static class Post extends EntityTickEvent {
        public Post(Entity entity) {
            super(entity);
        }
    }
}
