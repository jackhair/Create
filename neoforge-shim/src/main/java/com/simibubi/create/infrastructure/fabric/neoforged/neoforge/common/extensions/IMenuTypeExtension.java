package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.network.IContainerFactory;

/**
 * Menus that receive extra data when opened are Fabric {@link ExtendedScreenHandlerType}s whose opening data is
 * the raw bytes NeoForge would write into the buffer (see {@link IPlayerExtension#openMenu}).
 * <p>
 * Create-owned re-implementation of NeoForge's {@code IMenuTypeExtension} for Fabric (PORTING.md D7).
 */
public interface IMenuTypeExtension {
	static <T extends AbstractContainerMenu> MenuType<T> create(IContainerFactory<T> factory) {
		return new ExtendedScreenHandlerType<>(
			(windowId, inventory, data) -> factory.create(windowId, inventory, new RegistryFriendlyByteBuf(Unpooled.wrappedBuffer(data), inventory.player.registryAccess())),
			ByteBufCodecs.BYTE_ARRAY);
	}
}
