package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.crafting;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.MapCodec;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * Matches block items whose block is in a tag. Serialized as a Fabric custom ingredient
 * ({@code "fabric:type": "create:block_tag"}, see scripts/fabric/convert_generated_resources.py).
 * <p>
 * Create-owned re-implementation of NeoForge's {@code BlockTagIngredient} for Fabric (PORTING.md D7).
 */
public record BlockTagIngredient(TagKey<Block> tag) implements CustomIngredient {
	public static final Serializer SERIALIZER = new Serializer();

	public Ingredient toIngredient() {
		return toVanilla();
	}

	@Override
	public boolean test(ItemStack stack) {
		return stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock().defaultBlockState().is(tag);
	}

	@Override
	public List<ItemStack> getMatchingStacks() {
		List<ItemStack> stacks = new ArrayList<>();
		for (Holder<Block> block : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
			ItemStack stack = new ItemStack(block.value());
			if (!stack.isEmpty())
				stacks.add(stack);
		}
		return stacks;
	}

	@Override
	public boolean requiresTesting() {
		return true;
	}

	@Override
	public CustomIngredientSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final class Serializer implements CustomIngredientSerializer<BlockTagIngredient> {
		private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("create", "block_tag");
		private static final MapCodec<BlockTagIngredient> CODEC = TagKey.codec(Registries.BLOCK).fieldOf("tag").xmap(BlockTagIngredient::new, BlockTagIngredient::tag);
		private static final StreamCodec<RegistryFriendlyByteBuf, BlockTagIngredient> STREAM_CODEC = ResourceLocation.STREAM_CODEC
			.map(id -> new BlockTagIngredient(TagKey.create(Registries.BLOCK, id)), i -> i.tag().location()).cast();

		@Override
		public ResourceLocation getIdentifier() {
			return ID;
		}

		@Override
		public MapCodec<BlockTagIngredient> getCodec(boolean allowEmpty) {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, BlockTagIngredient> getPacketCodec() {
			return STREAM_CODEC;
		}
	}
}
