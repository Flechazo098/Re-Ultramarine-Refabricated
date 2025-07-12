package com.voxelutopia.ultramarine.client.integration.jei;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class CompositeSmeltingRecipeCategory implements IRecipeCategory<CompositeSmeltingRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "composite_smelting");

    public static final RecipeType<CompositeSmeltingRecipe> COMPOSITE_SMELTING_RECIPE_TYPE =
            new RecipeType<>(UID, CompositeSmeltingRecipe.class);

    public static final ResourceLocation TEXTURE_GUI = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "textures/gui/brick_kiln.png");

    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final IGuiHelper guiHelper;
    private final IDrawableAnimated animatedFlame;

    public CompositeSmeltingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE_GUI, 45, 16, 92, 54);
        this.regularCookTime = 200;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.BRICK_KILN));
        this.localizedName = Component.translatable("gui.jei.category.composite_smelting");
        this.guiHelper = guiHelper;

        // 创建动画火焰，使用原版的纹理坐标
        this.animatedFlame = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 114, 14, 14)
                .buildAnimated(300, IDrawableAnimated.StartDirection.TOP, true);
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
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public RecipeType<CompositeSmeltingRecipe> getRecipeType() {
        return COMPOSITE_SMELTING_RECIPE_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompositeSmeltingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .addIngredients(recipe.getPrimaryIngredient());

        builder.addSlot(INPUT, 21, 1)
                .addIngredients(recipe.getSecondaryIngredient());

        builder.addSlot(OUTPUT, 71, 19)
                .addItemStack(RecipeUtil.getResultItem(recipe));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, CompositeSmeltingRecipe recipe, IFocusGroup focuses) {
        builder.addWidget(createCookingArrowWidget(recipe, new ScreenPosition(34, 17)));
    }

    @Override
    public void draw(CompositeSmeltingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // 绘制动画火焰
        animatedFlame.draw(guiGraphics, 11, 20);

        // 绘制经验值和烹饪时间
        drawExperience(recipe, guiGraphics, 0);
        drawCookTime(recipe, guiGraphics, 45);
    }

    protected void drawExperience(CompositeSmeltingRecipe recipe, GuiGraphics guiGraphics, int y) {
        float experience = recipe.getExp();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            guiGraphics.drawString(fontRenderer, experienceString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    protected void drawCookTime(CompositeSmeltingRecipe recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            guiGraphics.drawString(fontRenderer, timeString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    protected IRecipeWidget createCookingArrowWidget(CompositeSmeltingRecipe recipe, ScreenPosition position) {
        return new CookingArrowRecipeWidget(guiHelper, recipe, regularCookTime, position);
    }

    private static class CookingArrowRecipeWidget implements IRecipeWidget {
        private final IDrawableAnimated arrow;
        private final ScreenPosition position;

        public CookingArrowRecipeWidget(IGuiHelper guiHelper, CompositeSmeltingRecipe recipe, int regularCookTime, ScreenPosition position) {
            int cookTime = recipe.getCookingTime();
            if (cookTime <= 0) {
                cookTime = regularCookTime;
            }
            this.arrow = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17)
                    .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
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