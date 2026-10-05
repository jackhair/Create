package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

/**
 * NeoForge's per-quad ambient occlusion flag. Vanilla quads always allow AO, so this is always true until the
 * model layer reads NeoForge's per-face data (PORTING.md 1d). Injected into {@code BakedQuad}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IBakedQuadExtension {
	default boolean hasAmbientOcclusion() {
		return true;
	}
}
