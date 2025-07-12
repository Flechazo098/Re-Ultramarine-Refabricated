package com.voxelutopia.ultramarine.client.integration.rei;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
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

import java.util.ArrayList;
import java.util.List;

import static com.voxelutopia.ultramarine.common.menu.ChiselTableMenu.SLOT_COLOR_START;
import static com.voxelutopia.ultramarine.common.menu.ChiselTableMenu.SLOT_TEMPLATE;

public class REIChiselTableRecipeCategory implements DisplayCategory<REIChiselTableRecipeDisplay> {

    public static final ResourceLocation TEXTURE_GUI = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "textures/gui/rei_chisel_table.png");

    public static final CategoryIdentifier<REIChiselTableRecipeDisplay> CHISEL_TABLE = CategoryIdentifier.of(Ultramarine.MOD_ID, "chisel_table");

    @Override
    public CategoryIdentifier<? extends REIChiselTableRecipeDisplay> getCategoryIdentifier() {
        return CHISEL_TABLE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.rei.category.chisel_table");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.CHISEL_TABLE.asItem().getDefaultInstance());
    }

    @Override
    public List<Widget> setupDisplay(REIChiselTableRecipeDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 45);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createTexturedWidget(
                TEXTURE_GUI,
                startPoint.x,
                startPoint.y,
                176, 81
        ));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> outputs = display.getOutputEntries();

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 26, startPoint.y + 25))
                .entries(inputs.getFirst())
                .markInput());

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 53, startPoint.y + 25))
                .entries(inputs.get(SLOT_TEMPLATE))
                .markInput());

        for (int i = 0; i < 4; i++) {
            int slotIndex = SLOT_COLOR_START + i;
            if (slotIndex < inputs.size()) {
                int xPos = 26 + i * 18;
                widgets.add(Widgets.createSlot(new Point(startPoint.x + xPos, startPoint.y + 52))
                        .entries(inputs.get(slotIndex))
                        .markInput());
            }
        }

        if (!outputs.isEmpty()) {
            widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 130, startPoint.y + 34)));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 130 - 1, startPoint.y + 34 - 1))
                    .entries(outputs.getFirst())
                    .disableBackground()
                    .markOutput());
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 90;
    }

    @Override
    public int getDisplayWidth(REIChiselTableRecipeDisplay display) {
        return 79;
    }
}