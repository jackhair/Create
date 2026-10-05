package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.attachment;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;

/**
 * Create-owned re-implementation of NeoForge's {@code IAttachmentSerializer} for Fabric (PORTING.md D7).
 */
public interface IAttachmentSerializer<S extends Tag, T> {
	T read(IAttachmentHolder holder, S tag, HolderLookup.Provider provider);

	@Nullable
	S write(T attachment, HolderLookup.Provider provider);
}
