package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * The parts of NeoForge's {@code IBlockEntityExtension} Create uses, injected into {@link BlockEntity}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IBlockEntityExtension {
	private BlockEntity self() {
		return (BlockEntity) this;
	}

	default void invalidateCapabilities() {
		if (self().getLevel() != null)
			self().getLevel().invalidateCapabilities(self().getBlockPos());
	}
}
