package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IRecipeOutputExtension;

/** Makes {@link net.minecraft.data.recipes.RecipeOutput} implement NeoForge's {@link IRecipeOutputExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.data.recipes.RecipeOutput.class)
public interface RecipeOutputMixin extends IRecipeOutputExtension {}
