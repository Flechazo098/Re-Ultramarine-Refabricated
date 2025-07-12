package com.voxelutopia.ultramarine.common.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Flechazo
 * <p>
 * A class that combines multiple FabricItemStorage instances into one.
 * This is useful for block entities that have multiple storage slots.
 */
public class CombinedStorage implements FabricItemStorage {
    protected final FabricItemStorage[] itemStorages;
    protected BlockEntity blockEntity;

    public CombinedStorage(FabricItemStorage... itemStorages) {
        this.itemStorages = itemStorages;
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        long inserted = 0;
        for (FabricItemStorage storage : itemStorages) {
            inserted += storage.insert(resource, maxAmount - inserted, transaction);
            if (inserted == maxAmount) break;
        }
        return inserted;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        long extracted = 0;
        for (FabricItemStorage storage : itemStorages) {
            extracted += storage.extract(resource, maxAmount - extracted, transaction);
            if (extracted == maxAmount) break;
        }
        return extracted;
    }

    @Override
    public Iterator<StorageView<ItemVariant>> iterator() {
        List<StorageView<ItemVariant>> views = new ArrayList<>();
        for (FabricItemStorage storage : itemStorages) {
            storage.forEach(views::add);
        }
        return views.iterator();
    }

    @Override
    public int getContainerSize() {
        int size = 0;
        for (FabricItemStorage storage : itemStorages) {
            size += storage.getContainerSize();
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        for (FabricItemStorage storage : itemStorages) {
            if (!storage.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        int currentSlot = 0;
        for (FabricItemStorage storage : itemStorages) {
            int storageSize = storage.getContainerSize();
            if (currentSlot + storageSize > slot) {
                return storage.getItem(slot - currentSlot);
            }
            currentSlot += storageSize;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int count) {
        int currentSlot = 0;
        for (FabricItemStorage storage : itemStorages) {
            int storageSize = storage.getContainerSize();
            if (currentSlot + storageSize > slot) {
                return storage.removeItem(slot - currentSlot, count);
            }
            currentSlot += storageSize;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        int currentSlot = 0;
        for (FabricItemStorage storage : itemStorages) {
            int storageSize = storage.getContainerSize();
            if (currentSlot + storageSize > slot) {
                return storage.removeItemNoUpdate(slot - currentSlot);
            }
            currentSlot += storageSize;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        int currentSlot = 0;
        for (FabricItemStorage storage : itemStorages) {
            int storageSize = storage.getContainerSize();
            if (currentSlot + storageSize > slot) {
                storage.setItem(slot - currentSlot, stack);
                return;
            }
            currentSlot += storageSize;
        }
    }

    /**
     * Set the associated block entity
     *
     * @param blockEntity Block entities
     */
    @Override
    public void setBlockEntity(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
        // 将方块实体传递给所有子存储
        for (FabricItemStorage storage : itemStorages) {
            if (storage != null) {
                storage.setBlockEntity(blockEntity);
            }
        }
    }

    /**
     * Gets the associated block entity
     *
     * @return The associated block entity
     */
    @Override
    public BlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    /**
     * 标记方块实体为已更改
     */
    @Override
    public void setChanged() {
        if (this.blockEntity != null) {
            this.blockEntity.setChanged();
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        setItem(slot, stack);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        int currentSlot = 0;
        for (FabricItemStorage storage : itemStorages) {
            int storageSize = storage.getContainerSize();
            if (currentSlot + storageSize > slot) {
                return storage.isItemValid(slot - currentSlot, stack);
            }
            currentSlot += storageSize;
        }
        return false;
    }

    @Override
    public void clearContent() {
        for (FabricItemStorage storage : itemStorages) {
            storage.clearContent();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        for (FabricItemStorage storage : itemStorages) {
            if (!storage.stillValid(player)) return false;
        }
        return true;
    }

    @Override
    public FabricItemStorage getResult() {
        if (itemStorages.length > 0) {
            return itemStorages[itemStorages.length - 1];
        }
        return null;
    }

    @Override
    public FabricItemStorage getPrimaryInput() {
        if (itemStorages.length > 0) {
            return itemStorages[0];
        }
        return null;
    }

    @Override
    public FabricItemStorage getSecondaryInput() {
        if (itemStorages.length > 1) {
            return itemStorages[1];
        }
        return null;
    }
}