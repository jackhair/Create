// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.conditions;

import com.mojang.serialization.MapCodec;

public final class FalseCondition implements ICondition {
    public static final FalseCondition INSTANCE = new FalseCondition();

    public static final MapCodec<FalseCondition> CODEC = MapCodec.unit(INSTANCE).stable();

    private FalseCondition() {}

    @Override
    public boolean test(IContext condition) {
        return false;
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    public String toString() {
        return "false";
    }
}
