package com.simibubi.create.infrastructure.fabric.compat.jei;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.bridge.FluidVariants;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidStack;

import mezz.jei.api.fabric.constants.FabricTypes;
import mezz.jei.api.fabric.ingredients.fluids.IJeiFluidIngredient;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.world.level.material.Fluid;

/**
 * JEI on Fabric represents fluids as {@link IJeiFluidIngredient} (variant + droplets) rather than NeoForge
 * {@link FluidStack}s; Create's JEI integration converts at the boundary with these helpers. Amounts are
 * droplets on both sides (PORTING.md D3).
 */
public final class JeiFluids {
	public static final IIngredientTypeWithSubtypes<Fluid, IJeiFluidIngredient> FLUID_STACK = FabricTypes.FLUID_STACK;

	private JeiFluids() {}

	public static IJeiFluidIngredient of(FluidStack stack) {
		return new Ingredient(FluidVariants.of(stack), stack.getAmount());
	}

	public static List<IJeiFluidIngredient> of(Collection<FluidStack> stacks) {
		return stacks.stream().map(JeiFluids::of).toList();
	}

	public static List<IJeiFluidIngredient> of(FluidStack[] stacks) {
		return of(Arrays.asList(stacks));
	}

	public static FluidStack toStack(IJeiFluidIngredient ingredient) {
		return FluidVariants.toStack(ingredient.getFluidVariant(), ingredient.getAmount());
	}

	private record Ingredient(FluidVariant getFluidVariant, long getAmount) implements IJeiFluidIngredient {}
}
