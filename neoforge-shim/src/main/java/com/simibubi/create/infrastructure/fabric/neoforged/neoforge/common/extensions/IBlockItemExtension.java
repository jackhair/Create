package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

/**
 * The parts of NeoForge's {@code IBlockItemExtension} Create uses, injected into {@link net.minecraft.world.item.BlockItem}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IBlockItemExtension {
	default void removeFromBlockToItemMap(java.util.Map<net.minecraft.world.level.block.Block, net.minecraft.world.item.Item> blockToItemMap, net.minecraft.world.item.Item item) {
		blockToItemMap.remove(((net.minecraft.world.item.BlockItem) this).getBlock());
	}
}
