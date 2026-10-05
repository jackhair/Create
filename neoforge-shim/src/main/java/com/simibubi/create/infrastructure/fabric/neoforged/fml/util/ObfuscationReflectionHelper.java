package com.simibubi.create.infrastructure.fabric.neoforged.fml.util;

import java.lang.reflect.Field;

/**
 * Plain reflection by field name. That's correct for other mods' classes, which Fabric doesn't remap, but
 * <b>not</b> for Minecraft classes in production, where names are intermediary: use the access widener for those.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code ObfuscationReflectionHelper} for Fabric (PORTING.md D7).
 */
public final class ObfuscationReflectionHelper {
	private ObfuscationReflectionHelper() {}

	@SuppressWarnings("unchecked")
	public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String fieldName) {
		try {
			return (T) findField(classToAccess, fieldName).get(instance);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String fieldName) {
		try {
			findField(classToAccess, fieldName).set(instance, value);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}

	public static Field findField(Class<?> clazz, String fieldName) {
		try {
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field;
		} catch (NoSuchFieldException e) {
			throw new IllegalStateException("No field " + fieldName + " in " + clazz.getName(), e);
		}
	}
}
