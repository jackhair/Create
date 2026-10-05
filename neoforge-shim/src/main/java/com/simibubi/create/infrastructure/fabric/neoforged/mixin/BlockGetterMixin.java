package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.BlockGetter;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IBlockGetterExtension;

/** Makes {@link BlockGetter} extend NeoForge's {@link IBlockGetterExtension}; Loom injects it at compile time. */
@Mixin(BlockGetter.class)
public interface BlockGetterMixin extends IBlockGetterExtension {}
