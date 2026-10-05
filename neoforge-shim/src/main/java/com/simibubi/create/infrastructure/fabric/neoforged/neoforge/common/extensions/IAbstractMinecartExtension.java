package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

/**
 * The parts of NeoForge's {@code IAbstractMinecartExtension} Create uses, injected into {@link net.minecraft.world.entity.vehicle.AbstractMinecart}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IAbstractMinecartExtension {
	private net.minecraft.world.entity.vehicle.AbstractMinecart self() {
		return (net.minecraft.world.entity.vehicle.AbstractMinecart) this;
	}

	default double getSlopeAdjustment() {
		return 0.0078125D;
	}

	default boolean canUseRail() {
		return true;
	}

	default void setCanUseRail(boolean use) {}

	default boolean canBeRidden() {
		return self().getMinecartType() == net.minecraft.world.entity.vehicle.AbstractMinecart.Type.RIDEABLE;
	}

	default double getMaxSpeedWithRail() {
		return (self().isInWater() ? 4.0D : 8.0D) / 20.0D; // fabric: vanilla AbstractMinecart#getMaxSpeed (protected)
	}

	default net.minecraft.core.BlockPos getCurrentRailPosition() {
		net.minecraft.core.BlockPos pos = self().blockPosition();
		return self().level().getBlockState(pos.below()).is(net.minecraft.tags.BlockTags.RAILS) ? pos.below() : pos;
	}

	/** NeoForge lets minecarts move along modded rails here; vanilla movement is unchanged on Fabric. */
	default void moveMinecartOnRail(net.minecraft.core.BlockPos pos) {}

	/** NeoForge's per-cart rail speed cap; vanilla's is 1.2 blocks per tick. */
	default float getMaxCartSpeedOnRail() {
		return 1.2F;
	}
}
