package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

import java.util.Locale;
import net.minecraft.client.Minecraft;

/**
 * The parts of NeoForge's {@code IMinecraftExtension} Create uses, injected into {@link Minecraft}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IMinecraftExtension {
	default Locale getLocale() {
		String[] code = ((Minecraft) this).getLanguageManager().getSelected().split("_", 2);
		return code.length == 1 ? Locale.forLanguageTag(code[0]) : Locale.of(code[0], code[1]);
	}
}
