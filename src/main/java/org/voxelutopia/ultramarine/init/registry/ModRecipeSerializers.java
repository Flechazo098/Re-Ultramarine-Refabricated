package org.voxelutopia.ultramarine.init.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;

public class ModRecipeSerializers {

    public static RecipeSerializer<WoodworkingRecipe> WOODWORKING_SERIALIZER;


    public static void registerModRecipeSerializers() {
        WOODWORKING_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "woodworking"),
                WoodworkingRecipe.Serializer.INSTANCE);
    }

}
