package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.Entity;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.attachment.IAttachmentHolder;

/** Makes {@link Entity} implement NeoForge's {@link IAttachmentHolder}; Loom injects it at compile time. */
@Mixin(Entity.class)
public abstract class EntityMixin implements IAttachmentHolder {}
