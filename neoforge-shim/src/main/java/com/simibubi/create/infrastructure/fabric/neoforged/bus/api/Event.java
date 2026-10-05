package com.simibubi.create.infrastructure.fabric.neoforged.bus.api;

/**
 * Base class for events posted on an {@link IEventBus}.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event} for Fabric (PORTING.md D7).
 */
public abstract class Event {
	boolean isCanceled;

	protected Event() {}
}
