package org.voxelutopia.ultramarine.common.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.jetbrains.annotations.NotNull;

/**
 * @author Flechazo
 * <p>
 * A specialized implementation of SimpleItemStorage for the Brick Kiln.
 * This class manages the input, fuel, and output slots of the Brick Kiln.
 */
public class BrickKilnItemStorage extends SimpleItemStorage {
    private final int slotType;
    private final boolean isFuelSlot;
    private final boolean isOutputSlot;

    public BrickKilnItemStorage(int capacity, int slotType) {
        super(capacity);
        this.slotType = slotType;
        this.isFuelSlot = slotType == 2;
        this.isOutputSlot = slotType == 3;
    }

    @Override
    public long insert(ItemVariant insertedVariant, long maxAmount, TransactionContext transaction) {
        // 如果是输出槽，确保可以插入物品（即使是通过内部逻辑）
        if (isOutputSlot) {
            // 允许内部逻辑插入物品到输出槽
            return super.insert(insertedVariant, maxAmount, transaction);
        }

        if (!isItemValid(0, insertedVariant.toStack())) {
            return 0;
        }
        return super.insert(insertedVariant, maxAmount, transaction);
    }
    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (isOutputSlot) {
            // 对于输出槽，只允许内部逻辑插入物品，不允许玩家直接放入
            return false;
        }
        if (isFuelSlot) {
            return AbstractFurnaceBlockEntity.isFuel(stack);
        }
        return true;
    }
}