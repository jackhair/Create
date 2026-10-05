package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/**
 * The parts of NeoForge's {@code IScreenExtension} Create uses, injected into {@link Screen}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IScreenExtension {
	default Minecraft getMinecraft() {
		return ((Screen) this).minecraft;
	}
}
