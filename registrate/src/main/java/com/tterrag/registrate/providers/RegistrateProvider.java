package com.tterrag.registrate.providers;

import net.minecraft.data.DataProvider;
import com.simibubi.create.infrastructure.fabric.neoforged.fml.LogicalSide;

public interface RegistrateProvider extends DataProvider {
    
    LogicalSide getSide();
}
