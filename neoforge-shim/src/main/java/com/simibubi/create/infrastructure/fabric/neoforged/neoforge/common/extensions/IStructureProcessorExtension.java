package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions;

/**
 * The parts of NeoForge's {@code IStructureProcessorExtension} Create uses, injected into {@link net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor}.
 * Create-owned re-implementation for Fabric (PORTING.md D7).
 */
public interface IStructureProcessorExtension {
	/** NeoForge's processor hook with the template; called by the shim's StructureTemplate mixin. */
	@org.jetbrains.annotations.Nullable
	default net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo process(net.minecraft.world.level.LevelReader level, net.minecraft.core.BlockPos offset, net.minecraft.core.BlockPos pos, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo blockInfo, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo relativeBlockInfo, net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings settings, @org.jetbrains.annotations.Nullable net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate template) {
		return ((net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor) this).processBlock(level, offset, pos, blockInfo, relativeBlockInfo, settings);
	}

	default net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureEntityInfo processEntity(net.minecraft.world.level.LevelReader level, net.minecraft.core.BlockPos seedPos, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureEntityInfo rawEntityInfo, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureEntityInfo entityInfo, net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings placementSettings, net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate template) {
		return entityInfo;
	}
}
