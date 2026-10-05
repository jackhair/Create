package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

/**
 * Holds GUI layers registered through {@code RegisterGuiLayersEvent}. Vanilla's layers are present as no-op
 * placeholders so mods can position theirs relative to them; Create's Fabric client entrypoint renders the
 * modded layers in order after the vanilla HUD (Fabric's {@code HudRenderCallback}).
 * <p>
 * Create-owned re-implementation of NeoForge's {@code GuiLayerManager} for Fabric (PORTING.md D7).
 */
public final class GuiLayerManager {
	private final List<NamedLayer> layers = new ArrayList<>();

	public GuiLayerManager() {
		for (ResourceLocation vanilla : VanillaGuiLayers.ALL)
			layers.add(new NamedLayer(vanilla, VANILLA_PLACEHOLDER));
	}

	public List<NamedLayer> layers() {
		return layers;
	}

	public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
		for (NamedLayer layer : layers)
			if (layer.layer() != VANILLA_PLACEHOLDER)
				layer.layer().render(graphics, deltaTracker);
	}

	private static final LayeredDraw.Layer VANILLA_PLACEHOLDER = (graphics, deltaTracker) -> {};

	public record NamedLayer(ResourceLocation name, LayeredDraw.Layer layer) {}
}
