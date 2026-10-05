package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

/**
 * The parts of NeoForge's {@code IFoodPropertiesBuilderExtension} Create uses, injected into {@link net.minecraft.world.food.FoodProperties.Builder}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IFoodPropertiesBuilderExtension {
	/** NeoForge's lazy effect overload; vanilla's builder takes the instance, so it's created here. */
	default net.minecraft.world.food.FoodProperties.Builder effect(java.util.function.Supplier<net.minecraft.world.effect.MobEffectInstance> effect, float probability) {
		return ((net.minecraft.world.food.FoodProperties.Builder) this).effect(effect.get(), probability);
	}
}
