package com.simibubi.create.infrastructure.fabric.neoforged.fml.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.simibubi.create.infrastructure.fabric.neoforged.api.distmarker.Dist;

/**
 * Documentation-only on Fabric: entrypoints in {@code fabric.mod.json} construct Create's {@code @Mod} classes
 * (see {@code infrastructure.fabric.CreateFabric}). Create-owned re-implementation for Fabric (PORTING.md D7).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
	String value();

	Dist[] dist() default { Dist.CLIENT, Dist.DEDICATED_SERVER };
}
