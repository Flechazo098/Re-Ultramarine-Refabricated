package com.voxelutopia.ultramarine.init.registry;

import com.voxelutopia.ultramarine.Ultramarine;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class ModPoiTypes {


    public static PoiType COOKING_POI;
    public static PoiType TRADE_POI;

    public static void registerModPOI() {

        COOKING_POI = PointOfInterestHelper.register(ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "cooking_poi"), 1, 1, ModBlocks.FOOD_HAMPER);
        TRADE_POI = PointOfInterestHelper.register(ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "trade_poi"), 1, 5, ModBlocks.TEAHOUSE_FLAG);

    }


}
