// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) NeoForged and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.gui;

import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.gui.screens.Screen;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.ModContainer;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.ModList;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforgespi.language.IModInfo;

/**
 * Register an instance to {@link ModContainer#registerExtensionPoint(Class, Supplier)}
 * to supply a config screen for your mod.
 *
 * <p>The config screen will be accessible from the mod list menu.
 */
public interface IConfigScreenFactory { // fabric: IExtensionPoint is only a marker
    /**
     * Creates a new config screen. The {@code modListScreen} parameter can be used for a "back" button.
     */
    Screen createScreen(ModContainer container, Screen modListScreen);

    static Optional<IConfigScreenFactory> getForMod(IModInfo selectedMod) {
        return ModList.get().getModContainerById(selectedMod.getModId()).flatMap(m -> m.getCustomExtension(IConfigScreenFactory.class));
    }
}
