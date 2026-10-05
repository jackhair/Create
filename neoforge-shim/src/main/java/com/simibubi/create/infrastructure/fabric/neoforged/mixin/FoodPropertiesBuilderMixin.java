package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IFoodPropertiesBuilderExtension;

/** Makes {@link net.minecraft.world.food.FoodProperties.Builder} implement NeoForge's {@link IFoodPropertiesBuilderExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.food.FoodProperties.Builder.class)
public abstract class FoodPropertiesBuilderMixin implements IFoodPropertiesBuilderExtension {}
