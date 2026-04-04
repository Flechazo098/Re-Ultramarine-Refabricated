package com.voxelutopia.ultramarine.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class FabricItemStorageContainer implements Container {
    private final FabricItemStorage storage;

    public FabricItemStorageContainer(FabricItemStorage storage) {
        this.storage = storage;
    }

    @Override
    public int getContainerSize() {
        return storage.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return storage.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return storage.removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return storage.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        storage.setItem(slot, itemStack);
    }

    @Override
    public void setChanged() {
        storage.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return storage.stillValid(player);
    }

    @Override
    public void clearContent() {
        storage.clearContent();
    }
}

