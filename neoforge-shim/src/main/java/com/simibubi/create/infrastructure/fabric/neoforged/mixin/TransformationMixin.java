package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.ITransformationExtension;

/** Makes {@link com.mojang.math.Transformation} implement NeoForge's {@link ITransformationExtension}; Loom injects it at compile time. */
@Mixin(com.mojang.math.Transformation.class)
public abstract class TransformationMixin implements ITransformationExtension {}
