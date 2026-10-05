package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IBlockParticleOptionExtension;

/** Makes {@link net.minecraft.core.particles.BlockParticleOption} implement NeoForge's {@link IBlockParticleOptionExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.core.particles.BlockParticleOption.class)
public abstract class BlockParticleOptionMixin implements IBlockParticleOptionExtension {}
