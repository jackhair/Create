package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.state.BlockState;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IBlockStateExtension;

/** Makes {@link BlockState} implement NeoForge's {@link IBlockStateExtension}; Loom injects it at compile time. */
@Mixin(BlockState.class)
public abstract class BlockStateMixin implements IBlockStateExtension {}
