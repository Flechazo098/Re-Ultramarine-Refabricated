package org.voxelutopia.ultramarine.common.wrapper;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.ItemStack;

public class RecipeWrapper implements RecipeInput {
    private final Container container;

    public RecipeWrapper(Container container) {
        this.container = container;
    }

    @Override
    public int size() {
        return container.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return container.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return container.getItem(slot);
    }
}