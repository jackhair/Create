// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.model.generators;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Builder for block models, does not currently provide any additional
 * functionality over {@link ModelBuilder}, purely a stub class with a concrete
 * generic.
 *
 * @see ModelProvider
 * @see ModelBuilder
 */
public class BlockModelBuilder extends ModelBuilder<BlockModelBuilder> {
    public BlockModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation, existingFileHelper);
    }

    @Override
    public JsonObject toJson() {
        return super.toJson();
    }
}
