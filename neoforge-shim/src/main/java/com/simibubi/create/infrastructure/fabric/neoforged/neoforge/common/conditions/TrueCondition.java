// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.conditions;

import com.mojang.serialization.MapCodec;

public final class TrueCondition implements ICondition {
    public static final TrueCondition INSTANCE = new TrueCondition();

    public static MapCodec<TrueCondition> CODEC = MapCodec.unit(INSTANCE).stable();

    private TrueCondition() {}

    @Override
    public boolean test(IContext context) {
        return true;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "true";
    }
}
