// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.event;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;

/**
 * Base class of the two client tick events.
 * <p>
 * For the event that fires once per frame (instead of per tick), see {@link RenderFrameEvent}.
 * 
 * @see ClientTickEvent.Pre
 * @see ClientTickEvent.Post
 */
public abstract class ClientTickEvent extends Event {
    /**
     * {@link ClientTickEvent.Pre} is fired once per client tick, before the client performs work for the current tick.
     * <p>
     * This event only fires on the physical client.
     */
    public static class Pre extends ClientTickEvent {
        public Pre() {}
    }

    /**
     * {@link ClientTickEvent.Post} is fired once per client tick, after the client performs work for the current tick.
     * <p>
     * This event only fires on the physical client.
     */
    public static class Post extends ClientTickEvent {
        public Post() {}
    }
}
