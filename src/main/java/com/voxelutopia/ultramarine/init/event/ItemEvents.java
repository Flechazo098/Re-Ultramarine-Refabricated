package com.voxelutopia.ultramarine.init.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.item.ItemEntity;

public class ItemEvents {
    public static final Event<ItemUpdateCallback> UPDATE_EVENT = EventFactory.createArrayBacked(
            ItemUpdateCallback.class,
            (listeners) -> (itemEntity) -> {
                for (ItemUpdateCallback listener : listeners) {
                    if (listener.onItemUpdate(itemEntity)) return true;
                }
                return false;
            }
    );

    @FunctionalInterface
    public interface ItemUpdateCallback {
        boolean onItemUpdate(ItemEntity itemEntity);
    }
}