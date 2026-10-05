package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.util;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.level.ServerLevel;

/**
 * NeoForge's fake player on top of Fabric's, so other Fabric mods recognize Create's fake players
 * ({@code instanceof net.fabricmc.fabric.api.entity.FakePlayer}).
 * <p>
 * Create-owned re-implementation of NeoForge's {@code FakePlayer} for Fabric (PORTING.md D7).
 */
public class FakePlayer extends net.fabricmc.fabric.api.entity.FakePlayer {
	public FakePlayer(ServerLevel level, GameProfile name) {
		super(level, name);
	}
}
