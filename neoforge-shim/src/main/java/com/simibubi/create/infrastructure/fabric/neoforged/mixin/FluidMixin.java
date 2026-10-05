package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.material.Fluid;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IFluidExtension;

/** Makes {@link Fluid} implement NeoForge's {@link IFluidExtension}; Loom injects it at compile time. */
@Mixin(Fluid.class)
public abstract class FluidMixin implements IFluidExtension {}
