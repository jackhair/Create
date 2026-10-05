package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IBlockEntityRendererExtension;

/** Makes {@link net.minecraft.client.renderer.blockentity.BlockEntityRenderer} implement NeoForge's {@link IBlockEntityRendererExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.renderer.blockentity.BlockEntityRenderer.class)
public interface BlockEntityRendererMixin<T extends net.minecraft.world.level.block.entity.BlockEntity> extends IBlockEntityRendererExtension<T> {}
