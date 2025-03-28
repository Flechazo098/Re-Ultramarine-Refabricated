package org.voxelutopia.ultramarine.common.inventory;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.voxelutopia.ultramarine.init.data.ModItemTags;

/**
 * @author Flechazo
 * <p>
 * A specialized implementation of SimpleItemStorage for the Chisel Table.
 * This class manages the material, template, color, and output slots of the Chisel Table.
 */
public class ChiselTableItemStorage extends SimpleItemStorage {
    private final int slotType;
    private final boolean isTemplateSlot;
    private final boolean isOutputSlot;

    public ChiselTableItemStorage(int capacity, int slotType) {
        super(capacity);
        this.slotType = slotType;
        this.isTemplateSlot = slotType == 1;
        this.isOutputSlot = slotType == 6;
    }

    @Override
    public long insert(ItemVariant insertedVariant, long maxAmount, TransactionContext transaction) {
        if (!isItemValid(0, insertedVariant.toStack())) {
            return 0;
        }
        return super.insert(insertedVariant, maxAmount, transaction);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (isOutputSlot) {
            return false;
        }
        if (isTemplateSlot) {
            return stack.is(ModItemTags.CHISEL_TEMPLATES);
        }
        if (slotType == 0) { // Material slot
            return stack.is(ItemTags.LOGS) || stack.is(ModItemTags.POLISHED_PLANKS);
        }
        if (slotType >= 2 && slotType <= 5) { // Color slots
            return stack.is(ModItemTags.DYES) || stack.is(ModItemTags.DYE_POWDER);
        }
        return true;
    }
}