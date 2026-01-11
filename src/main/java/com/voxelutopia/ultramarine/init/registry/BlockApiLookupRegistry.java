package com.voxelutopia.ultramarine.init.registry;

import com.voxelutopia.ultramarine.common.tile.BrickKilnBlockEntity;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BlockApiLookupRegistry {

    public static void register() {
        ItemStorage.SIDED.registerForBlockEntities((blockEntity, direction) -> {
            if (!(blockEntity instanceof BrickKilnBlockEntity be)) return null;
            if (direction == null) return null;
            return InventoryStorage.of(new BrickKilnSidedInventory(be), direction);
        }, ModBlockEntities.BRICK_KILN);
    }

    static class BrickKilnSidedInventory implements WorldlyContainer {
        private final BrickKilnBlockEntity be;

        BrickKilnSidedInventory(BrickKilnBlockEntity be) {
            this.be = be;
        }

        @Override
        public int getContainerSize() {
            return BrickKilnBlockEntity.NUM_SLOTS;
        }

        @Override
        public boolean isEmpty() {
            return be.isEmpty();
        }

        @Override
        public ItemStack getItem(int index) {
            return be.getItem(index);
        }

        @Override
        public ItemStack removeItem(int index, int count) {
            ItemStack stack = be.getItem(index);
            if (stack.isEmpty() || count <= 0) return ItemStack.EMPTY;
            ItemStack result = stack.split(count);
            be.setItem(index, stack);
            return result;
        }

        @Override
        public ItemStack removeItemNoUpdate(int index) {
            ItemStack stack = be.getItem(index);
            be.setItem(index, ItemStack.EMPTY);
            return stack;
        }

        @Override
        public void setItem(int index, ItemStack stack) {
            be.setItem(index, stack);
        }

        @Override
        public void setChanged() {
        }

        @Override
        public boolean stillValid(Player player) {
            return true;
        }

        @Override
        public void clearContent() {
            for (int i = 0; i < BrickKilnBlockEntity.NUM_SLOTS; i++) {
                be.setItem(i, ItemStack.EMPTY);
            }
        }

        @Override
        public boolean canPlaceItem(int index, ItemStack stack) {
            if (index == BrickKilnBlockEntity.SLOT_RESULT) return false;
            if (index == BrickKilnBlockEntity.SLOT_FUEL) {
                Integer burn = FuelRegistry.INSTANCE.get(stack.getItem());
                return burn != null && burn > 0;
            }
            return true;
        }

        @Override
        public int[] getSlotsForFace(Direction dir) {
            if (dir == Direction.UP) {
                return new int[]{BrickKilnBlockEntity.SLOT_FUEL};
            } else if (dir == Direction.DOWN) {
                return new int[]{BrickKilnBlockEntity.SLOT_RESULT};
            } else {
                return new int[]{BrickKilnBlockEntity.SLOT_INPUT_PRIMARY, BrickKilnBlockEntity.SLOT_INPUT_SECONDARY};
            }
        }

        @Override
        public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction dir) {
            if (dir == Direction.UP) {
                return index == BrickKilnBlockEntity.SLOT_FUEL && canPlaceItem(index, stack);
            } else if (dir == Direction.DOWN) {
                return false;
            } else {
                return (index == BrickKilnBlockEntity.SLOT_INPUT_PRIMARY || index == BrickKilnBlockEntity.SLOT_INPUT_SECONDARY) && canPlaceItem(index, stack);
            }
        }

        @Override
        public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction dir) {
            if (dir == Direction.DOWN) {
                return index == BrickKilnBlockEntity.SLOT_RESULT;
            } else if (dir == Direction.UP) {
                return index == BrickKilnBlockEntity.SLOT_FUEL;
            } else {
                return index == BrickKilnBlockEntity.SLOT_INPUT_PRIMARY || index == BrickKilnBlockEntity.SLOT_INPUT_SECONDARY;
            }
        }
    }
}
