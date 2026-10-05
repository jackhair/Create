package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.core.Holder;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IHolderExtension;

/** Makes {@link Holder} extend NeoForge's {@link IHolderExtension}; Loom injects it at compile time. */
@Mixin(Holder.class)
public interface HolderMixin<T> extends IHolderExtension<T> {}
