package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.data.DataGenerator;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.extensions.IDataGeneratorExtension;

/** Makes {@link DataGenerator} implement NeoForge's {@link IDataGeneratorExtension}; Loom injects it at compile time. */
@Mixin(DataGenerator.class)
public abstract class DataGeneratorMixin implements IDataGeneratorExtension {}
