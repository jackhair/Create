package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.NeoForgeMod;

/**
 * Resolves {@link FluidType}s for fluids that don't define one: vanilla fluids get NeoForge's water, lava and
 * empty types; other mods' fluids get a type derived from Fabric's {@link FluidVariantAttributes}.
 * <p>
 * Create code (PORTING.md D7), not a NeoForge class.
 */
public final class FluidTypeLookup {
	private static final Map<Fluid, FluidType> DERIVED = new ConcurrentHashMap<>();

	private FluidTypeLookup() {}

	public static FluidType of(Fluid fluid) {
		if (fluid == Fluids.EMPTY)
			return NeoForgeMod.EMPTY_TYPE.value();
		if (fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER)
			return NeoForgeMod.WATER_TYPE.value();
		if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA)
			return NeoForgeMod.LAVA_TYPE.value();
		return DERIVED.computeIfAbsent(fluid, FluidTypeLookup::derive);
	}

	private static FluidType derive(Fluid fluid) {
		FluidVariant variant = FluidVariant.of(fluid);
		FluidType.Properties properties = FluidType.Properties.create()
			// Fabric's viscosity ratio for water is 5 and NeoForge's viscosity for water is 1000
			.viscosity(FluidVariantAttributes.getViscosity(variant, null) * (1000 / FluidConstants.WATER_VISCOSITY))
			.temperature(FluidVariantAttributes.getTemperature(variant))
			.lightLevel(FluidVariantAttributes.getLuminance(variant))
			.density(FluidVariantAttributes.isLighterThanAir(variant) ? -1000 : 1000);
		Component name = FluidVariantAttributes.getName(variant);
		if (name.getContents() instanceof TranslatableContents translatable)
			properties.descriptionId(translatable.getKey());
		return new FluidType(properties);
	}
}
