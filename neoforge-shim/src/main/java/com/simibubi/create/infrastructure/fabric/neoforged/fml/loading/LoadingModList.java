package com.simibubi.create.infrastructure.fabric.neoforged.fml.loading;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

/**
 * Create-owned re-implementation of the parts of NeoForge's {@code LoadingModList} Create uses, for Fabric
 * (PORTING.md D7). Mod files are represented by Fabric's {@link ModContainer}.
 */
public class LoadingModList {
	private static final LoadingModList INSTANCE = new LoadingModList();

	public static LoadingModList get() {
		return INSTANCE;
	}

	@Nullable
	public ModContainer getModFileById(String modId) {
		return FabricLoader.getInstance().getModContainer(modId).orElse(null);
	}
}
