package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IAbstractContainerScreenExtension;

/** Makes {@link net.minecraft.client.gui.screens.inventory.AbstractContainerScreen} implement NeoForge's {@link IAbstractContainerScreenExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.gui.screens.inventory.AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin implements IAbstractContainerScreenExtension {}
