package com.voxelutopia.ultramarine.common.world;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_JADE_ORE_KEY = of("overworld_jade_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_MAGNESITE_ORE_KEY = of("overworld_magnesite_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_HEMATITE_ORE_KEY = of("overworld_hematite_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NETHER_COBALT_ORE_KEY = of("nether_cobalt_ore");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> featureBootstrapContext) {
        RuleTest stoneReplace = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepSlateReplace = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherReplace = new TagMatchTest(BlockTags.BASE_STONE_NETHER);

        List<OreConfiguration.TargetBlockState> overWorldTargets = List.of(
                OreConfiguration.target(stoneReplace, ModBlocks.JADE_ORE.defaultBlockState()),
                OreConfiguration.target(stoneReplace, ModBlocks.MAGNESITE_ORE.defaultBlockState()),
                OreConfiguration.target(stoneReplace, ModBlocks.HEMATITE_ORE.defaultBlockState()),

                OreConfiguration.target(deepSlateReplace, ModBlocks.DEEPSLATE_JADE_ORE.defaultBlockState()),
                OreConfiguration.target(deepSlateReplace, ModBlocks.DEEPSLATE_MAGNESITE_ORE.defaultBlockState()),
                OreConfiguration.target(deepSlateReplace, ModBlocks.DEEPSLATE_HEMATITE_ORE.defaultBlockState())
        );

        List<OreConfiguration.TargetBlockState> netherTargets = List.of(
                OreConfiguration.target(netherReplace, ModBlocks.NETHER_COBALT_ORE.defaultBlockState())
        );

        register(featureBootstrapContext, OVERWORLD_JADE_ORE_KEY, Feature.ORE, new OreConfiguration(overWorldTargets, 4));
        register(featureBootstrapContext, OVERWORLD_MAGNESITE_ORE_KEY, Feature.ORE, new OreConfiguration(overWorldTargets, 7));
        register(featureBootstrapContext, OVERWORLD_HEMATITE_ORE_KEY, Feature.ORE, new OreConfiguration(overWorldTargets, 9));
        register(featureBootstrapContext, NETHER_COBALT_ORE_KEY, Feature.ORE, new OreConfiguration(netherTargets, 6));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> of(String id) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, id));
    }

    public static <FL extends FeatureConfiguration, F extends Feature<FL>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> bootstrapContext, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FL config
    ) {
        bootstrapContext.register(key, new ConfiguredFeature<>(feature, config));
    }
}
