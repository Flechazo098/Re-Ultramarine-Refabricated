package org.voxelutopia.ultramarine.init.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.recipe.ChiselTableRecipe;
import org.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;
import org.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;

public class ModRecipeTypes {

    public static RecipeType<WoodworkingRecipe> WOODWORKING;
    public static RecipeType<CompositeSmeltingRecipe> COMPOSITE_SMELTING;
    public static RecipeType<ChiselTableRecipe> CHISEL_TABLE;

    public static void registerModRecipeTypes () {
        WOODWORKING = register("woodworking");
        COMPOSITE_SMELTING = register("composite_smelting");
        CHISEL_TABLE = register("chisel_table");
    }

    private static <T extends Recipe<?>> RecipeType<T> register (String name) {
        return Registry.register(
                BuiltInRegistries.RECIPE_TYPE,
                ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, name),
                new RecipeType<T>() {
                    @Override
                    public String toString () {
                        return ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, name).toString();
                    }
                }
        );
    }
}
