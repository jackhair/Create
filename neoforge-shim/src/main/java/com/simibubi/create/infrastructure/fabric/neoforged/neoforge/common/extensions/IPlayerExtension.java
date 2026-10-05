package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import java.util.OptionalInt;
import java.util.function.Consumer;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * The parts of NeoForge's {@code IPlayerExtension} Create uses, injected into {@link Player}: opening menus with
 * extra data, sent through Fabric's extended screen handler factory.
 * <p>
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IPlayerExtension {
	private Player self() {
		return (Player) this;
	}

	default OptionalInt openMenu(MenuProvider menuProvider, BlockPos pos) {
		return openMenu(menuProvider, buf -> buf.writeBlockPos(pos));
	}

	default OptionalInt openMenu(MenuProvider menuProvider, Consumer<RegistryFriendlyByteBuf> extraDataWriter) {
		return self().openMenu(new ExtendedScreenHandlerFactory<byte[]>() {
			@Override
			public byte[] getScreenOpeningData(ServerPlayer player) {
				RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), player.registryAccess());
				try {
					extraDataWriter.accept(buf);
					byte[] bytes = new byte[buf.readableBytes()];
					buf.readBytes(bytes);
					return bytes;
				} finally {
					buf.release();
				}
			}

			@Override
			public Component getDisplayName() {
				return menuProvider.getDisplayName();
			}

			@Override
			public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
				return menuProvider.createMenu(containerId, inventory, player);
			}

			@Override
			public boolean shouldCloseCurrentScreen() {
				return menuProvider.shouldCloseCurrentScreen();
			}
		});
	}
}
