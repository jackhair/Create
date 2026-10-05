package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.ItemStack;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IItemStackExtension;

/** Makes {@link ItemStack} implement NeoForge's {@link IItemStackExtension}; Loom injects it at compile time. */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements IItemStackExtension {}
