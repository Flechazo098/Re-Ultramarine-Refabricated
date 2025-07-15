package com.voxelutopia.ultramarine.data.registry;

import com.voxelutopia.ultramarine.world.block.entity.BrickKilnBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;

public class BlockApiLookupRegistry {

    public static void init() {
        ItemStorage.SIDED.registerForBlockEntity(
                (blockEntity, direction) -> {
                    if (blockEntity.remove || direction == null) return null;

                    return switch (direction) {
                        case UP -> blockEntity.getIngredientsHandler();
                        case DOWN -> blockEntity.getResultHandler();
                        default -> blockEntity.getFuelHandler();
                    };
                },
                BlockEntityRegistry.BRICK_KILN
        );
    }
}