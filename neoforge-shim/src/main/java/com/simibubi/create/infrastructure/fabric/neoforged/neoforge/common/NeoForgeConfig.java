package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common;

/**
 * Stand-in for the NeoForge config values Create touches. Fabric has no NeoForge light pipeline, so setting
 * it does nothing. Create-owned (PORTING.md D7).
 */
public final class NeoForgeConfig {
	public static final Client CLIENT = new Client();

	private NeoForgeConfig() {}

	public static final class Client {
		public final Value experimentalForgeLightPipelineEnabled = new Value();
	}

	public static final class Value {
		public boolean get() {
			return false;
		}

		public void set(boolean value) {}
	}
}
