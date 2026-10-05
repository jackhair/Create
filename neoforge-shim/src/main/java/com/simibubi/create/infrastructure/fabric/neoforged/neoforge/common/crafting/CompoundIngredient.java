package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.crafting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.mojang.serialization.MapCodec;

import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredient;
import net.fabricmc.fabric.api.recipe.v1.ingredient.CustomIngredientSerializer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Matches if any child ingredient matches. A Fabric API {@link CustomIngredient}, so code that checks
 * {@code ingredient.getCustomIngredient() instanceof CompoundIngredient} keeps working.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code CompoundIngredient} for Fabric (PORTING.md D7).
 */
public record CompoundIngredient(List<Ingredient> children) implements CustomIngredient {
	public static final Serializer SERIALIZER = new Serializer();

	public static Ingredient of(Ingredient... children) {
		if (children.length == 1)
			return children[0];
		return new CompoundIngredient(List.of(children)).toVanilla();
	}

	@Override
	public boolean test(ItemStack stack) {
		for (Ingredient child : children)
			if (child.test(stack))
				return true;
		return false;
	}

	@Override
	public List<ItemStack> getMatchingStacks() {
		Set<ItemStack> stacks = new LinkedHashSet<>();
		for (Ingredient child : children)
			stacks.addAll(Arrays.asList(child.getItems()));
		return new ArrayList<>(stacks);
	}

	@Override
	public boolean requiresTesting() {
		for (Ingredient child : children)
			if (child.requiresTesting())
				return true;
		return false;
	}

	@Override
	public CustomIngredientSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final class Serializer implements CustomIngredientSerializer<CompoundIngredient> {
		private static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath("neoforge", "compound");
		private static final MapCodec<CompoundIngredient> CODEC = Ingredient.LIST_CODEC_NONEMPTY.fieldOf("children").xmap(CompoundIngredient::new, CompoundIngredient::children);
		private static final StreamCodec<RegistryFriendlyByteBuf, CompoundIngredient> STREAM_CODEC = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list())
			.map(CompoundIngredient::new, CompoundIngredient::children);

		@Override
		public ResourceLocation getIdentifier() {
			return ID;
		}

		@Override
		public MapCodec<CompoundIngredient> getCodec(boolean allowEmpty) {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, CompoundIngredient> getPacketCodec() {
			return STREAM_CODEC;
		}
	}
}
