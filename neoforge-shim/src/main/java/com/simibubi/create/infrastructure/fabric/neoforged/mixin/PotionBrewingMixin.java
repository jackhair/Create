package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IPotionBrewingExtension;

/** Makes {@link net.minecraft.world.item.alchemy.PotionBrewing} implement NeoForge's {@link IPotionBrewingExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.item.alchemy.PotionBrewing.class)
public abstract class PotionBrewingMixin implements IPotionBrewingExtension {}
