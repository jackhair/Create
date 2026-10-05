package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

/**
 * Tracks {@link BlockCapabilityCache} listeners so {@code Level#invalidateCapabilities} can notify them, as on
 * NeoForge. Fabric's lookups don't cache providers, so invalidation never affects the lookup result itself.
 * <p>
 * Create code (PORTING.md D7), not a NeoForge class.
 */
public final class CapabilityInvalidation {
	private static final Map<ServerLevel, Map<BlockPos, List<WeakReference<BlockCapabilityCache<?, ?>>>>> LISTENERS = new WeakHashMap<>();

	private CapabilityInvalidation() {}

	static synchronized void listen(ServerLevel level, BlockPos pos, BlockCapabilityCache<?, ?> cache) {
		LISTENERS.computeIfAbsent(level, l -> new java.util.HashMap<>())
			.computeIfAbsent(pos, p -> new ArrayList<>())
			.add(new WeakReference<>(cache));
	}

	public static synchronized void invalidate(Level level, BlockPos pos) {
		if (!(level instanceof ServerLevel serverLevel))
			return;
		Map<BlockPos, List<WeakReference<BlockCapabilityCache<?, ?>>>> byPos = LISTENERS.get(serverLevel);
		if (byPos == null)
			return;
		List<WeakReference<BlockCapabilityCache<?, ?>>> caches = byPos.get(pos);
		if (caches == null)
			return;
		caches.removeIf(ref -> {
			BlockCapabilityCache<?, ?> cache = ref.get();
			return cache == null || !cache.notifyInvalidated();
		});
		if (caches.isEmpty())
			byPos.remove(pos);
	}

	public static void invalidate(Level level, ChunkPos chunk) {
		if (!(level instanceof ServerLevel serverLevel))
			return;
		List<BlockPos> positions;
		synchronized (CapabilityInvalidation.class) {
			Map<BlockPos, List<WeakReference<BlockCapabilityCache<?, ?>>>> byPos = LISTENERS.get(serverLevel);
			if (byPos == null)
				return;
			positions = byPos.keySet().stream().filter(p -> new ChunkPos(p).equals(chunk)).toList();
		}
		positions.forEach(p -> invalidate(level, p));
	}
}
