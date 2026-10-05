package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.ITerrainParticleExtension;

/** Makes {@link net.minecraft.client.particle.TerrainParticle} implement NeoForge's {@link ITerrainParticleExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.particle.TerrainParticle.class)
public abstract class TerrainParticleMixin implements ITerrainParticleExtension {}
