package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions;

import net.minecraft.client.renderer.RenderType;

/**
 * The parts of NeoForge's {@code IRenderTypeExtension} Create uses, injected into {@link RenderType}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IRenderTypeExtension {
	/** Index among the chunk layers, or -1. */
	default int getChunkLayerId() {
		return RenderType.chunkBufferLayers().indexOf((RenderType) this);
	}
}
