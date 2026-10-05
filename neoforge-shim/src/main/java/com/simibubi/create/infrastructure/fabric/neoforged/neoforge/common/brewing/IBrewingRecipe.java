// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.brewing;

import static net.minecraft.world.item.alchemy.PotionBrewing.Builder;

import net.minecraft.world.item.ItemStack;

/**
 * Interface for more flexible brewing recipes.
 *
 * <p>Register using {@code RegisterBrewingRecipesEvent} and {@link Builder#addRecipe(IBrewingRecipe)}.
 */
public interface IBrewingRecipe {
    /**
     * Returns true is the passed ItemStack is an input for this recipe. "Input"
     * being the item that goes in one of the three bottom slots of the brewing
     * stand (e.g: water bottle)
     */
    boolean isInput(ItemStack input);

    /**
     * Returns true if the passed ItemStack is an ingredient for this recipe.
     * "Ingredient" being the item that goes in the top slot of the brewing
     * stand (e.g: nether wart)
     */
    boolean isIngredient(ItemStack ingredient);

    /**
     * Returns the output when the passed input is brewed with the passed
     * ingredient. Empty if invalid input or ingredient.
     */
    ItemStack getOutput(ItemStack input, ItemStack ingredient);
}
