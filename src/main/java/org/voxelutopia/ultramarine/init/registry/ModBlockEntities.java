package org.voxelutopia.ultramarine.init.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.tile.*;

import java.util.HashSet;
import java.util.Set;

public class ModBlockEntities {

    public static BlockEntityType<ContainerDecorativeBlockEntity> CONTAINER_DECORATIVE_BLOCK;
    public static BlockEntityType<CenserBlockEntity> CENSER;
    public static BlockEntityType<BottleGourdBlockEntity> BOTTLE_GOURD;
    public static BlockEntityType<BrickKilnBlockEntity> BRICK_KILN;
    public static BlockEntityType<ChiselTableBlockEntity> CHISEL_TABLE;

    static Set<Block> CONTAINER_BLOCKS = Set.of(
            ModBlocks.SACK, ModBlocks.BAMBOO_TEA_BASKET, ModBlocks.EMPTY_BAMBOO_TEA_BASKET,
            ModBlocks.GUNNY_SACK, ModBlocks.FRUIT_BOX, ModBlocks.WOODEN_CRATE, ModBlocks.FOOD_HAMPER,
            ModBlocks.OAK_CABINET, ModBlocks.WARPED_CABINET, ModBlocks.EBONY_CABINET);
    static Set<Block> CENSERS = Set.of(ModBlocks.BRONZE_CENSER, ModBlocks.ROYAL_CENSER);

    public static void registerModBlockEntities() {
        CONTAINER_DECORATIVE_BLOCK = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, 
            ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "container_decorative_block_entity"),
            BlockEntityType.Builder.of(ContainerDecorativeBlockEntity::new, 
                new HashSet<>(CONTAINER_BLOCKS).toArray(new Block[0])).build());

        CENSER = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "censer_block_entity"),
            BlockEntityType.Builder.of(CenserBlockEntity::new,
                new HashSet<>(CENSERS).toArray(new Block[0])).build());

        BOTTLE_GOURD = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "bottle_gourd_entity"),
            BlockEntityType.Builder.of(BottleGourdBlockEntity::new, 
                ModBlocks.BOTTLE_GOURD).build());

        BRICK_KILN = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "brick_kiln_block_entity"),
            BlockEntityType.Builder.of(BrickKilnBlockEntity::new,
                ModBlocks.BRICK_KILN).build(null));

        CHISEL_TABLE = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,
                ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "chisel_table_block_entity"),
                BlockEntityType.Builder.of(ChiselTableBlockEntity::new,
                        ModBlocks.CHISEL_TABLE).build(null));
    }
}
