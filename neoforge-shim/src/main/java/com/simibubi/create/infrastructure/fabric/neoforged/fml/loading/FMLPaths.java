package com.simibubi.create.infrastructure.fabric.neoforged.fml.loading;

import java.nio.file.Path;
import java.util.function.Supplier;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Create-owned re-implementation of NeoForge's {@code FMLPaths} for Fabric (PORTING.md D7).
 */
public enum FMLPaths {
	GAMEDIR(() -> FabricLoader.getInstance().getGameDir()),
	CONFIGDIR(() -> FabricLoader.getInstance().getConfigDir()),
	MODSDIR(() -> FabricLoader.getInstance().getGameDir().resolve("mods"));

	private final Supplier<Path> path;

	FMLPaths(Supplier<Path> path) {
		this.path = path;
	}

	public Path get() {
		return path.get();
	}
}
