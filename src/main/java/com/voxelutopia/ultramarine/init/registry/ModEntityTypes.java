package com.voxelutopia.ultramarine.init.registry;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.common.tile.SeatEntity;
import com.voxelutopia.ultramarine.common.tile.TravellingMerchant;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntityTypes {
    public static EntityType<SeatEntity> SEAT;
    public static EntityType<TravellingMerchant> TRAVELLING_MERCHANT;

    public static void registerModEntities() {
        Identifier seatId = Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "seat");
        ResourceKey<EntityType<?>> seatKey = ResourceKey.create(Registries.ENTITY_TYPE, seatId);
        SEAT = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                seatId,
                EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                        .sized(0.1F, 0.1F)
                        .clientTrackingRange(64)
                        .updateInterval(20)
                        .build(seatKey)
        );

        Identifier merchantId = Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "travelling_merchant");
        ResourceKey<EntityType<?>> merchantKey = ResourceKey.create(Registries.ENTITY_TYPE, merchantId);
        TRAVELLING_MERCHANT = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                merchantId,
                EntityType.Builder.of(TravellingMerchant::new, MobCategory.CREATURE)
                        .sized(0.6F, 1.95F)
                        .eyeHeight(1.62F)
                        .clientTrackingRange(64)
//                        .updateInterval(20)
                        .build(merchantKey)
        );
    }
}
