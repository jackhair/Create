package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;

/**
 * The parts of NeoForge's {@code ITagAppenderExtension} Create and Registrate use, injected into
 * {@link TagsProvider.TagAppender}. Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface ITagAppenderExtension<T> {
	@SuppressWarnings("unchecked")
	private TagsProvider.TagAppender<T> self() {
		return (TagsProvider.TagAppender<T>) this;
	}

	default TagBuilder getInternalBuilder() {
		return self().builder;
	}

	default TagsProvider.TagAppender<T> add(TagEntry entry) {
		self().builder.add(entry);
		return self();
	}
}
