package com.voxelutopia.ultramarine.init.registry;

import com.voxelutopia.ultramarine.Ultramarine;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PoiHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class ModPoiTypes {


    public static PoiType COOKING_POI;
    public static PoiType TRADE_POI;

    public static void registerModPOI() {

        COOKING_POI = PoiHelper.register(Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "cooking_poi"), 1, 1, ModBlocks.FOOD_HAMPER);
        TRADE_POI = PoiHelper.register(Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "trade_poi"), 1, 5, ModBlocks.TEAHOUSE_FLAG);

    }


}
