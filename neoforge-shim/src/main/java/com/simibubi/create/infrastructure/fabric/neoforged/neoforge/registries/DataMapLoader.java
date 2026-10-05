package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import com.mojang.datafixers.util.Either;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.conditions.WithConditions;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.datamaps.DataMapEntry;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.datamaps.DataMapFile;
import net.minecraft.tags.TagKey;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.datamaps.DataMapType;

/**
 * Loads data maps from datapacks ({@code data/<ns>/data_maps/<registry>/<name>.json}, NeoForge's format) for
 * built-in registries, and answers {@code Holder#getData}. Server-side only for now: data maps aren't synced
 * to clients (Create only reads them on the server).
 * <p>
 * Create code (PORTING.md D7), modelled on NeoForge's {@code DataMapLoader}.
 */
public final class DataMapLoader implements IdentifiableResourceReloadListener {
	public static final String PATH = "data_maps";
	private static final Logger LOGGER = LoggerFactory.getLogger("Create/DataMaps");
	private static final Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, DataMapType<?, ?>>> TYPES = new HashMap<>();
	private static volatile Map<DataMapType<?, ?>, Map<ResourceKey<?>, Object>> values = new IdentityHashMap<>();

	public static synchronized void register(DataMapType<?, ?> type) {
		TYPES.computeIfAbsent(type.registryKey(), k -> new HashMap<>()).put(type.id(), type);
	}

	public static String getFolderLocation(ResourceLocation registryId) {
		return (registryId.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE) ? "" : registryId.getNamespace() + "/") + registryId.getPath();
	}

	@Nullable
	@SuppressWarnings("unchecked")
	public static <R, T> T getData(DataMapType<R, T> type, Holder<R> holder) {
		Map<ResourceKey<?>, Object> forType = values.get(type);
		if (forType == null)
			return null;
		Optional<ResourceKey<R>> key = holder.unwrapKey();
		return key.map(k -> (T) forType.get(k)).orElse(null);
	}

	@Override
	public ResourceLocation getFabricId() {
		return ResourceLocation.fromNamespaceAndPath("create", "neoforge_data_maps");
	}

	@Override
	public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier barrier, ResourceManager manager, ProfilerFiller prepProfiler, ProfilerFiller applyProfiler, Executor backgroundExecutor, Executor gameExecutor) {
		return CompletableFuture.supplyAsync(() -> load(manager), backgroundExecutor)
			.thenCompose(barrier::wait)
			.thenAcceptAsync(loaded -> values = loaded, gameExecutor);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<DataMapType<?, ?>, Map<ResourceKey<?>, Object>> load(ResourceManager manager) {
		Map<DataMapType<?, ?>, Map<ResourceKey<?>, Object>> loaded = new IdentityHashMap<>();
		Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, DataMapType<?, ?>>> types;
		synchronized (DataMapLoader.class) {
			types = new HashMap<>(TYPES);
		}
		types.forEach((registryKey, byId) -> {
			Registry registry = BuiltInRegistries.REGISTRY.get((ResourceKey) registryKey);
			if (registry == null)
				return;
			FileToIdConverter files = FileToIdConverter.json(PATH + "/" + getFolderLocation(registryKey.location()));
			for (Map.Entry<ResourceLocation, List<Resource>> entry : files.listMatchingResourceStacks(manager).entrySet()) {
				DataMapType type = byId.get(files.fileToId(entry.getKey()));
				if (type == null)
					continue;
				Map<ResourceKey<?>, Object> result = loaded.computeIfAbsent(type, t -> new HashMap<>());
				for (Resource resource : entry.getValue()) {
					try (var reader = resource.openAsReader()) {
						JsonElement json = JsonParser.parseReader(reader);
						DataMapFile file = (DataMapFile) DataMapFile.codec(registryKey, type).parse(JsonOps.INSTANCE, json).getOrThrow();
						apply(registry, file, result);
					} catch (Exception e) {
						LOGGER.error("Failed to load data map {} from {}", type.id(), resource.sourcePackId(), e);
					}
				}
			}
		});
		return loaded;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void apply(Registry registry, DataMapFile file, Map<ResourceKey<?>, Object> result) {
		if (file.replace())
			result.clear();
		Map<Either<TagKey<?>, ResourceKey<?>>, Optional<WithConditions<DataMapEntry<?>>>> values = file.values();
		values.forEach((target, entry) -> entry.ifPresent(withConditions -> {
			Object value = withConditions.carrier().value();
			target.ifLeft(tag -> registry.getTagOrEmpty((TagKey) tag).forEach(holder ->
					((Holder<?>) holder).unwrapKey().ifPresent(key -> result.put(key, value))))
				.ifRight(key -> result.put(key, value));
		}));
		List<DataMapEntry.Removal<?, ?>> removals = file.removals();
		for (DataMapEntry.Removal<?, ?> removal : removals)
			((Either<TagKey<?>, ResourceKey<?>>) (Either) removal.key()).ifLeft(tag -> registry.getTagOrEmpty((TagKey) tag).forEach(holder ->
					((Holder<?>) holder).unwrapKey().ifPresent(result::remove)))
				.ifRight(result::remove);
	}
}
