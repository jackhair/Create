package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

/**
 * The parts of NeoForge's {@code ILivingEntityExtension} Create uses, injected into {@link net.minecraft.world.entity.LivingEntity}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface ILivingEntityExtension {
	/** Effects have no NeoForge cures on Fabric; milk clears everything, as in vanilla. */
	default boolean removeEffectsCuredBy(com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.EffectCure cure) {
		if (cure == com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.EffectCures.MILK)
			return ((net.minecraft.world.entity.LivingEntity) this).removeAllEffects();
		return false;
	}
}
