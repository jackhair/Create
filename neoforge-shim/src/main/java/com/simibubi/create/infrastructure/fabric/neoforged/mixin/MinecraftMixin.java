package com.simibubi.create.infrastructure.fabric.neoforged.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.extensions.IMinecraftExtension;

/** Makes {@link net.minecraft.client.Minecraft} implement NeoForge's {@link IMinecraftExtension}; Loom injects it at compile time. */
@Mixin(net.minecraft.client.Minecraft.class)
public abstract class MinecraftMixin implements IMinecraftExtension {}
