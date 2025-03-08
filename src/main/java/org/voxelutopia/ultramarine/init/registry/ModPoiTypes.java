package org.voxelutopia.ultramarine.init.registry;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import org.voxelutopia.ultramarine.Ultramarine;

public class ModPoiTypes {


    public static PoiType COOKING_POI;

    public static void registerModPOI() {

        COOKING_POI = PointOfInterestHelper.register(ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "cooking_poi"), 1, 1, ModBlocks.FOOD_HAMPER);

    }


}
