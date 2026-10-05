package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

/**
 * The parts of NeoForge's {@code IPoweredRailBlockExtension} Create uses, injected into {@link net.minecraft.world.level.block.PoweredRailBlock}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IPoweredRailBlockExtension {
	default boolean isActivatorRail() {
		return this == net.minecraft.world.level.block.Blocks.ACTIVATOR_RAIL;
	}
}
