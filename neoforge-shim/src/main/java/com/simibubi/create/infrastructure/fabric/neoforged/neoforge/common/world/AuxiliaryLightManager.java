package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.world;

import net.minecraft.core.BlockPos;

/**
 * NeoForge's per-position light emission API. Fabric has no equivalent, so {@code getAuxLightManager} returns
 * null and callers fall back to state-based light (copycats don't emit their material's light yet).
 * <p>
 * Create-owned re-implementation of NeoForge's {@code AuxiliaryLightManager} for Fabric (PORTING.md D7).
 */
public interface AuxiliaryLightManager {
	void setLightAt(BlockPos pos, int value);

	int getLightAt(BlockPos pos);
}
