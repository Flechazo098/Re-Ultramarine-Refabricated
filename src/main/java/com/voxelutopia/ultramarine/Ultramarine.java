package com.voxelutopia.ultramarine;

import com.voxelutopia.ultramarine.data.ModFoods;
import com.voxelutopia.ultramarine.data.loot.ReplaceToSingleItemLootModifier;
import com.voxelutopia.ultramarine.data.registry.*;
import com.voxelutopia.ultramarine.event.CommonEventHandler;
import com.voxelutopia.ultramarine.world.worldgen.ModBiomeModifiers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ultramarine implements ModInitializer {

    public static final String MOD_ID = "ultramarine";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModBiomeModifiers.register();
        SoundRegistry.registerModSounds();
        ReplaceToSingleItemLootModifier.register();
        ModFoods.registerModFoods();
        BlockRegistry.registerModBlocks();
        ItemRegistry.registerModItems();
        BlockEntityRegistry.registerModBlockEntities();
        EntityTypeRegistry.registerModEntities();
        ItemStorageRegistry.register();
        VillagerProfessionRegistry.registerModVillagerProfession();
        PoiTypeRegistry.registerModPOI();
        MenuTypeRegistry.registerModMenus();
        RecipeTypeRegistry.registerModRecipeTypes();
        RecipeSerializerRegistry.registerModRecipeSerializers();
        CreativeTabRegistry.registerModGroups();
        VillagerTradingsRegister.loadTrades();
        CommonEventHandler.init();
    }
}