package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IEntityRenderDispatcherExtension;

/** Makes {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher} implement NeoForge's {@link IEntityRenderDispatcherExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.renderer.entity.EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin implements IEntityRenderDispatcherExtension {}
