package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * The NeoForge {@code ClientHooks} Create calls. Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public final class ClientHooks {
	private ClientHooks() {}

	public static BakedModel handleCameraTransforms(PoseStack poseStack, BakedModel model, ItemDisplayContext context, boolean applyLeftHandTransform) {
		model.getTransforms().getTransform(context).apply(applyLeftHandTransform, poseStack);
		return model;
	}

	/** Vanilla's tooltip assembly; NeoForge's line wrapping and tooltip events aren't reproduced. */
	public static List<ClientTooltipComponent> gatherTooltipComponents(ItemStack stack, List<? extends FormattedText> textElements, int mouseX, int screenWidth, int screenHeight, Font fallbackFont) {
		List<ClientTooltipComponent> components = new ArrayList<>();
		for (FormattedText text : textElements)
			components.add(ClientTooltipComponent.create(Language.getInstance().getVisualOrder(text)));
		stack.getTooltipImage().ifPresent(image -> components.add(Math.min(1, components.size()), ClientTooltipComponent.create(image)));
		return components;
	}

	public static boolean isBlockInSolidLayer(net.minecraft.world.level.block.state.BlockState state) {
		return net.minecraft.client.renderer.ItemBlockRenderTypes.getChunkRenderType(state) == net.minecraft.client.renderer.RenderType.solid();
	}
}
