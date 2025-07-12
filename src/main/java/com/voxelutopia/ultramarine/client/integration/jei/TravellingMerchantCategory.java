package com.voxelutopia.ultramarine.client.integration.jei;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TravellingMerchantCategory implements IRecipeCategory<TravellingMerchantWrapper> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "custom_wandering_trader");

    public static final RecipeType<TravellingMerchantWrapper> CUSTOM_WANDERING_TRADER_WRAPPER_RECIPE_TYPE =
            new RecipeType<>(UID, TravellingMerchantWrapper.class);

    public static final int WIDTH = 82;
    public static final int HEIGHT = 34;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public TravellingMerchantCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.TEAHOUSE_FLAG));
        this.localizedName = Component.translatable("gui.jei.category.travelling_merchant");
    }

    @Override
    public RecipeType<TravellingMerchantWrapper> getRecipeType() {
        return CUSTOM_WANDERING_TRADER_WRAPPER_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return localizedName;
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
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addItemStack(recipe.getOutput());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, TravellingMerchantWrapper recipe, IFocusGroup focuses) {
        builder.addWidget(new SimpleArrowWidget(26, 9));
    }

    @Override
    public void draw(TravellingMerchantWrapper recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }

    private record SimpleArrowWidget(int x, int y) implements mezz.jei.api.gui.widgets.IRecipeWidget {

        @Override
        public ScreenPosition getPosition() {
            return new ScreenPosition(x, y);
        }

        @Override
        public void draw(GuiGraphics guiGraphics, double mouseX, double mouseY) {
            ResourceLocation texture = Constants.RECIPE_GUI_VANILLA;
            guiGraphics.blit(texture, 0, 0, 82, 128, 24, 17);
        }
    }
}
