package com.voxelutopia.ultramarine.common.world;


import com.voxelutopia.ultramarine.Ultramarine;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> OVERWORLD_JADE_ORE_PLACED_KEY = of("overworld_jade_ore_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_MAGNESITE_ORE_PLACED_KEY = of("overworld_magnesite_ore_placed");
    public static final ResourceKey<PlacedFeature> OVERWORLD_HEMATITE_ORE_PLACED_KEY = of("overworld_hematite_ore_placed");
    public static final ResourceKey<PlacedFeature> NETHER_COBALT_ORE_PLACED_KEY = of("nether_cobalt_ore_placed");


    public static void bootstrap(BootstrapContext<PlacedFeature> bootstrapContext) {
        HolderGetter<ConfiguredFeature<?, ?>> registryHolderGetter = bootstrapContext.lookup(Registries.CONFIGURED_FEATURE);

        register(bootstrapContext, OVERWORLD_JADE_ORE_PLACED_KEY, registryHolderGetter.getOrThrow(ModConfiguredFeatures.OVERWORLD_JADE_ORE_KEY),
                commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.absolute(-80), VerticalAnchor.absolute(80))));
        register(bootstrapContext, OVERWORLD_MAGNESITE_ORE_PLACED_KEY, registryHolderGetter.getOrThrow(ModConfiguredFeatures.OVERWORLD_MAGNESITE_ORE_KEY),
                commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-40), VerticalAnchor.absolute(60))));
        register(bootstrapContext, OVERWORLD_HEMATITE_ORE_PLACED_KEY, registryHolderGetter.getOrThrow(ModConfiguredFeatures.OVERWORLD_HEMATITE_ORE_KEY),
                commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
        register(bootstrapContext, NETHER_COBALT_ORE_PLACED_KEY, registryHolderGetter.getOrThrow(ModConfiguredFeatures.NETHER_COBALT_ORE_KEY),
                commonOrePlacement(8, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(32))));
    }

    public static ResourceKey<PlacedFeature> of(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, id));
    }

    private static void register
            (BootstrapContext<PlacedFeature> bootstrapContext,
             ResourceKey<PlacedFeature> key,
             Holder<ConfiguredFeature<?, ?>> configuredFeature,
             List<PlacementModifier> modifiers) {
        bootstrapContext.register(key, new PlacedFeature(configuredFeature, List.copyOf(modifiers)));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier2) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier2, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int i, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(i), placementModifier);
    }

    private static List<PlacementModifier> rareOrePlacement(int i, PlacementModifier placementModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(i), placementModifier);
    }
}

