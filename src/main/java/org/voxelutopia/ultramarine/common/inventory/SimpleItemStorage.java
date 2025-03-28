package org.voxelutopia.ultramarine.common.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
            return insertedAmount;
        } else if (variant.equals(insertedVariant)) {
            long insertedAmount = Math.min(maxAmount, capacity - amount);
            if (insertedAmount > 0) {
                updateSnapshots(transaction);
                amount += insertedAmount;
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
                if (amount == 0) {
                    variant = ItemVariant.blank();
                }
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
        ItemVariant current = variant;
        long extracted = extract(current, count, null);
        return current.toStack((int) extracted);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        setItem(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        if (stack.isEmpty()) {
            variant = ItemVariant.blank();
            amount = 0;
        } else {
            variant = ItemVariant.of(stack);
            amount = stack.getCount();
        }
        setChanged();
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        setItem(slot, stack);
    }

    @Override
    public void setChanged() {
        // Override in subclasses if needed
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
        // 保存当前状态的快照
        ItemVariant originalVariant = variant;
        long originalAmount = amount;
        
        transaction.addCloseCallback((t, result) -> {
            if (result.wasAborted()) {  // 使用wasAborted()方法检查事务结果
                // 如果事务被中止，恢复到原始状态
                variant = originalVariant;
                amount = originalAmount;
            }
        });
    }
}