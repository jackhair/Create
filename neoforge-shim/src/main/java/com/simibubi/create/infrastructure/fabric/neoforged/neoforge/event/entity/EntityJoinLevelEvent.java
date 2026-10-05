// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.ICancellableEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.LogicalSide;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.NeoForge;

/**
 * This event is fired whenever an {@link Entity} joins a {@link Level}.
 * This event is fired whenever an entity is added to a level in {@link Level#addFreshEntity(Entity)}
 * and {@code PersistentEntitySectionManager#addNewEntity(Entity, boolean)}.
 * <p>
 * <strong>Note:</strong> This event may be called before the underlying {@link LevelChunk} is promoted to {@link ChunkStatus#FULL}.
 * You will cause chunk loading deadlocks if you do not delay your world interactions.
 * <p>
 * This event is {@linkplain ICancellableEvent cancellable} and does not {@linkplain HasResult have a result}.
 * If the event is canceled, the entity will not be added to the level.
 * <p>
 * This event is fired on the {@linkplain NeoForge#EVENT_BUS main Forge event bus}
 * on both logical sides.
 **/
public class EntityJoinLevelEvent extends EntityEvent implements ICancellableEvent {
    private final Level level;
    private final boolean loadedFromDisk;

    public EntityJoinLevelEvent(Entity entity, Level level) {
        this(entity, level, false);
    }

    public EntityJoinLevelEvent(Entity entity, Level level, boolean loadedFromDisk) {
        super(entity);
        this.level = level;
        this.loadedFromDisk = loadedFromDisk;
    }

    /**
     * {@return the level that the entity is set to join}
     */
    public Level getLevel() {
        return level;
    }

    /**
     * @return {@code true} if the entity was loaded from disk, {@code false} otherwise.
     *         On the {@linkplain LogicalSide#CLIENT logical client}, this will always return {@code false}.
     */
    public boolean loadedFromDisk() {
        return loadedFromDisk;
    }
}
