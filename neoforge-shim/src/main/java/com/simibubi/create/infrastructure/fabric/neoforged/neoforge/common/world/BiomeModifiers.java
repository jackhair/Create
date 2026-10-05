package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * The NeoForge biome modifier records Create uses (see {@link BiomeModifier}).
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public final class BiomeModifiers {
	private BiomeModifiers() {}

	public record AddFeaturesBiomeModifier(HolderSet<Biome> biomes, HolderSet<PlacedFeature> features, Decoration step) implements BiomeModifier {
		public static final MapCodec<AddFeaturesBiomeModifier> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
			RegistryCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(AddFeaturesBiomeModifier::biomes),
			PlacedFeature.LIST_CODEC.fieldOf("features").forGetter(AddFeaturesBiomeModifier::features),
			Decoration.CODEC.fieldOf("step").forGetter(AddFeaturesBiomeModifier::step)
		).apply(builder, AddFeaturesBiomeModifier::new));

		@Override
		public MapCodec<? extends BiomeModifier> codec() {
			return CODEC;
		}
	}
}
