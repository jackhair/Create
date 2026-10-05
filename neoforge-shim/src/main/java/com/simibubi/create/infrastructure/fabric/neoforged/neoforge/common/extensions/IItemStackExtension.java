package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.ItemAbility;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.ItemStack;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.ItemCapability;

/**
 * The parts of NeoForge's {@code IItemStackExtension} Create uses, injected into {@link ItemStack}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IItemStackExtension {
	private ItemStack self() {
		return (ItemStack) this;
	}

	@Nullable
	default <T, C> T getCapability(ItemCapability<T, C> capability, C context) {
		return capability.getCapability(self(), context);
	}

	@Nullable
	default <T> T getCapability(ItemCapability<T, @Nullable Void> capability) {
		return capability.getCapability(self(), null);
	}

	default ItemStack getCraftingRemainingItem() {
		return self().getItem().getCraftingRemainingItem(self());
	}

	default boolean hasCraftingRemainingItem() {
		return self().getItem().hasCraftingRemainingItem(self());
	}

	default ItemEnchantments getTagEnchantments() {
		return self().getEnchantments();
	}

	default boolean isComponentsPatchEmpty() {
		return self().getComponentsPatch().isEmpty();
	}

	default int getEnchantmentLevel(Holder<Enchantment> enchantment) {
		return self().getItem().getEnchantmentLevel(self(), enchantment);
	}

	/** fabric: burn times come from Fabric's FuelRegistry; the recipe type is ignored. */
	default int getBurnTime(@Nullable RecipeType<?> recipeType) {
		Integer time = FuelRegistry.INSTANCE.get(self().getItem());
		return time == null ? 0 : time;
	}

	default boolean supportsEnchantment(Holder<Enchantment> enchantment) {
		return self().getItem().supportsEnchantment(self(), enchantment);
	}

	default InteractionResult onItemUseFirst(UseOnContext context) {
		return self().getItem().onItemUseFirst(self(), context);
	}

	default ItemAttributeModifiers getAttributeModifiers() {
		return self().getItem().getDefaultAttributeModifiers(self());
	}

	default boolean doesSneakBypassUse(LevelReader level, BlockPos pos, Player player) {
		return self().isEmpty() || self().getItem().doesSneakBypassUse(self(), level, pos, player);
	}

	default boolean canPerformAction(ItemAbility itemAbility) {
		return self().getItem().canPerformAction(self(), itemAbility);
	}
}
