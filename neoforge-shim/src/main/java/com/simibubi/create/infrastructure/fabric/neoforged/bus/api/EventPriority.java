package com.simibubi.create.infrastructure.fabric.neoforged.bus.api;

/**
 * Listener ordering; higher priorities run first.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code EventPriority} for Fabric (PORTING.md D7).
 */
public enum EventPriority {
	HIGHEST,
	HIGH,
	NORMAL,
	LOW,
	LOWEST
}
