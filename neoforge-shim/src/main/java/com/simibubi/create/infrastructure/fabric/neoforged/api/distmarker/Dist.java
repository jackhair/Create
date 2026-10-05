package com.simibubi.create.infrastructure.fabric.neoforged.api.distmarker;

import net.fabricmc.api.EnvType;

/**
 * Physical side. Create-owned re-implementation of NeoForge's {@code Dist} for Fabric (PORTING.md D7).
 */
public enum Dist {
	CLIENT,
	DEDICATED_SERVER;

	public boolean isClient() {
		return this == CLIENT;
	}

	public boolean isDedicatedServer() {
		return this == DEDICATED_SERVER;
	}

	public static Dist fromEnvType(EnvType type) {
		return type == EnvType.CLIENT ? CLIENT : DEDICATED_SERVER;
	}
}
