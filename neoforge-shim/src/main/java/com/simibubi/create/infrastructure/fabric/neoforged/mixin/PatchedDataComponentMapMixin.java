package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IPatchedDataComponentMapExtension;

/** Makes {@link net.minecraft.core.component.PatchedDataComponentMap} implement NeoForge's {@link IPatchedDataComponentMapExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.core.component.PatchedDataComponentMap.class)
public abstract class PatchedDataComponentMapMixin implements IPatchedDataComponentMapExtension {}
