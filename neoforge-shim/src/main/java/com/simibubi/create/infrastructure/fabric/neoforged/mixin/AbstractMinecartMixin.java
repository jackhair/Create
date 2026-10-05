package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IAbstractMinecartExtension;

/** Makes {@link net.minecraft.world.entity.vehicle.AbstractMinecart} implement NeoForge's {@link IAbstractMinecartExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.entity.vehicle.AbstractMinecart.class)
public abstract class AbstractMinecartMixin implements IAbstractMinecartExtension {}
