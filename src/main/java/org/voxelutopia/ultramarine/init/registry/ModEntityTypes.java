package org.voxelutopia.ultramarine.init.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.tile.TravellingMerchant;
import org.voxelutopia.ultramarine.common.tile.SeatEntity;

public class ModEntityTypes {
    public static EntityType<SeatEntity> SEAT;
    public static EntityType<TravellingMerchant> TRAVELLING_MERCHANT;

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

        TRAVELLING_MERCHANT = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "travelling_merchant"),
            EntityType.Builder.<TravellingMerchant>of(TravellingMerchant::new, MobCategory.MISC)
                .sized(0.6F, 0.2F)
                .clientTrackingRange(64)
                .updateInterval(20)
                .build()
        );
    }
}
