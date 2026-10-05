package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.SimpleEventBus;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.IEventBus;

/**
 * Create-owned re-implementation of NeoForge's {@code NeoForge} class for Fabric (PORTING.md D7). Events on this
 * bus are fired by the adapters in {@code infrastructure.fabric.events}.
 */
public class NeoForge {
	public static final IEventBus EVENT_BUS = new SimpleEventBus("game");
}
