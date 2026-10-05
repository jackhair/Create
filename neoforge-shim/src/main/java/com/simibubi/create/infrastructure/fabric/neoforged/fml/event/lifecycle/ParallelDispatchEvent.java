package com.simibubi.create.infrastructure.fabric.neoforged.fml.event.lifecycle;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;

/**
 * Create-owned re-implementation of NeoForge's {@code ParallelDispatchEvent} for Fabric (PORTING.md D7).
 * Fabric initializes mods sequentially on the main thread, so enqueued work runs immediately.
 */
public abstract class ParallelDispatchEvent extends Event implements IModBusEvent {
	public CompletableFuture<Void> enqueueWork(Runnable work) {
		work.run();
		return CompletableFuture.completedFuture(null);
	}

	public <T> CompletableFuture<T> enqueueWork(Supplier<T> work) {
		return CompletableFuture.completedFuture(work.get());
	}
}
