package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.crafting;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Registry entry type for {@code NeoForgeRegistries.INGREDIENT_TYPES}. Create registers none; custom item
 * ingredients on Fabric are Fabric API {@code CustomIngredient}s (see {@link CompoundIngredient}).
 * <p>
 * Create-owned re-implementation of NeoForge's {@code IngredientType} for Fabric (PORTING.md D7).
 */
public record IngredientType<T>(MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
	public IngredientType(MapCodec<T> codec) {
		this(codec, ByteBufCodecs.fromCodecWithRegistries(codec.codec()));
	}
}
