package org.voxelutopia.ultramarine.init.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.voxelutopia.ultramarine.Ultramarine;

public class ModItemTags {

    public static final TagKey<Item> POLISHED_PLANKS = modTag("polished_planks");

    private static TagKey<Item> modTag(String path) {
        return bind(path);
    }

    private static TagKey<Item> bind(String string) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, string));
    }
}
