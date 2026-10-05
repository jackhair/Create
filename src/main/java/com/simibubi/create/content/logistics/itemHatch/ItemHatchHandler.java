package com.simibubi.create.content.logistics.itemHatch;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;

import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.EventPriority;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.SubscribeEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.common.EventBusSubscriber;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.util.TriState;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;


@EventBusSubscriber(modid = Create.ID)
public class ItemHatchHandler {

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void useOnItemHatchIgnoresSneak(RightClickBlock event) {
		if (event.getUseItem() == TriState.DEFAULT && AllBlocks.ITEM_HATCH.has(event.getLevel()
			.getBlockState(event.getPos())))
			event.setUseBlock(TriState.TRUE);
	}

}
