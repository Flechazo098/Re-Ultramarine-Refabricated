package com.voxelutopia.ultramarine.client.integration.jei;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WoodworkingRecipeCategory implements IRecipeCategory<WoodworkingRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "woodworking");

    public static final RecipeType<WoodworkingRecipe> WOODWORKING_RECIPE_TYPE =
            new RecipeType<>(UID, WoodworkingRecipe.class);

    public static final int WIDTH = 82;
    public static final int HEIGHT = 34;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;
    private final IGuiHelper guiHelper;

    public WoodworkingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WOODWORKING_WORKBENCH));
        this.localizedName = Component.translatable("gui.jei.category.woodworking");
        this.guiHelper = guiHelper;
    }

    @Override
    public RecipeType<WoodworkingRecipe> getRecipeType() {
        return WOODWORKING_RECIPE_TYPE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, WoodworkingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9)
                .addItemStack(RecipeUtil.getResultItem(recipe));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, WoodworkingRecipe recipe, IFocusGroup focuses) {
        builder.addWidget(createArrowWidget(new ScreenPosition(26, 9)));
    }

    @Override
    public void draw(WoodworkingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    }

    protected IRecipeWidget createArrowWidget(ScreenPosition position) {
        return new SimpleArrowWidget(guiHelper, position);
    }

    private static class SimpleArrowWidget implements IRecipeWidget {
        private final IDrawable arrow;
        private final ScreenPosition position;

        public SimpleArrowWidget(IGuiHelper guiHelper, ScreenPosition position) {
            this.arrow = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17)
                    .build();
            this.position = position;
        }

        @Override
        public ScreenPosition getPosition() {
            return position;
        }

        @Override
        public void draw(GuiGraphics guiGraphics, double mouseX, double mouseY) {
            arrow.draw(guiGraphics);
        }
    }
}
