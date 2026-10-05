package com.simibubi.create.infrastructure.fabric.compat.ponder;

import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.lib.model.baked.BakedModelBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.model.data.ModelData;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.model.data.ModelProperty;

/**
 * Stands in for Ponder's NeoForge-only {@code com.simibubi.create.infrastructure.fabric.compat.ponder.VirtualRenderHelper}: model data that
 * marks a block as rendered inside a Ponder scene. Fabric's Ponder doesn't pass model data yet, so nothing is
 * marked virtual until the model layer is ported (PORTING.md 1d).
 */
public final class VirtualRenderHelper {
	public static final ModelProperty<Boolean> VIRTUAL_PROPERTY = new ModelProperty<>();
	public static final ModelData VIRTUAL_DATA = ModelData.builder().with(VIRTUAL_PROPERTY, true).build();

	private VirtualRenderHelper() {}

	public static boolean isVirtual(ModelData data) {
		return data.has(VIRTUAL_PROPERTY) && Boolean.TRUE.equals(data.get(VIRTUAL_PROPERTY));
	}

	public static Model blockModel(BlockState state) {
		return new BakedModelBuilder(Minecraft.getInstance().getBlockRenderer().getBlockModel(state)).build();
	}
}
