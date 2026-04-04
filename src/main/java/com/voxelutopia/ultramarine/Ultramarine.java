package com.voxelutopia.ultramarine;

import com.voxelutopia.ultramarine.common.world.gen.ModWorldGeneration;
import com.voxelutopia.ultramarine.dev.DevCommands;
import com.voxelutopia.ultramarine.init.handler.CommonEventHandler;
import com.voxelutopia.ultramarine.init.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ultramarine implements ModInitializer {

    public static final String MOD_ID = "ultramarine";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void error(String format, Object... data) {
        LOGGER.error(format, data);
    }

    public static void warn(String format, Object... data) {
        LOGGER.warn(format, data);
    }

    public static boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static void info(String format, Object... data) {
        LOGGER.info(format, data);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public void onInitialize() {
        ModPackets.register();
        ModWorldGeneration.registerWorldGenerations();
        ModSounds.registerModSounds();
        ModFoods.registerModFoods();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModFuels.registerFuels();
        ModBlockEntities.registerModBlockEntities();
        ModEntityTypes.registerModEntities();
        ModVillagerProfessions.registerModVillagerProfession();
        ModPoiTypes.registerModPOI();
        ModMenuTypes.registerModMenus();
        ModRecipeTypes.registerModRecipeTypes();
        ModRecipeSerializers.registerModRecipeSerializers();
        ModCreativeTabs.registerModGroups();

        BlockApiLookupRegistry.register();
        CommonEventHandler.init();
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) DevCommands.register();
    }

}
