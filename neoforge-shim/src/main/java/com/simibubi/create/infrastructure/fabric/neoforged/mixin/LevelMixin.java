package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.level.Level;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.ILevelExtension;

/** Makes {@link Level} implement NeoForge's {@link ILevelExtension}; Loom injects it at compile time. */
@Mixin(Level.class)
public abstract class LevelMixin implements ILevelExtension {}
