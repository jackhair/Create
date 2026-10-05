package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IRegistryExtension;

/** Makes {@link net.minecraft.core.Registry} extend NeoForge's {@link IRegistryExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.core.Registry.class)
public interface RegistryMixin<T> extends IRegistryExtension<T> {}
