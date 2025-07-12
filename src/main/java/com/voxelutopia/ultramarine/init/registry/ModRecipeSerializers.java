package com.voxelutopia.ultramarine.init.registry;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.common.recipe.ChiselTableRecipe;
import com.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;
import com.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ModRecipeSerializers {

    public static RecipeSerializer<WoodworkingRecipe> WOODWORKING_SERIALIZER;
    public static RecipeSerializer<CompositeSmeltingRecipe> COMPOSITE_SMELTING_SERIALIZER;
    public static RecipeSerializer<ChiselTableRecipe> CHISEL_TABLE_SERIALIZER;


    public static void registerModRecipeSerializers() {
        WOODWORKING_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "woodworking"),
                WoodworkingRecipe.Serializer.INSTANCE);
        COMPOSITE_SMELTING_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "composite_smelting"),
                CompositeSmeltingRecipe.Serializer.INSTANCE);
        CHISEL_TABLE_SERIALIZER = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "chisel_table"),
                ChiselTableRecipe.Serializer.INSTANCE);
    }

}
