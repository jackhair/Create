package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * An item capability is a Fabric {@link ItemApiLookup}.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code ItemCapability} for Fabric (PORTING.md D7).
 */
public final class ItemCapability<T, C> extends BaseCapability<T, C> {
	private final ItemApiLookup<T, C> lookup;

	private ItemCapability(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
		super(name, typeClass, contextClass);
		this.lookup = ItemApiLookup.get(name, typeClass, contextClass);
	}

	public static <T, C> ItemCapability<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
		return new ItemCapability<>(name, typeClass, contextClass);
	}

	public static <T> ItemCapability<T, @Nullable Void> createVoid(ResourceLocation name, Class<T> typeClass) {
		return create(name, typeClass, Void.class);
	}

	public ItemApiLookup<T, C> lookup() {
		return lookup;
	}

	@Nullable
	public T getCapability(ItemStack stack, C context) {
		return stack.isEmpty() ? null : lookup.find(stack, context);
	}
}
