package com.simibubi.create.infrastructure.fabric.neoforged.bus.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an event listener when its class or instance is {@linkplain IEventBus#register(Object) registered}.
 * The method's single parameter decides which events it receives.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code SubscribeEvent} for Fabric (PORTING.md D7).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscribeEvent {
	EventPriority priority() default EventPriority.NORMAL;

	boolean receiveCanceled() default false;
}
