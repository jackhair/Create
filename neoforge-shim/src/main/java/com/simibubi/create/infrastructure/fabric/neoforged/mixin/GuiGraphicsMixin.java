package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.gui.GuiGraphics;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IGuiGraphicsExtension;

/** Makes {@link GuiGraphics} implement NeoForge's {@link IGuiGraphicsExtension}; Loom injects it at compile time. */
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin implements IGuiGraphicsExtension {}
