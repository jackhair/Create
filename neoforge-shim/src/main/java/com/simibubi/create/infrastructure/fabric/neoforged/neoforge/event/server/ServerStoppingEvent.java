// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.server;

import net.minecraft.server.MinecraftServer;

/**
 * Called when the server begins an orderly shutdown, before {@link ServerStoppedEvent}.
 *
 * @author cpw
 */
public class ServerStoppingEvent extends ServerLifecycleEvent {
    public ServerStoppingEvent(MinecraftServer server) {
        super(server);
    }
}
