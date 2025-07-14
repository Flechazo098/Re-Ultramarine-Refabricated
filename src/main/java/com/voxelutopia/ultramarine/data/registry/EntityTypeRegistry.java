package com.voxelutopia.ultramarine.data.registry;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.world.entity.SeatEntity;
import com.voxelutopia.ultramarine.world.entity.TravellingMerchant;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;


public class EntityTypeRegistry {
    public static EntityType<SeatEntity> SEAT;
    public static EntityType<TravellingMerchant> TRAVELLING_MERCHANT;

    public static void registerModEntities() {
        SEAT = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                new ResourceLocation(Ultramarine.MOD_ID, "seat"),
                EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                        .sized(0.1F, 0.1F)
                        .clientTrackingRange(64)
                        .updateInterval(20)
                        .build("seat")
        );

        TRAVELLING_MERCHANT = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                new ResourceLocation(Ultramarine.MOD_ID, "travelling_merchant"),
                EntityType.Builder.of(TravellingMerchant::new, MobCategory.MISC)
                        .sized(0.6F, 0.2F)
                        .clientTrackingRange(64)
                        .updateInterval(20)
                        .build("travelling_merchant")
        );
    }
}
