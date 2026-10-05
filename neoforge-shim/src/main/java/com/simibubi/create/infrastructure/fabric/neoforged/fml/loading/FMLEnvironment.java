package com.simibubi.create.infrastructure.fabric.neoforged.fml.loading;

import net.fabricmc.loader.api.FabricLoader;

import com.simibubi.create.infrastructure.fabric.neoforged.api.distmarker.Dist;

/**
 * Create-owned re-implementation of NeoForge's {@code FMLEnvironment} for Fabric (PORTING.md D7).
 */
public class FMLEnvironment {
	public static final Dist dist = Dist.fromEnvType(FabricLoader.getInstance().getEnvironmentType());
	public static final boolean production = !FabricLoader.getInstance().isDevelopmentEnvironment();
}
