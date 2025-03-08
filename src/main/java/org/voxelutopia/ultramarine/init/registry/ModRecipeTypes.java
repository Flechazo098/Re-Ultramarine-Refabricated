package org.voxelutopia.ultramarine.init.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;

public class ModRecipeTypes {


    public static RecipeType<WoodworkingRecipe> WOODWORKING;

    public static void registerModRecipeTypes() {
        WOODWORKING = Registry.register(BuiltInRegistries.RECIPE_TYPE, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "woodworking"),
                Type.INSTANCE);
    }

    static class Type implements RecipeType<WoodworkingRecipe> {
        public final static Type INSTANCE = new Type();
    }

}
