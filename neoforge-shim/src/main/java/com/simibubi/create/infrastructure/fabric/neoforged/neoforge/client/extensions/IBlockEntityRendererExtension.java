package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

/**
 * The parts of NeoForge's {@code IBlockEntityRendererExtension} Create uses, injected into {@link net.minecraft.client.renderer.blockentity.BlockEntityRenderer}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IBlockEntityRendererExtension<T extends net.minecraft.world.level.block.entity.BlockEntity> {
	/** Culling box; the shim's LevelRenderer mixin uses it for block entities with wide renderers (PORTING.md 1d). */
	default net.minecraft.world.phys.AABB getRenderBoundingBox(T blockEntity) {
		return new net.minecraft.world.phys.AABB(blockEntity.getBlockPos());
	}
}
