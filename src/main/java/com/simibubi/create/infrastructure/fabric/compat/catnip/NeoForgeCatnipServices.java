package com.simibubi.create.infrastructure.fabric.compat.catnip;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.createmod.catnip.platform.FabricCatnipServices;
import net.minecraft.client.renderer.MultiBufferSource;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge.FluidVariants;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidStack;

/**
 * Catnip's NeoForge services as used by Create, on Catnip's Fabric services: the fluid renderer takes NeoForge
 * {@link FluidStack}s and converts them to Fabric fluid variants.
 */
public final class NeoForgeCatnipServices {
	public static final FluidRenderer FLUID_RENDERER = new FluidRenderer();

	private NeoForgeCatnipServices() {}

	public static final class FluidRenderer {
		private FluidRenderer() {}

		public void renderFluidBox(FluidStack fluid, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax,
								   MultiBufferSource buffer, PoseStack ms, int light, boolean renderBottom, boolean invertGasses) {
			FabricCatnipServices.FLUID_RENDERER.renderFluidBox(FluidVariants.of(fluid), xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light, renderBottom, invertGasses);
		}

		public void renderFluidBox(FluidStack fluid, float xMin, float yMin, float zMin, float xMax, float yMax, float zMax,
								   VertexConsumer builder, PoseStack ms, int light, boolean renderBottom, boolean invertGasses) {
			FabricCatnipServices.FLUID_RENDERER.renderFluidBox(FluidVariants.of(fluid), xMin, yMin, zMin, xMax, yMax, zMax, builder, ms, light, renderBottom, invertGasses);
		}
	}
}
