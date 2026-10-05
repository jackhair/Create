package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

/**
 * The parts of NeoForge's {@code IDyeColorExtension} Create uses, injected into {@link DyeColor}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IDyeColorExtension {
	/** The {@code c:dyes/<color>} item tag. */
	default TagKey<Item> getTag() {
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/" + ((DyeColor) this).getSerializedName()));
	}
}
