package org.voxelutopia.ultramarine.init.data;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.voxelutopia.ultramarine.init.registry.ModCreativeTabs;

import java.util.*;

public enum CreativeTabData {
    MATERIALS(ModCreativeTabs.MATERIALS),
    TOOLS(ModCreativeTabs.TOOLS),
    BUILDING_BLOCKS(ModCreativeTabs.BUILDING_BLOCKS),
    DECORATIVE_BLOCKS(ModCreativeTabs.DECORATIVE_BLOCKS),
    DECORATIONS(ModCreativeTabs.DECORATIONS),
    FURNITURE(ModCreativeTabs.FURNITURE),
    WINDOWS_AND_DOORS(ModCreativeTabs.WINDOWS_AND_DOORS),
    PLANTS(ModCreativeTabs.PLANTS),
    LAMPS(ModCreativeTabs.LAMPS);

    public static final Map<CreativeTabData, Set<Item>> itemSets = new LinkedHashMap<>();

    static {
        Arrays.stream(CreativeTabData.values()).forEach(tab ->
                itemSets.put(tab, new LinkedHashSet<>())
        );
    }
    final CreativeModeTab tab;

    CreativeTabData(CreativeModeTab tab) {
        this.tab = tab;
    }

    public static void putItemInSet(Item item, CreativeTabData tab) {
        itemSets.get(tab).add(item);
    }

    public CreativeModeTab getTab() {
        return tab;
    }
}
