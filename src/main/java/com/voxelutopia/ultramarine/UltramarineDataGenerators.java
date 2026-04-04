package com.voxelutopia.ultramarine;

import com.voxelutopia.ultramarine.common.world.ModConfiguredFeatures;
import com.voxelutopia.ultramarine.common.world.ModPlacedFeatures;
import com.voxelutopia.ultramarine.init.datagen.ModVillagerTradeProvider;
import com.voxelutopia.ultramarine.init.datagen.ModWorldGen;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class UltramarineDataGenerators implements DataGeneratorEntrypoint {

    public static final String MOD_ID = Ultramarine.MOD_ID;

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack generator = fabricDataGenerator.createPack();
        generator.addProvider(ModWorldGen::new);
        generator.addProvider(ModVillagerTradeProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
    }
}
