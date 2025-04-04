package org.voxelutopia.ultramarine.common.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.voxelutopia.ultramarine.common.inventory.BrickKilnCombinedStorage;
import org.voxelutopia.ultramarine.common.inventory.FabricItemStorage;
import org.voxelutopia.ultramarine.common.tile.BrickKilnBlockEntity;
import org.voxelutopia.ultramarine.init.registry.ModBlocks;
import org.voxelutopia.ultramarine.init.registry.ModMenuTypes;
import org.voxelutopia.ultramarine.init.registry.ModRecipeTypes;

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
    private final BlockPos pos;


    public BrickKilnMenu(int pId, BlockPos pos, Inventory inventory) {
        this(pId, inventory, pos, new BrickKilnCombinedStorage(), new SimpleContainerData(4));
    }

    public BrickKilnMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory,
                extraData != null && extraData.readableBytes() > 0 ? extraData.readBlockPos() : BlockPos.ZERO,
                new BrickKilnCombinedStorage(),
                new SimpleContainerData(4));

//        // 添加调试日志
//        Ultramarine.LOGGER.debug("[客户端] 创建砖窑菜单，位置：{}",
//                extraData != null && extraData.readableBytes() > 0 ? "有效数据" : "无数据");
    }

    public BrickKilnMenu(int id, Inventory inventory, BlockPos pos, BrickKilnCombinedStorage container, ContainerData containerData) {
        super(ModMenuTypes.BRICK_KILN, id);
        this.pos = pos;
        this.playerEntity = inventory.player;
        Level level = playerEntity.getCommandSenderWorld();

//        // 添加调试日志
//        Ultramarine.LOGGER.debug("创建砖窑菜单，位置：{}", pos);

        // 先获取方块实体
        this.blockEntity = level.getBlockEntity(pos);

        // 如果方块实体不存在则记录错误
            if (this.blockEntity instanceof BrickKilnBlockEntity kilnEntity) {
            // 如果在服务端且有有效的方块实体，使用其存储和数据
            if (!level.isClientSide() && container == null) {
                container = kilnEntity.wrapHandlers();
                containerData = kilnEntity.dataAccess;
            }
        }

        // 确保容器不为空
        if(container == null) {
            container = new BrickKilnCombinedStorage();
        }

        FabricItemStorage storage = container;
        FabricItemStorage inventory1 = new InventoryFabricWrapper(inventory);
        this.data = containerData;

        this.addSlot(new IngredientSlot(storage, SLOT_INPUT_PRIMARY, 46, 17));
        this.addSlot(new IngredientSlot(storage, SLOT_INPUT_SECONDARY, 66, 17));
        this.addSlot(new FuelSlot(storage, SLOT_FUEL, 56, 53));
        this.addSlot(new OutputSlot(storage, SLOT_RESULT, 116, 35));

        for(int r = 0; r < 3; ++r) {
            for(int c = 0; c < 9; ++c) {
                this.addSlot(new SlotFabricItemStorage(inventory1, c + r * 9 + 9, 8 + c * 18, 84 + r * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new SlotFabricItemStorage(inventory1, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.data);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        // 添加空检查
        if (this.blockEntity == null) {
            return ItemStack.EMPTY;
        }

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();

            if (pIndex == SLOT_RESULT) {
                // 结果槽位的物品移动到玩家物品栏
                if (!this.moveItemStackTo(slotItem, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotItem, itemstack);
            } else if (pIndex != SLOT_FUEL && pIndex != SLOT_INPUT_PRIMARY && pIndex != SLOT_INPUT_SECONDARY) {
                // 玩家物品栏的物品移动到砖窑
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

    @Override
    protected boolean moveItemStackTo(ItemStack itemStack, int startIndex, int endIndex, boolean reverse) {
        boolean success = false;
        int currentIndex = startIndex;
        if (reverse) {
            currentIndex = endIndex - 1;
        }

        // 如果物品可堆叠，尝试与已有物品堆叠
        if (itemStack.isStackable()) {
            while (!itemStack.isEmpty() && (reverse ? currentIndex >= startIndex : currentIndex < endIndex)) {
                Slot slot = this.slots.get(currentIndex);
                ItemStack slotStack = slot.getItem();

                if (!slotStack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, slotStack)) {
                    int totalCount = slotStack.getCount() + itemStack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(itemStack), itemStack.getMaxStackSize());

                    if (totalCount <= maxSize) {
                        itemStack.setCount(0);
                        slotStack.setCount(totalCount);
                        slot.setChanged();
                        success = true;
                    } else if (slotStack.getCount() < maxSize) {
                        int remainingSpace = maxSize - slotStack.getCount();
                        itemStack.shrink(remainingSpace);
                        slotStack.setCount(maxSize);
                        slot.setChanged();
                        success = true;
                    }
                }

                if (reverse) {
                    --currentIndex;
                } else {
                    ++currentIndex;
                }
            }
        }

        // 如果还有剩余物品，尝试放入空槽位
        if (!itemStack.isEmpty()) {
            currentIndex = reverse ? endIndex - 1 : startIndex;

            while (reverse ? currentIndex >= startIndex : currentIndex < endIndex) {
                Slot slot = this.slots.get(currentIndex);
                ItemStack slotStack = slot.getItem();

                if (slotStack.isEmpty() && slot.mayPlace(itemStack)) {
                    int maxSize = Math.min(slot.getMaxStackSize(itemStack), itemStack.getMaxStackSize());
                    int transferAmount = Math.min(maxSize, itemStack.getCount());
                    ItemStack transferStack = itemStack.split(transferAmount);
                    slot.set(transferStack);
                    slot.setChanged();
                    success = true;
                    break;
                }

                if (reverse) {
                    --currentIndex;
                } else {
                    ++currentIndex;
                }
            }
        }

        return success;
    }
    protected boolean canProcess(ItemStack item) {
        // 使用玩家所在的世界代替方块实体的世界
        Level level = playerEntity.level();
        if (level == null || blockEntity == null) {
            return false;
        }
        return level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COMPOSITE_SMELTING).stream()
                .anyMatch(recipe -> recipe.value().partialMatch(new SimpleContainer(item), level));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        // 使用存储的位置而不是blockEntity的位置
        return stillValid(ContainerLevelAccess.create(pPlayer.level(), pos), pPlayer, ModBlocks.BRICK_KILN);
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

    static class OutputSlot extends SlotFabricItemStorage {
        public OutputSlot(FabricItemStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return false;
        }
    }

    static class IngredientSlot extends SlotFabricItemStorage {
        public IngredientSlot(FabricItemStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }
    }

    static class FuelSlot extends SlotFabricItemStorage {
        public FuelSlot(FabricItemStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return isFuel(stack);
        }
    }

    private static boolean isFuel(@NotNull ItemStack stack) {
        return AbstractFurnaceBlockEntity.isFuel(stack);
    }

}