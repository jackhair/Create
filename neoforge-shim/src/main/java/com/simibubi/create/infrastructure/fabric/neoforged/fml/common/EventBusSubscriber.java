package com.simibubi.create.infrastructure.fabric.neoforged.fml.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.simibubi.create.infrastructure.fabric.neoforged.api.distmarker.Dist;

/**
 * Registers a class's static {@code @SubscribeEvent} methods at startup: on the mod bus for events implementing
 * {@code IModBusEvent}, on {@code NeoForge.EVENT_BUS} otherwise. Fabric doesn't scan annotations, so the build
 * generates an index of annotated classes (see {@code generateEventSubscriberIndex} in build.gradle).
 * <p>
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusSubscriber {
	Dist[] value() default { Dist.CLIENT, Dist.DEDICATED_SERVER };

	String modid() default "";
}
