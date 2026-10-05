package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.block.BaseRailBlock;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IBaseRailBlockExtension;

/** Makes {@link BaseRailBlock} implement NeoForge's {@link IBaseRailBlockExtension}; Loom injects it at compile time. */
@Mixin(BaseRailBlock.class)
public abstract class BaseRailBlockMixin implements IBaseRailBlockExtension {}
