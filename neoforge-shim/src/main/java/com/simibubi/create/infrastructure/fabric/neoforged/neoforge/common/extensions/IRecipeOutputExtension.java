package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

/**
 * The parts of NeoForge's {@code IRecipeOutputExtension} Create uses, injected into {@link net.minecraft.data.recipes.RecipeOutput}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IRecipeOutputExtension {
	/** fabric: conditions aren't written until datagen runs on Fabric (PORTING.md 1e). */
	default net.minecraft.data.recipes.RecipeOutput withConditions(com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.conditions.ICondition... conditions) {
		return (net.minecraft.data.recipes.RecipeOutput) this;
	}

	default void accept(net.minecraft.resources.ResourceLocation id, net.minecraft.world.item.crafting.Recipe<?> recipe, @org.jetbrains.annotations.Nullable net.minecraft.advancements.AdvancementHolder advancement, com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.conditions.ICondition... conditions) {
		((net.minecraft.data.recipes.RecipeOutput) this).accept(id, recipe, advancement);
	}
}
