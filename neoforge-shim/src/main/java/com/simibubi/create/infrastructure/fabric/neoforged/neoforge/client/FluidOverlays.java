package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

/**
 * The full-screen overlay drawn while the camera is inside a fluid, with a configurable texture. Vanilla's
 * underwater overlay code; NeoForge adds this as {@code ScreenEffectRenderer#renderFluid}.
 * <p>
 * Create code (PORTING.md D7), not a NeoForge class.
 */
public final class FluidOverlays {
	private FluidOverlays() {}

	public static void renderFluid(Minecraft minecraft, PoseStack poseStack, ResourceLocation texture) {
		if (minecraft.player == null)
			return;
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, texture);
		BlockPos pos = BlockPos.containing(minecraft.player.getX(), minecraft.player.getEyeY(), minecraft.player.getZ());
		float brightness = LightTexture.getBrightness(minecraft.player.level().dimensionType(), minecraft.player.level().getMaxLocalRawBrightness(pos));
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(brightness, brightness, brightness, 0.1F);
		float u = -minecraft.player.getYRot() / 64.0F;
		float v = minecraft.player.getXRot() / 64.0F;
		Matrix4f pose = poseStack.last().pose();
		BufferBuilder buffer = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		buffer.addVertex(pose, -1.0F, -1.0F, -0.5F).setUv(4.0F + u, 4.0F + v);
		buffer.addVertex(pose, 1.0F, -1.0F, -0.5F).setUv(0.0F + u, 4.0F + v);
		buffer.addVertex(pose, 1.0F, 1.0F, -0.5F).setUv(0.0F + u, 0.0F + v);
		buffer.addVertex(pose, -1.0F, 1.0F, -0.5F).setUv(4.0F + u, 0.0F + v);
		BufferUploader.drawWithShader(buffer.buildOrThrow());
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
	}
}
