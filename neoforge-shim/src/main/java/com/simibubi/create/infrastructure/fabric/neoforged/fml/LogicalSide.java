package com.simibubi.create.infrastructure.fabric.neoforged.fml;

/**
 * Logical side. Create-owned re-implementation of NeoForge's {@code LogicalSide} for Fabric (PORTING.md D7).
 */
public enum LogicalSide {
	CLIENT,
	SERVER;

	public boolean isClient() {
		return this == CLIENT;
	}

	public boolean isServer() {
		return this == SERVER;
	}
}
