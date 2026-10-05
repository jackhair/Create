package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.resources.model.BakedModel;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IBakedModelExtension;

/** Makes {@link BakedModel} implement NeoForge's {@link IBakedModelExtension}; Loom injects it at compile time. */
@Mixin(BakedModel.class)
public interface BakedModelMixin extends IBakedModelExtension {}
