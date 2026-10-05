package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import net.minecraft.resources.ResourceLocation;

/**
 * Create-owned re-implementation of NeoForge's {@code BaseCapability} for Fabric (PORTING.md D7).
 */
public abstract class BaseCapability<T, C> {
	private final ResourceLocation name;
	private final Class<T> typeClass;
	private final Class<C> contextClass;

	protected BaseCapability(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
		this.name = name;
		this.typeClass = typeClass;
		this.contextClass = contextClass;
	}

	public final ResourceLocation name() {
		return name;
	}

	public final Class<T> typeClass() {
		return typeClass;
	}

	public final Class<C> contextClass() {
		return contextClass;
	}
}
