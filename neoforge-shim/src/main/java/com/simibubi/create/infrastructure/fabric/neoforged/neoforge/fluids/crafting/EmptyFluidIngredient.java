// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting;

import com.mojang.serialization.MapCodec;
import java.util.stream.Stream;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.NeoForgeMod;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidStack;

/**
 * Singleton that represents an empty fluid ingredient.
 * <p>
 * This is the only instance of an <b>explicitly</b> empty ingredient,
 * and may be used as a fallback in FluidIngredient convenience methods
 * (such as when trying to create an ingredient from an empty list).
 *
 * @see FluidIngredient#empty()
 * @see FluidIngredient#isEmpty()
 */
public class EmptyFluidIngredient extends FluidIngredient {
    public static final EmptyFluidIngredient INSTANCE = new EmptyFluidIngredient();

    public static final MapCodec<EmptyFluidIngredient> CODEC = MapCodec.unit(INSTANCE);

    private EmptyFluidIngredient() {}

    @Override
    public boolean test(FluidStack fluidStack) {
        return fluidStack.isEmpty();
    }

    @Override
    protected Stream<FluidStack> generateStacks() {
        return Stream.empty();
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public FluidIngredientType<?> getType() {
        return NeoForgeMod.EMPTY_FLUID_INGREDIENT_TYPE.get();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}
