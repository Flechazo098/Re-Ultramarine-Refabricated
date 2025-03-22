package org.voxelutopia.ultramarine.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface IForgeMenuType<T> {
    static <T extends AbstractContainerMenu> MenuType<T> create(IForgeContainerFactory<T> factory) {
        return new MenuType(factory, FeatureFlags.DEFAULT_FLAGS);
    }

    T create(int var1, Inventory var2, FriendlyByteBuf var3);
}
