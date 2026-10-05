package com.simibubi.create.infrastructure.fabric.neoforged.fml;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import net.fabricmc.loader.api.FabricLoader;

/**
 * Create-owned re-implementation of the parts of NeoForge's {@code ModList} Create uses, for Fabric (PORTING.md D7).
 */
public class ModList {
	private static final ModList INSTANCE = new ModList();
	private final Map<String, ModContainer> containers = new ConcurrentHashMap<>();

	public static ModList get() {
		return INSTANCE;
	}

	/** Called by Create's Fabric bootstrap so its container exposes the mod bus. */
	public void putContainer(ModContainer container) {
		containers.put(container.getModId(), container);
	}

	public boolean isLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	public Optional<? extends ModContainer> getModContainerById(String modId) {
		ModContainer known = containers.get(modId);
		if (known != null)
			return Optional.of(known);
		return FabricLoader.getInstance().getModContainer(modId).map(c -> new ModContainer(c, null));
	}

	public void forEachModContainer(BiConsumer<String, ModContainer> consumer) {
		for (net.fabricmc.loader.api.ModContainer c : FabricLoader.getInstance().getAllMods()) {
			String id = c.getMetadata().getId();
			consumer.accept(id, getModContainerById(id).orElseThrow());
		}
	}
}
