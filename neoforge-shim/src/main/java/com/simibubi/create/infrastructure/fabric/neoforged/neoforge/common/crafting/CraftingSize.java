package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.crafting;

/**
 * The largest shaped recipe pattern allowed, raised by {@code ShapedRecipePattern.setCraftingSize} on NeoForge
 * (Create's mechanical crafting goes up to 9x9). Applied by the shim's ShapedRecipePatternDataMixin.
 * Create code (PORTING.md D7).
 */
public final class CraftingSize {
	private static int max = 3;

	private CraftingSize() {}

	public static synchronized void setCraftingSize(int width, int height) {
		max = Math.max(max, Math.max(width, height));
	}

	public static int max() {
		return max;
	}
}
