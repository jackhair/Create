package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

/**
 * The parts of NeoForge's {@code IBlockRenderDispatcherExtension} Create uses, injected into {@link net.minecraft.client.renderer.block.BlockRenderDispatcher}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IBlockRenderDispatcherExtension {
	/** NeoForge's model data / render type overload; model data isn't used on Fabric yet (PORTING.md 1d). */
	default void renderSingleBlock(net.minecraft.world.level.block.state.BlockState state, com.mojang.blaze3d.vertex.PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight, int packedOverlay, com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.model.data.ModelData modelData, @org.jetbrains.annotations.Nullable net.minecraft.client.renderer.RenderType renderType) {
		((net.minecraft.client.renderer.block.BlockRenderDispatcher) this).renderSingleBlock(state, poseStack, buffer, packedLight, packedOverlay);
	}
}
