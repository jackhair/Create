package com.tterrag.registrate.util;

import com.simibubi.create.infrastructure.fabric.neoforged.api.distmarker.Dist;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

public class RegistrateDistExecutor {
    public static void unsafeRunWhenOn(Dist dist, Supplier<Runnable> toRun) {
        if (dist == FMLEnvironment.dist) {
            toRun.get().run();
        }
    }
}
