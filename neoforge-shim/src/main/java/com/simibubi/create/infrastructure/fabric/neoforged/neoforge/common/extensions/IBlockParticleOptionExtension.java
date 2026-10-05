package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;

/**
 * The parts of NeoForge's {@code IBlockParticleOptionExtension} Create uses, injected into {@link BlockParticleOption}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IBlockParticleOptionExtension {
	/** NeoForge tracks the source position for tinting; vanilla doesn't, so it's ignored. */
	default BlockParticleOption setPos(BlockPos pos) {
		return (BlockParticleOption) this;
	}
}
