package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

/**
 * The parts of NeoForge's {@code IEntityTypeBuilderExtension} Create uses, injected into {@link EntityType.Builder}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IEntityTypeBuilderExtension<T extends Entity> {
	@SuppressWarnings("unchecked")
	private EntityType.Builder<T> self() {
		return (EntityType.Builder<T>) this;
	}

	default EntityType.Builder<T> setTrackingRange(int range) {
		return self().clientTrackingRange(range);
	}

	default EntityType.Builder<T> setUpdateInterval(int interval) {
		return self().updateInterval(interval);
	}

	/** fabric: Fabric API's {@code alwaysUpdateVelocity}. */
	default EntityType.Builder<T> setShouldReceiveVelocityUpdates(boolean value) {
		return self().alwaysUpdateVelocity(value);
	}
}
