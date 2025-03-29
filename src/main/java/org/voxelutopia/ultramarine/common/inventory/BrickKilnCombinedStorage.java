package org.voxelutopia.ultramarine.common.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Flechazo
 *<p>
 * A specialized implementation of CombinedStorage for the Brick Kiln.
 * This class manages all storage slots of the Brick Kiln, including input, fuel, and output slots.
 */
public class BrickKilnCombinedStorage extends CombinedStorage {
    private final BrickKilnItemStorage primaryInput;
    private final BrickKilnItemStorage secondaryInput;
    private final BrickKilnItemStorage fuel;
    private final BrickKilnItemStorage result;

    public BrickKilnCombinedStorage() {
        super(
            new BrickKilnItemStorage(64, 0),  // Primary input
            new BrickKilnItemStorage(64, 1),  // Secondary input
            new BrickKilnItemStorage(64, 2),  // Fuel
            new BrickKilnItemStorage(64, 3)   // Result
        );
        this.primaryInput = (BrickKilnItemStorage) itemStorages[0];
        this.secondaryInput = (BrickKilnItemStorage) itemStorages[1];
        this.fuel = (BrickKilnItemStorage) itemStorages[2];
        this.result = (BrickKilnItemStorage) itemStorages[3];
    }

    public BrickKilnItemStorage getPrimaryInput() {
        return primaryInput;
    }

    public BrickKilnItemStorage getSecondaryInput() {
        return secondaryInput;
    }

    public BrickKilnItemStorage getFuel() {
        return fuel;
    }

    public BrickKilnItemStorage getResult() {
        return result;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch (slot) {
            case 0 -> primaryInput.isItemValid(0, stack);
            case 1 -> secondaryInput.isItemValid(0, stack);
            case 2 -> fuel.isItemValid(0, stack);
            case 3 -> result.isItemValid(0, stack);
            default -> false;
        };
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        super.setStackInSlot(slot, stack);
        // 标记方块实体为已更改
        this.setChanged();
    }

    @Override
    public void setChanged() {
        if (this.blockEntity != null) {
            this.blockEntity.setChanged();
        }
    }
}