package com.simibubi.create.infrastructure.fabric.neoforged.fml;

/**
 * Create-owned re-implementation of NeoForge's {@code ModLoadingContext} for Fabric (PORTING.md D7).
 * Create only passes it along; containers are supplied explicitly.
 */
public class ModLoadingContext {
	private static final ModLoadingContext INSTANCE = new ModLoadingContext();

	public static ModLoadingContext get() {
		return INSTANCE;
	}
}
