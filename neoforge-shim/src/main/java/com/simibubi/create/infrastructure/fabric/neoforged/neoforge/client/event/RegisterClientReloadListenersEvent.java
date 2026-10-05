// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.ICancellableEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.LogicalSide;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.AddReloadListenerEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired to allow mods to register their reload listeners on the client-side resource manager.
 * This event is fired once during the construction of the {@link Minecraft} instance.
 *
 * <p>For registering reload listeners on the server-side resource manager, see {@link AddReloadListenerEvent}.</p>
 *
 * <p>This event is not {@linkplain ICancellableEvent cancellable}, and does not {@linkplain HasResult have a result}.</p>
 *
 * <p>This event is fired on the mod-specific event bus, only on the {@linkplain LogicalSide#CLIENT logical client}.</p>
 */
public class RegisterClientReloadListenersEvent extends Event implements IModBusEvent {
    private final ReloadableResourceManager resourceManager;

    @ApiStatus.Internal
    public RegisterClientReloadListenersEvent(ReloadableResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * Registers the given reload listener to the client-side resource manager.
     *
     * @param reloadListener the reload listener
     */
    public void registerReloadListener(PreparableReloadListener reloadListener) {
        resourceManager.registerReloadListener(reloadListener);
    }
}
