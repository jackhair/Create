package com.simibubi.create.infrastructure.fabric.neoforged.fml;

import java.util.function.Supplier;

/**
 * NeoForge's inter-mod messaging. Fabric has no equivalent and the NeoForge-only mods Create messages aren't on
 * Fabric, so messages are dropped. Create-owned re-implementation (PORTING.md D7).
 */
public final class InterModComms {
	private InterModComms() {}

	public static boolean sendTo(String modId, String method, Supplier<?> thing) {
		return false;
	}

	public static boolean sendTo(String senderModId, String modId, String method, Supplier<?> thing) {
		return false;
	}
}
