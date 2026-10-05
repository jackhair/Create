package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.item.Item;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IItemExtension;

/** Makes {@link Item} implement NeoForge's {@link IItemExtension}; Loom injects it at compile time. */
@Mixin(Item.class)
public abstract class ItemMixin implements IItemExtension {}
