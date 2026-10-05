package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

/**
 * An entity capability is a Fabric {@link EntityApiLookup}.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code EntityCapability} for Fabric (PORTING.md D7).
 */
public final class EntityCapability<T, C> extends BaseCapability<T, C> {
	private final EntityApiLookup<T, C> lookup;

	private EntityCapability(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
		super(name, typeClass, contextClass);
		this.lookup = EntityApiLookup.get(name, typeClass, contextClass);
	}

	public static <T, C> EntityCapability<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
		return new EntityCapability<>(name, typeClass, contextClass);
	}

	public static <T> EntityCapability<T, @Nullable Direction> createSided(ResourceLocation name, Class<T> typeClass) {
		return create(name, typeClass, Direction.class);
	}

	public static <T> EntityCapability<T, @Nullable Void> createVoid(ResourceLocation name, Class<T> typeClass) {
		return create(name, typeClass, Void.class);
	}

	public EntityApiLookup<T, C> lookup() {
		return lookup;
	}

	@Nullable
	public T getCapability(Entity entity, C context) {
		return lookup.find(entity, context);
	}
}
