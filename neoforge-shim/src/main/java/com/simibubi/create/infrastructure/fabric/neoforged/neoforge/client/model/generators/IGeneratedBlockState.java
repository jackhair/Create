// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.client.model.generators;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.VisibleForTesting;

@VisibleForTesting
public interface IGeneratedBlockState {
    JsonObject toJson();
}
