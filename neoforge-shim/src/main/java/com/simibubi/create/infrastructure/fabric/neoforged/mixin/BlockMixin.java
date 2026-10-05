package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.Block;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IBlockExtension;

/** Makes {@link Block} implement NeoForge's {@link IBlockExtension}; Loom injects it at compile time. */
@Mixin(Block.class)
public abstract class BlockMixin implements IBlockExtension {}
