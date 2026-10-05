package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IBlockRenderDispatcherExtension;

/** Makes {@link net.minecraft.client.renderer.block.BlockRenderDispatcher} implement NeoForge's {@link IBlockRenderDispatcherExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.renderer.block.BlockRenderDispatcher.class)
public abstract class BlockRenderDispatcherMixin implements IBlockRenderDispatcherExtension {}
