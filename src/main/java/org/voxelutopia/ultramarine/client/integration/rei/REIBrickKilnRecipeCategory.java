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

        // 添加背景
        widgets.add(Widgets.createTexturedWidget(
                TEXTURE_GUI,
                startPoint.x,
                startPoint.y,
                0, 0,
                176, 70
        ));

        List<EntryIngredient> inputs = display.getInputEntries();
        List<EntryIngredient> outputs = display.getOutputEntries();

        // 主要输入槽 (46,17)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 46, startPoint.y + 9))
                .entries(inputs.get(BrickKilnMenu.SLOT_INPUT_PRIMARY))
                .markInput());

        // 次要输入槽 (66,17)
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 66, startPoint.y + 9))
                .entries(inputs.get(BrickKilnMenu.SLOT_INPUT_SECONDARY))
                .markInput());

// 添加燃料图标和进度条
        int cookingTime = display.getCookingTime();

// 添加燃料进度条
        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            // 燃烧动画参数
            long time = System.currentTimeMillis();
            int litDuration = 200;
            int litTime = (int)((time / 50) % litDuration);

            // 计算火焰应显示的高度（从 14px 逐渐减少到 0px）
            int fuelHeight = 14 - (litTime * 14 / litDuration); // 修正高度计算方向

            // 绘制燃料底图（灰色背景）
            graphics.blit(TEXTURE_GUI, startPoint.x + 57, startPoint.y + 28, 176, 0, 14, 14);

            // 动态绘制收缩的火焰（从底部向上消失）
            if (fuelHeight > 0) {
                graphics.blit(
                        TEXTURE_GUI,
                        startPoint.x + 57,                   // X 坐标不变
                        startPoint.y + 28 + (14 - fuelHeight), // Y 坐标向下偏移
                        176, 14 + (14 - fuelHeight),        // 源纹理坐标动态调整
                        14, fuelHeight                       // 仅绘制需要的部分高度
                );
            }
        }));

// 添加箭头，使用实际烹饪时间
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 79, startPoint.y + 26))
                .animationDurationTicks(cookingTime)); // 调整为更接近实际速度

        // 输出槽 (116,35)
        if (!outputs.isEmpty()) {
            widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 116, startPoint.y + 27)));
            widgets.add(Widgets.createSlot(new Point(startPoint.x + 116 - 1, startPoint.y + 27 - 1))
                    .entries(outputs.get(0))
                    .disableBackground()
                    .markOutput());
        }

        // 添加经验值显示
        if (display.getExperience() > 0) {
            widgets.add(Widgets.createLabel(new Point(startPoint.x + 116, startPoint.y + 58),
                            Component.translatable("gui.rei.experience", String.format("%.1f", display.getExperience())))
                    .color(0xFF808080, 0xFF808080)
                    .noShadow()
                    .leftAligned());
        }

        // 添加烹饪时间显示，转换为秒并四舍五入
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