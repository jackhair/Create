package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IItemContainerContentsExtension;

/** Makes {@link net.minecraft.world.item.component.ItemContainerContents} implement NeoForge's {@link IItemContainerContentsExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.item.component.ItemContainerContents.class)
public abstract class ItemContainerContentsMixin implements IItemContainerContentsExtension {}
