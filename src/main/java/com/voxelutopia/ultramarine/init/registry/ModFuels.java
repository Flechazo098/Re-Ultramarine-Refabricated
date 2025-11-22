package com.voxelutopia.ultramarine.init.registry;

import com.voxelutopia.ultramarine.init.data.ModItemTags;
import net.fabricmc.fabric.api.registry.FuelRegistry;

public class ModFuels {
    public static void registerFuels() {
        FuelRegistry.INSTANCE.add(ModItemTags.POLISHED_PLANKS, 200);
        FuelRegistry.INSTANCE.add(ModItems.WOODEN_FRAME, 200);
    }
}
