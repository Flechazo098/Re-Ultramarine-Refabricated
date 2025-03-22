package org.voxelutopia.ultramarine.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public interface IForgeContainerFactory<T extends AbstractContainerMenu> extends MenuType.MenuSupplier<T> {
    T create(int var1, Inventory var2, FriendlyByteBuf var3);

    default T create(int p_create_1_, Inventory p_create_2_) {
        return (T)this.create(p_create_1_, p_create_2_, (FriendlyByteBuf)null);
    }
}
