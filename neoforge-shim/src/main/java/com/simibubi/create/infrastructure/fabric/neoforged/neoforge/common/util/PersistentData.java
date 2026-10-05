package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.util;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * NeoForge's per-entity / per-block-entity "persistent data" tag, stored as a persistent Fabric attachment.
 * Like NeoForge, it isn't copied when a player respawns. Create code (PORTING.md D7).
 */
public final class PersistentData {
	public static final AttachmentType<CompoundTag> TYPE = AttachmentRegistry.<CompoundTag>builder()
		.persistent(CompoundTag.CODEC)
		.initializer(CompoundTag::new)
		.buildAndRegister(ResourceLocation.fromNamespaceAndPath("create", "neoforge_persistent_data"));

	private PersistentData() {}

	public static CompoundTag get(AttachmentTarget target) {
		return target.getAttachedOrCreate(TYPE);
	}
}
