package com.voxelutopia.ultramarine.data.registry;

import com.voxelutopia.ultramarine.world.block.entity.BrickKilnBlockEntity;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class BlockApiLookupRegistry {
    public static final BlockApiLookup<Storage<ItemVariant>, Direction> ITEM_HANDLER =
            getLookup(
                    new ResourceLocation("yourmodid", "item_handler"),
                    Storage.class,
                    Direction.class
            );

    public static void init() {
        ITEM_HANDLER.registerForBlockEntities(
                (blockEntity, direction) -> {
                    if (blockEntity.remove || direction == null) return null;

                    if (!(blockEntity instanceof BrickKilnBlockEntity brickKiln)) return null;

                    return switch (direction) {
                        case UP -> brickKiln.getIngredientsHandler();
                        case DOWN -> brickKiln.getResultHandler();
                        default -> brickKiln.getFuelHandler();
                    };
                },
                BlockEntityRegistry.BRICK_KILN
        );
    }

    @SuppressWarnings("unchecked")
    public static <T, C> BlockApiLookup<T, C> getLookup(ResourceLocation id, Class<?> apiClass, Class<C> context) {
        return BlockApiLookup.get(id, (Class<T>) apiClass, context);
    }
}