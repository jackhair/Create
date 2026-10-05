package com.simibubi.create.infrastructure.fabric.compat.computercraft;

import org.jetbrains.annotations.Nullable;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.minecraft.core.Direction;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.capabilities.BlockCapability;

/**
 * CC: Tweaked's peripheral capability on Fabric: a {@link BlockCapability} over CC's {@link PeripheralLookup}.
 * Stands in for NeoForge CC's {@code com.simibubi.create.infrastructure.fabric.compat.computercraft.PeripheralCapability}; only call it
 * when ComputerCraft is loaded.
 */
public final class PeripheralCapability {
	private static BlockCapability<IPeripheral, @Nullable Direction> capability;

	private PeripheralCapability() {}

	public static synchronized BlockCapability<IPeripheral, @Nullable Direction> get() {
		if (capability == null)
			capability = BlockCapability.of(PeripheralLookup.get());
		return capability;
	}
}
