package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

/**
 * The parts of NeoForge's {@code IPotionBrewingExtension} Create uses, injected into {@link net.minecraft.world.item.alchemy.PotionBrewing}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IPotionBrewingExtension {
	/** NeoForge's extra brewing recipes; Fabric brewing is vanilla's mixes, so there are none. */
	default java.util.List<com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.brewing.IBrewingRecipe> getRecipes() {
		return java.util.List.of();
	}
}
