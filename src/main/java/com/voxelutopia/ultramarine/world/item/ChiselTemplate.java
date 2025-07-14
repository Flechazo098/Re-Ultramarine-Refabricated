package com.voxelutopia.ultramarine.world.item;

import net.minecraft.world.item.Item;

public class ChiselTemplate extends Item {

    public ChiselTemplate() {
        super(new Item.Properties());
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

}
