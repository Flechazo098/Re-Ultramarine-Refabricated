package com.voxelutopia.ultramarine.data.registry;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.data.recipe.ChiselTableRecipe;
import com.voxelutopia.ultramarine.data.recipe.CompositeSmeltingRecipe;
import com.voxelutopia.ultramarine.data.recipe.WoodworkingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeTypeRegistry {


    public static RecipeType<WoodworkingRecipe> WOODWORKING;
    public static RecipeType<CompositeSmeltingRecipe> COMPOSITE_SMELTING;
    public static RecipeType<ChiselTableRecipe> CHISEL_TABLE;

    public static void registerModRecipeTypes() {
        WOODWORKING = register("woodworking");
        COMPOSITE_SMELTING = register("composite_smelting");
        CHISEL_TABLE = register("chisel_table");
    }

    private static <T extends Recipe<?>> RecipeType<T> register(String name) {
        return Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                new ResourceLocation(Ultramarine.MOD_ID, name),
                new RecipeType<T>() {
                    @Override
                    public String toString() {
                        return new ResourceLocation(Ultramarine.MOD_ID, name).toString();
                    }
                }
        );
    }

}
