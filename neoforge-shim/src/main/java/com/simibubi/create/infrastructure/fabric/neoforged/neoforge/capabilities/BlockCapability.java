package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A block capability is a Fabric {@link BlockApiLookup} with the same name, type and context; NeoForge
 * designed capabilities after Fabric's API Lookup, so the semantics match.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code BlockCapability} for Fabric (PORTING.md D7).
 */
public final class BlockCapability<T, C> extends BaseCapability<T, C> {
	private final BlockApiLookup<T, C> lookup;

	private BlockCapability(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
		super(name, typeClass, contextClass);
		this.lookup = BlockApiLookup.get(name, typeClass, contextClass);
	}

	public static <T, C> BlockCapability<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
		return new BlockCapability<>(name, typeClass, contextClass);
	}

	public static <T> BlockCapability<T, @Nullable Direction> createSided(ResourceLocation name, Class<T> typeClass) {
		return create(name, typeClass, Direction.class);
	}

	public static <T> BlockCapability<T, @Nullable Void> createVoid(ResourceLocation name, Class<T> typeClass) {
		return create(name, typeClass, Void.class);
	}

	public BlockApiLookup<T, C> lookup() {
		return lookup;
	}

	@Nullable
	public T getCapability(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity blockEntity, C context) {
		return lookup.find(level, pos, state, blockEntity, context);
	}
}
