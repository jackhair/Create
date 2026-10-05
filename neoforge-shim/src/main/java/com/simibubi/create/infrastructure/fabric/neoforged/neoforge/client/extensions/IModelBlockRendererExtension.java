package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

/**
 * The parts of NeoForge's {@code IModelBlockRendererExtension} Create uses, injected into {@link net.minecraft.client.renderer.block.ModelBlockRenderer}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IModelBlockRendererExtension {
	/** NeoForge's model data / render type overload; model data isn't used on Fabric yet (PORTING.md 1d). */
	default void tesselateBlock(net.minecraft.world.level.BlockAndTintGetter level, net.minecraft.client.resources.model.BakedModel model, net.minecraft.world.level.block.state.BlockState state, net.minecraft.core.BlockPos pos, com.mojang.blaze3d.vertex.PoseStack poseStack, com.mojang.blaze3d.vertex.VertexConsumer consumer, boolean checkSides, net.minecraft.util.RandomSource random, long seed, int packedOverlay, com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.model.data.ModelData modelData, @org.jetbrains.annotations.Nullable net.minecraft.client.renderer.RenderType renderType) {
		((net.minecraft.client.renderer.block.ModelBlockRenderer) this).tesselateBlock(level, model, state, pos, poseStack, consumer, checkSides, random, seed, packedOverlay);
	}
}
