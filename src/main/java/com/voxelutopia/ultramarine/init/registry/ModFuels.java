package com.voxelutopia.ultramarine.init.registry;

import com.voxelutopia.ultramarine.init.data.ModItemTags;
import net.fabricmc.fabric.api.registry.FuelValueEvents;

public final class ModFuels {
    private ModFuels() {
    }

    public static void registerFuels() {
        FuelValueEvents.BUILD.register((builder, _) -> {
            builder.add(ModItemTags.POLISHED_PLANKS, 200);
            builder.add(ModItems.WOODEN_FRAME, 200);
        });
    }
}

