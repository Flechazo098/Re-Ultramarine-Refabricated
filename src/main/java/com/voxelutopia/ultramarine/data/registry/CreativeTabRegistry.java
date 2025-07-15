package com.voxelutopia.ultramarine.data.registry;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.data.ModCreativeTab;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;


public class CreativeTabRegistry {

    public static CreativeModeTab MATERIALS;
    public static CreativeModeTab TOOLS;
    public static CreativeModeTab BUILDING_BLOCKS;
    public static CreativeModeTab DECORATIVE_BLOCKS;
    public static CreativeModeTab DECORATIONS;
    public static CreativeModeTab FURNITURE;
    public static CreativeModeTab WINDOWS_AND_DOORS;
    public static CreativeModeTab PLANTS;
    public static CreativeModeTab LAMPS;

    public static void registerModGroups() {
        MATERIALS = register("materials_tab", FabricItemGroup.builder()
                .title(Component.translatable("item_group." + Ultramarine.MOD_ID + ".materials"))
                .icon(() -> new ItemStack(ItemRegistry.CYAN_BRICK))
                .displayItems((itemDisplayParameters, output) -> {
                    for (Item item : ModCreativeTab.itemSets.get(ModCreativeTab.MATERIALS)) {
                        output.accept(item);
                    }
                })
                .build());
        TOOLS = register("tools_tab", FabricItemGroup.builder()
                .title(Component.translatable("item_group." + Ultramarine.MOD_ID + ".tools"))
                .icon(() -> new ItemStack(ItemRegistry.WOODEN_MALLET))
                .displayItems((itemDisplayParameters, output) -> {
                    for (Item item : ModCreativeTab.itemSets.get(ModCreativeTab.TOOLS)) {
                        output.accept(item);
                    }
                })
                .build());
        BUILDING_BLOCKS = register("building_blocks_tab", FabricItemGroup.builder()
                .title(Component.translatable("item_group." + Ultramarine.MOD_ID + ".building_blocks"))
                .icon(() -> new ItemStack(ItemRegistry.CYAN_BRICKS))
                .displayItems((itemDisplayParameters, output) -> {
                    for (Item item : ModCreativeTab.itemSets.get(ModCreativeTab.BUILDING_BLOCKS)) {
                        output.accept(item);
                    }
                })
                .build());
        DECORATIVE_BLOCKS = register("decorative_blocks_tab", FabricItemGroup.builder()
                .title(Component.translatable("item_group." + Ultramarine.MOD_ID + ".decorative_blocks"))
                .icon(() -> new ItemStack(ItemRegistry.CARVED_RED_PILLAR_BASE))
                .displayItems((itemDisplayParameters, output) -> {
                    for (Item item : ModCreativeTab.itemSets.get(ModCreativeTab.DECORATIVE_BLOCKS)) {
                        output.accept(item);
                    }
                })
                .build());
        DECORATIONS = register("decorations_tab", FabricItemGroup.builder()
                .title(Component.translatable("item_group." + Ultramarine.MOD_ID + ".decorations"))
                .icon(() -> new ItemStack(ItemRegistry.LARGE_BLUE_AND_WHITE_PORCELAIN_VASE))
                .displayItems((itemDisplayParameters, output) -> {
                    for (Item item : ModCreativeTab.itemSets.get(ModCreativeTab.DECORATIONS)) {
                        output.accept(item);
                    }
                })
                .build());
        FURNITURE = register("furniture_tab", FabricItemGroup.builder()
                .title(Component.translatable("item_group." + Ultramarine.MOD_ID + ".furniture"))
                .icon(() -> new ItemStack(ItemRegistry.OAK_CABINET))
                .displayItems((itemDisplayParameters, output) -> {
                    for (Item item : ModCreativeTab.itemSets.get(ModCreativeTab.FURNITURE)) {
                        output.accept(item);
                    }
                })
                .build());
        WINDOWS_AND_DOORS = register("windows_and_doors_tab", FabricItemGroup.builder()
                .title(Component.translatable("item_group." + Ultramarine.MOD_ID + ".windows_and_doors"))
                .icon(() -> new ItemStack(ItemRegistry.CARVED_WOODEN_DOOR))
                .displayItems((itemDisplayParameters, output) -> {
                    for (Item item : ModCreativeTab.itemSets.get(ModCreativeTab.WINDOWS_AND_DOORS)) {
                        output.accept(item);
                    }
                })
                .build());
        PLANTS = register("plants_tab", FabricItemGroup.builder()
                .title(Component.translatable("item_group." + Ultramarine.MOD_ID + ".plants"))
                .icon(() -> new ItemStack(ItemRegistry.MEDIUM_LOTUS))
                .displayItems((itemDisplayParameters, output) -> {
                    for (Item item : ModCreativeTab.itemSets.get(ModCreativeTab.PLANTS)) {
                        output.accept(item);
                    }
                })
                .build());
        LAMPS = register("lamps_tab", FabricItemGroup.builder()
                .title(Component.translatable("item_group." + Ultramarine.MOD_ID + ".lamps"))
                .icon(() -> new ItemStack(ItemRegistry.OCTAGONAL_PALACE_LANTERN))
                .displayItems((itemDisplayParameters, output) -> {
                    for (Item item : ModCreativeTab.itemSets.get(ModCreativeTab.LAMPS)) {
                        output.accept(item);
                    }
                })
                .build());
    }

    private static CreativeModeTab register(String name, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Ultramarine.MOD_ID, name), tab);
    }

}