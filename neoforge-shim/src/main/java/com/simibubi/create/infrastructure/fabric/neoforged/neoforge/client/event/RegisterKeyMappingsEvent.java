// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.ICancellableEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.LogicalSide;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.ApiStatus;

/**
 * Allows users to register custom {@link net.minecraft.client.KeyMapping key mappings}.
 *
 * <p>This event is not {@linkplain ICancellableEvent cancellable}, and does not {@linkplain HasResult have a result}.
 *
 * <p>This event is fired on the mod-specific event bus, only on the {@linkplain LogicalSide#CLIENT logical client}.</p>
 */
public class RegisterKeyMappingsEvent extends Event implements IModBusEvent {
    private final Options options;

    @ApiStatus.Internal
    public RegisterKeyMappingsEvent(Options options) {
        this.options = options;
    }

    /**
     * Registers a new key mapping.
     */
    public void register(KeyMapping key) {
        net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper.registerKeyBinding(key); // fabric
    }
}
