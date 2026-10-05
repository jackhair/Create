package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IEntityTypeBuilderExtension;

/** Makes {@link net.minecraft.world.entity.EntityType.Builder} implement NeoForge's {@link IEntityTypeBuilderExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.entity.EntityType.Builder.class)
public abstract class EntityTypeBuilderMixin<T extends net.minecraft.world.entity.Entity> implements IEntityTypeBuilderExtension<T> {}
