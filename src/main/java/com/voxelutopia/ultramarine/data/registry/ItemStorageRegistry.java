package com.voxelutopia.ultramarine.data.registry;

import com.voxelutopia.ultramarine.world.block.entity.BrickKilnBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;

public class ItemStorageRegistry {

    public static void register() {
        ItemStorage.SIDED.registerForBlockEntity(BrickKilnBlockEntity::getItemStorage, BlockEntityRegistry.BRICK_KILN);
    }
}