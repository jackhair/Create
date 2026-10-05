package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IStructureProcessorExtension;

/** Makes {@link net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor} implement NeoForge's {@link IStructureProcessorExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor.class)
public abstract class StructureProcessorMixin implements IStructureProcessorExtension {}
