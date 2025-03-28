package org.voxelutopia.ultramarine.client.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.slf4j.Logger;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.client.screen.BrickKilnScreen;
import org.voxelutopia.ultramarine.client.screen.ChiselTableScreen;
import org.voxelutopia.ultramarine.common.recipe.ChiselTableRecipe;
import org.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;
import org.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;
import org.voxelutopia.ultramarine.init.registry.ModBlocks;
import org.voxelutopia.ultramarine.init.registry.ModRecipeTypes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class UltramarineJEIPlugin implements IModPlugin {

    public static final ResourceLocation JEI_GUI_VANILLA = ResourceLocation.fromNamespaceAndPath("jei", "textures/gui/gui_vanilla.png");
    private static final Logger LOGGER = Ultramarine.getLogger();

    public UltramarineJEIPlugin(){
        LOGGER.info("JEI Integration Loaded");
    }

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
//        registration.addRecipeClickArea(BrickKilnScreen.class, 78, 32, 28, 23, CompositeSmeltingRecipeCategory.COMPOSITE_SMELTING_RECIPE_TYPE);
        registration.addRecipeClickArea(ChiselTableScreen.class, 79, 24, 18, 30, ChiselTableRecipeCategory.CHISEL_TABLE_RECIPE_TYPE);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
//        registration.addRecipeCategories(new WoodworkingRecipeCategory(guiHelper));
//        registration.addRecipeCategories(new CompositeSmeltingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ChiselTableRecipeCategory(guiHelper));
//        registration.addRecipeCategories(new TravellingMerchantCategory(guiHelper));
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Objects.requireNonNull(Minecraft.getInstance().level);
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();

        // 修改这里：从 RecipeHolder 中提取实际的配方对象
//        List<WoodworkingRecipe> woodworkingRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.WOODWORKING)
//                .stream().map(RecipeHolder::value).collect(Collectors.toList());
//        registration.addRecipes(WoodworkingRecipeCategory.WOODWORKING_RECIPE_TYPE, woodworkingRecipes);
//
//        List<CompositeSmeltingRecipe> compositeSmeltingRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.COMPOSITE_SMELTING)
//                .stream().map(RecipeHolder::value).collect(Collectors.toList());
//        registration.addRecipes(CompositeSmeltingRecipeCategory.COMPOSITE_SMELTING_RECIPE_TYPE, compositeSmeltingRecipes);

        List<ChiselTableRecipe> chiselTableRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.CHISEL_TABLE)
                .stream().map(RecipeHolder::value).collect(Collectors.toList());
        registration.addRecipes(ChiselTableRecipeCategory.CHISEL_TABLE_RECIPE_TYPE, chiselTableRecipes);

//        List<TravellingMerchantWrapper> customWanderingTraderTrades = TravellingMerchant.getTradeOptions().stream().map(TravellingMerchantWrapper::new).toList();
//        registration.addRecipes(TravellingMerchantCategory.CUSTOM_WANDERING_TRADER_WRAPPER_RECIPE_TYPE, customWanderingTraderTrades);
    }
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
//        registration.addRecipeCatalyst(new ItemStack(ModBlocks.WOODWORKING_WORKBENCH), WoodworkingRecipeCategory.WOODWORKING_RECIPE_TYPE);
//        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BRICK_KILN), CompositeSmeltingRecipeCategory.COMPOSITE_SMELTING_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CHISEL_TABLE), ChiselTableRecipeCategory.CHISEL_TABLE_RECIPE_TYPE);
//        registration.addRecipeCatalyst(new ItemStack(ModBlocks.TEAHOUSE_FLAG), TravellingMerchantCategory.CUSTOM_WANDERING_TRADER_WRAPPER_RECIPE_TYPE);
    }

}