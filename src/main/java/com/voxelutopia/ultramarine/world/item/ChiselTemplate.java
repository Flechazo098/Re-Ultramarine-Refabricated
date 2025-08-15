package com.voxelutopia.ultramarine.world.item;

import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class ChiselTemplate extends Item {

    public ChiselTemplate() {
        super(new Item.Properties());
    }

    @Override
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public @Nullable Item getCraftingRemainingItem() {
        return this;
    }
}
