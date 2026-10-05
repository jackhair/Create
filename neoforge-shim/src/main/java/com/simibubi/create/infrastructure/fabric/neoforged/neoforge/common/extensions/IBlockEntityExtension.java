package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.model.data.ModelData;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.util.PersistentData;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * The parts of NeoForge's {@code IBlockEntityExtension} Create uses, injected into {@link BlockEntity}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IBlockEntityExtension {
	private BlockEntity self() {
		return (BlockEntity) this;
	}

	default void invalidateCapabilities() {
		if (self().getLevel() != null)
			self().getLevel().invalidateCapabilities(self().getBlockPos());
	}

	/** NeoForge's persistent data tag (a persistent Fabric attachment here). */
	default CompoundTag getPersistentData() {
		return PersistentData.get((AttachmentTarget) self());
	}

	/** Model data isn't wired to Fabric rendering yet (PORTING.md 1d); requests are ignored. */
	default void requestModelDataUpdate() {}

	default ModelData getModelData() {
		return ModelData.EMPTY;
	}

	/** Called when the block entity is added to a loaded chunk (fired by Fabric's block entity load event). */
	default void onLoad() {}

	/** Called when the chunk holding this block entity unloads (fired by Fabric's block entity unload event). */
	default void onChunkUnloaded() {}

	default void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
		self().loadWithComponents(tag, registries);
	}

	/** Called on the client for update packets (wired by the shim's block entity data packet mixin). */
	default void onDataPacket(net.minecraft.network.Connection connection, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket packet, HolderLookup.Provider registries) {
		CompoundTag tag = packet.getTag();
		if (!tag.isEmpty())
			self().loadWithComponents(tag, registries);
	}
}
