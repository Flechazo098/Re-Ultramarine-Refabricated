package com.voxelutopia.ultramarine.common.menu;

import com.voxelutopia.ultramarine.common.inventory.FabricItemStorage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Flechazo
 * A slot implementation for Fabric's Storage API.
 * This class provides a bridge between Minecraft's slot system and Fabric's Storage API.
 */
public class SlotFabricItemStorage extends Slot {
    protected final FabricItemStorage storage;
    protected final int index;

    public SlotFabricItemStorage(FabricItemStorage storage, int index, int x, int y) {
        super(storage, index, x, y);
        this.storage = storage;
        this.index = index;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return storage.isItemValid(index, stack);
    }

    @Override
    public @NotNull ItemStack getItem() {
        return storage.getStackInSlot(index);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        storage.setStackInSlot(index, stack);
    }

    @Override
    public void setChanged() {
        storage.setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return super.getMaxStackSize();
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        // Uses the smaller of the item's own maximum stacks and slots' maximum stacks
        return Math.min(getMaxStackSize(), stack.getMaxStackSize());
    }

    @Override
    public boolean mayPickup(Player player) {
        return true;
    }

}