package org.voxelutopia.ultramarine.init.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.voxelutopia.ultramarine.Ultramarine;

public class ModItemTags {

    // 模组自定义标签（使用自己的命名空间）
    public static final TagKey<Item> POLISHED_PLANKS = modTag("polished_planks");
    public static final TagKey<Item> DYE_POWDER = modTag("dye_powder");
    public static final TagKey<Item> PARTS = modTag("parts");
    public static final TagKey<Item> CHISEL_TEMPLATES = modTag("chisel_templates");
    public static final TagKey<Item> PAINTING_SCROLL_ITEMS = modTag("painting_scroll_items");

    // 使用 Fabric 社区通用标签（c:dyes）
    public static final TagKey<Item> DYES = commonTag("dyes");
    public static final TagKey<Item> WHITE_DYES = commonTag("white_dyes");
    public static final TagKey<Item> ORANGE_DYES = commonTag("orange_dyes");
    public static final TagKey<Item> MAGENTA_DYES = commonTag("magenta_dyes");
    public static final TagKey<Item> LIGHT_BLUE_DYES = commonTag("light_blue_dyes");
    public static final TagKey<Item> YELLOW_DYES = commonTag("yellow_dyes");
    public static final TagKey<Item> LIME_DYES = commonTag("lime_dyes");
    public static final TagKey<Item> PINK_DYES = commonTag("pink_dyes");
    public static final TagKey<Item> GRAY_DYES = commonTag("gray_dyes");
    public static final TagKey<Item> LIGHT_GRAY_DYES = commonTag("light_gray_dyes");
    public static final TagKey<Item> CYAN_DYES = commonTag("cyan_dyes");
    public static final TagKey<Item> PURPLE_DYES = commonTag("purple_dyes");
    public static final TagKey<Item> BLUE_DYES = commonTag("blue_dyes");
    public static final TagKey<Item> BROWN_DYES = commonTag("brown_dyes");
    public static final TagKey<Item> GREEN_DYES = commonTag("green_dyes");
    public static final TagKey<Item> RED_DYES = commonTag("red_dyes");
    public static final TagKey<Item> BLACK_DYES = commonTag("black_dyes");

    // 标签创建方法
    private static TagKey<Item> commonTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path)); // 社区通用标签
    }

    private static TagKey<Item> modTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, path)); // 模组私有标签
    }
}
