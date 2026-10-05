package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries;

import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.attachment.AttachmentType;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.world.BiomeModifier;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.crafting.IngredientType;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidType;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting.FluidIngredientType;

/**
 * The NeoForge registries Create uses, as Fabric registries under NeoForge's ids so registry keys and data
 * match upstream. Created and added to the root registry by {@link RegistrationPhase}.
 * <p>
 * Create-owned re-implementation of NeoForge's {@code NeoForgeRegistries} for Fabric (PORTING.md D7).
 */
public class NeoForgeRegistries {
	public static final Registry<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = new RegistryBuilder<>(Keys.ENTITY_DATA_SERIALIZERS)
		// fabric: vanilla keeps its own serializer id map; mirror registrations into it (same order on both sides)
		.onAdd((registry, id, key, serializer) -> EntityDataSerializers.registerSerializer(serializer))
		.create();
	public static final Registry<FluidType> FLUID_TYPES = new RegistryBuilder<>(Keys.FLUID_TYPES).sync(true).create();
	public static final Registry<IngredientType<?>> INGREDIENT_TYPES = new RegistryBuilder<>(Keys.INGREDIENT_TYPES).sync(true).create();
	public static final Registry<FluidIngredientType<?>> FLUID_INGREDIENT_TYPES = new RegistryBuilder<>(Keys.FLUID_INGREDIENT_TYPES).sync(true).create();
	public static final Registry<AttachmentType<?>> ATTACHMENT_TYPES = new RegistryBuilder<>(Keys.ATTACHMENT_TYPES)
		// fabric: each NeoForge attachment type is backed by a Fabric attachment with the same id
		.onAdd((registry, id, key, type) -> type.bind(key.location()))
		.create();

	static void registerAll(NewRegistryEvent event) {
		event.register(ENTITY_DATA_SERIALIZERS);
		event.register(FLUID_TYPES);
		event.register(INGREDIENT_TYPES);
		event.register(FLUID_INGREDIENT_TYPES);
		event.register(ATTACHMENT_TYPES);
	}

	public static final class Keys {
		public static final ResourceKey<Registry<EntityDataSerializer<?>>> ENTITY_DATA_SERIALIZERS = key("entity_data_serializers");
		public static final ResourceKey<Registry<FluidType>> FLUID_TYPES = key("fluid_type");
		public static final ResourceKey<Registry<IngredientType<?>>> INGREDIENT_TYPES = key("ingredient_serializer");
		public static final ResourceKey<Registry<FluidIngredientType<?>>> FLUID_INGREDIENT_TYPES = key("fluid_ingredient_type");
		public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES = key("attachment_types");
		/** Datagen only on Fabric: biome modifiers are applied in code with Fabric's BiomeModifications. */
		public static final ResourceKey<Registry<BiomeModifier>> BIOME_MODIFIERS = key("biome_modifier");

		private static <T> ResourceKey<Registry<T>> key(String name) {
			return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("neoforge", name));
		}
	}
}
