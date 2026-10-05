package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * The parts of NeoForge's {@code ITooltipContextExtension} Create uses, injected into {@link Item.TooltipContext}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface ITooltipContextExtension {
	/** Vanilla's tooltip context doesn't carry the level; NeoForge's does when one exists. */
	@Nullable
	default Level level() {
		return null;
	}
}
