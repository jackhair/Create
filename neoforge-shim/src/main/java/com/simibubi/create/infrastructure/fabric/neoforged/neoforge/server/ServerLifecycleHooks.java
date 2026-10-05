package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.server;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

/**
 * Tracks the running server with Fabric's lifecycle events. {@link #init()} is called by Create's entrypoint.
 * <p>
 * Create-owned re-implementation of the parts of NeoForge's {@code ServerLifecycleHooks} Create uses (PORTING.md D7).
 */
public final class ServerLifecycleHooks {
	@Nullable
	private static MinecraftServer currentServer;

	private ServerLifecycleHooks() {}

	public static void init() {
		ServerLifecycleEvents.SERVER_STARTING.register(server -> currentServer = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> currentServer = null);
	}

	@Nullable
	public static MinecraftServer getCurrentServer() {
		return currentServer;
	}
}
