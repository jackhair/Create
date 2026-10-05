package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiCache;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

/**
 * Looks up a block capability at a fixed position through a Fabric {@link BlockApiCache}, which tracks block
 * entity and state changes itself. NeoForge's invalidation listeners are kept but only fire on explicit
 * {@code invalidateCapabilities} calls (see {@code ILevelExtension}).
 * <p>
 * Create-owned re-implementation of NeoForge's {@code BlockCapabilityCache} for Fabric (PORTING.md D7).
 */
public final class BlockCapabilityCache<T, C> {
	private final BlockCapability<T, C> capability;
	private final ServerLevel level;
	private final BlockPos pos;
	private final C context;
	private final BlockApiCache<T, C> cache;
	private final BooleanSupplier isValid;
	private final Runnable invalidationListener;

	private BlockCapabilityCache(BlockCapability<T, C> capability, ServerLevel level, BlockPos pos, C context, BooleanSupplier isValid, Runnable invalidationListener) {
		this.capability = capability;
		this.level = level;
		this.pos = pos.immutable();
		this.context = context;
		this.cache = BlockApiCache.create(capability.lookup(), level, this.pos);
		this.isValid = isValid;
		this.invalidationListener = invalidationListener;
		CapabilityInvalidation.listen(level, this.pos, this);
	}

	public static <T, C> BlockCapabilityCache<T, C> create(BlockCapability<T, C> capability, ServerLevel level, BlockPos pos, C context) {
		return create(capability, level, pos, context, () -> true, () -> {});
	}

	public static <T, C> BlockCapabilityCache<T, C> create(BlockCapability<T, C> capability, ServerLevel level, BlockPos pos, C context, BooleanSupplier isValid, Runnable invalidationListener) {
		return new BlockCapabilityCache<>(capability, level, pos, context, isValid, invalidationListener);
	}

	public ServerLevel level() {
		return level;
	}

	public BlockPos pos() {
		return pos;
	}

	public C context() {
		return context;
	}

	public BlockCapability<T, C> capability() {
		return capability;
	}

	@Nullable
	public T getCapability() {
		return isValid.getAsBoolean() ? cache.find(context) : null;
	}

	/** Returns false once the owner reports the cache invalid, so it can be dropped. */
	boolean notifyInvalidated() {
		if (!isValid.getAsBoolean())
			return false;
		invalidationListener.run();
		return true;
	}
}
