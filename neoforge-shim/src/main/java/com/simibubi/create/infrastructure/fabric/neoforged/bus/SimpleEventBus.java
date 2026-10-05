package com.simibubi.create.infrastructure.fabric.neoforged.bus;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.EventPriority;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.ICancellableEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.IEventBus;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.SubscribeEvent;

/**
 * Small {@link IEventBus} with NeoForge's dispatch semantics: listeners receive the event class they
 * subscribe to and its subclasses, ordered by {@link EventPriority} then registration order, and cancelled
 * events skip listeners that don't {@linkplain SubscribeEvent#receiveCanceled() receive cancelled events}.
 * <p>
 * Create code (PORTING.md D7), not a NeoForge class.
 */
public class SimpleEventBus implements IEventBus {
	private static final Logger LOGGER = LoggerFactory.getLogger("Create/EventBus");
	private static final Comparator<Listener> ORDER = Comparator.comparing(Listener::priority).thenComparingLong(Listener::order);

	private final String name;
	private final List<Listener> listeners = new CopyOnWriteArrayList<>();
	private final Map<Class<?>, Listener[]> dispatchCache = new ConcurrentHashMap<>();
	private final AtomicLong nextOrder = new AtomicLong();

	public SimpleEventBus(String name) {
		this.name = name;
	}

	@Override
	public void register(Object target) {
		boolean isStatic = target instanceof Class<?>;
		Class<?> clazz = isStatic ? (Class<?>) target : target.getClass();
		int found = 0;
		for (Method method : clazz.getDeclaredMethods()) {
			SubscribeEvent annotation = method.getAnnotation(SubscribeEvent.class);
			if (annotation == null || Modifier.isStatic(method.getModifiers()) != isStatic)
				continue;
			Class<?>[] params = method.getParameterTypes();
			if (params.length != 1 || !Event.class.isAssignableFrom(params[0]))
				throw new IllegalArgumentException("@SubscribeEvent method " + method + " must take exactly one Event parameter");
			addListener(annotation.priority(), annotation.receiveCanceled(), params[0].asSubclass(Event.class), invoker(method, isStatic ? null : target), target);
			found++;
		}
		if (found == 0)
			LOGGER.warn("[{}] {} has no {} @SubscribeEvent methods", name, clazz.getName(), isStatic ? "static" : "instance");
	}

	@Override
	public void unregister(Object target) {
		listeners.removeIf(l -> l.owner == target);
		dispatchCache.clear();
	}

	@Override
	public <T extends Event> void addListener(EventPriority priority, boolean receiveCanceled, Class<T> eventType, Consumer<T> consumer) {
		addListener(priority, receiveCanceled, eventType, consumer, consumer);
	}

	@SuppressWarnings("unchecked")
	private void addListener(EventPriority priority, boolean receiveCanceled, Class<? extends Event> eventType, Consumer<?> consumer, Object owner) {
		listeners.add(new Listener(eventType, priority, receiveCanceled, (Consumer<Event>) consumer, owner, nextOrder.getAndIncrement()));
		dispatchCache.clear();
	}

	@Override
	public <T extends Event> T post(T event) {
		for (Listener listener : listenersFor(event.getClass())) {
			if (!listener.receiveCanceled && event instanceof ICancellableEvent cancellable && cancellable.isCanceled())
				continue;
			listener.consumer.accept(event);
		}
		return event;
	}

	private Listener[] listenersFor(Class<?> eventClass) {
		return dispatchCache.computeIfAbsent(eventClass, c -> {
			List<Listener> matching = new ArrayList<>();
			for (Listener listener : listeners)
				if (listener.eventType.isAssignableFrom(c))
					matching.add(listener);
			matching.sort(ORDER);
			return matching.toArray(Listener[]::new);
		});
	}

	private static Consumer<Event> invoker(Method method, Object instance) {
		try {
			method.setAccessible(true);
			MethodHandle handle = MethodHandles.lookup().unreflect(method);
			MethodHandle bound = instance == null ? handle : handle.bindTo(instance);
			MethodHandle generic = bound.asType(bound.type().changeParameterType(0, Event.class).changeReturnType(void.class));
			return event -> {
				try {
					generic.invokeExact(event);
				} catch (RuntimeException | Error e) {
					throw e;
				} catch (Throwable t) {
					throw new RuntimeException(t);
				}
			};
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Can't access @SubscribeEvent method " + method, e);
		}
	}

	@Override
	public String toString() {
		return "SimpleEventBus[" + name + "]";
	}

	private record Listener(Class<?> eventType, EventPriority priority, boolean receiveCanceled, Consumer<Event> consumer, Object owner, long order) {}
}
