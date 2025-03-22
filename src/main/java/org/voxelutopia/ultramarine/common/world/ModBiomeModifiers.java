package org.voxelutopia.ultramarine.common.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.voxelutopia.ultramarine.Ultramarine;


public class ModBiomeModifiers {

    public static void register() {
        addOreFeature(ModPlacedFeatures.OVERWORLD_JADE_ORE_PLACED_KEY,
                BiomeTags.IS_OVERWORLD, GenerationStep.Decoration.UNDERGROUND_ORES);
        addOreFeature(ModPlacedFeatures.OVERWORLD_MAGNESITE_ORE_PLACED_KEY,
                BiomeTags.IS_OVERWORLD, GenerationStep.Decoration.UNDERGROUND_ORES);
        addOreFeature(ModPlacedFeatures.OVERWORLD_HEMATITE_ORE_PLACED_KEY,
                BiomeTags.IS_OVERWORLD, GenerationStep.Decoration.UNDERGROUND_ORES);
        addOreFeature(ModPlacedFeatures.NETHER_COBALT_ORE_PLACED_KEY,
                BiomeTags.IS_NETHER, GenerationStep.Decoration.UNDERGROUND_ORES);
    }

    private static void addOreFeature(ResourceKey<PlacedFeature> featureKey,
                                      TagKey<Biome> biomeTag, // 修改参数类型为TagKey<Biome>
                                      GenerationStep.Decoration step) {
        BiomeModifications.addFeature(
                context -> context.hasTag(biomeTag), // 直接使用TagKey
                step,
                featureKey
        );
    }

    public static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(
                Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, name)
        );
    }
}