package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

/**
 * The parts of NeoForge's {@code ITerrainParticleExtension} Create uses, injected into {@link net.minecraft.client.particle.TerrainParticle}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface ITerrainParticleExtension {
	default net.minecraft.client.particle.TerrainParticle updateSprite(net.minecraft.world.level.block.state.BlockState state, net.minecraft.core.BlockPos pos) {
		net.minecraft.client.particle.TerrainParticle self = (net.minecraft.client.particle.TerrainParticle) this;
		self.setSprite(net.minecraft.client.Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
		return self;
	}
}
