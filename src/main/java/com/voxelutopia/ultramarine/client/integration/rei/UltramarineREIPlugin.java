//package com.voxelutopia.ultramarine.client.integration.rei;
//
//import com.voxelutopia.ultramarine.client.screen.BrickKilnScreen;
//import com.voxelutopia.ultramarine.client.screen.ChiselTableScreen;
//import com.voxelutopia.ultramarine.client.screen.WoodworkingWorkbenchScreen;
//import com.voxelutopia.ultramarine.common.recipe.ChiselTableRecipe;
//import com.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;
//import com.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;
//import com.voxelutopia.ultramarine.init.registry.ModBlocks;
//import com.voxelutopia.ultramarine.init.registry.ModRecipeTypes;
//import me.shedaniel.math.Rectangle;
//import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
//import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
//import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
//import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
//import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
//import me.shedaniel.rei.api.common.category.CategoryIdentifier;
//import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
//import me.shedaniel.rei.api.common.util.EntryStacks;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.block.Block;
//
//import java.util.List;
//
//public class UltramarineREIPlugin implements REIClientPlugin {
//
//    private enum RecipeTypeInfo {
//        CHISEL_TABLE(
//                ModRecipeTypes.CHISEL_TABLE,
//                REIChiselTableRecipeCategory.CHISEL_TABLE,
//                ChiselTableScreen.class,
//                new Rectangle(75, 30, 20, 30),
//                ModBlocks.CHISEL_TABLE,
//                REIChiselTableRecipeCategory::new
//        ),
//        WOODWORKING(
//                ModRecipeTypes.WOODWORKING,
//                REIWoodworkingRecipeCategory.WOODWORKING,
//                WoodworkingWorkbenchScreen.class,
//                new Rectangle(80, 33, 40, 20),
//                ModBlocks.WOODWORKING_WORKBENCH,
//                REIWoodworkingRecipeCategory::new
//        ),
//        BRICK_KILN(
//                ModRecipeTypes.COMPOSITE_SMELTING,
//                REIBrickKilnRecipeCategory.BRICK_KILN,
//                BrickKilnScreen.class,
//                new Rectangle(92, 35, 24, 17),
//                ModBlocks.BRICK_KILN,
//                REIBrickKilnRecipeCategory::new
//        );
//
//        private final RecipeType<?> recipeType;
//        private final CategoryIdentifier<?> categoryId;
//        private final Class<? extends Screen> screenClass;
//        private final Rectangle clickArea;
//        private final Block workstation;
//        private final Supplier<DisplayCategory<?>> categorySupplier;
//
//        RecipeTypeInfo(
//                RecipeType<?> recipeType,
//                CategoryIdentifier<?> categoryId,
//                Class<? extends Screen> screenClass,
//                Rectangle clickArea,
//                Block workstation,
//                Supplier<DisplayCategory<?>> categorySupplier
//        ) {
//            this.recipeType = recipeType;
//            this.categoryId = categoryId;
//            this.screenClass = screenClass;
//            this.clickArea = clickArea;
//            this.workstation = workstation;
//            this.categorySupplier = categorySupplier;
//        }
//    }
//
//    @FunctionalInterface
//    private interface Supplier<T> {
//        T get();
//    }
//
//    @Override
//    public void registerCategories(CategoryRegistry registry) {
//        for (RecipeTypeInfo typeInfo : RecipeTypeInfo.values()) {
//            registry.add(typeInfo.categorySupplier.get());
//            registry.addWorkstations(typeInfo.categoryId, EntryStacks.of(typeInfo.workstation));
//        }
//    }
//
//    @Override
//    public void registerDisplays(DisplayRegistry registry) {
//        registry.registerRecipeFiller(ChiselTableRecipe.class, ModRecipeTypes.CHISEL_TABLE, REIChiselTableRecipeDisplay::of);
//        registry.registerRecipeFiller(WoodworkingRecipe.class, ModRecipeTypes.WOODWORKING, REIWoodworkingRecipeDisplay::of);
//        registry.registerRecipeFiller(CompositeSmeltingRecipe.class, ModRecipeTypes.COMPOSITE_SMELTING, REIBrickKilnRecipeDisplay::of);
//    }
//
//    @Override
//    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
//        registry.register(REIChiselTableRecipeCategory.CHISEL_TABLE, REIChiselTableRecipeDisplay.Serializer.INSTANCE);
//        registry.register(REIWoodworkingRecipeCategory.WOODWORKING, REIWoodworkingRecipeDisplay.Serializer.INSTANCE);
//        registry.register(REIBrickKilnRecipeCategory.BRICK_KILN, REIBrickKilnRecipeDisplay.Serializer.INSTANCE);
//    }
//
//    @Override
//    public void registerScreens(ScreenRegistry registry) {
//        for (RecipeTypeInfo typeInfo : RecipeTypeInfo.values()) {
//            registry.registerClickArea(
//                    screen -> typeInfo.clickArea,
//                    typeInfo.screenClass,
//                    typeInfo.categoryId
//            );
//        }
//    }
//}