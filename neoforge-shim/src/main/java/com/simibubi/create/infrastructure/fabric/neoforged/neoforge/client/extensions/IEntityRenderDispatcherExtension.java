package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

import java.util.Map;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;

/**
 * The parts of NeoForge's {@code IEntityRenderDispatcherExtension} Create uses, injected into {@link EntityRenderDispatcher}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IEntityRenderDispatcherExtension {
	default Map<PlayerSkin.Model, EntityRenderer<? extends Player>> getSkinMap() {
		return ((EntityRenderDispatcher) this).playerRenderers;
	}
}
