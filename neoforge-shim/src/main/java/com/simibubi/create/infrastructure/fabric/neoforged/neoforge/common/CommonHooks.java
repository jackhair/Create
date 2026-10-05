package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.level.BlockEvent;

/**
 * The NeoForge {@code CommonHooks} Create calls, mostly to make Deployers act like players. Each fires the
 * NeoForge event on {@link NeoForge#EVENT_BUS} and then Fabric's matching callback, so other Fabric mods see
 * Deployer interactions the same way they see players'.
 * <p>
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public final class CommonHooks {
	private static final ThreadLocal<Player> CRAFTING_PLAYER = new ThreadLocal<>();

	private CommonHooks() {}

	public static void setCraftingPlayer(@Nullable Player player) {
		CRAFTING_PLAYER.set(player);
	}

	@Nullable
	public static Player getCraftingPlayer() {
		return CRAFTING_PLAYER.get();
	}

	public static PlayerInteractEvent.RightClickBlock onRightClickBlock(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
		PlayerInteractEvent.RightClickBlock event = NeoForge.EVENT_BUS.post(new PlayerInteractEvent.RightClickBlock(player, hand, pos, hitVec));
		if (!event.isCanceled()) {
			InteractionResult result = UseBlockCallback.EVENT.invoker().interact(player, player.level(), hand, hitVec);
			if (result != InteractionResult.PASS) {
				event.setCanceled(true);
				event.setCancellationResult(result);
			}
		}
		return event;
	}

	public static PlayerInteractEvent.LeftClickBlock onLeftClickBlock(Player player, BlockPos pos, Direction face, ServerboundPlayerActionPacket.Action action) {
		PlayerInteractEvent.LeftClickBlock event = NeoForge.EVENT_BUS.post(new PlayerInteractEvent.LeftClickBlock(player, pos, face, PlayerInteractEvent.LeftClickBlock.Action.convert(action)));
		if (!event.isCanceled() && AttackBlockCallback.EVENT.invoker().interact(player, player.level(), InteractionHand.MAIN_HAND, pos, face) != InteractionResult.PASS)
			event.setCanceled(true);
		return event;
	}

	/** @return false if the attack was cancelled */
	public static boolean onPlayerAttackTarget(Player player, Entity target) {
		if (NeoForge.EVENT_BUS.post(new AttackEntityEvent(player, target)).isCanceled())
			return false;
		return AttackEntityCallback.EVENT.invoker().interact(player, player.level(), InteractionHand.MAIN_HAND, target, null) == InteractionResult.PASS;
	}

	/** @return the result to use instead of normal interaction, or null to continue */
	@Nullable
	public static InteractionResult onInteractEntity(Player player, Entity entity, InteractionHand hand) {
		PlayerInteractEvent.EntityInteract event = NeoForge.EVENT_BUS.post(new PlayerInteractEvent.EntityInteract(player, hand, entity));
		if (event.isCanceled())
			return event.getCancellationResult();
		InteractionResult result = UseEntityCallback.EVENT.invoker().interact(player, player.level(), hand, entity, null);
		return result == InteractionResult.PASS ? null : result;
	}

	public static BlockEvent.BreakEvent fireBlockBreak(Level level, GameType gameType, ServerPlayer player, BlockPos pos, BlockState state) {
		BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, player);
		if (player.blockActionRestricted(level, pos, gameType))
			event.setCanceled(true);
		NeoForge.EVENT_BUS.post(event);
		if (!event.isCanceled() && !PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(level, player, pos, state, level.getBlockEntity(pos)))
			event.setCanceled(true);
		return event;
	}
}
