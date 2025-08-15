package com.voxelutopia.ultramarine.client.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.data.recipe.CompositeSmeltingRecipe;
import com.voxelutopia.ultramarine.data.registry.BlockRegistry;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class CompositeSmeltingRecipeCategory implements IRecipeCategory<CompositeSmeltingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Ultramarine.MOD_ID, "composite_smelting");

    public static final RecipeType<CompositeSmeltingRecipe> COMPOSITE_SMELTING_RECIPE_TYPE =
            new RecipeType<>(UID, CompositeSmeltingRecipe.class);

    public static final ResourceLocation TEXTURE_GUI = new ResourceLocation(Ultramarine.MOD_ID, "textures/gui/brick_kiln.png");

    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;
    private final LoadingCache<Integer, IDrawableAnimated> cachedArrows;
    private final IDrawableAnimated animatedFlame;
    private final IGuiHelper guiHelper;

    public CompositeSmeltingRecipeCategory(IGuiHelper guiHelper) {
        this.guiHelper = guiHelper;
        this.background = guiHelper.createDrawable(TEXTURE_GUI, 45, 16, 92, 54);
        this.regularCookTime = 200;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegistry.BRICK_KILN));
        this.localizedName = Component.translatable("gui.jei.category.composite_smelting");

        // 创建缓存的动画箭头
        this.cachedArrows = CacheBuilder.newBuilder()
                .maximumSize(25)
                .build(new CacheLoader<>() {
                    @Override
                    public IDrawableAnimated load(Integer cookTime) {
                        return guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 128, 24, 17)
                                .buildAnimated(cookTime, IDrawableAnimated.StartDirection.LEFT, false);
                    }
                });

        // 创建动画火焰
        this.animatedFlame = guiHelper.drawableBuilder(Constants.RECIPE_GUI_VANILLA, 82, 114, 14, 14)
                .buildAnimated(300, IDrawableAnimated.StartDirection.TOP, true);
    }

    protected IDrawableAnimated getArrow(CompositeSmeltingRecipe recipe) {
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        return this.cachedArrows.getUnchecked(cookTime);
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
    public void draw(CompositeSmeltingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // 绘制动画火焰
        animatedFlame.draw(guiGraphics, 11, 20);

        // 绘制动画箭头
        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(guiGraphics, 34, 17);

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
            cookTime = this.regularCookTime;
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

    @Override
    public Component getTitle() {
        return localizedName;
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
    public RecipeType<CompositeSmeltingRecipe> getRecipeType() {
        return COMPOSITE_SMELTING_RECIPE_TYPE;
    }
}