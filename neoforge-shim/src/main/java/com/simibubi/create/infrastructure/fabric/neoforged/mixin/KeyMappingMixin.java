package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IKeyMappingExtension;

/** Makes {@link net.minecraft.client.KeyMapping} implement NeoForge's {@link IKeyMappingExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.KeyMapping.class)
public abstract class KeyMappingMixin implements IKeyMappingExtension {}
