package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Create-owned re-implementation of NeoForge's {@code IBlockCapabilityProvider} for Fabric (PORTING.md D7).
 */
@FunctionalInterface
public interface IBlockCapabilityProvider<T, C> {
	@Nullable
	T getCapability(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, C context);
}
