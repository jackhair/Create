package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IDyeColorExtension;

/** Makes {@link net.minecraft.world.item.DyeColor} implement NeoForge's {@link IDyeColorExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.item.DyeColor.class)
public abstract class DyeColorMixin implements IDyeColorExtension {}
