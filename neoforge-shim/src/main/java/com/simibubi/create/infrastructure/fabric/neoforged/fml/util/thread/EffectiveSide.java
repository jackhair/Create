package com.simibubi.create.infrastructure.fabric.neoforged.fml.util.thread;

import com.simibubi.create.infrastructure.fabric.neoforged.fml.LogicalSide;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.loading.FMLEnvironment;

/**
 * The logical side of the current thread: the client thread is {@link LogicalSide#CLIENT}, anything else
 * (the integrated or dedicated server) is {@link LogicalSide#SERVER}.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code EffectiveSide} for Fabric (PORTING.md D7).
 */
public final class EffectiveSide {
	private EffectiveSide() {}

	public static LogicalSide get() {
		if (FMLEnvironment.dist.isDedicatedServer())
			return LogicalSide.SERVER;
		return ClientThread.isClientThread() ? LogicalSide.CLIENT : LogicalSide.SERVER;
	}

	private static final class ClientThread {
		static boolean isClientThread() {
			return net.minecraft.client.Minecraft.getInstance().isSameThread();
		}
	}
}
