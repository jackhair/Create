package com.simibubi.create.infrastructure.fabric.neoforged.bus.api;

import java.util.function.Consumer;

/**
 * Create-owned re-implementation of NeoForge's {@code IEventBus} for Fabric (PORTING.md D7).
 * <p>
 * Unlike NeoForge, there's no {@code addListener(Consumer)} overload: NeoForge infers the event type of a
 * bare lambda with TypeTools, which needs JVM flags Fabric doesn't set. Pass the event class explicitly
 * (also valid NeoForge API), or use {@link SubscribeEvent} methods, whose type comes from the parameter.
 */
public interface IEventBus {
	/**
	 * Registers every {@link SubscribeEvent} method: static methods when {@code target} is a {@link Class},
	 * instance methods otherwise.
	 */
	void register(Object target);

	void unregister(Object target);

	default <T extends Event> void addListener(Class<T> eventType, Consumer<T> consumer) {
		addListener(EventPriority.NORMAL, false, eventType, consumer);
	}

	default <T extends Event> void addListener(EventPriority priority, Class<T> eventType, Consumer<T> consumer) {
		addListener(priority, false, eventType, consumer);
	}

	default <T extends Event> void addListener(boolean receiveCanceled, Class<T> eventType, Consumer<T> consumer) {
		addListener(EventPriority.NORMAL, receiveCanceled, eventType, consumer);
	}

	<T extends Event> void addListener(EventPriority priority, boolean receiveCanceled, Class<T> eventType, Consumer<T> consumer);

	/**
	 * Posts an event to every listener of its class or a supertype, in priority order, then registration order.
	 *
	 * @return the same event, for reading results
	 */
	<T extends Event> T post(T event);
}
