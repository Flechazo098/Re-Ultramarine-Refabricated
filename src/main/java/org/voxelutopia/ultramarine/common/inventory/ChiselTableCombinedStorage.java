package org.voxelutopia.ultramarine.common.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author Flechazo
 *<p>
 * A specialized implementation of CombinedStorage for the Chisel Table.
 * This class manages all storage slots of the Chisel Table, including material, template, colors, and output slots.
 */
public class ChiselTableCombinedStorage extends CombinedStorage {
    private final ChiselTableItemStorage material;
    private final ChiselTableItemStorage template;
    private final ChiselTableItemStorage[] colors;
    private final ChiselTableItemStorage result;

    public ChiselTableCombinedStorage() {
        super(
            new ChiselTableItemStorage(64, 0),  // Material
            new ChiselTableItemStorage(64, 1),  // Template
            new ChiselTableItemStorage(64, 2),  // Color 1
            new ChiselTableItemStorage(64, 3),  // Color 2
            new ChiselTableItemStorage(64, 4),  // Color 3
            new ChiselTableItemStorage(64, 5),  // Color 4
            new ChiselTableItemStorage(64, 6)   // Result
        );
        this.material = (ChiselTableItemStorage) itemStorages[0];
        this.template = (ChiselTableItemStorage) itemStorages[1];
        this.colors = new ChiselTableItemStorage[4];
        for (int i = 0; i < 4; i++) {
            this.colors[i] = (ChiselTableItemStorage) itemStorages[i + 2];
        }
        this.result = (ChiselTableItemStorage) itemStorages[6];
    }

    public ChiselTableItemStorage getMaterial() {
        return material;
    }

    public ChiselTableItemStorage getTemplate() {
        return template;
    }

    public ChiselTableItemStorage getColor(int index) {
        if (index >= 0 && index < colors.length) {
            return colors[index];
        }
        return null;
    }

    public ChiselTableItemStorage getResult() {
        return result;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch (slot) {
            case 0 -> material.isItemValid(0, stack);
            case 1 -> template.isItemValid(0, stack);
            case 2, 3, 4, 5 -> colors[slot - 2].isItemValid(0, stack);
            case 6 -> result.isItemValid(0, stack);
            default -> false;
        };
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}