package org.voxelutopia.ultramarine.init.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.tile.CustomWanderingTrader;
import org.voxelutopia.ultramarine.common.tile.SeatEntity;

public class ModEntityTypes {
    public static EntityType<SeatEntity> SEAT;
    public static EntityType<CustomWanderingTrader> CUSTOM_WANDERING_TRADER;

    public static void registerModEntities() {
        SEAT = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "seat"),
            EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                .sized(0.1F, 0.1F)
                .clientTrackingRange(64)
                .updateInterval(20)
                .build()
        );

        CUSTOM_WANDERING_TRADER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "custom_wandering_trader"),
            EntityType.Builder.<CustomWanderingTrader>of(CustomWanderingTrader::new, MobCategory.MISC)
                .sized(0.6F, 0.2F)
                .clientTrackingRange(64)
                .updateInterval(20)
                .build()
        );
    }
}
