package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.block.model.BakedQuad;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IBakedQuadExtension;

/** Makes {@link BakedQuad} implement NeoForge's {@link IBakedQuadExtension}; Loom injects it at compile time. */
@Mixin(BakedQuad.class)
public abstract class BakedQuadMixin implements IBakedQuadExtension {}
