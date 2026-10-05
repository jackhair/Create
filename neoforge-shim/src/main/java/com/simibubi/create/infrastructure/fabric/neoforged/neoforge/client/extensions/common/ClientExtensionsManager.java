package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.common;

import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidType;

/**
 * Client extensions registered through {@link RegisterClientExtensionsEvent}, looked up by the
 * {@code IClient*Extensions.of} helpers.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code ClientExtensionsManager} for Fabric (PORTING.md D7).
 */
public final class ClientExtensionsManager {
	public static final Map<Block, IClientBlockExtensions> BLOCK_EXTENSIONS = new IdentityHashMap<>();
	public static final Map<Item, IClientItemExtensions> ITEM_EXTENSIONS = new IdentityHashMap<>();
	public static final Map<FluidType, IClientFluidTypeExtensions> FLUID_TYPE_EXTENSIONS = new IdentityHashMap<>();

	private ClientExtensionsManager() {}
}
