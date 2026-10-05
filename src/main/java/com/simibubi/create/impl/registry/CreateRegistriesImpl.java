package com.simibubi.create.impl.registry;

import com.simibubi.create.api.equipment.potatoCannon.PotatoCannonProjectileType;
import com.simibubi.create.api.registry.CreateRegistries;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.SubscribeEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.common.EventBusSubscriber;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber
public class CreateRegistriesImpl {
	@SubscribeEvent
	public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(
			CreateRegistries.POTATO_PROJECTILE_TYPE,
			PotatoCannonProjectileType.CODEC,
			PotatoCannonProjectileType.CODEC
		);
	}
}
