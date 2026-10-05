package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.settings.KeyModifier;
import net.minecraft.client.KeyMapping;

/**
 * The parts of NeoForge's {@code IKeyMappingExtension} Create uses, injected into {@link KeyMapping}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IKeyMappingExtension {
	private KeyMapping self() {
		return (KeyMapping) this;
	}

	default InputConstants.Key getKey() {
		return self().key;
	}

	default KeyModifier getKeyModifier() {
		return KeyModifier.NONE;
	}

	/** Fabric has no key conflict contexts or modifiers, so this is a plain key comparison. */
	default boolean isActiveAndMatches(InputConstants.Key keyCode) {
		return keyCode != InputConstants.UNKNOWN && keyCode.equals(getKey());
	}
}
