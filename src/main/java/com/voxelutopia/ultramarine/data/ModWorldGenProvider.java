package com.voxelutopia.ultramarine.data;

import com.voxelutopia.ultramarine.Ultramarine;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

public class ModWorldGenProvider extends FabricDynamicRegistryProvider {

    public ModWorldGenProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        // 添加 configured features
        entries.addAll(registries.lookupOrThrow(Registries.CONFIGURED_FEATURE));

        // 添加 placed features
        entries.addAll(registries.lookupOrThrow(Registries.PLACED_FEATURE));
    }

    @Override
    public String getName() {
        return Ultramarine.MOD_ID + " World Generation";
    }
}