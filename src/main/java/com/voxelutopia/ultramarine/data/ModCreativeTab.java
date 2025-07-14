package com.voxelutopia.ultramarine.data;

import com.voxelutopia.ultramarine.data.registry.CreativeTabRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.*;

public enum ModCreativeTab {
    MATERIALS(CreativeTabRegistry.MATERIALS),
    TOOLS(CreativeTabRegistry.TOOLS),
    BUILDING_BLOCKS(CreativeTabRegistry.BUILDING_BLOCKS),
    DECORATIVE_BLOCKS(CreativeTabRegistry.DECORATIVE_BLOCKS),
    DECORATIONS(CreativeTabRegistry.DECORATIONS),
    FURNITURE(CreativeTabRegistry.FURNITURE),
    WINDOWS_AND_DOORS(CreativeTabRegistry.WINDOWS_AND_DOORS),
    PLANTS(CreativeTabRegistry.PLANTS),
    LAMPS(CreativeTabRegistry.LAMPS);

    public static final Map<ModCreativeTab, Set<Item>> itemSets = new LinkedHashMap<>();

    static {
        Arrays.stream(ModCreativeTab.values()).forEach(tab ->
                itemSets.put(tab, new LinkedHashSet<>())
        );
    }

    final CreativeModeTab tab;

    ModCreativeTab(CreativeModeTab tab) {
        this.tab = tab;
    }

    public static void putItemInSet(Item item, ModCreativeTab tab) {
        itemSets.get(tab).add(item);
    }

    public CreativeModeTab getTab() {
        return tab;
    }
}
