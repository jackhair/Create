package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import net.minecraft.resources.ResourceLocation;

/**
 * Biome modifiers as datagen records only. On Fabric, Create's entrypoint reads the generated
 * {@code data/create/neoforge/biome_modifier/*.json} files and applies them with Fabric's BiomeModifications.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code BiomeModifier} for Fabric (PORTING.md D7).
 */
public interface BiomeModifier {
	Codec<BiomeModifier> DIRECT_CODEC = BiomeModifiers.AddFeaturesBiomeModifier.CODEC.codec().xmap(m -> m, m -> (BiomeModifiers.AddFeaturesBiomeModifier) m);

	MapCodec<? extends BiomeModifier> codec();

	static ResourceLocation type(String path) {
		return ResourceLocation.fromNamespaceAndPath("neoforge", path);
	}
}
