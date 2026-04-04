package com.voxelutopia.ultramarine.init.registry;

import com.google.common.collect.ImmutableSet;
import com.voxelutopia.ultramarine.Ultramarine;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

public class ModVillagerProfessions {


    public static VillagerProfession COOK;


    public static void registerModVillagerProfession() {
        Identifier id = Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "cook");
        ResourceKey<VillagerProfession> key = ResourceKey.create(Registries.VILLAGER_PROFESSION, id);

        COOK = Registry.register(
                BuiltInRegistries.VILLAGER_PROFESSION,
                key,
                new VillagerProfession(
                        Component.translatable("entity." + id.getNamespace() + ".villager." + id.getPath()),
                        (Holder<PoiType> holder) -> ModPoiTypes.COOKING_POI != null && holder.value() == ModPoiTypes.COOKING_POI,
                        (Holder<PoiType> holder) -> ModPoiTypes.COOKING_POI != null && holder.value() == ModPoiTypes.COOKING_POI,
                        ImmutableSet.of(),
                        ImmutableSet.of(),
                        SoundEvents.VILLAGER_WORK_BUTCHER,
                        Int2ObjectMap.ofEntries(
                                Int2ObjectMap.entry(
                                        1,
                                        ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "cook/level_1"))
                                ),
                                Int2ObjectMap.entry(
                                        2,
                                        ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "cook/level_2"))
                                ),
                                Int2ObjectMap.entry(
                                        3,
                                        ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "cook/level_3"))
                                ),
                                Int2ObjectMap.entry(
                                        4,
                                        ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "cook/level_4"))
                                ),
                                Int2ObjectMap.entry(
                                        5,
                                        ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, "cook/level_5"))
                                )
                        )
                )
        );
    }
}
