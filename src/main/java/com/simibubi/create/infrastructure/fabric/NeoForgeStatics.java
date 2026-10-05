package com.simibubi.create.infrastructure.fabric;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.crafting.CraftingSize;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * Static helpers NeoForge adds to vanilla classes, which interface injection can't provide. Create's call sites
 * use these with a {@code // fabric:} comment (PORTING.md D7).
 */
public final class NeoForgeStatics {
	/** NeoForge's {@code AABB.INFINITE}. */
	public static final AABB INFINITE_AABB = new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
		Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

	private NeoForgeStatics() {}

	/** NeoForge's {@code RecipeType.simple}. */
	public static <T extends Recipe<?>> RecipeType<T> recipeType(ResourceLocation id) {
		String name = id.toString();
		return new RecipeType<>() {
			@Override
			public String toString() {
				return name;
			}
		};
	}

	/** NeoForge's {@code ShapedRecipePattern.setCraftingSize}. */
	public static void setCraftingSize(int width, int height) {
		CraftingSize.setCraftingSize(width, height);
	}

	/** NeoForge's {@code DyeColor.getColor(ItemStack)}: dye items, then the {@code c:dyes/<color>} tags. */
	@Nullable
	public static DyeColor dyeColor(ItemStack stack) {
		if (stack.getItem() instanceof DyeItem dye)
			return dye.getDyeColor();
		for (DyeColor color : DyeColor.values())
			if (stack.is(color.getTag()))
				return color;
		return null;
	}

	/** NeoForge's {@code ComposterBlock.getValue(ItemStack)}. */
	public static float compostValue(ItemLike item) {
		return ComposterBlock.COMPOSTABLES.getFloat(item.asItem());
	}

	/** NeoForge's {@code AxeItem.getAxeStrippingState}. */
	@Nullable
	public static BlockState axeStrippingState(BlockState state) {
		return Optional.ofNullable(AxeItem.STRIPPABLES.get(state.getBlock()))
			.map(block -> block.withPropertiesOf(state))
			.orElse(null);
	}

	public static float compostValue(ItemStack stack) {
		return compostValue(stack.getItem());
	}

	/** NeoForge's {@code ItemTags.create(ResourceLocation)}. */
	public static TagKey<net.minecraft.world.item.Item> itemTag(ResourceLocation id) {
		return TagKey.create(Registries.ITEM, id);
	}

	public static ResourceLocation key(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem());
	}
}
