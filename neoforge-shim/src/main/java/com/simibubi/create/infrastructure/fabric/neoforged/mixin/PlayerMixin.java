package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.player.Player;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IPlayerExtension;

/** Makes {@link Player} implement NeoForge's {@link IPlayerExtension}; Loom injects it at compile time. */
@Mixin(Player.class)
public abstract class PlayerMixin implements IPlayerExtension {}
