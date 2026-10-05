package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event;

import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;

/**
 * Collects game test holders. Create's Fabric game test entrypoint fires it and hands the classes to Fabric
 * API's game test runner.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code RegisterGameTestsEvent} for Fabric (PORTING.md D7).
 */
public class RegisterGameTestsEvent extends Event implements IModBusEvent {
	private final Set<Class<?>> holders = new LinkedHashSet<>();
	private final Set<Method> methods = new LinkedHashSet<>();

	public void register(Class<?> testClass) {
		holders.add(testClass);
	}

	public void register(Method testMethod) {
		methods.add(testMethod);
	}

	public Set<Class<?>> getHolders() {
		return holders;
	}

	public Set<Method> getMethods() {
		return methods;
	}
}
