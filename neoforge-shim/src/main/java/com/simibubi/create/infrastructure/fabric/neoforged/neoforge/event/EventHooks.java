package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.NeoForge;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.util.BlockSnapshot;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.entity.EntityTeleportEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.level.BlockEvent;

/**
 * The NeoForge {@code EventHooks} Create calls. Events go to {@link NeoForge#EVENT_BUS}; NeoForge-only events
 * with no Fabric listeners are no-ops.
 * <p>
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public final class EventHooks {
	private EventHooks() {}

	/** NeoForge's PlayerDestroyItemEvent has no Fabric equivalent or listeners. */
	public static void onPlayerDestroyItem(Player player, ItemStack stack, @Nullable InteractionHand hand) {}

	/** @return true if the placement was cancelled */
	public static boolean onBlockPlace(@Nullable Entity entity, BlockSnapshot blockSnapshot, Direction direction) {
		var placedAgainst = blockSnapshot.getLevel().getBlockState(blockSnapshot.getPos().relative(direction.getOpposite()));
		return NeoForge.EVENT_BUS.post(new BlockEvent.EntityPlaceEvent(blockSnapshot, placedAgainst, entity)).isCanceled();
	}

	public static EntityTeleportEvent.ChorusFruit onChorusFruitTeleport(LivingEntity entity, double targetX, double targetY, double targetZ) {
		return NeoForge.EVENT_BUS.post(new EntityTeleportEvent.ChorusFruit(entity, targetX, targetY, targetZ));
	}

	/** NeoForge's ItemCraftedEvent has no Fabric equivalent or listeners. */
	public static void firePlayerCraftingEvent(Player player, ItemStack crafted, Container craftMatrix) {}
}
