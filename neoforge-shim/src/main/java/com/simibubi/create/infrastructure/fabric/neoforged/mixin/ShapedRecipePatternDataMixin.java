package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.crafting.CraftingSize;

/** Lets shaped recipe patterns exceed 3x3 when a mod raises the limit, as NeoForge's setCraftingSize does. */
@Mixin(targets = "net.minecraft.world.item.crafting.ShapedRecipePattern$Data")
public abstract class ShapedRecipePatternDataMixin {
	@ModifyConstant(method = "method_55096", constant = @Constant(intValue = 3))
	private static int create$craftingSize(int original) {
		return Math.max(original, CraftingSize.max());
	}
}
