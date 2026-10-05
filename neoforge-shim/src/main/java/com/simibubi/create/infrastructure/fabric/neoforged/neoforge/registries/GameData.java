package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries;

import net.minecraft.core.IdMapper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Create-owned re-implementation of the part of NeoForge's {@code GameData} Create uses (PORTING.md D7).
 */
public final class GameData {
	private GameData() {}

	public static IdMapper<BlockState> getBlockStateIDMap() {
		return Block.BLOCK_STATE_REGISTRY;
	}
}
