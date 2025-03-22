package org.voxelutopia.ultramarine.init.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.voxelutopia.ultramarine.Ultramarine;

public class ModItemTags {

    public static final TagKey<Item> POLISHED_PLANKS = modTag("polished_planks");
    public static final TagKey<Item> DYE_POWDER = modTag("dye_powder");
    public static final TagKey<Item> PARTS = modTag("parts");
    public static final TagKey<Item> CHISEL_TEMPLATES = modTag("chisel_templates");
    public static final TagKey<Item> PAINTING_SCROLL_ITEMS = modTag("painting_scroll_items");

    public static final TagKey<Item> FORGE_WHITE_DYE = fabricTag("dyes/white");
    public static final TagKey<Item> FORGE_ORANGE_DYE = fabricTag("dyes/orange");
    public static final TagKey<Item> FORGE_MAGENTA_DYE = fabricTag("dyes/magenta");
    public static final TagKey<Item> FORGE_LIGHT_BLUE_DYE = fabricTag("dyes/light_blue");
    public static final TagKey<Item> FORGE_YELLOW_DYE = fabricTag("dyes/yellow");
    public static final TagKey<Item> FORGE_LIME_DYE = fabricTag("dyes/lime");
    public static final TagKey<Item> FORGE_PINK_DYE = fabricTag("dyes/pink");
    public static final TagKey<Item> FORGE_GRAY_DYE = fabricTag("dyes/gray");
    public static final TagKey<Item> FORGE_LIGHT_GRAY_DYE = fabricTag("dyes/light_gray");
    public static final TagKey<Item> FORGE_CYAN_DYE = fabricTag("dyes/cyan");
    public static final TagKey<Item> FORGE_PURPLE_DYE = fabricTag("dyes/purple");
    public static final TagKey<Item> FORGE_BLUE_DYE = fabricTag("dyes/blue");
    public static final TagKey<Item> FORGE_BROWN_DYE = fabricTag("dyes/brown");
    public static final TagKey<Item> FORGE_GREEN_DYE = fabricTag("dyes/green");
    public static final TagKey<Item> FORGE_RED_DYE = fabricTag("dyes/red");
    public static final TagKey<Item> FORGE_BLACK_DYE = fabricTag("dyes/black");
    public static final TagKey<Item> FORGE_DYES = fabricTag("dyes");


    private static TagKey<Item> modTag(String path) {
        return bind(path);
    }
    private static TagKey<Item> fabricTag(String path) {
        return bind("fabric/" +path);
    }

    private static TagKey<Item> bind(String string) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, string));
    }
}
