package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.datamaps;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.DataMapLoader;

/**
 * Registers data map types with {@link DataMapLoader}. Fired once by Create's Fabric entrypoint.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code RegisterDataMapTypesEvent} for Fabric (PORTING.md D7).
 */
public class RegisterDataMapTypesEvent extends Event implements IModBusEvent {
	public <T, R> void register(DataMapType<R, T> type) {
		DataMapLoader.register(type);
	}
}
