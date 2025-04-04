package org.voxelutopia.ultramarine.common.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.impl.transfer.item.ItemVariantImpl;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

/**
 * @author Flechazo
 * A simple implementation of FabricItemStorage that stores a single ItemStack.
 * This class provides basic item storage functionality using Fabric's Storage API.
 */
public class SimpleItemStorage implements FabricItemStorage, SingleSlotStorage<ItemVariant> {
    protected ItemVariant variant = ItemVariant.blank();
    protected long amount;
    protected final long capacity;
    protected BlockEntity blockEntity;

    public SimpleItemStorage(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public long insert(ItemVariant insertedVariant, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(insertedVariant, maxAmount);

        if (!isItemValid(0, insertedVariant.toStack())) {
            return 0;
        }

        if (variant.isBlank()) {
            long insertedAmount = Math.min(maxAmount, capacity);
            updateSnapshots(transaction);
            variant = insertedVariant;
            amount = insertedAmount;
            setChanged();
            return insertedAmount;
        } else if (variant.equals(insertedVariant)) {
            long insertedAmount = Math.min(maxAmount, capacity - amount);
            if (insertedAmount > 0) {
                updateSnapshots(transaction);
                amount += insertedAmount;
                setChanged();
            }
            return insertedAmount;
        }

        return 0;
    }

    @Override
    public long extract(ItemVariant extractedVariant, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(extractedVariant, maxAmount);

        if (variant.equals(extractedVariant)) {
            long extractedAmount = Math.min(maxAmount, amount);
            if (extractedAmount > 0) {
                updateSnapshots(transaction);
                amount -= extractedAmount;
                if (amount <= 0) {
                    variant = ItemVariant.blank();
                    amount = 0;
                }
                // 确保状态更新被保存
                setChanged();
            }

            return extractedAmount;
        }

        return 0;
    }

    @Override
    public boolean isResourceBlank () {
        return false;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @Override
    public FabricItemStorage getResult () {
        return null;
    }

    @Override
    public FabricItemStorage getPrimaryInput () {
        return null;
    }

    @Override
    public FabricItemStorage getSecondaryInput () {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return amount == 0;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return variant.toStack((int) amount);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int count) {
        if (count <= 0 || variant.isBlank() || amount <= 0) {
            return ItemStack.EMPTY;
        }

        // 计算实际可以提取的数量
        int actualCount = (int) Math.min(count, amount);

        // 创建要返回的物品堆
        ItemStack result = variant.toStack(actualCount);

        // 更新存储状态
        amount -= actualCount;
        if (amount <= 0) {
            variant = ItemVariant.blank();
            amount = 0;
        }

        // 确保状态更新被保存
        setChanged();
        return result;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        setItem(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        if (slot != 0) return; // 只处理第一个槽位

        // 保存旧状态用于调试
        ItemVariant oldVariant = this.variant;
        long oldAmount = this.amount;

        if (stack.isEmpty()) {
            this.variant = ItemVariant.blank();
            this.amount = 0;
        } else {
            // 确保使用正确的方法创建 ItemVariant
            this.variant = ItemVariant.of(stack);
            this.amount = stack.getCount();
        }

        // 如果状态发生变化，标记为已更改
        if (!this.variant.equals(oldVariant) || this.amount != oldAmount) {
            setChanged();
        }
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        setItem(slot, stack);
    }

    /**
     * Set the associated block entity
     * @param blockEntity Block entities
     */
    @Override
    public void setBlockEntity(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    /**
     * Gets the associated block entity
     * @return The associated block entity
     */
    @Override
    public BlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    /**
     * Mark the block entity as changed
     */
    @Override
    public void setChanged() {
        if (this.blockEntity != null) {
            this.blockEntity.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        variant = ItemVariant.blank();
        amount = 0;
        setChanged();
    }

    @Override
    public Iterator<StorageView<ItemVariant>> iterator() {
        return List.of(this.getUnderlyingView()).iterator();
    }

    @Override
    public ItemVariant getResource() {
        return variant;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    protected void updateSnapshots(TransactionContext transaction) {
        if (transaction == null) return;
        // Save a snapshot of the current state
        ItemVariant originalVariant = variant;
        long originalAmount = amount;
        
        transaction.addCloseCallback((t, result) -> {
            if (result.wasAborted()) {  // check the transaction result
                // If the transaction is aborted, it reverts to its original state
                variant = originalVariant;
                amount = originalAmount;
            }
        });
    }
}