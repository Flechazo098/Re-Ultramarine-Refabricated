package com.voxelutopia.ultramarine.client.integration.jei;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.data.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TravellingMerchantCategory implements IRecipeCategory<TravellingMerchantWrapper> {

    public static final ResourceLocation UID = new ResourceLocation(Ultramarine.MOD_ID, "custom_wandering_trader");

    public static final RecipeType<TravellingMerchantWrapper> CUSTOM_WANDERING_TRADER_WRAPPER_RECIPE_TYPE =
            new RecipeType<>(UID, TravellingMerchantWrapper.class);

    public static final int WIDTH = 82;
    public static final int HEIGHT = 34;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IDrawableStatic arrow;

    public TravellingMerchantCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.TEAHOUSE_FLAG));
        this.title = Component.translatable("gui.jei.category.travelling_merchant");
        this.arrow = guiHelper.createDrawable(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17);
    }

    @Override
    public RecipeType<TravellingMerchantWrapper> getRecipeType() {
        return CUSTOM_WANDERING_TRADER_WRAPPER_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TravellingMerchantWrapper recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                .addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9)
                .addItemStack(recipe.getOutput());
    }

    @Override
    public void draw(TravellingMerchantWrapper recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 26, 9);
    }
}