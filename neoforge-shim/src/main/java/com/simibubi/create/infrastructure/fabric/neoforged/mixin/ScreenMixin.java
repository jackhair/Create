package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IScreenExtension;

/** Makes {@link net.minecraft.client.gui.screens.Screen} implement NeoForge's {@link IScreenExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.gui.screens.Screen.class)
public abstract class ScreenMixin implements IScreenExtension {}
