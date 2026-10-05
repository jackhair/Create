package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IPoweredRailBlockExtension;

/** Makes {@link net.minecraft.world.level.block.PoweredRailBlock} implement NeoForge's {@link IPoweredRailBlockExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.level.block.PoweredRailBlock.class)
public abstract class PoweredRailBlockMixin implements IPoweredRailBlockExtension {}
