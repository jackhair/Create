package com.simibubi.create.content.logistics.box;

import com.simibubi.create.foundation.mixin.accessor.MinecraftAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.fabricmc.api.EnvType;
import com.simibubi.create.infrastructure.fabric.neoforged.api.distmarker.Dist;
import net.fabricmc.api.Environment;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.SubscribeEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.common.EventBusSubscriber;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.entity.player.AttackEntityEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class PackageClientInteractionHandler {

	// In vanilla, punching an entity doesnt reset the attack timer. This leads to
	// accidentally breaking blocks behind an armorstand or package when punching it
	// in creative mode

	@SubscribeEvent
	@Environment(EnvType.CLIENT)
	public static void onPlayerPunchPackage(AttackEntityEvent event) {
		Player attacker = event.getEntity();
		if (!attacker.level()
			.isClientSide())
			return;
		Minecraft mc = Minecraft.getInstance();
		if (attacker != mc.player)
			return;
		if (!(event.getTarget() instanceof PackageEntity))
			return;
		((MinecraftAccessor) mc).create$setMissTime(10);
	}

}
