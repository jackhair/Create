// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.event;

import java.util.Map;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import com.simibubi.create.infrastructure.fabric.neoforged.bus.api.Event;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus;

public class RegisterMenuScreensEvent extends Event implements IModBusEvent {
    private final Map<MenuType<?>, MenuScreens.ScreenConstructor<?, ?>> registeredScreens;

    @ApiStatus.Internal
    public RegisterMenuScreensEvent(Map<MenuType<?>, MenuScreens.ScreenConstructor<?, ?>> registeredScreens) {
        this.registeredScreens = registeredScreens;
    }

    public <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(
            MenuType<? extends M> menuType, MenuScreens.ScreenConstructor<M, U> screenConstructor) {
        if (registeredScreens.containsKey(menuType)) {
            throw new IllegalStateException("Duplicate attempt to register screen: " + BuiltInRegistries.MENU.getKey(menuType));
        }
        registeredScreens.put(menuType, screenConstructor);
    }
}
