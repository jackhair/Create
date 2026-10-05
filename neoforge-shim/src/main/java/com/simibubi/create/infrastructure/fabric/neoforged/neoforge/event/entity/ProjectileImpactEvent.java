// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.ICancellableEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.NeoForge;

/**
 * This event is fired on the {@link NeoForge#EVENT_BUS}.<br>
 * This event is fired when a projectile entity impacts something.<br>
 * This event is fired via {@code EventHooks#onProjectileImpact(Projectile,}
 * This event is fired for all vanilla projectiles by Forge,
 * custom projectiles should fire this event and check the result in a similar fashion.
 * This event is cancelable. When canceled, the impact will not be processed and the projectile will continue flying.
 * Killing or other handling of the entity after event cancellation is up to the modder.
 */
public class ProjectileImpactEvent extends EntityEvent implements ICancellableEvent {
    private final HitResult ray;
    private final Projectile projectile;

    public ProjectileImpactEvent(Projectile projectile, HitResult ray) {
        super(projectile);
        this.ray = ray;
        this.projectile = projectile;
    }

    public HitResult getRayTraceResult() {
        return ray;
    }

    public Projectile getProjectile() {
        return projectile;
    }
}
