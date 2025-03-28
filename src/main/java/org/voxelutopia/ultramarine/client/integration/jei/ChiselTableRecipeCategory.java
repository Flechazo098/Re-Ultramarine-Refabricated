package org.voxelutopia.ultramarine.client.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.recipe.ChiselTableRecipe;
import org.voxelutopia.ultramarine.init.registry.ModBlocks;

import java.util.List;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public class ChiselTableRecipeCategory implements IRecipeCategory<ChiselTableRecipe> {

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "chisel_table");

    public static final RecipeType<ChiselTableRecipe> CHISEL_TABLE_RECIPE_TYPE =
            new RecipeType<>(UID, ChiselTableRecipe.class);

    public static final ResourceLocation TEXTURE_GUI = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "textures/gui/chisel_table.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final Component localizedName;

    public ChiselTableRecipeCategory(IGuiHelper guiHelper){
        this.background = guiHelper.createDrawable(TEXTURE_GUI, 25, 24, 126, 45);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CHISEL_TABLE));
        this.localizedName = Component.translatable("gui.jei.category.chisel_table");
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChiselTableRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .addIngredients(recipe.getMaterial());

        builder.addSlot(INPUT, 28, 1)
                .addIngredients(recipe.getTemplate());

        List<Ingredient> colors = recipe.getColors();

        for (int i = 0; i < colors.size(); i++){
            builder.addSlot(INPUT, 1 + 18 * i, 28).addIngredients(colors.get(i));
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            throw new NullPointerException("ClientLevel must not be null.");
        }
        RegistryAccess registryAccess = level.registryAccess();

        builder.addSlot(OUTPUT, 105, 10)
                .addItemStack(recipe.getResultItem(registryAccess));
    }

    @Override
    public void draw(ChiselTableRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public RecipeType<ChiselTableRecipe> getRecipeType() {
        return CHISEL_TABLE_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    @SuppressWarnings("removal")
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

}
