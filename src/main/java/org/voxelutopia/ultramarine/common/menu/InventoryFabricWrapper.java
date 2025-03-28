package org.voxelutopia.ultramarine.common.menu;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.voxelutopia.ultramarine.common.inventory.FabricItemStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Flechazo
 * <p>
 * A wrapper class that adapts Minecraft's Inventory to Fabric's Storage API.
 * This class bridges the gap between traditional inventory systems and modern transfer APIs.
 */
public class InventoryFabricWrapper implements FabricItemStorage {
    private final Inventory inventory;

    public InventoryFabricWrapper(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        ItemStack stack = resource.toStack((int) maxAmount);
        long inserted = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack current = inventory.getItem(i);
            if (current.isEmpty()) {
                inventory.setItem(i, stack.copy());
                inserted = maxAmount;
                break;
            } else if (ItemStack.isSameItemSameComponents(current, stack)) {
                int space = Math.min(current.getMaxStackSize() - current.getCount(), stack.getCount());
                if (space > 0) {
                    current.grow(space);
                    inserted = space;
                    break;
                }
            }
        }
        return inserted;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        long extracted = 0;
        ItemStack toExtract = resource.toStack((int) maxAmount);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack current = inventory.getItem(i);
            if (ItemStack.isSameItemSameComponents(current, toExtract)) {
                int amount = Math.min(current.getCount(), (int) (maxAmount - extracted));
                current.shrink(amount);
                extracted += amount;
                if (extracted == maxAmount) break;
            }
        }
        return extracted;
    }

    @Override
    public Iterator<StorageView<ItemVariant>> iterator() {
        List<StorageView<ItemVariant>> views = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            final int slot = i;
            views.add(new StorageView<>() {
                @Override
                public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
                    ItemStack stack = inventory.getItem(slot);
                    if (resource.matches(stack)) {
                        int amount = Math.min(stack.getCount(), (int) maxAmount);
                        stack.shrink(amount);
                        return amount;
                    }
                    return 0;
                }

                @Override
                public boolean isResourceBlank() {
                    return getResource().isBlank();
                }

                @Override
                public ItemVariant getResource() {
                    return ItemVariant.of(inventory.getItem(slot));
                }

                @Override
                public long getAmount() {
                    return inventory.getItem(slot).getCount();
                }

                @Override
                public long getCapacity() {
                    return inventory.getItem(slot).getMaxStackSize();
                }
            });
        }
        return views.iterator();
    }

    @Override
    public int getContainerSize() {
        return inventory.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return inventory.getItem(slot);
    }

    @Override
    public @NotNull ItemStack removeItem(int slot, int count) {
        return inventory.removeItem(slot, count);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int slot) {
        return inventory.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, @NotNull ItemStack stack) {
        inventory.setItem(slot, stack);
    }

    @Override
    public void setChanged() {
        inventory.setChanged();
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        setItem(slot, stack);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true; // Player inventory accepts any item
    }

    @Override
    public void clearContent() {
        inventory.clearContent();
    }

    @Override
    public boolean stillValid(Player player) {
        return true; // Player's own inventory is always accessible
    }
    @Override
    public FabricItemStorage getResult() {
        return new SingleSlotStorage(this, 0);
    }

    @Override
    public FabricItemStorage getPrimaryInput() {
        return new SingleSlotStorage(this, 1);
    }

    @Override
    public FabricItemStorage getSecondaryInput() {
        return new SingleSlotStorage(this, 2);
    }

    private static class SingleSlotStorage implements FabricItemStorage {
        private final FabricItemStorage parent;
        private final int slot;

        public SingleSlotStorage(FabricItemStorage parent, int slot) {
            this.parent = parent;
            this.slot = slot;
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            if (!isItemValid(slot, resource.toStack())) return 0;
            ItemStack current = getItem(slot);
            if (current.isEmpty()) {
                setItem(slot, resource.toStack((int) Math.min(maxAmount, resource.getItem().getDefaultMaxStackSize())));
                return Math.min(maxAmount, resource.getItem().getDefaultMaxStackSize());
            } else if (ItemStack.isSameItemSameComponents(current, resource.toStack())) {
                int space = Math.min(current.getMaxStackSize() - current.getCount(), (int) maxAmount);
                if (space > 0) {
                    current.grow(space);
                    return space;
                }
            }
            return 0;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            ItemStack current = getItem(slot);
            if (resource.matches(current)) {
                int amount = Math.min(current.getCount(), (int) maxAmount);
                current.shrink(amount);
                return amount;
            }
            return 0;
        }

        @Override
        public Iterator<StorageView<ItemVariant>> iterator() {
            List<StorageView<ItemVariant>> views = new ArrayList<>();
            views.add(new StorageView<>() {
                @Override
                public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
                    ItemStack stack = getItem(slot);
                    if (resource.matches(stack)) {
                        int amount = Math.min(stack.getCount(), (int) maxAmount);
                        stack.shrink(amount);
                        return amount;
                    }
                    return 0;
                }

                @Override
                public boolean isResourceBlank() {
                    return getResource().isBlank();
                }

                @Override
                public ItemVariant getResource() {
                    return ItemVariant.of(getItem(slot));
                }

                @Override
                public long getAmount() {
                    return getItem(slot).getCount();
                }

                @Override
                public long getCapacity() {
                    return getItem(slot).getMaxStackSize();
                }
            });
            return views.iterator();
        }

        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return getItem(slot).isEmpty();
        }

        @Override
        public @NotNull ItemStack getItem(int index) {
            return parent.getItem(slot);
        }

        @Override
        public @NotNull ItemStack removeItem(int index, int count) {
            return parent.removeItem(slot, count);
        }

        @Override
        public @NotNull ItemStack removeItemNoUpdate(int index) {
            return parent.removeItemNoUpdate(slot);
        }

        @Override
        public void setItem(int index, @NotNull ItemStack stack) {
            parent.setItem(slot, stack);
        }

        @Override
        public void setChanged() {
            parent.setChanged();
        }

        @Override
        public boolean stillValid(Player player) {
            return parent.stillValid(player);
        }

        @Override
        public void clearContent() {
            setItem(slot, ItemStack.EMPTY);
        }

        @Override
        public void setStackInSlot(int index, ItemStack stack) {
            setItem(index, stack);
        }

        @Override
        public boolean isItemValid(int index, @NotNull ItemStack stack) {
            return parent.isItemValid(slot, stack);
        }

        @Override
        public FabricItemStorage getResult() {
            return null;
        }

        @Override
        public FabricItemStorage getPrimaryInput() {
            return null;
        }

        @Override
        public FabricItemStorage getSecondaryInput() {
            return null;
        }
    }
}