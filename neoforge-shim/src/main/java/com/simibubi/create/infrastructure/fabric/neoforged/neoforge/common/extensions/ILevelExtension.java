package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.BlockCapability;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.CapabilityInvalidation;

/**
 * The parts of NeoForge's {@code ILevelExtension} Create uses, injected into {@link Level}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface ILevelExtension {
	/** Prefix of dimension translation keys ({@code dimension.<namespace>.<path>}). */
	String TRANSLATION_PREFIX = "dimension";

	private Level self() {
		return (Level) this;
	}

	@Nullable
	default <T, C> T getCapability(BlockCapability<T, C> cap, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity, C context) {
		return cap.getCapability(self(), pos, state, blockEntity, context);
	}

	@Nullable
	default <T, C> T getCapability(BlockCapability<T, C> cap, BlockPos pos, C context) {
		return cap.getCapability(self(), pos, null, null, context);
	}

	@Nullable
	default <T> T getCapability(BlockCapability<T, @Nullable Void> cap, BlockPos pos) {
		return cap.getCapability(self(), pos, null, null, null);
	}

	default void invalidateCapabilities(BlockPos pos) {
		CapabilityInvalidation.invalidate(self(), pos);
	}

	default void invalidateCapabilities(ChunkPos pos) {
		CapabilityInvalidation.invalidate(self(), pos);
	}

	/**
	 * The second half of {@code Level#setBlock}: marks the block for rendering, notifies neighbours and updates
	 * shapes, as NeoForge exposes it.
	 */
	default void markAndNotifyBlock(BlockPos pos, @Nullable net.minecraft.world.level.chunk.LevelChunk chunk, BlockState oldState, BlockState newState, int flags, int recursionLeft) {
		Level level = self();
		net.minecraft.world.level.block.Block block = newState.getBlock();
		BlockState currentState = level.getBlockState(pos);
		if (currentState == newState) {
			if (oldState != currentState)
				level.setBlocksDirty(pos, oldState, currentState);
			if ((flags & 2) != 0 && (!level.isClientSide || (flags & 4) == 0) && (level.isClientSide || chunk == null || (chunk.getFullStatus() != null && chunk.getFullStatus().isOrAfter(net.minecraft.server.level.FullChunkStatus.BLOCK_TICKING))))
				level.sendBlockUpdated(pos, oldState, newState, flags);
			if ((flags & 1) != 0) {
				level.blockUpdated(pos, oldState.getBlock());
				if (!level.isClientSide && newState.hasAnalogOutputSignal())
					level.updateNeighbourForOutputSignal(pos, block);
			}
			if ((flags & 16) == 0 && recursionLeft > 0) {
				int neighbourFlags = flags & -34;
				oldState.updateIndirectNeighbourShapes(level, pos, neighbourFlags, recursionLeft - 1);
				newState.updateNeighbourShapes(level, pos, neighbourFlags, recursionLeft - 1);
				newState.updateIndirectNeighbourShapes(level, pos, neighbourFlags, recursionLeft - 1);
			}
			level.onBlockStateChange(pos, oldState, currentState);
		}
	}

	default boolean isAreaLoaded(BlockPos center, int range) {
		return self().hasChunksAt(center.offset(-range, -range, -range), center.offset(range, range, range));
	}

	default <T> net.minecraft.core.Holder<T> holderOrThrow(net.minecraft.resources.ResourceKey<T> key) {
		return self().registryAccess().registryOrThrow(net.minecraft.resources.ResourceKey.<T>createRegistryKey(key.registry())).getHolderOrThrow(key);
	}

	/** NeoForge's adjustable day speed isn't available on Fabric; these report vanilla behaviour. */
	default float getDayTimeFraction() {
		return 0;
	}

	default float getDayTimePerTick() {
		return -1;
	}

	default void setDayTimeFraction(float fraction) {}

	default void setDayTimePerTick(float perTick) {}
}
