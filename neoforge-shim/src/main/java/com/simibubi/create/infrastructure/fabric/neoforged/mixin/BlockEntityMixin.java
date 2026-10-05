package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.entity.BlockEntity;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IBlockEntityExtension;

/** Makes {@link BlockEntity} implement NeoForge's {@link IBlockEntityExtension}; Loom injects it at compile time. */
@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements IBlockEntityExtension {}
