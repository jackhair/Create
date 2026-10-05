package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IModelBlockRendererExtension;

/** Makes {@link net.minecraft.client.renderer.block.ModelBlockRenderer} implement NeoForge's {@link IModelBlockRendererExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.renderer.block.ModelBlockRenderer.class)
public abstract class ModelBlockRendererMixin implements IModelBlockRendererExtension {}
