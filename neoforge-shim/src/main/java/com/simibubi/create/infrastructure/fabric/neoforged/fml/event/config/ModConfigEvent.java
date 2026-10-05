package com.simibubi.create.infrastructure.fabric.neoforged.fml.event.config;

import net.neoforged.fml.config.ModConfig;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;

/**
 * Config load/reload events, fired on Create's mod bus from Forge Config API Port's
 * {@code NeoForgeModConfigEvents} by Create's Fabric entrypoint.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code ModConfigEvent} for Fabric (PORTING.md D7).
 */
public abstract class ModConfigEvent extends Event implements IModBusEvent {
	private final ModConfig config;

	protected ModConfigEvent(ModConfig config) {
		this.config = config;
	}

	public ModConfig getConfig() {
		return config;
	}

	public static class Loading extends ModConfigEvent {
		public Loading(ModConfig config) {
			super(config);
		}
	}

	public static class Reloading extends ModConfigEvent {
		public Reloading(ModConfig config) {
			super(config);
		}
	}

	public static class Unloading extends ModConfigEvent {
		public Unloading(ModConfig config) {
			super(config);
		}
	}
}
