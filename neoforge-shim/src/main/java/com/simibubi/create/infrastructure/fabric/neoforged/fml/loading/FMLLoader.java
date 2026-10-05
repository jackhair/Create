package com.simibubi.create.infrastructure.fabric.neoforged.fml.loading;

import com.simibubi.create.infrastructure.fabric.neoforged.api.distmarker.Dist;

/**
 * Create-owned re-implementation of the parts of NeoForge's {@code FMLLoader} Create uses, for Fabric (PORTING.md D7).
 */
public class FMLLoader {
	public static boolean isProduction() {
		return FMLEnvironment.production;
	}

	public static Dist getDist() {
		return FMLEnvironment.dist;
	}
}
