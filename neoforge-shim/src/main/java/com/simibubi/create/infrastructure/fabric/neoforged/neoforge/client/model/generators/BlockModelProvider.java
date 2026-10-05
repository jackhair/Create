// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.model.generators;

import net.minecraft.data.PackOutput;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Stub class to extend for block model data providers, eliminates some
 * boilerplate constructor parameters.
 */
public abstract class BlockModelProvider extends ModelProvider<BlockModelBuilder> {
    public BlockModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, BLOCK_FOLDER, BlockModelBuilder::new, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Block Models: " + modid;
    }
}
