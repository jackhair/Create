package com.simibubi.create.infrastructure.fabric.neoforged.fml;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Storage for {@link ModContainer#registerExtensionPoint}. Create code (PORTING.md D7), not a NeoForge class.
 */
final class ExtensionPoints {
	private static final Map<String, Map<Class<?>, Supplier<?>>> POINTS = new ConcurrentHashMap<>();

	static <T> void register(String modId, Class<T> point, Supplier<? extends T> extension) {
		POINTS.computeIfAbsent(modId, k -> new ConcurrentHashMap<>()).put(point, extension);
	}

	@SuppressWarnings("unchecked")
	static <T> Optional<T> get(String modId, Class<T> point) {
		return Optional.ofNullable(POINTS.getOrDefault(modId, Map.of()).get(point)).map(s -> (T) s.get());
	}
}
