package org.voxelutopia.ultramarine;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.voxelutopia.ultramarine.common.world.gen.ModWorldGeneration;
import org.voxelutopia.ultramarine.init.handler.CommonEventHandler;
import org.voxelutopia.ultramarine.init.registry.*;

public class Ultramarine implements ModInitializer {

    public static final String MOD_ID = "ultramarine";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void error(String format, Object... data) {
        LOGGER.error(format, data);
    }

    public static void warn(String format, Object... data) {
        LOGGER.warn(format, data);
    }

    public static void info(String format, Object... data) {
        LOGGER.info(format, data);
    }

    public static Logger getLogger() {return LOGGER;}

    @Override
    public void onInitialize() {
        ModWorldGeneration.registerWorldGenerations();
        ModFoods.registerModFoods();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModBlockEntities.registerModBlockEntities();
        ModEntityTypes.registerModEntities();
        ModVillagerProfessions.registerModVillagerProfession();
        ModPoiTypes.registerModPOI();
        ModMenuTypes.registerModMenus();
        ModRecipeTypes.registerModRecipeTypes();
        ModRecipeSerializers.registerModRecipeSerializers();
        ModSounds.registerModSounds();
        ModCreativeTabs.registerModGroups();
        ModVillagerTradings.loadTrades();

        CommonEventHandler.init();
    }

}
