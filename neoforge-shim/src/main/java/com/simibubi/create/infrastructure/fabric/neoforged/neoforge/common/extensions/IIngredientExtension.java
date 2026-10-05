package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.world.item.crafting.Ingredient;

/**
 * The parts of NeoForge's {@code IIngredientExtension} Create uses, injected into {@link Ingredient}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IIngredientExtension {
	/** Simple ingredients match by item alone (Fabric's {@code requiresTesting} is the inverse). */
	default boolean isSimple() {
		return !((Ingredient) this).requiresTesting();
	}
}
