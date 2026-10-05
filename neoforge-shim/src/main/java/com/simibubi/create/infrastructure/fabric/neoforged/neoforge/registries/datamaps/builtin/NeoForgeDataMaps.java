// fabric: derived from NeoForge 21.1.219's NeoForgeDataMaps (LGPL-2.1-only), reduced to the maps Create uses
package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.datamaps.builtin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.datamaps.DataMapType;

/**
 * NeoForge's built-in data maps that Create generates data for. On Fabric, Create's entrypoint also applies the
 * shipped files to Fabric's registries (oxidizables, waxables, fuels, compostables), since vanilla doesn't read them.
 */
public class NeoForgeDataMaps {
	public static final DataMapType<Item, Compostable> COMPOSTABLES = DataMapType.builder(
		id("compostables"), Registries.ITEM, Compostable.CODEC).synced(Compostable.CHANCE_CODEC, false).build();

	public static final DataMapType<Item, FurnaceFuel> FURNACE_FUELS = DataMapType.builder(
		id("furnace_fuels"), Registries.ITEM, FurnaceFuel.CODEC).synced(FurnaceFuel.BURN_TIME_CODEC, false).build();

	public static final DataMapType<Block, Oxidizable> OXIDIZABLES = DataMapType.builder(
		id("oxidizables"), Registries.BLOCK, Oxidizable.CODEC).synced(Oxidizable.OXIDIZABLE_CODEC, false).build();

	public static final DataMapType<Block, Waxable> WAXABLES = DataMapType.builder(
		id("waxables"), Registries.BLOCK, Waxable.CODEC).synced(Waxable.WAXABLE_CODEC, false).build();

	private static ResourceLocation id(final String name) {
		return ResourceLocation.fromNamespaceAndPath("neoforge", name);
	}
}
