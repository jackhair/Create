package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IIngredientExtension;

/** Makes {@link net.minecraft.world.item.crafting.Ingredient} implement NeoForge's {@link IIngredientExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.item.crafting.Ingredient.class)
public abstract class IngredientMixin implements IIngredientExtension {}
