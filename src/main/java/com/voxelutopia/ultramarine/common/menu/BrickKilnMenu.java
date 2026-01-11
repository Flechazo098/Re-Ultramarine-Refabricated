package com.voxelutopia.ultramarine.common.menu;

import com.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;
import com.voxelutopia.ultramarine.common.tile.BrickKilnBlockEntity;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import com.voxelutopia.ultramarine.init.registry.ModMenuTypes;
import com.voxelutopia.ultramarine.init.registry.ModRecipeTypes;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Arrays;

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
    private final BrickKilnBlockEntity brickKiln;
    private final ContainerData data;
    private final BrickKilnInventory inventory;

    public BrickKilnMenu(int id, BlockPos pos, Inventory inventory) {
        this(id, pos, inventory, null, new SimpleContainerData(4));
    }

    public BrickKilnMenu(int id, BlockPos pos, Inventory inventory, BrickKilnBlockEntity container, ContainerData containerData) {
        super(ModMenuTypes.BRICK_KILN, id);
        this.playerEntity = inventory.player;
        this.blockEntity = playerEntity.getCommandSenderWorld().getBlockEntity(pos);
        this.brickKiln = container != null ? container : (this.blockEntity instanceof BrickKilnBlockEntity ? (BrickKilnBlockEntity) this.blockEntity : null);
        this.data = containerData;
        this.inventory = new BrickKilnInventory(this.brickKiln);

        this.addSlot(new IngredientSlot(this.inventory, SLOT_INPUT_PRIMARY, 46, 17));
        this.addSlot(new IngredientSlot(this.inventory, SLOT_INPUT_SECONDARY, 66, 17));
        this.addSlot(new FuelSlot(this.inventory, SLOT_FUEL, 56, 53));
        this.addSlot(new OutputSlot(playerEntity, this.inventory, blockEntity, SLOT_RESULT, 116, 35));

        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(inventory, c + r * 9 + 9, 8 + c * 18, 84 + r * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();
            if (index == SLOT_RESULT) {
                if (!this.moveItemStackTo(slotItem, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotItem, itemstack);
            } else if (index != SLOT_FUEL && index != SLOT_INPUT_PRIMARY && index != SLOT_INPUT_SECONDARY) {
                if (this.canProcess(slotItem)) {
                    if (!this.moveItemStackTo(slotItem, SLOT_INPUT_PRIMARY, SLOT_INPUT_SECONDARY + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (isFuel(slotItem)) {
                    if (!this.moveItemStackTo(slotItem, SLOT_FUEL, SLOT_FUEL + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= INV_SLOT_START && index < INV_SLOT_END) {
                    if (!this.moveItemStackTo(slotItem, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= USE_ROW_SLOT_START && index < USE_ROW_SLOT_END && !this.moveItemStackTo(slotItem, INV_SLOT_START, INV_SLOT_END, false)) {
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

            slot.onTake(player, slotItem);
        }

        return itemstack;
    }

    protected boolean canProcess(ItemStack item) {
        if (blockEntity == null || blockEntity.getLevel() == null) {
            return false;
        }
        return blockEntity.getLevel().getRecipeManager().getAllRecipesFor(ModRecipeTypes.COMPOSITE_SMELTING).stream()
                .anyMatch(recipe -> recipe.value().partialMatch(new SingleRecipeInput(item)));
    }

    @Override
    public boolean stillValid(Player player) {
        if (blockEntity == null) {
            return false;
        }
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), playerEntity, ModBlocks.BRICK_KILN);
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

    static class OutputSlot extends Slot {

        private final Player player;
        private final BlockEntity blockEntity;
        private int removeCount;

        public OutputSlot(Player player, BrickKilnInventory container, BlockEntity blockEntity, int index, int xPosition, int yPosition) {
            super(container, index, xPosition, yPosition);
            this.player = player;
            this.blockEntity = blockEntity;
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return false;
        }


        @Override
        public ItemStack remove(int amount) {
            if (this.hasItem()) {
                this.removeCount = this.removeCount + Math.min(amount, this.getItem().getCount());
            }
            return super.remove(amount);
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            this.checkTakeAchievements(stack);
            super.onTake(player, stack);
        }

        @Override
        protected void onQuickCraft(ItemStack stack, int amount) {
            this.removeCount += amount;
            this.checkTakeAchievements(stack);
        }

        @Override
        protected void checkTakeAchievements(ItemStack stack) {
            stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
            if (this.player instanceof ServerPlayer serverplayer && blockEntity instanceof BrickKilnBlockEntity kiln) {
                kiln.awardUsedRecipesAndPopExperience(serverplayer);
            }
            this.removeCount = 0;
        }
    }

    static class IngredientSlot extends Slot {
        public IngredientSlot(BrickKilnInventory container, int index, int xPosition, int yPosition) {
            super(container, index, xPosition, yPosition);
        }
    }

    static class FuelSlot extends Slot {
        public FuelSlot(BrickKilnInventory container, int index, int xPosition, int yPosition) {
            super(container, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return isFuel(stack);
        }
    }

    private static boolean isFuel(@NotNull ItemStack stack) {
        Integer fuelValue = FuelRegistry.INSTANCE.get(stack.getItem());
        return fuelValue != null && fuelValue > 0;
    }

    // 简单的 Container 实现来适配 Slot
    public static class BrickKilnInventory implements net.minecraft.world.Container {
        private final BrickKilnBlockEntity blockEntity;
        private final ItemStack[] items = new ItemStack[BrickKilnBlockEntity.NUM_SLOTS];

        public BrickKilnInventory(BrickKilnBlockEntity blockEntity) {
            this.blockEntity = blockEntity;
            // 如果 blockEntity 为 null，初始化空的物品数组
            if (blockEntity == null) {
                Arrays.fill(items, ItemStack.EMPTY);
            }
        }

        @Override
        public int getContainerSize() {
            return BrickKilnBlockEntity.NUM_SLOTS;
        }

        @Override
        public boolean isEmpty() {
            if (blockEntity != null) {
                return blockEntity.isEmpty();
            }
            for (ItemStack item : items) {
                if (!item.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public ItemStack getItem(int slot) {
            if (blockEntity != null) {
                return blockEntity.getItem(slot);
            }
            return slot >= 0 && slot < items.length ? items[slot] : ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack stack = getItem(slot);
            if (!stack.isEmpty()) {
                ItemStack result = stack.split(amount);
                setChanged();
                return result;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            ItemStack stack = getItem(slot);
            setItem(slot, ItemStack.EMPTY);
            return stack;
        }

        @Override
        public void setItem(int slot, ItemStack stack) {
            if (blockEntity != null) {
                blockEntity.setItem(slot, stack);
            } else if (slot >= 0 && slot < items.length) {
                items[slot] = stack;
            }
        }

        @Override
        public void setChanged() {
            if (blockEntity != null) {
                blockEntity.setChanged();
            }
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }

        @Override
        public void clearContent() {
            if (blockEntity != null) {
                for (int i = 0; i < getContainerSize(); i++) {
                    blockEntity.setItem(i, ItemStack.EMPTY);
                }
            } else {
                for (int i = 0; i < items.length; i++) {
                    items[i] = ItemStack.EMPTY;
                }
            }
        }
    }
}