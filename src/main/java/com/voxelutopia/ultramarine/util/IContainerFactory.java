package com.voxelutopia.ultramarine.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface IContainerFactory<T extends AbstractContainerMenu> extends MenuType.MenuSupplier<T> {
    T create(int containerId, Inventory playerInventory, FriendlyByteBuf extraData);

    default T create(int containerId, Inventory playerInventory) {
        return this.create(containerId, playerInventory, null);
    }
}