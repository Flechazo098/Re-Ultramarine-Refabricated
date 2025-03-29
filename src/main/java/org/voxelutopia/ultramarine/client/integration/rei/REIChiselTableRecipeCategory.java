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

import static org.voxelutopia.ultramarine.common.menu.ChiselTableMenu.*;

public class REIChiselTableRecipeCategory implements DisplayCategory<REIChiselTableRecipeDisplay> {

    public static final ResourceLocation TEXTURE_GUI = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "textures/gui/rei_chisel_table.png");

    public static final CategoryIdentifier<REIChiselTableRecipeDisplay> CHISEL_TABLE = CategoryIdentifier.of(Ultramarine.MOD_ID, "chisel_table");

    // 添加自定义槽位大小常量
    private static final int MATERIAL_SLOT_SIZE = 18;
    private static final int TEMPLATE_SLOT_SIZE = 18;
    private static final int COLOR_SLOT_SIZE = 18;
    private static final int OUTPUT_SLOT_SIZE = 26; // 更大的输出槽

    @Override
    public CategoryIdentifier<? extends REIChiselTableRecipeDisplay> getCategoryIdentifier () {
        return CHISEL_TABLE;
    }

    @Override
    public Component getTitle () {
        return Component.translatable("gui.rei.category.chisel_table");
    }

    @Override
    public Renderer getIcon () {
        return EntryStacks.of(ModBlocks.CHISEL_TABLE.asItem().getDefaultInstance());
    }

    @Override
    public List<Widget> setupDisplay (REIChiselTableRecipeDisplay display, Rectangle bounds) {
        final Point startPoint = new Point(bounds.getCenterX() - 87, bounds.getCenterY() - 45);
        List<Widget> widgets = new ArrayList<>();

        // 添加背景
        widgets.add(Widgets.createTexturedWidget(
                TEXTURE_GUI,
                startPoint.x, // 背景图在 REI 界面中的 X 偏移
                startPoint.y, // 背景图在 REI 界面中的 Y 偏移
                176, 81// 贴图尺寸
        ));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> outputs = display.getOutputEntries();

        // 材料输入槽 (26,25)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 26, startPoint.y + 25))
                .entries(inputs.get(SLOT_MATERIAL))
                .markInput());

        // 模板输入槽 (53,25)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 53, startPoint.y + 25))
                .entries(inputs.get(SLOT_TEMPLATE))
                .markInput());

        // 颜色输入槽 (26 + i*18, 52)
        for (int i = 0; i < 4; i++) {
            int slotIndex = SLOT_COLOR_START + i;
            if (slotIndex < inputs.size()) {
                int xPos = 26 + i * 18;
                widgets.add(Widgets.createSlot(new Point(startPoint.x + xPos, startPoint.y + 52))
                        .entries(inputs.get(slotIndex))
                        .markInput());
            }
        }

        // 输出槽 (130,34) - 使用自定义大小
        if (!outputs.isEmpty()) {
            // 创建大型输出槽
            widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 130, startPoint.y + 34)));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 130 - 1, startPoint.y + 34 - 1))
                    .entries(outputs.get(0))
                    .disableBackground()
                    .markOutput());
        }

        return widgets;
    }

    @Override
    public int getDisplayHeight () {
        return 90;
    }

    @Override
    public int getDisplayWidth (REIChiselTableRecipeDisplay display) {
        return 79;
    }
}