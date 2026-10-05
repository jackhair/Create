package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.ISlotExtension;

/** Makes {@link net.minecraft.world.inventory.Slot} implement NeoForge's {@link ISlotExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.inventory.Slot.class)
public abstract class SlotMixin implements ISlotExtension {}
