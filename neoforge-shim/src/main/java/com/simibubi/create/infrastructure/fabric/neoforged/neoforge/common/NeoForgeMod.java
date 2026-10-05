// fabric: derived from NeoForge 21.1.219's NeoForgeMod (LGPL-2.1-only), reduced to what Create uses; see PORTING.md D7
package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common;

import org.jetbrains.annotations.Nullable;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.IEventBus;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.BaseFlowingFluid;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.FluidType;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting.CompoundFluidIngredient;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting.DataComponentFluidIngredient;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting.DifferenceFluidIngredient;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting.EmptyFluidIngredient;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting.FluidIngredientType;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting.IntersectionFluidIngredient;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting.SingleFluidIngredient;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.fluids.crafting.TagFluidIngredient;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.DeferredHolder;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.DeferredRegister;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.NeoForgeRegistries;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.RegisterEvent;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

/**
 * The parts of NeoForge's mod class Create uses: vanilla fluid types, the optional milk fluid and the
 * built-in fluid ingredient types, registered under NeoForge's ids so data matches upstream.
 * {@link #register(IEventBus)} is called by Create's Fabric entrypoint before the registration phase.
 */
public class NeoForgeMod {
	private static final DeferredRegister<FluidIngredientType<?>> FLUID_INGREDIENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES, "neoforge");

	public static final DeferredHolder<FluidIngredientType<?>, FluidIngredientType<SingleFluidIngredient>> SINGLE_FLUID_INGREDIENT_TYPE = FLUID_INGREDIENT_TYPES.register("single", () -> new FluidIngredientType<>(SingleFluidIngredient.CODEC));
	public static final DeferredHolder<FluidIngredientType<?>, FluidIngredientType<TagFluidIngredient>> TAG_FLUID_INGREDIENT_TYPE = FLUID_INGREDIENT_TYPES.register("tag", () -> new FluidIngredientType<>(TagFluidIngredient.CODEC));
	public static final DeferredHolder<FluidIngredientType<?>, FluidIngredientType<EmptyFluidIngredient>> EMPTY_FLUID_INGREDIENT_TYPE = FLUID_INGREDIENT_TYPES.register("empty", () -> new FluidIngredientType<>(EmptyFluidIngredient.CODEC));
	public static final DeferredHolder<FluidIngredientType<?>, FluidIngredientType<CompoundFluidIngredient>> COMPOUND_FLUID_INGREDIENT_TYPE = FLUID_INGREDIENT_TYPES.register("compound", () -> new FluidIngredientType<>(CompoundFluidIngredient.CODEC));
	public static final DeferredHolder<FluidIngredientType<?>, FluidIngredientType<DataComponentFluidIngredient>> DATA_COMPONENT_FLUID_INGREDIENT_TYPE = FLUID_INGREDIENT_TYPES.register("components", () -> new FluidIngredientType<>(DataComponentFluidIngredient.CODEC));
	public static final DeferredHolder<FluidIngredientType<?>, FluidIngredientType<DifferenceFluidIngredient>> DIFFERENCE_FLUID_INGREDIENT_TYPE = FLUID_INGREDIENT_TYPES.register("difference", () -> new FluidIngredientType<>(DifferenceFluidIngredient.CODEC));
	public static final DeferredHolder<FluidIngredientType<?>, FluidIngredientType<IntersectionFluidIngredient>> INTERSECTION_FLUID_INGREDIENT_TYPE = FLUID_INGREDIENT_TYPES.register("intersection", () -> new FluidIngredientType<>(IntersectionFluidIngredient.CODEC));

	private static final DeferredRegister<FluidType> VANILLA_FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, "minecraft");

	public static final Holder<FluidType> EMPTY_TYPE = VANILLA_FLUID_TYPES.register("empty", () -> new FluidType(FluidType.Properties.create()
		.descriptionId("block.minecraft.air")
		.motionScale(1D)
		.canPushEntity(false)
		.canSwim(false)
		.canDrown(false)
		.fallDistanceModifier(1F)
		.pathType(null)
		.adjacentPathType(null)
		.density(0)
		.temperature(0)
		.viscosity(0)) {
		@Override
		public void setItemMovement(ItemEntity entity) {
			if (!entity.isNoGravity()) entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
		}
	});
	public static final Holder<FluidType> WATER_TYPE = VANILLA_FLUID_TYPES.register("water", () -> new FluidType(FluidType.Properties.create()
		.descriptionId("block.minecraft.water")
		.fallDistanceModifier(0F)
		.canExtinguish(true)
		.canConvertToSource(true)
		.supportsBoating(true)
		.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
		.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
		.sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
		.canHydrate(true)
		.addDripstoneDripping(PointedDripstoneBlock.WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK, ParticleTypes.DRIPPING_DRIPSTONE_WATER, Blocks.WATER_CAULDRON, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON)) {
		@Override
		public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
			if (reader instanceof Level level)
				return level.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
			return super.canConvertToSource(state, reader, pos);
		}

		@Override
		public @Nullable PathType getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog) {
			return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
		}
	});
	public static final Holder<FluidType> LAVA_TYPE = VANILLA_FLUID_TYPES.register("lava", () -> new FluidType(FluidType.Properties.create()
		.descriptionId("block.minecraft.lava")
		.canSwim(false)
		.canDrown(false)
		.pathType(PathType.LAVA)
		.adjacentPathType(null)
		.sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
		.sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
		.lightLevel(15)
		.density(3000)
		.viscosity(6000)
		.temperature(1300)
		.addDripstoneDripping(PointedDripstoneBlock.LAVA_TRANSFER_PROBABILITY_PER_RANDOM_TICK, ParticleTypes.DRIPPING_DRIPSTONE_LAVA, Blocks.LAVA_CAULDRON, SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON)) {
		@Override
		public boolean canConvertToSource(FluidState state, LevelReader reader, BlockPos pos) {
			if (reader instanceof Level level)
				return level.getGameRules().getBoolean(GameRules.RULE_LAVA_SOURCE_CONVERSION);
			return super.canConvertToSource(state, reader, pos);
		}

		@Override
		public double motionScale(Entity entity) {
			return entity.level().dimensionType().ultraWarm() ? 0.007D : 0.0023333333333333335D;
		}

		@Override
		public void setItemMovement(ItemEntity entity) {
			Vec3 vec3 = entity.getDeltaMovement();
			entity.setDeltaMovement(vec3.x * (double) 0.95F, vec3.y + (double) (vec3.y < (double) 0.06F ? 5.0E-4F : 0.0F), vec3.z * (double) 0.95F);
		}

		@Override
		public boolean move(FluidState state, LivingEntity entity, Vec3 movementVector, double gravity) {
			return true;
		}
	});

	private static boolean enableMilkFluid = false;

	public static final DeferredHolder<SoundEvent, SoundEvent> BUCKET_EMPTY_MILK = DeferredHolder.create(Registries.SOUND_EVENT, ResourceLocation.withDefaultNamespace("item.bucket.empty_milk"));
	public static final DeferredHolder<SoundEvent, SoundEvent> BUCKET_FILL_MILK = DeferredHolder.create(Registries.SOUND_EVENT, ResourceLocation.withDefaultNamespace("item.bucket.fill_milk"));
	public static final DeferredHolder<FluidType, FluidType> MILK_TYPE = DeferredHolder.create(NeoForgeRegistries.Keys.FLUID_TYPES, ResourceLocation.withDefaultNamespace("milk"));
	public static final DeferredHolder<Fluid, Fluid> MILK = DeferredHolder.create(Registries.FLUID, ResourceLocation.withDefaultNamespace("milk"));
	public static final DeferredHolder<Fluid, Fluid> FLOWING_MILK = DeferredHolder.create(Registries.FLUID, ResourceLocation.withDefaultNamespace("flowing_milk"));

	/**
	 * Run this during mod construction to enable milk and add it to the Minecraft milk bucket.
	 */
	public static void enableMilkFluid() {
		enableMilkFluid = true;
	}

	public static void register(IEventBus modBus) {
		FLUID_INGREDIENT_TYPES.register(modBus);
		VANILLA_FLUID_TYPES.register(modBus);
		modBus.addListener(RegisterEvent.class, NeoForgeMod::registerFluids);
	}

	private static void registerFluids(RegisterEvent event) {
		if (!enableMilkFluid)
			return;

		event.register(Registries.SOUND_EVENT, helper -> {
			helper.register(BUCKET_EMPTY_MILK.getId(), SoundEvent.createVariableRangeEvent(BUCKET_EMPTY_MILK.getId()));
			helper.register(BUCKET_FILL_MILK.getId(), SoundEvent.createVariableRangeEvent(BUCKET_FILL_MILK.getId()));
		});

		event.register(NeoForgeRegistries.Keys.FLUID_TYPES, helper -> helper.register(MILK_TYPE.unwrapKey().orElseThrow(), new FluidType(
			FluidType.Properties.create().density(1024).viscosity(1024)
				.sound(SoundActions.BUCKET_FILL, BUCKET_FILL_MILK.value())
				.sound(SoundActions.BUCKET_EMPTY, BUCKET_EMPTY_MILK.value()))));

		event.register(Registries.FLUID, helper -> {
			BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(MILK_TYPE::value, MILK::value, FLOWING_MILK::value).bucket(() -> Items.MILK_BUCKET);
			helper.register(MILK.getId(), new BaseFlowingFluid.Source(properties));
			helper.register(FLOWING_MILK.getId(), new BaseFlowingFluid.Flowing(properties));
		});
	}
}
