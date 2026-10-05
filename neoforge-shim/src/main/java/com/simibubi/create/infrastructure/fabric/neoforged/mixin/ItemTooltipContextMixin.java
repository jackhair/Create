package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.ITooltipContextExtension;

/** Makes {@link net.minecraft.world.item.Item.TooltipContext} extend NeoForge's {@link ITooltipContextExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.item.Item.TooltipContext.class)
public interface ItemTooltipContextMixin extends ITooltipContextExtension {}
