package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.data.loading;

/**
 * Create-owned re-implementation of NeoForge's {@code DatagenModLoader} for Fabric (PORTING.md D7):
 * data generation runs through Fabric API's datagen entrypoint, which sets {@code fabric-api.datagen}.
 */
public final class DatagenModLoader {
	private DatagenModLoader() {}

	public static boolean isRunningDataGen() {
		return System.getProperty("fabric-api.datagen") != null;
	}
}
