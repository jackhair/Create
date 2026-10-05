package com.simibubi.create.compat.thresholdSwitch;

import net.minecraft.world.level.block.entity.BlockEntity;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.items.IItemHandler;

public interface ThresholdSwitchCompat {

	boolean isFromThisMod(BlockEntity blockEntity);

	long getSpaceInSlot(IItemHandler inv, int slot);

}
