// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.event;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.NeoForge;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.conditions.ICondition;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.resource.ContextAwareReloadListener;

/**
 * The main ResourceManager is recreated on each reload, just after {@link ReloadableServerResources}'s creation.
 *
 * The event is fired on each reload and lets modders add their own ReloadListeners, for server-side resources.
 * The event is fired on the {@link NeoForge#EVENT_BUS}
 */
public class AddReloadListenerEvent extends Event {
    private final List<PreparableReloadListener> listeners = new ArrayList<>();
    private final ReloadableServerResources serverResources;
    private final RegistryAccess registryAccess;

    public AddReloadListenerEvent(ReloadableServerResources serverResources, RegistryAccess registryAccess) {
        this.serverResources = serverResources;
        this.registryAccess = registryAccess;
    }

    /**
     * @param listener the listener to add to the ResourceManager on reload
     */
    public void addListener(PreparableReloadListener listener) {
        listeners.add(new WrappedStateAwareListener(listener));
    }

    public List<PreparableReloadListener> getListeners() {
        return ImmutableList.copyOf(listeners);
    }

    /**
     * @return The ReloableServerResources being reloaded.
     */
    public ReloadableServerResources getServerResources() {
        return serverResources;
    }

    // fabric: getConditionContext removed; conditions are Fabric resource conditions

    /**
     * Provides access to the loaded registries associated with these server resources.
     * All built-in and dynamic registries are loaded and frozen by this point.
     * 
     * @return The RegistryAccess context for the currently active reload.
     */
    public RegistryAccess getRegistryAccess() {
        return registryAccess;
    }

    private static class WrappedStateAwareListener extends ContextAwareReloadListener implements PreparableReloadListener {
        private final PreparableReloadListener wrapped;

        private WrappedStateAwareListener(final PreparableReloadListener wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void injectContext(ICondition.IContext context, HolderLookup.Provider registryLookup) {
            if (this.wrapped instanceof ContextAwareReloadListener contextAwareListener) {
                contextAwareListener.injectContext(context, registryLookup);
            }
        }

        @Override
        public CompletableFuture<Void> reload(final PreparationBarrier stage, final ResourceManager resourceManager, final ProfilerFiller preparationsProfiler, final ProfilerFiller reloadProfiler, final Executor backgroundExecutor, final Executor gameExecutor) {
            if (true) // fabric: no ModLoader error state
                return wrapped.reload(stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
            else
                return CompletableFuture.completedFuture(null);
        }
    }
}
