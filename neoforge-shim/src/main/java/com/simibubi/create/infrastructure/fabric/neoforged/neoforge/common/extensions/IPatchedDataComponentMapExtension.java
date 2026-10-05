package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.core.component.PatchedDataComponentMap;

/**
 * The parts of NeoForge's {@code IPatchedDataComponentMapExtension} Create uses, injected into {@link PatchedDataComponentMap}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IPatchedDataComponentMapExtension {
	default boolean isPatchEmpty() {
		return ((PatchedDataComponentMap) this).asPatch().isEmpty();
	}
}
