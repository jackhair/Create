package com.simibubi.create.infrastructure.fabric.neoforged.neoforgespi.language;

/**
 * Create-owned re-implementation of the parts of NeoForge's {@code IModInfo} Create uses, for Fabric (PORTING.md D7).
 */
public interface IModInfo {
	String getModId();

	String getDisplayName();

	/** Fabric's {@code Version}; Create only calls {@code toString()} on it. */
	Object getVersion();
}
