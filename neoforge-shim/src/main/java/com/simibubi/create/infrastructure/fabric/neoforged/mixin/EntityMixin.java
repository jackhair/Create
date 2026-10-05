package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import java.util.Collection;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.attachment.IAttachmentHolder;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IEntityExtension;

/**
 * Makes {@link Entity} implement NeoForge's injected interfaces, and implements NeoForge's drop capturing:
 * while a collection is set, items the entity spawns go into it instead of the level.
 */
@Mixin(Entity.class)
public abstract class EntityMixin implements IAttachmentHolder, IEntityExtension {
	@Unique
	@Nullable
	private Collection<ItemEntity> create$capturedDrops;

	@Override
	@Nullable
	public Collection<ItemEntity> captureDrops(@Nullable Collection<ItemEntity> value) {
		Collection<ItemEntity> previous = create$capturedDrops;
		create$capturedDrops = value;
		return previous;
	}

	@Override
	@Nullable
	public Collection<ItemEntity> captureDrops() {
		return create$capturedDrops;
	}

	@WrapOperation(method = "spawnAtLocation(Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
	private boolean create$captureDrop(Level level, Entity entity, Operation<Boolean> original) {
		if (create$capturedDrops != null && entity instanceof ItemEntity item) {
			create$capturedDrops.add(item);
			return true;
		}
		return original.call(level, entity);
	}
}
