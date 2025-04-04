package org.voxelutopia.ultramarine.client.integration.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.init.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class REIWoodworkingRecipeCategory implements DisplayCategory<REIWoodworkingRecipeDisplay> {

    public static final ResourceLocation TEXTURE_GUI = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "textures/gui/rei_woodworking.png");

    public static final CategoryIdentifier<REIWoodworkingRecipeDisplay> WOODWORKING = CategoryIdentifier.of(Ultramarine.MOD_ID, "woodworking");

    @Override
    public CategoryIdentifier<? extends REIWoodworkingRecipeDisplay> getCategoryIdentifier() {
        return WOODWORKING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.rei.category.woodworking");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.WOODWORKING_WORKBENCH.asItem().getDefaultInstance());
    }

    @Override
    public List<Widget> setupDisplay(REIWoodworkingRecipeDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - 50, bounds.getCenterY() - 20);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createTexturedWidget(
                TEXTURE_GUI,
                startPoint.x,
                startPoint.y,
                109, 42
        ));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> outputs = display.getOutputEntries();

        if (!inputs.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 16, startPoint.y + 14))
                    .entries(inputs.getFirst())
                    .markInput());
        }


        if (!outputs.isEmpty()) {
            widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 73, startPoint.y + 14)));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 73 - 1, startPoint.y + 14 - 1))
                    .entries(outputs.getFirst())
                    .disableBackground()
                    .markOutput());
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 70;
    }

    @Override
    public int getDisplayWidth(REIWoodworkingRecipeDisplay display) {
        return 176;
    }
}