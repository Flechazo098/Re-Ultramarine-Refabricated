package org.voxelutopia.ultramarine.client.integration.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import org.voxelutopia.ultramarine.client.screen.BrickKilnScreen;
import org.voxelutopia.ultramarine.client.screen.ChiselTableScreen;
import org.voxelutopia.ultramarine.client.screen.WoodworkingWorkbenchScreen;
import org.voxelutopia.ultramarine.common.recipe.ChiselTableRecipe;
import org.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;
import org.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;
import org.voxelutopia.ultramarine.init.registry.ModBlocks;
import org.voxelutopia.ultramarine.init.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class UltramarineREIPlugin implements REIClientPlugin {


    @SuppressWarnings("unchecked")
     private enum RecipeTypeInfo {
         CHISEL_TABLE(
             ModRecipeTypes.CHISEL_TABLE,
             REIChiselTableRecipeCategory.CHISEL_TABLE,
             holder -> {
                 if (holder.value() instanceof ChiselTableRecipe) {
                       return REIChiselTableRecipeDisplay.of((RecipeHolder<ChiselTableRecipe>) holder);
                 }
                 return null;
                },
                ChiselTableScreen.class,
                new Rectangle(75, 30, 20, 30),
                ModBlocks.CHISEL_TABLE,
                REIChiselTableRecipeCategory::new
     ),
        WOODWORKING(
                ModRecipeTypes.WOODWORKING,
                REIWoodworkingRecipeCategory.WOODWORKING,
                holder -> {
                    if (holder.value() instanceof WoodworkingRecipe recipe) {
                        return REIWoodworkingRecipeDisplay.of((RecipeHolder<WoodworkingRecipe>) holder);
                    }
                    return null;
                },
                WoodworkingWorkbenchScreen.class,
                new Rectangle(80, 33, 40, 20),
                ModBlocks.WOODWORKING_WORKBENCH,
                REIWoodworkingRecipeCategory::new
        ),
        BRICK_KILN(
                ModRecipeTypes.COMPOSITE_SMELTING,
                REIBrickKilnRecipeCategory.BRICK_KILN,
                holder -> {
                    if (holder.value() instanceof CompositeSmeltingRecipe recipe) {
                        return REIBrickKilnRecipeDisplay.of((RecipeHolder<CompositeSmeltingRecipe>) holder);
                    }
                    return null;
                },
                BrickKilnScreen.class,
                new Rectangle(92, 35, 24, 17),
                ModBlocks.BRICK_KILN,
                REIBrickKilnRecipeCategory::new
        );

        private final RecipeType<?> recipeType;
        private final CategoryIdentifier<?> categoryId;
        private final Function<RecipeHolder<?>, Display> displayFactory;
        private final Class<? extends Screen> screenClass;
        private final Rectangle clickArea;
        private final Block workstation;
        private final Supplier<DisplayCategory<?>> categorySupplier;

        RecipeTypeInfo(
                RecipeType<?> recipeType,
                CategoryIdentifier<?> categoryId,
                Function<RecipeHolder<?>, Display> displayFactory,
                Class<? extends Screen> screenClass,
                Rectangle clickArea,
                Block workstation,
                Supplier<DisplayCategory<?>> categorySupplier
        ) {
            this.recipeType = recipeType;
            this.categoryId = categoryId;
            this.displayFactory = displayFactory;
            this.screenClass = screenClass;
            this.clickArea = clickArea;
            this.workstation = workstation;
            this.categorySupplier = categorySupplier;
        }
    }

    /**
     * 简单的Supplier接口，用于创建分类实例
     */
    @FunctionalInterface
    private interface Supplier<T> {
        T get();
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        // 使用枚举注册所有分类
        for (RecipeTypeInfo typeInfo : RecipeTypeInfo.values()) {
            registry.add(typeInfo.categorySupplier.get());
            registry.addWorkstations(typeInfo.categoryId, EntryStacks.of(typeInfo.workstation));
        }
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();

        // 为每种配方类型注册显示
        for (RecipeTypeInfo typeInfo : RecipeTypeInfo.values()) {
            registerRecipeDisplays(registry, recipeManager, typeInfo);
        }
    }

    /**
     * 通用的配方显示注册方法
     */
    @SuppressWarnings("unchecked")
    private void registerRecipeDisplays(
            DisplayRegistry registry,
            RecipeManager recipeManager,
            RecipeTypeInfo typeInfo
    ) {
        // 获取所有指定类型的配方
        List<RecipeHolder<?>> recipes = new ArrayList<>();

        RecipeType<?> rawType = typeInfo.recipeType;
        if (rawType == ModRecipeTypes.CHISEL_TABLE) {
            recipes.addAll(recipeManager.getAllRecipesFor((RecipeType<ChiselTableRecipe>) rawType));
        } else if (rawType == ModRecipeTypes.WOODWORKING) {
            recipes.addAll(recipeManager.getAllRecipesFor((RecipeType<WoodworkingRecipe>) rawType));
        } else if (rawType == ModRecipeTypes.COMPOSITE_SMELTING) {
            recipes.addAll(recipeManager.getAllRecipesFor((RecipeType<CompositeSmeltingRecipe>) rawType));
        }

        // 按照输出物品的注册ID排序
        recipes.sort(Comparator.comparingInt(recipe -> {
            Item resultItem = recipe.value().getResultItem(null).getItem();
            return BuiltInRegistries.ITEM.getId(resultItem);
        }));

        // 注册所有配方显示
        for (int i = 0; i < recipes.size(); i++) {
            RecipeHolder<?> recipe = recipes.get(i);
            Display display = typeInfo.displayFactory.apply(recipe);

            // 如果display为null，跳过此配方
            if (display == null) {
                continue;
            }

            // 使用反射调用setSortIndex方法
            try {
                display.getClass().getMethod("setSortIndex", int.class).invoke(display, i);
                registry.add(display);
            } catch (Exception e) {
                // 如果反射失败，直接添加显示
                registry.add(display);
            }
        }
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        // 使用枚举注册所有屏幕点击区域
        for (RecipeTypeInfo typeInfo : RecipeTypeInfo.values()) {
            registry.registerClickArea(
                    screen -> typeInfo.clickArea,
                    typeInfo.screenClass,
                    typeInfo.categoryId
            );
        }
    }
}