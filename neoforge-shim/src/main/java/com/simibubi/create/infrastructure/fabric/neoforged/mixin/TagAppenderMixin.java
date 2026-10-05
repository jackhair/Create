package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.data.tags.TagsProvider;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.ITagAppenderExtension;

/** Makes {@link TagsProvider.TagAppender} implement NeoForge's {@link ITagAppenderExtension}; Loom injects it at compile time. */
@Mixin(TagsProvider.TagAppender.class)
public abstract class TagAppenderMixin<T> implements ITagAppenderExtension<T> {}
