package org.voxelutopia.ultramarine.common.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * @author Flechazo
 * <p>
 * A wrapper interface that combines Fabric's Storage API with Minecraft's Container interface.
 * This allows us to use both modern transfer API features and traditional container functionality.
 */
public interface FabricItemStorage extends Storage<ItemVariant>, Container {
    /**
     * Gets an item from the storage at the specified slot.
     * This is a convenience method that wraps the Storage API's view functionality.
     *
     * @param slot The slot index to query
     * @return The ItemStack in the specified slot
     */
    default ItemStack getStackInSlot(int slot) {
        int currentSlot = 0;
        for (StorageView<ItemVariant> view : this) {
            if (currentSlot == slot) {
                return view.getResource().toStack((int) view.getAmount());
            }
            currentSlot++;
        }
        return ItemStack.EMPTY;
    }

    /**
     * Sets an item in the storage at the specified slot.
     * This is a convenience method that wraps the Storage API's insert/extract functionality.
     *
     * @param slot The slot index to modify
     * @param stack The ItemStack to set
     */
    void setStackInSlot(int slot, ItemStack stack);

    /**
     * Checks if the storage can accept the given item in the specified slot.
     *
     * @param slot The slot index to check
     * @param stack The ItemStack to test
     * @return true if the item can be inserted, false otherwise
     */
    boolean isItemValid(int slot, ItemStack stack);

    /**
     * Gets the storage of the result slot
     * @return Storage of result slots
     */
    FabricItemStorage getResult();

    /**
     * Gets the storage of the main input slots
     * @return Storage of the main input slots
     */
    FabricItemStorage getPrimaryInput();

    /**
     * Gets the storage of the secondary input slots
     * @return Storage of secondary input slots
     */
    FabricItemStorage getSecondaryInput();

    /**
     * Set the associated block entity
     * @param blockEntity Block Entity
     */
    void setBlockEntity(BlockEntity blockEntity);

    /**
     * Gets the associated block entity
     * @return The associated block entity
     */
    BlockEntity getBlockEntity();
}