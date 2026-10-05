package com.simibubi.create.infrastructure.fabric.neoforged.bus.api;

/**
 * An {@link Event} that listeners can cancel. Cancelled events skip listeners that didn't opt into
 * {@link SubscribeEvent#receiveCanceled()}; the code that posted the event decides what cancellation means.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code ICancellableEvent} for Fabric (PORTING.md D7).
 */
public interface ICancellableEvent {
	default void setCanceled(boolean canceled) {
		((Event) this).isCanceled = canceled;
	}

	default boolean isCanceled() {
		return ((Event) this).isCanceled;
	}
}
