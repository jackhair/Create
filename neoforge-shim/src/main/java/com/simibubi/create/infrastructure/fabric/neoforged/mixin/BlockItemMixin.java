package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IBlockItemExtension;

/** Makes {@link net.minecraft.world.item.BlockItem} implement NeoForge's {@link IBlockItemExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.item.BlockItem.class)
public abstract class BlockItemMixin implements IBlockItemExtension {}
