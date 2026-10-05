package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.material.FluidState;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IFluidStateExtension;

/** Makes {@link FluidState} implement NeoForge's {@link IFluidStateExtension}; Loom injects it at compile time. */
@Mixin(FluidState.class)
public abstract class FluidStateMixin implements IFluidStateExtension {}
