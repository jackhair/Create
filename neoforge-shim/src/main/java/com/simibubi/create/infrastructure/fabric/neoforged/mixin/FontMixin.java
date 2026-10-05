package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IFontExtension;

/** Makes {@link net.minecraft.client.gui.Font} implement NeoForge's {@link IFontExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.gui.Font.class)
public abstract class FontMixin implements IFontExtension {}
