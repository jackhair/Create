package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.entity.Entity;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.attachment.IAttachmentHolder;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IEntityExtension;

/** Makes {@link Entity} implement NeoForge's injected interfaces; Loom injects them at compile time. */
@Mixin(Entity.class)
public abstract class EntityMixin implements IAttachmentHolder, IEntityExtension {}
