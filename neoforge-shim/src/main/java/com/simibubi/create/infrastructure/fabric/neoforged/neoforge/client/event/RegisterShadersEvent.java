// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.event;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.ICancellableEvent;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.LogicalSide;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired to allow mods to register custom {@linkplain ShaderInstance shaders}.
 * This event is fired after the default Minecraft shaders have been registered.
 *
 * <p>This event is not {@linkplain ICancellableEvent cancellable}, and does not {@linkplain HasResult have a result}.</p>
 *
 * <p>This event is fired on the mod-specific event bus, only on the {@linkplain LogicalSide#CLIENT logical client}.</p>
 */
public class RegisterShadersEvent extends Event implements IModBusEvent {
    private final ResourceProvider resourceProvider;
    private final List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderList;

    @ApiStatus.Internal
    public RegisterShadersEvent(ResourceProvider resourceProvider, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderList) {
        this.resourceProvider = resourceProvider;
        this.shaderList = shaderList;
    }

    /**
     * {@return the client-side resource provider}
     */
    public ResourceProvider getResourceProvider() {
        return resourceProvider;
    }

    /**
     * Registers a shader, and a callback for when the shader is loaded.
     *
     * <p>When creating a {@link ShaderInstance}, pass in the {@linkplain #getResourceProvider()
     * client-side resource provider} as the resource provider.</p>
     *
     * <p>Mods should not store the shader instance passed into this method. Instead, mods should store the shader
     * passed into the registered load callback.</p>
     *
     * @param shaderInstance a shader
     * @param onLoaded       a callback for when the shader is loaded
     */
    public void registerShader(ShaderInstance shaderInstance, Consumer<ShaderInstance> onLoaded) {
        shaderList.add(Pair.of(shaderInstance, onLoaded));
    }
}
