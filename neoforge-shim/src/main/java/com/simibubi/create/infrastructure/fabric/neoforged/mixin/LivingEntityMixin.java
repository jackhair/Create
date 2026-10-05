package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.ILivingEntityExtension;

/** Makes {@link net.minecraft.world.entity.LivingEntity} implement NeoForge's {@link ILivingEntityExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.entity.LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntityExtension {}
