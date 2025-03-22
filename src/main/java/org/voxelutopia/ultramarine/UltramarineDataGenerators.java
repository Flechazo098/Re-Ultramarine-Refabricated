package org.voxelutopia.ultramarine;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import org.voxelutopia.ultramarine.common.world.ModConfiguredFeatures;
import org.voxelutopia.ultramarine.common.world.ModPlacedFeatures;
import org.voxelutopia.ultramarine.init.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class UltramarineDataGenerators implements DataGeneratorEntrypoint {

    public static final String MOD_ID = Ultramarine.MOD_ID;

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack generator = fabricDataGenerator.createPack();
        generator.addProvider(ModWorldGen::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
        registryBuilder.add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
    }
}
