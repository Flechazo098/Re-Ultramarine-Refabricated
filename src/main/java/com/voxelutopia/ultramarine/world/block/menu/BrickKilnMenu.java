package com.voxelutopia.ultramarine.world.block.menu;

import com.voxelutopia.ultramarine.data.registry.BlockRegistry;
import com.voxelutopia.ultramarine.data.registry.MenuTypeRegistry;
import com.voxelutopia.ultramarine.data.registry.RecipeTypeRegistry;
import com.voxelutopia.ultramarine.world.block.entity.BrickKilnBlockEntity;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlottedStackStorage;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class BrickKilnMenu extends AbstractContainerMenu {

    public static final int SLOT_INPUT_PRIMARY = BrickKilnBlockEntity.SLOT_INPUT_PRIMARY; //0
    public static final int SLOT_INPUT_SECONDARY = BrickKilnBlockEntity.SLOT_INPUT_SECONDARY; //1
    public static final int SLOT_FUEL = BrickKilnBlockEntity.SLOT_FUEL; //2
    public static final int SLOT_RESULT = BrickKilnBlockEntity.SLOT_RESULT; //3
    private static final int INV_SLOT_START = 4;
    private static final int INV_SLOT_END = 31;
    private static final int USE_ROW_SLOT_START = 31;
    private static final int USE_ROW_SLOT_END = 40;
    private final BlockEntity blockEntity;
    private final Player playerEntity;
    private final ContainerData data;

    public static class InventoryWrapper implements SlottedStackStorage {
        private final Inventory inventory;

        public InventoryWrapper(Inventory inventory) {
            this.inventory = inventory;
        }

        @Override
        public int getSlotCount() {
            return inventory.getContainerSize();
        }

        @Override
        public SingleSlotStorage<ItemVariant> getSlot(int slot) {
            if (slot < 0 || slot >= inventory.getContainerSize()) {
                return null;
            }

            return new SingleSlotStorage<>() {
                @Override
                public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
                    ItemStack stack = inventory.getItem(slot);
                    if (stack.isEmpty()) {
                        int toInsert = (int) Math.min(maxAmount, inventory.getMaxStackSize());
                        inventory.setItem(slot, resource.toStack(toInsert));
                        return toInsert;
                    } else if (ItemVariant.of(stack).equals(resource)) {
                        int canInsert = Math.min((int) maxAmount, inventory.getMaxStackSize() - stack.getCount());
                        if (canInsert > 0) {
                            stack.grow(canInsert);
                            return canInsert;
                        }
                    }
                    return 0;
                }

                @Override
                public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
                    ItemStack stack = inventory.getItem(slot);
                    if (!stack.isEmpty() && ItemVariant.of(stack).equals(resource)) {
                        int toExtract = (int) Math.min(maxAmount, stack.getCount());
                        stack.shrink(toExtract);
                        if (stack.isEmpty()) {
                            inventory.setItem(slot, ItemStack.EMPTY);
                        }
                        return toExtract;
                    }
                    return 0;
                }

                @Override
                public boolean isResourceBlank() {
                    return inventory.getItem(slot).isEmpty();
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
                    return inventory.getMaxStackSize();
                }
            };
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return inventory.getItem(slot);
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            inventory.setItem(slot, stack);
        }

        @Override
        public int getSlotLimit(int slot) {
            return inventory.getMaxStackSize();
        }

        @Override
        public boolean isItemValid(int slot, ItemVariant resource, int count) {
            return true;
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            long inserted = 0;
            for (int i = 0; i < inventory.getContainerSize() && inserted < maxAmount; i++) {
                SingleSlotStorage<ItemVariant> slot = getSlot(i);
                if (slot != null) {
                    inserted += slot.insert(resource, maxAmount - inserted, transaction);
                }
            }
            return inserted;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            long extracted = 0;
            for (int i = 0; i < inventory.getContainerSize() && extracted < maxAmount; i++) {
                SingleSlotStorage<ItemVariant> slot = getSlot(i);
                if (slot != null) {
                    extracted += slot.extract(resource, maxAmount - extracted, transaction);
                }
            }
            return extracted;
        }
    }

    public BrickKilnMenu(int pId, BlockPos pos, Inventory inventory) {
        this(pId, pos, inventory, new ItemStackHandler(4), new SimpleContainerData(4));
    }

    public BrickKilnMenu(int id, BlockPos pos, Inventory inventory, SlottedStackStorage container, ContainerData containerData) {
        super(MenuTypeRegistry.BRICK_KILN, id);
        this.playerEntity = inventory.player;
        this.blockEntity = playerEntity.getCommandSenderWorld().getBlockEntity(pos);
        SlottedStackStorage inventory1 = new InventoryWrapper(inventory);
        this.data = containerData;

        this.addSlot(new IngredientSlot(container, SLOT_INPUT_PRIMARY, 46, 17));
        this.addSlot(new IngredientSlot(container, SLOT_INPUT_SECONDARY, 66, 17));
        this.addSlot(new FuelSlot(container, SLOT_FUEL, 56, 53));
        this.addSlot(new OutputSlot(container, SLOT_RESULT, 116, 35));

        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new SlotItemHandler(inventory1, c + r * 9 + 9, 8 + c * 18, 84 + r * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new SlotItemHandler(inventory1, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.data);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();
            if (pIndex == SLOT_RESULT) {
                if (!this.moveItemStackTo(slotItem, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotItem, itemstack);
            } else if (pIndex != SLOT_FUEL && pIndex != SLOT_INPUT_PRIMARY && pIndex != SLOT_INPUT_SECONDARY) {
                if (this.canProcess(slotItem)) {
                    if (!this.moveItemStackTo(slotItem, SLOT_INPUT_PRIMARY, SLOT_INPUT_SECONDARY + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isFuel(slotItem)) {
                    if (!this.moveItemStackTo(slotItem, SLOT_FUEL, SLOT_FUEL + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= INV_SLOT_START && pIndex < INV_SLOT_END) {
                    if (!this.moveItemStackTo(slotItem, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex >= USE_ROW_SLOT_START && pIndex < USE_ROW_SLOT_END && !this.moveItemStackTo(slotItem, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotItem, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotItem.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, slotItem);
        }

        return itemstack;
    }

    protected boolean canProcess(ItemStack item) {
        if (blockEntity == null || blockEntity.getLevel() == null) {
            return false;
        }
        return blockEntity.getLevel().getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.COMPOSITE_SMELTING).stream()
                .anyMatch(recipe -> recipe.partialMatch(new SimpleContainer(item), blockEntity.getLevel()));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        if (blockEntity == null || blockEntity.getLevel() == null) {
            return false;
        }
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, BlockRegistry.BRICK_KILN);
    }

    public boolean isLit() {
        return this.data.get(BrickKilnBlockEntity.DATA_LIT_TIME) > 0;
    }

    public int getBurnProgress() {
        int i = this.data.get(BrickKilnBlockEntity.DATA_COOKING_PROGRESS);
        int j = this.data.get(BrickKilnBlockEntity.DATA_COOKING_TOTAL_TIME);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public int getLitProgress() {
        int i = this.data.get(BrickKilnBlockEntity.DATA_LIT_DURATION);
        if (i == 0) {
            i = 200;
        }

        return this.data.get(BrickKilnBlockEntity.DATA_LIT_TIME) * 13 / i;
    }

    static class OutputSlot extends SlotItemHandler {
        public OutputSlot(SlottedStackStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return false;
        }
    }

    static class IngredientSlot extends SlotItemHandler {
        public IngredientSlot(SlottedStackStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }
    }

    static class FuelSlot extends SlotItemHandler {
        public FuelSlot(SlottedStackStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return isFuel(stack);
        }
    }

    private static boolean isFuel(@NotNull ItemStack stack) {
        return FuelRegistry.INSTANCE.get(stack.getItem()) != null;
    }


}
