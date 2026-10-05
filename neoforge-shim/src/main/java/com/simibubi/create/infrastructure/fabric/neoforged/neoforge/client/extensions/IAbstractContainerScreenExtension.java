package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * The parts of NeoForge's {@code IAbstractContainerScreenExtension} Create uses, injected into {@link AbstractContainerScreen}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IAbstractContainerScreenExtension {
	private AbstractContainerScreen<?> self() {
		return (AbstractContainerScreen<?>) this;
	}

	default int getGuiLeft() {
		return self().leftPos;
	}

	default int getGuiTop() {
		return self().topPos;
	}

	default int getXSize() {
		return self().imageWidth;
	}

	default int getYSize() {
		return self().imageHeight;
	}
}
