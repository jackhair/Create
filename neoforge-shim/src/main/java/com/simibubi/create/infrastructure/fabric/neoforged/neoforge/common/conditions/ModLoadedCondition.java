// fabric: vendored from NeoForge 21.1.219 (LGPL-2.1-only) into Create's shim layer, see PORTING.md D7
/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package com.simibubi.create.infrastructure.fabric.neoforged.neoforge.common.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.ModList;

public record ModLoadedCondition(String modid) implements ICondition {
    public static MapCodec<ModLoadedCondition> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(
                            Codec.STRING.fieldOf("modid").forGetter(ModLoadedCondition::modid))
                    .apply(builder, ModLoadedCondition::new));

    @Override
    public boolean test(IContext context) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public String toString() {
        return "mod_loaded(\"" + modid + "\")";
    }
}
