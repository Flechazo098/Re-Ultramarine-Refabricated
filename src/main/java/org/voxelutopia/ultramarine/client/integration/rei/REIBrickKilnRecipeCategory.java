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
import org.voxelutopia.ultramarine.common.menu.BrickKilnMenu;
import org.voxelutopia.ultramarine.init.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;

public class REIBrickKilnRecipeCategory implements DisplayCategory<REIBrickKilnRecipeDisplay> {

    public static final ResourceLocation TEXTURE_GUI = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "textures/gui/rei_brick_kiln.png");

    public static final CategoryIdentifier<REIBrickKilnRecipeDisplay> BRICK_KILN = CategoryIdentifier.of(Ultramarine.MOD_ID, "brick_kiln");

    @Override
    public CategoryIdentifier<? extends REIBrickKilnRecipeDisplay> getCategoryIdentifier() {
        return BRICK_KILN;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.rei.category.composite_smelting");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.BRICK_KILN.asItem().getDefaultInstance());
    }

    @Override
    public List<Widget> setupDisplay(REIBrickKilnRecipeDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 35);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(Widgets.createTexturedWidget(
                TEXTURE_GUI,
                startPoint.x,
                startPoint.y,
                0, 0,
                176, 70
        ));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> outputs = display.getOutputEntries();

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 46, startPoint.y + 9))
                .entries(inputs.getFirst())
                .markInput());

        widgets.add(Widgets.createSlot(new Point(startPoint.x + 66, startPoint.y + 9))
                .entries(inputs.get(BrickKilnMenu.SLOT_INPUT_SECONDARY))
                .markInput());

        int cookingTime = display.getCookingTime();

        widgets.add(Widgets.createBurningFire(new Point(startPoint.x + 57, startPoint.y + 28)).animationDurationMS(10000.0F));

        widgets.add(Widgets.createArrow(new Point(startPoint.x + 79, startPoint.y + 26))
                .animationDurationTicks(cookingTime));

        if (!outputs.isEmpty()) {
            widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 116, startPoint.y + 27)));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 116 - 1, startPoint.y + 27 - 1))
                    .entries(outputs.getFirst())
                    .disableBackground()
                    .markOutput());
        }

        if (display.getExperience() > 0) {
            widgets.add(Widgets.createLabel(new Point(startPoint.x + 116, startPoint.y + 58),
                            Component.translatable("gui.rei.experience", String.format("%.1f", display.getExperience())))
                    .color(0xFF808080, 0xFF808080)
                    .noShadow()
                    .leftAligned());
        }

        float cookingTimeInSeconds = Math.round(cookingTime / 20.0f * 10) / 10.0f;
        widgets.add(Widgets.createLabel(new Point(startPoint.x + 92, startPoint.y + 58),
                        Component.literal(cookingTimeInSeconds + "s"))
                .color(0xFF808080, 0xFF808080)
                .noShadow()
                .centered());

        return widgets;
    }

    @Override
    public int getDisplayHeight() {
        return 70;
    }

    @Override
    public int getDisplayWidth(REIBrickKilnRecipeDisplay display) {
        return 176;
    }
}