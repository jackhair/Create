package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidType;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.util.PersistentData;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import java.util.Collection;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.Entity;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.EntityCapability;

/**
 * The parts of NeoForge's {@code IEntityExtension} Create uses, injected into {@link Entity}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IEntityExtension {
	private Entity self() {
		return (Entity) this;
	}

	@Nullable
	default <T, C> T getCapability(EntityCapability<T, C> capability, C context) {
		return capability.getCapability(self(), context);
	}

	@Nullable
	default <T> T getCapability(EntityCapability<T, @Nullable Void> capability) {
		return capability.getCapability(self(), null);
	}

	/** NeoForge's persistent data tag (a persistent Fabric attachment here). */
	default CompoundTag getPersistentData() {
		return PersistentData.get((AttachmentTarget) self());
	}

	/** Starts (non-null) or stops (null) capturing item drops; implemented by the shim's EntityMixin. */
	@Nullable
	default Collection<ItemEntity> captureDrops(@Nullable Collection<ItemEntity> value) {
		throw new AssertionError("Implemented by EntityMixin");
	}

	@Nullable
	default Collection<ItemEntity> captureDrops() {
		throw new AssertionError("Implemented by EntityMixin");
	}

	default boolean isAddedToLevel() {
		return self().level() != null && !self().isRemoved();
	}

	@Nullable
	default ItemStack getPickedResult(HitResult target) {
		return self().getPickResult();
	}

	default MobCategory getClassification(boolean forSpawnCount) {
		return self().getType().getCategory();
	}

	default FluidType getEyeInFluidType() {
		return self().level().getFluidState(BlockPos.containing(self().getEyePosition())).getFluidType();
	}

	default CompoundTag serializeNBT(HolderLookup.Provider registries) {
		CompoundTag tag = new CompoundTag();
		self().saveAsPassenger(tag);
		return tag;
	}

	default void deserializeNBT(HolderLookup.Provider registries, CompoundTag tag) {
		self().load(tag);
	}
}
