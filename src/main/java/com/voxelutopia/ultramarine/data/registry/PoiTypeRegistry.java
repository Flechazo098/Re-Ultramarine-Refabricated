package com.voxelutopia.ultramarine.data.registry;

import com.voxelutopia.ultramarine.Ultramarine;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class PoiTypeRegistry {

    public static PoiType COOKING_POI;
    public static PoiType TRADE_POI;

    public static void registerModPOI() {

        COOKING_POI = PointOfInterestHelper.register(new ResourceLocation(Ultramarine.MOD_ID, "cooking_poi"), 1, 1, BlockRegistry.FOOD_HAMPER);
        TRADE_POI = PointOfInterestHelper.register(new ResourceLocation(Ultramarine.MOD_ID, "trade_poi"), 1, 5, BlockRegistry.TEAHOUSE_FLAG);

    }
}
