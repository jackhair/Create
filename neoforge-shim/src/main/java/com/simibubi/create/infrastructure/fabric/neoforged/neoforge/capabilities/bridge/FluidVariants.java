package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidStack;

/**
 * Conversions between NeoForge {@link FluidStack}s and Fabric {@link FluidVariant}s; both use droplets.
 * Create code (PORTING.md D7).
 */
public final class FluidVariants {
	private FluidVariants() {}

	public static FluidVariant of(FluidStack stack) {
		return stack.isEmpty() ? FluidVariant.blank() : FluidVariant.of(stack.getFluid(), stack.getComponentsPatch());
	}

	public static FluidStack toStack(FluidVariant variant, long amount) {
		if (variant.isBlank() || amount <= 0)
			return FluidStack.EMPTY;
		return new FluidStack(variant.getRegistryEntry(), amount, variant.getComponents());
	}
}
