package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IRenderTypeExtension;

/** Makes {@link net.minecraft.client.renderer.RenderType} implement NeoForge's {@link IRenderTypeExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.renderer.RenderType.class)
public abstract class RenderTypeMixin implements IRenderTypeExtension {}
