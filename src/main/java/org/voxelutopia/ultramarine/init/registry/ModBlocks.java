package org.voxelutopia.ultramarine.init.registry;

import net.minecraft.world.item.DyeColor;
import org.voxelutopia.ultramarine.common.block.*;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.block.state.ModBlockStateProperties;
import org.voxelutopia.ultramarine.init.data.ContainerType;
import org.voxelutopia.ultramarine.init.data.RawVoxelShape;
import org.voxelutopia.ultramarine.init.data.shape.BlockShapes;
import org.voxelutopia.ultramarine.init.data.shape.ReShapeFunction;

@SuppressWarnings("unused")
public class ModBlocks {


    /**
     * BUILDING BLOCKS
     */

    // BRICKS

    public static final Block CYAN_BRICKS = registerBlock("cyan_bricks", new BaseBlock());
    public static final Block CYAN_BRICK_SLAB = registerBlock("cyan_brick_slab", new BaseSlab((BaseBlock) CYAN_BRICKS));
    public static final Block CYAN_BRICK_STAIRS = registerBlock("cyan_brick_stairs", new BaseStairs((BaseBlock) CYAN_BRICKS));
    public static final Block CYAN_BRICK_WALL = registerBlock("cyan_brick_wall", new BaseWall((BaseBlock) CYAN_BRICKS));

    public static final Block BLACK_BRICKS = registerBlock("black_bricks", new BaseBlock());
    public static final Block BLACK_BRICK_SLAB = registerBlock("black_brick_slab", new BaseSlab((BaseBlock) BLACK_BRICKS));
    public static final Block BLACK_BRICK_STAIRS = registerBlock("black_brick_stairs", new BaseStairs((BaseBlock) BLACK_BRICKS));
    public static final Block BLACK_BRICK_WALL = registerBlock("black_brick_wall", new BaseWall((BaseBlock) BLACK_BRICKS));

    public static final Block BROWNISH_RED_STONE_BRICKS = registerBlock("brownish_red_stone_bricks", new BaseBlock());
    public static final Block BROWNISH_RED_STONE_BRICK_SLAB = registerBlock("brownish_red_stone_brick_slab", new BaseSlab((BaseBlock) BROWNISH_RED_STONE_BRICKS));
    public static final Block BROWNISH_RED_STONE_BRICK_STAIRS = registerBlock("brownish_red_stone_brick_stairs", new BaseStairs((BaseBlock) BROWNISH_RED_STONE_BRICKS));
    public static final Block BROWNISH_RED_STONE_BRICK_WALL = registerBlock("brownish_red_stone_brick_wall", new BaseWall((BaseBlock) BROWNISH_RED_STONE_BRICKS));

    public static final Block WHITE_AND_PINK_MIXED_BRICKS = registerBlock("white_and_pink_mixed_bricks", new BaseBlock(BaseBlockProperty.STONE));
    public static final Block WHITE_AND_PINK_MIXED_BRICK_SLAB = registerBlock("white_and_pink_mixed_brick_slab", new BaseSlab((BaseBlock) WHITE_AND_PINK_MIXED_BRICKS));
    public static final Block WHITE_AND_PINK_MIXED_BRICK_STAIRS = registerBlock("white_and_pink_mixed_brick_stairs", new BaseStairs((BaseBlock) WHITE_AND_PINK_MIXED_BRICKS));
    public static final Block WHITE_AND_PINK_MIXED_BRICK_WALL = registerBlock("white_and_pink_mixed_brick_wall", new BaseWall((BaseBlock) WHITE_AND_PINK_MIXED_BRICKS));

    public static final Block GREEN_WEATHERED_BRICKS = registerBlock("green_weathered_bricks", new BaseBlock(BaseBlockProperty.STONE));
    public static final Block GREEN_WEATHERED_BRICK_SLAB = registerBlock("green_weathered_brick_slab", new BaseSlab((BaseBlock) GREEN_WEATHERED_BRICKS));
    public static final Block GREEN_WEATHERED_BRICK_STAIRS = registerBlock("green_weathered_brick_stairs", new BaseStairs((BaseBlock) GREEN_WEATHERED_BRICKS));
    public static final Block GREEN_WEATHERED_BRICK_WALL = registerBlock("green_weathered_brick_wall", new BaseWall((BaseBlock) GREEN_WEATHERED_BRICKS));

    // STONES

    public static final Block PALE_YELLOW_STONE = registerBlock("pale_yellow_stone", new BaseBlock());
    public static final Block PALE_YELLOW_STONE_SLAB = registerBlock("pale_yellow_stone_slab", new BaseSlab((BaseBlock) PALE_YELLOW_STONE));
    public static final Block PALE_YELLOW_STONE_STAIRS = registerBlock("pale_yellow_stone_stairs", new BaseStairs((BaseBlock) PALE_YELLOW_STONE));
    public static final Block VARIEGATED_ROCKS = registerBlock("variegated_rocks", new BaseBlock());
    public static final Block VARIEGATED_ROCK_SLAB = registerBlock("variegated_rock_slab", new BaseSlab((BaseBlock) VARIEGATED_ROCKS));
    public static final Block VARIEGATED_ROCK_STAIRS = registerBlock("variegated_rock_stairs", new BaseStairs((BaseBlock) VARIEGATED_ROCKS));
    public static final Block WEATHERED_STONE = registerBlock("weathered_stone", new BaseBlock());
    public static final Block WEATHERED_STONE_SLAB = registerBlock("weathered_stone_slab", new BaseSlab((BaseBlock) WEATHERED_STONE));
    public static final Block WEATHERED_STONE_STAIRS = registerBlock("weathered_stone_stairs", new BaseStairs((BaseBlock) WEATHERED_STONE));
    public static final Block POLISHED_WEATHERED_STONE = registerBlock("polished_weathered_stone", new BaseBlock());
    public static final Block POLISHED_WEATHERED_STONE_SLAB = registerBlock("polished_weathered_stone_slab", new BaseSlab((BaseBlock) POLISHED_WEATHERED_STONE));
    public static final Block POLISHED_WEATHERED_STONE_STAIRS = registerBlock("polished_weathered_stone_stairs", new BaseStairs((BaseBlock) POLISHED_WEATHERED_STONE));
    public static final Block POLISHED_WEATHERED_STONE_WALL = registerBlock("polished_weathered_stone_wall", new BaseWall((BaseBlock) POLISHED_WEATHERED_STONE));
    public static final Block GREEN_WEATHERED_STONE = registerBlock("green_weathered_stone", new BaseBlock(BaseBlockProperty.STONE));

    // FLOOR TILES

    public static final Block LIGHT_CYAN_FLOOR_TILE = registerBlock("light_cyan_floor_tile", new BaseBlock(BaseBlockProperty.TILE));
    public static final Block LIGHT_CYAN_FLOOR_TILE_SLAB = registerBlock("light_cyan_floor_tile_slab", new BaseSlab((BaseBlock) LIGHT_CYAN_FLOOR_TILE));
    public static final Block LIGHT_CYAN_FLOOR_TILE_STAIRS = registerBlock("light_cyan_floor_tile_stairs", new BaseStairs((BaseBlock) LIGHT_CYAN_FLOOR_TILE));
    public static final Block LIGHT_CYAN_FLOOR_TILE_WALL = registerBlock("light_cyan_floor_tile_wall", new BaseWall((BaseBlock) LIGHT_CYAN_FLOOR_TILE));

    public static final Block CYAN_FLOOR_TILE = registerBlock("cyan_floor_tile", new BaseBlock(BaseBlockProperty.TILE));
    public static final Block CYAN_FLOOR_TILE_SLAB = registerBlock("cyan_floor_tile_slab", new BaseSlab((BaseBlock) CYAN_FLOOR_TILE));
    public static final Block CYAN_FLOOR_TILE_STAIRS = registerBlock("cyan_floor_tile_stairs", new BaseStairs((BaseBlock) CYAN_FLOOR_TILE));
    public static final Block CYAN_FLOOR_TILE_WALL = registerBlock("cyan_floor_tile_wall", new BaseWall((BaseBlock) CYAN_FLOOR_TILE));

    public static final Block DARK_CYAN_FLOOR_TILE = registerBlock("dark_cyan_floor_tile", new BaseBlock(BaseBlockProperty.STONE));
    public static final Block DARK_CYAN_FLOOR_TILE_SLAB = registerBlock("dark_cyan_floor_tile_slab", new BaseSlab((BaseBlock) DARK_CYAN_FLOOR_TILE));
    public static final Block DARK_CYAN_FLOOR_TILE_STAIRS = registerBlock("dark_cyan_floor_tile_stairs", new BaseStairs((BaseBlock) DARK_CYAN_FLOOR_TILE));
    public static final Block DARK_CYAN_FLOOR_TILE_WALL = registerBlock("dark_cyan_floor_tile_wall", new BaseWall((BaseBlock) DARK_CYAN_FLOOR_TILE));

    public static final Block LAYERED_CYAN_FLOOR_TILES = registerBlock("layered_cyan_floor_tiles", new BaseHorizontalDirectionalBlock(BaseBlockProperty.STONE));
    public static final Block VERTICAL_CYAN_FLOOR_TILES = registerBlock("vertical_cyan_floor_tiles", new BaseHorizontalDirectionalBlock(BaseBlockProperty.STONE));
    public static final Block MIXED_CYAN_FLOOR_TILES = registerBlock("mixed_cyan_floor_tiles", new BaseHorizontalDirectionalBlock(BaseBlockProperty.STONE));
    public static final Block CHISELED_CYAN_FLOOR_TILE = registerBlock("chiseled_cyan_floor_tile", new BaseBlock(BaseBlockProperty.STONE));
    public static final Block CUT_CYAN_FLOOR_TILES = registerBlock("cut_cyan_floor_tiles", new BaseBlock(BaseBlockProperty.STONE));

    public static final Block WEATHERED_RED_STONE_TILE = registerBlock("weathered_red_stone_tile", new BaseBlock(BaseBlockProperty.STONE));
    public static final Block WEATHERED_RED_STONE_TILE_SLAB = registerBlock("weathered_red_stone_tile_slab", new BaseSlab((BaseBlock) WEATHERED_RED_STONE_TILE));
    public static final Block WEATHERED_RED_STONE_TILE_STAIRS = registerBlock("weathered_red_stone_tile_stairs", new BaseStairs((BaseBlock) WEATHERED_RED_STONE_TILE));
    public static final Block WEATHERED_RED_STONE_TILE_WALL = registerBlock("weathered_red_stone_tile_wall", new BaseWall((BaseBlock) WEATHERED_RED_STONE_TILE));

    public static final Block BLUE_AND_BLACK_TILE = registerBlock("blue_and_black_tile", new BaseBlock(BaseBlockProperty.STONE));
    public static final Block BLUE_AND_BLACK_TILE_SLAB = registerBlock("blue_and_black_tile_slab", new BaseSlab((BaseBlock) BLUE_AND_BLACK_TILE));
    public static final Block BLUE_AND_BLACK_TILE_STAIRS = registerBlock("blue_and_black_tile_stairs", new BaseStairs((BaseBlock) BLUE_AND_BLACK_TILE));
    public static final Block BLUE_AND_BLACK_TILE_WALL = registerBlock("blue_and_black_tile_wall", new BaseWall((BaseBlock) BLUE_AND_BLACK_TILE));

    public static final Block GREEN_GLAZED_TILES = registerBlock("green_glazed_tiles", new BaseBlock(BaseBlockProperty.GLAZED));
    public static final Block GREEN_GLAZED_TILE_SLAB = registerBlock("green_glazed_tile_slab", new BaseSlab((BaseBlock) GREEN_GLAZED_TILES));
    public static final Block GREEN_GLAZED_TILE_STAIRS = registerBlock("green_glazed_tile_stairs", new BaseStairs((BaseBlock) GREEN_GLAZED_TILES));

    public static final Block BLACK_FLOOR_TILES = registerBlock("black_floor_tiles", new BaseBlock(BaseBlockProperty.STONE));
    public static final Block BLACK_FLOOR_TILES_LITTLE_MOSSY = registerBlock("black_floor_tiles_little_mossy", new BaseHorizontalDirectionalBlock(BaseBlockProperty.STONE));
    public static final Block BLACK_FLOOR_TILES_MODERATE_MOSSY = registerBlock("black_floor_tiles_moderate_mossy", new BaseHorizontalDirectionalBlock(BaseBlockProperty.STONE));
    public static final Block BLACK_FLOOR_TILES_VERY_MOSSY = registerBlock("black_floor_tiles_very_mossy", new BaseHorizontalDirectionalBlock(BaseBlockProperty.STONE));

    // WOODEN

    public static final Block ROSEWOOD_PLANKS = registerBlock("rosewood_planks", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block ROSEWOOD_SLAB = registerBlock("rosewood_slab", new BaseSlab((BaseBlock) ROSEWOOD_PLANKS));
    public static final Block ROSEWOOD_STAIRS = registerBlock("rosewood_stairs", new BaseStairs((BaseBlock) ROSEWOOD_PLANKS));
    public static final Block ROSEWOOD_FENCE = registerBlock("rosewood_fence", new BaseFence((BaseBlock) ROSEWOOD_PLANKS));
    public static final Block VARNISHED_ROSEWOOD = registerBlock("varnished_rosewood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block BAMBOO_MAT = registerBlock("bamboo_mat", new BaseBlock(BaseBlockProperty.BAMBOO_WOOD));
    public static final Block BAMBOO_MAT_SLAB = registerBlock("bamboo_mat_slab", new BaseSlab((BaseBlock) BAMBOO_MAT));
    public static final Block BAMBOO_MAT_STAIRS = registerBlock("bamboo_mat_stairs", new BaseStairs((BaseBlock) BAMBOO_MAT));

    // ROOF TILES

    public static final Block GRAY_ROOF_TILES = registerBlock("gray_roof_tiles", new RoofTiles(DyeColor.GRAY, RoofTiles.RoofTileType.NORMAL));
    public static final Block GRAY_ROOF_TILE_STAIRS = registerBlock("gray_roof_tile_stairs", new RoofTiles(DyeColor.GRAY, RoofTiles.RoofTileType.STAIRS));
    public static final Block GRAY_ROOF_TILE_EDGE = registerBlock("gray_roof_tile_edge", new RoofTiles(DyeColor.GRAY, RoofTiles.RoofTileType.EDGE));
    public static final Block YELLOW_ROOF_TILES = registerBlock("yellow_roof_tiles", new RoofTiles(DyeColor.YELLOW, RoofTiles.RoofTileType.NORMAL));
    public static final Block YELLOW_ROOF_TILE_STAIRS = registerBlock("yellow_roof_tile_stairs", new RoofTiles(DyeColor.YELLOW, RoofTiles.RoofTileType.STAIRS));
    public static final Block YELLOW_ROOF_TILE_EDGE = registerBlock("yellow_roof_tile_edge", new RoofTiles(DyeColor.YELLOW, RoofTiles.RoofTileType.EDGE));
    public static final Block GREEN_ROOF_TILES = registerBlock("green_roof_tiles", new RoofTiles(DyeColor.GREEN, RoofTiles.RoofTileType.NORMAL));
    public static final Block GREEN_ROOF_TILE_STAIRS = registerBlock("green_roof_tile_stairs", new RoofTiles(DyeColor.GREEN, RoofTiles.RoofTileType.STAIRS));
    public static final Block GREEN_ROOF_TILE_EDGE = registerBlock("green_roof_tile_edge", new RoofTiles(DyeColor.GREEN, RoofTiles.RoofTileType.EDGE));
    public static final Block BLUE_ROOF_TILES = registerBlock("blue_roof_tiles", new RoofTiles(DyeColor.BLUE, RoofTiles.RoofTileType.NORMAL));
    public static final Block BLUE_ROOF_TILE_STAIRS = registerBlock("blue_roof_tile_stairs", new RoofTiles(DyeColor.BLUE, RoofTiles.RoofTileType.STAIRS));
    public static final Block BLUE_ROOF_TILE_EDGE = registerBlock("blue_roof_tile_edge", new RoofTiles(DyeColor.BLUE, RoofTiles.RoofTileType.EDGE));
    public static final Block CYAN_ROOF_TILES = registerBlock("cyan_roof_tiles", new RoofTiles(DyeColor.CYAN, RoofTiles.RoofTileType.NORMAL));
    public static final Block CYAN_ROOF_TILE_STAIRS = registerBlock("cyan_roof_tile_stairs", new RoofTiles(DyeColor.CYAN, RoofTiles.RoofTileType.STAIRS));
    public static final Block CYAN_ROOF_TILE_EDGE = registerBlock("cyan_roof_tile_edge", new RoofTiles(DyeColor.CYAN, RoofTiles.RoofTileType.EDGE));
    public static final Block BLACK_ROOF_TILES = registerBlock("black_roof_tiles", new RoofTiles(DyeColor.BLACK, RoofTiles.RoofTileType.NORMAL));
    public static final Block BLACK_ROOF_TILE_STAIRS = registerBlock("black_roof_tile_stairs", new RoofTiles(DyeColor.BLACK, RoofTiles.RoofTileType.STAIRS));
    public static final Block BLACK_ROOF_TILE_EDGE = registerBlock("black_roof_tile_edge", new RoofTiles(DyeColor.BLACK, RoofTiles.RoofTileType.EDGE));

    // ROOF RIDGE

    public static final Block BLACK_ROOF_RIDGE_UPPER = registerBlock("black_roof_ridge_upper", new RoofRidge(DyeColor.BLACK, RoofRidge.RoofRidgeType.UPPER));
    public static final Block BLACK_ROOF_RIDGE_LOWER = registerBlock("black_roof_ridge_lower", new RoofRidge(DyeColor.BLACK, RoofRidge.RoofRidgeType.LOWER));
    public static final Block BLACK_ROOF_RIDGE_CONNECTION = registerBlock("black_roof_ridge_connection", new RoofRidgeConnection(DyeColor.BLACK, SnowRoofRidge.RoofRidgeType.CONNECTION));
    public static final Block BLACK_MAIN_ROOF_RIDGE_CONNECTION = registerBlock("black_main_roof_ridge_connection", new RoofMainRidgeConnection(DyeColor.BLACK, SnowRoofRidge.RoofRidgeType.MAIN_CONNECTION));
    public static final Block BLACK_ROOF_RIDGE_UPPER_SLAB = registerBlock("black_roof_ridge_upper_slab", new BaseSlab(BaseBlockProperty.STONE));
    public static final Block BLACK_MAIN_ROOF_RIDGE_SLAB = registerBlock("black_main_roof_ridge_slab", new BaseHorizontalDirectionalSlab(BaseBlockProperty.STONE));
    public static final Block BLACK_MAIN_ROOF_RIDGE_PANEL = registerBlock("black_main_roof_ridge_panel", new RoofRidgePanel(BaseBlockProperty.STONE));
    public static final Block BLACK_MAIN_ROOF_RIDGE_PLATE = registerBlock("black_main_roof_ridge_plate", new RoofRidgePlate(BaseBlockProperty.STONE));
    public static final Block BLACK_MAIN_ROOF_RIDGE_STAIRS = registerBlock("black_main_roof_ridge_stairs", new StraightStairBlock(BaseBlockProperty.STONE));

    public static final Block YELLOW_ROOF_RIDGE_UPPER = registerBlock("yellow_roof_ridge_upper", new RoofRidge(DyeColor.YELLOW, RoofRidge.RoofRidgeType.UPPER));
    public static final Block YELLOW_ROOF_RIDGE_LOWER = registerBlock("yellow_roof_ridge_lower", new RoofRidge(DyeColor.YELLOW, RoofRidge.RoofRidgeType.LOWER));
    public static final Block YELLOW_ROOF_RIDGE_CONNECTION = registerBlock("yellow_roof_ridge_connection", new RoofRidgeConnection(DyeColor.YELLOW, SnowRoofRidge.RoofRidgeType.CONNECTION));
    public static final Block YELLOW_MAIN_ROOF_RIDGE_CONNECTION = registerBlock("yellow_main_roof_ridge_connection", new RoofMainRidgeConnection(DyeColor.YELLOW, SnowRoofRidge.RoofRidgeType.MAIN_CONNECTION));
    public static final Block YELLOW_ROOF_RIDGE_UPPER_SLAB = registerBlock("yellow_roof_ridge_upper_slab", new BaseSlab(BaseBlockProperty.STONE));
    public static final Block YELLOW_MAIN_ROOF_RIDGE_SLAB = registerBlock("yellow_main_roof_ridge_slab", new BaseHorizontalDirectionalSlab(BaseBlockProperty.STONE));
    public static final Block YELLOW_MAIN_ROOF_RIDGE_PANEL = registerBlock("yellow_main_roof_ridge_panel", new RoofRidgePanel(BaseBlockProperty.STONE));
    public static final Block YELLOW_MAIN_ROOF_RIDGE_PLATE = registerBlock("yellow_main_roof_ridge_plate", new RoofRidgePlate(BaseBlockProperty.STONE));
    public static final Block YELLOW_MAIN_ROOF_RIDGE_STAIRS = registerBlock("yellow_main_roof_ridge_stairs", new StraightStairBlock(BaseBlockProperty.STONE));

    /**
     * DECORATIVE BLOCKS
     */

    // SIMPLE WOODEN

    public static final Block OAK_BRACKET = registerBlock("oak_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block SPRUCE_BRACKET = registerBlock("spruce_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block BIRCH_BRACKET = registerBlock("birch_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block JUNGLE_BRACKET = registerBlock("jungle_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block ACACIA_BRACKET = registerBlock("acacia_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block DARK_OAK_BRACKET = registerBlock("dark_oak_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block CRIMSON_BRACKET = registerBlock("crimson_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block WARPED_BRACKET = registerBlock("warped_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block MANGROVE_BRACKET = registerBlock("mangrove_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block CHERRY_BRACKET = registerBlock("cherry_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block VARNISHED_ROSEWOOD_BRACKET = registerBlock("varnished_rosewood_bracket", new Bracket(BaseBlockProperty.WOOD));

    // SIMPLE CARVED WOOD

    public static final Block RED_CARVED_WOOD = registerBlock("red_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block RED_CARVED_WOODEN_SLAB = registerBlock("red_carved_wooden_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block RED_CARVED_WOODEN_STAIRS = registerBlock("red_carved_wooden_stairs", new StraightStairBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_CARVED_WOOD = registerBlock("blue_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_CARVED_WOODEN_SLAB = registerBlock("blue_carved_wooden_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block BLUE_CARVED_WOODEN_STAIRS = registerBlock("blue_carved_wooden_stairs", new StraightStairBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_CARVED_WOODEN_BRACKET_STAIRS = registerBlock("blue_carved_wooden_bracket_stairs", new StraightStairBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_CARVED_WOODEN_BRACKET = registerBlock("blue_carved_wooden_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block CYAN_CARVED_WOOD = registerBlock("cyan_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_CARVED_WOODEN_SLAB = registerBlock("cyan_carved_wooden_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block CYAN_CARVED_WOODEN_STAIRS = registerBlock("cyan_carved_wooden_stairs", new StraightStairBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_CARVED_WOODEN_BRACKET = registerBlock("cyan_carved_wooden_bracket", new Bracket(BaseBlockProperty.WOOD));
    public static final Block GREEN_CARVED_WOOD = registerBlock("green_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block GREEN_CARVED_WOODEN_SLAB = registerBlock("green_carved_wooden_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block GREEN_CARVED_WOODEN_STAIRS = registerBlock("green_carved_wooden_stairs", new StraightStairBlock(BaseBlockProperty.WOOD));
    public static final Block GREEN_CARVED_WOODEN_BRACKET = registerBlock("green_carved_wooden_bracket", new Bracket(BaseBlockProperty.WOOD));

    // MIXED CARVED WOOD

    public static final Block ORANGE_AND_BLUE_CARVED_WOOD = registerBlock("orange_and_blue_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block ORANGE_AND_GREEN_CARVED_WOOD = registerBlock("orange_and_green_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block GREEN_AND_BLUE_CARVED_WOOD = registerBlock("green_and_blue_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block DARK_GREEN_AND_DARK_BLUE_CARVED_WOOD = registerBlock("dark_green_and_dark_blue_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block DARK_BLUE_AND_DARK_GREEN_CARVED_WOOD = registerBlock("dark_blue_and_dark_green_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block BLACK_WHITE_GREEN_CARVED_WOOD = registerBlock("black_white_green_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block BLACK_WHITE_BLUE_CARVED_WOOD = registerBlock("black_white_blue_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block RED_AND_GREEN_CARVED_WOOD = registerBlock("red_and_green_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_AND_RED_CARVED_WOOD = registerBlock("blue_and_red_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_WHITE_RED_CARVED_WOOD = registerBlock("blue_white_red_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block GREEN_WHITE_RED_CARVED_WOOD = registerBlock("green_white_red_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_AND_YELLOW_CARVED_WOOD_VARIANT_1 = registerBlock("blue_and_yellow_carved_wood_variant_1", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_AND_YELLOW_CARVED_WOOD_VARIANT_2 = registerBlock("blue_and_yellow_carved_wood_variant_2", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_AND_YELLOW_CARVED_WOOD_VARIANT_3 = registerBlock("blue_and_yellow_carved_wood_variant_3", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block YELLOW_AND_GREEN_CARVED_WOOD = registerBlock("yellow_and_green_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block YELLOW_GREEN_BLUE_CARVED_WOOD = registerBlock("yellow_green_blue_carved_wood", new BaseBlock(BaseBlockProperty.WOOD));

    // DARK OAK

    public static final Block CARVED_DARK_OAK_BEAM = registerBlock("carved_dark_oak_beam", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CARVED_DARK_OAK_BEAM_EDGE = registerBlock("carved_dark_oak_beam_edge", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK = registerBlock("gilded_dark_oak", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block CHISELED_GILDED_DARK_OAK = registerBlock("chiseled_gilded_dark_oak", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK_SLAB = registerBlock("gilded_dark_oak_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK_STAIRS = registerBlock("gilded_dark_oak_stairs", new StraightStairBlock(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK_BRACKET = registerBlock("gilded_dark_oak_bracket", new Bracket(BaseBlockProperty.WOOD));

    // PILLARS

    public static final Block CARVED_RED_PILLAR = registerBlock("carved_red_pillar", new BaseBlock(BaseBlockProperty.TERRACOTTA));
    public static final Block CARVED_RED_PILLAR_BASE = registerBlock("carved_red_pillar_base", new BaseBlock(BaseBlockProperty.TERRACOTTA));
    public static final Block CARVED_RED_PILLAR_HEAD = registerBlock("carved_red_pillar_head", new BaseDirectionalBlock(BaseBlockProperty.TERRACOTTA));
    public static final Block SPRUCE_PILLAR_BASE = registerBlock("spruce_pillar_base", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block STRIPPED_DARK_OAK_PILLAR_BASE = registerBlock("stripped_dark_oak_pillar_base", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK_PILLAR_HEAD_VARIANT_1 = registerBlock("gilded_dark_oak_pillar_head_variant_1", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK_PILLAR_HEAD_VARIANT_2 = registerBlock("gilded_dark_oak_pillar_head_variant_2", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK_PILLAR_HEAD_VARIANT_3 = registerBlock("gilded_dark_oak_pillar_head_variant_3", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK_PILLAR_HEAD_VARIANT_4 = registerBlock("gilded_dark_oak_pillar_head_variant_4", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK_PILLAR_HEAD_VARIANT_5 = registerBlock("gilded_dark_oak_pillar_head_variant_5", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_AND_WHITE_PILLAR_BASE = registerBlock("cyan_and_white_pillar_base", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_AND_WHITE_DECORATED_PILLAR = registerBlock("cyan_and_white_decorated_pillar", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_AND_WHITE_PILLAR_HEAD = registerBlock("cyan_and_white_pillar_head", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block STONE_PILLAR_BASE = registerBlock("stone_pillar_base", new BaseBlock(BaseBlockProperty.WOOD));

    // FANGXIN

    public static final Block GREEN_FANGXIN = registerBlock("green_fangxin", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block GREEN_FANGXIN_EDGE = registerBlock("green_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_FANGXIN = registerBlock("blue_fangxin", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_FANGXIN_EDGE = registerBlock("blue_fangxin_edge", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_AND_BLUE_FANGXIN = registerBlock("cyan_and_blue_fangxin", new BasePillarBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_AND_BLUE_FANGXIN_EDGE = registerBlock("cyan_and_blue_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_AND_BLUE_FANGXIN_EDGE_VARIANT = registerBlock("cyan_and_blue_fangxin_edge_variant", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_BLUE_WHITE_FANGXIN = registerBlock("cyan_blue_white_fangxin", new BasePillarBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_BLUE_WHITE_FANGXIN_EDGE = registerBlock("cyan_blue_white_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_AND_GREEN_FANGXIN_EDGE = registerBlock("blue_and_green_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YELLOW_AND_GREEN_FANGXIN_EDGE = registerBlock("yellow_and_green_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block DARK_BLUE_FANGXIN = registerBlock("dark_blue_fangxin", new BasePillarBlock(BaseBlockProperty.WOOD));
    public static final Block DARK_BLUE_FANGXIN_EDGE = registerBlock("dark_blue_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_AND_WHITE_FANGXIN = registerBlock("blue_and_white_fangxin", new BasePillarBlock(BaseBlockProperty.WOOD));
    public static final Block GREEN_AND_DARK_BLUE_FANGXIN = registerBlock("green_and_dark_blue_fangxin", new BasePillarBlock(BaseBlockProperty.WOOD));
    public static final Block WHITE_AND_CYAN_FANGXIN = registerBlock("white_and_cyan_fangxin", new BasePillarBlock(BaseBlockProperty.WOOD));
    public static final Block WHITE_AND_CYAN_FANGXIN_EDGE = registerBlock("white_and_cyan_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block WHITE_AND_BLUE_FANGXIN = registerBlock("white_and_blue_fangxin", new BasePillarBlock(BaseBlockProperty.WOOD));
    public static final Block WHITE_AND_BLUE_FANGXIN_EDGE = registerBlock("white_and_blue_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block RED_CYAN_BLUE_FANGXIN = registerBlock("red_cyan_blue_fangxin", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block RED_CYAN_BLUE_FANGXIN_EDGE = registerBlock("red_cyan_blue_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_CYAN_BLUE_FANGXIN_EDGE = registerBlock("blue_cyan_blue_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_CYAN_RED_FANGXIN = registerBlock("blue_cyan_red_fangxin", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_CYAN_RED_FANGXIN_EDGE = registerBlock("blue_cyan_red_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_BLUE_CYAN_FANGXIN = registerBlock("cyan_blue_cyan_fangxin", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block RED_BLUE_RED_FANGXIN = registerBlock("red_blue_red_fangxin", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_AND_YELLOW_FANGXIN_EDGE = registerBlock("cyan_and_yellow_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block MING_BLUE_JINZHUOMO_SHINIANYU_XUANZI_FANGXIN_EDGE = registerBlock("ming_blue_jinzhuomo_shinianyu_xuanzi_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block MING_BLUE_GILDED_JINZHUOMO_SHINIANYU_XUANZI_FANGXIN = registerBlock("ming_blue_gilded_jinzhuomo_shinianyu_xuanzi_fangxin", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block MING_BLUE_GILDED_JINZHUOMO_SHINIANYU_XUANZI_FANGXIN_EDGE = registerBlock("ming_blue_gilded_jinzhuomo_shinianyu_xuanzi_fangxin_edge", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block MING_GREEN_GILDED_JINZHUOMO_SHINIANYU_XUANZI_FANGXIN = registerBlock("ming_green_gilded_jinzhuomo_shinianyu_xuanzi_fangxin", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block MING_GREEN_GILDED_JINZHUOMO_SHINIANYU_XUANZI_FANGXIN_EDGE = registerBlock("ming_green_gilded_jinzhuomo_shinianyu_xuanzi_fangxin_edge", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block QING_BLUE_PLAIN_JINZHUOMO_SHINIANYU_FANGXIN = registerBlock("qing_blue_plain_jinzhuomo_shinianyu_fangxin", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_NIANYUZHUANG_FANGXIN = registerBlock("yuan_nianyuzhuang_fangxin", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_NIANYUZHUANG_FANGXIN_EDGE = registerBlock("yuan_nianyuzhuang_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_PATTERNED_NIANYUZHUANG_FANGXIN = registerBlock("yuan_patterned_nianyuzhuang_fangxin", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_PATTERNED_NIANYUZHUANG_FANGXIN_EDGE = registerBlock("yuan_patterned_nianyuzhuang_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_GREEN_NIANYUZHUANG_FANGXIN = registerBlock("yuan_green_nianyuzhuang_fangxin", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_GREEN_NIANYUZHUANG_FANGXIN_EDGE = registerBlock("yuan_green_nianyuzhuang_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_PATTERNED_WUCHAIZHUANG_FANGXIN = registerBlock("yuan_patterned_wuchaizhuang_fangxin", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_PATTERNED_WUCHAIZHUANG_FANGXIN_EDGE = registerBlock("yuan_patterned_wuchaizhuang_fangxin_edge", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_GREEN_WUCHAIZHUANG_FANGXIN = registerBlock("yuan_green_wuchaizhuang_fangxin", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_GREEN_WUCHAIZHUANG_FANGXIN_EDGE = registerBlock("yuan_green_wuchaizhuang_fangxin_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));


    // ZHAOTOU

    public static final Block BLUE_ZHAOTOU_EDGE = registerBlock("blue_zhaotou_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block GREEN_AND_BLUE_ZHAOTOU = registerBlock("green_and_blue_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_AND_GREEN_ZHAOTOU = registerBlock("blue_and_green_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block blue_and_green_double_layered_zhaotou = registerBlock("blue_and_green_double_layered_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_AND_RED_ZHAOTOU = registerBlock("cyan_and_red_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block CYAN_AND_BLUE_ZHAOTOU = registerBlock("cyan_and_blue_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block GRAY_BLACK_RED_ZHAOTOU = registerBlock("gray_black_red_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_GREEN_RED_ZHAOTOU = registerBlock("blue_green_red_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block RED_AND_BLUE_ZHAOTOU = registerBlock("red_and_blue_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block GREEN_AND_YELLOW_ZHAOTOU = registerBlock("green_and_yellow_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block RED_GREEN_BLUE_ZHAOTOU = registerBlock("red_green_blue_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block WHITE_BLUE_GREEN_ZHAOTOU = registerBlock("white_blue_green_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block MING_YANZHUOMO_SHINIANYU_OUTER_ZHAOTOU = registerBlock("ming_yanzhuomo_shinianyu_outer_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block MING_YANZHUOMO_SHINIANYU_OUTER_ZHAOTOU_EDGE = registerBlock("ming_yanzhuomo_shinianyu_outer_zhaotou_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_NIANYUZHUANG_ZHAOTOU = registerBlock("yuan_nianyuzhuang_zhaotou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_NIANYUZHUANG_ZHAOTOU_CONNECTION = registerBlock("yuan_nianyuzhuang_zhaotou_connection", new BaseDirectionalBlock(BaseBlockProperty.WOOD));

    // GUTOU

    public static final Block GREEN_BLUE_BLACK_GUTOU = registerBlock("green_blue_black_gutou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_GREEN_YELLOW_GUTOU = registerBlock("blue_green_yellow_gutou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BLUE_AND_YELLOW_GUTOU = registerBlock("blue_and_yellow_gutou", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block MING_YANZHUOMO_SHINIANYU_OUTER_GUTOU_EDGE = registerBlock("ming_yanzhuomo_shinianyu_outer_gutou_edge", new BaseDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block YUAN_NIANYUZHUANG_GUTOU = registerBlock("yuan_nianyuzhuang_gutou", new ChiralDirectionalBlock(BaseBlockProperty.WOOD));

    // RAFTERS

    public static final Block GILDED_DARK_OAK_RAFTER = registerBlock("gilded_dark_oak_rafter", new Rafter(BaseBlockProperty.WOOD));
    public static final Block GILDED_DARK_OAK_RAFTER_END = registerBlock("gilded_dark_oak_rafter_end", new RafterEnd(BaseBlockProperty.WOOD));
    public static final Block DARK_OAK_RAFTER = registerBlock("dark_oak_rafter", new Rafter(BaseBlockProperty.WOOD));
    public static final Block DARK_OAK_RAFTER_END = registerBlock("dark_oak_rafter_end", new RafterEnd(BaseBlockProperty.WOOD));
    public static final Block BLUE_TIGER_EYE_RAFTER = registerBlock("blue_tiger_eye_rafter", new Rafter(BaseBlockProperty.WOOD));
    public static final Block BLUE_TIGER_EYE_RAFTER_END = registerBlock("blue_tiger_eye_rafter_end", new RafterEnd(BaseBlockProperty.WOOD));
    public static final Block BLUE_CARVED_TIGER_EYE_RAFTER = registerBlock("blue_carved_tiger_eye_rafter", new Rafter(BaseBlockProperty.WOOD));
    public static final Block BLUE_CARVED_TIGER_EYE_RAFTER_END = registerBlock("blue_carved_tiger_eye_rafter_end", new RafterEnd(BaseBlockProperty.WOOD));
    public static final Block GREEN_CARVED_TIGER_EYE_RAFTER = registerBlock("green_tiger_eye_rafter", new Rafter(BaseBlockProperty.WOOD));
    public static final Block GREEN_CARVED_TIGER_EYE_RAFTER_END = registerBlock("green_tiger_eye_rafter_end", new RafterEnd(BaseBlockProperty.WOOD));
    public static final Block GREEN_WANZI_RAFTER = registerBlock("green_wanzi_rafter", new Rafter(BaseBlockProperty.WOOD));
    public static final Block GREEN_WANZI_RAFTER_END = registerBlock("green_wanzi_rafter_end", new RafterEnd(BaseBlockProperty.WOOD));
    public static final Block GREEN_CARVED_WANZI_RAFTER = registerBlock("green_carved_wanzi_rafter", new Rafter(BaseBlockProperty.WOOD));
    public static final Block GREEN_CARVED_WANZI_RAFTER_END = registerBlock("green_carved_wanzi_rafter_end", new RafterEnd(BaseBlockProperty.WOOD));

    // BEAM HEAD

    public static final Block GILDED_DARK_OAK_BEAM_HEAD = registerBlock("gilded_dark_oak_beam_head", new BeamHead(BaseBlockProperty.WOOD));
    public static final Block QING_GREEN_BEAM_HEAD = registerBlock("qing_green_beam_head", new WallSideBlock(BaseBlockProperty.WOOD, 14));

    // ARCHITRAVE

    public static final Block RED_AND_CYAN_CARVED_ARCHITRAVE = registerBlock("red_and_cyan_carved_architrave", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block RED_AND_CYAN_CARVED_ARCHITRAVE_SLAB = registerBlock("red_and_cyan_carved_architrave_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block RED_AND_BLUE_CARVED_ARCHITRAVE = registerBlock("red_and_blue_carved_architrave", new BaseBlock(BaseBlockProperty.WOOD));
    public static final Block RED_AND_BLUE_CARVED_ARCHITRAVE_SLAB = registerBlock("red_and_blue_carved_architrave_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block EMPTY_TUANHUA_SLAB = registerBlock("empty_tuanhua_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block FULL_TUANHUA_SLAB = registerBlock("full_tuanhua_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block RED_AND_CYAN_TUANHUA_SLAB = registerBlock("red_and_cyan_tuanhua_slab", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block RED_CARVED_ARCHITRAVE = registerBlock("red_carved_architrave", new BaseSlab(BaseBlockProperty.WOOD));
    public static final Block WHITE_AND_RED_SLAB = registerBlock("white_and_red_slab", new BaseSlab(BaseBlockProperty.WOOD));

    // QUETI

    public static final Block LONG_GILDED_DARK_OAK_QUETI = registerBlock("long_gilded_dark_oak_queti", new WallSideBlock(BaseBlockProperty.WOOD, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,4,13,16,16,15))));
    public static final Block LONG_GILDED_DARK_OAK_QUETI_EDGE = registerBlock("long_gilded_dark_oak_queti_edge", new OrientableWallSideBlock(BaseBlockProperty.WOOD, ReShapeFunction.sideOrientedShape(new RawVoxelShape(0,-4,12,32,16,16))));
    public static final Block HORIZONTAL_GILDED_DARK_OAK_QUETI = registerBlock("horizontal_gilded_dark_oak_queti", new SideAxialBlock(BaseBlockProperty.WOOD,
            ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(6,0,0,10,16,16)), ReShapeFunction.cardinalRotations(new RawVoxelShape(6,8,-16,10,16,0)))));
    public static final Block CENTRAL_GILDED_DARK_OAK_QUETI = registerBlock("central_gilded_dark_oak_queti", new CentralAxialBlock(BaseBlockProperty.WOOD,
            ReShapeFunction.exclude(ReShapeFunction.axialRotations(new RawVoxelShape(-16,0,6,32,16,10)), ReShapeFunction.axialRotations(new RawVoxelShape(0,0,6,16,8,10)))));
    public static final Block VERTICAL_GILDED_DARK_OAK_QUETI = registerBlock("vertical_gilded_dark_oak_queti", new SideAxialBlock(BaseBlockProperty.WOOD, 4));
    public static final Block WOODEN_QUETI = registerBlock("wooden_queti", new WallSideBlock(BaseBlockProperty.WOOD, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,11,14,16,16,15))));
    public static final Block WOODEN_QUETI_EDGE = registerBlock("wooden_queti_edge", new OrientableWallSideBlock(BaseBlockProperty.WOOD, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,14,16,16,15))));
    public static final Block TALL_WOODEN_QUETI_EDGE = registerBlock("tall_wooden_queti_edge", new OrientableWallSideBlock(BaseBlockProperty.WOOD,
            ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,14,16,16,15)), ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,14,8,0,15)))));
    public static final Block LARGE_WOODEN_QUETI_EDGE = registerBlock("large_wooden_queti_edge", new OrientableWallSideBlock(BaseBlockProperty.WOOD,
            ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,14,16,16,15)), ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,14,8,0,15)), ReShapeFunction.cardinalRotations(new RawVoxelShape(16,8,14,32,16,15)))));
    public static final Block SHORT_GLAZED_QUETI = registerBlock("short_glazed_queti", new OrientableWallSideBlock(BaseBlockProperty.WOOD, ReShapeFunction.sideOrientedShape(new RawVoxelShape(0,0,14,32,16,15))));
    public static final Block THICK_CARVED_QUETI = registerBlock("thick_carved_queti", new SideAxialBlock(BaseBlockProperty.WOOD, ReShapeFunction.cardinalRotations(new RawVoxelShape(6,0,-7,10,16,32))));
    public static final Block SHORT_THICK_GLAZED_QUETI = registerBlock("short_thick_glazed_queti", new SideAxialBlock(BaseBlockProperty.WOOD, ReShapeFunction.cardinalRotations(new RawVoxelShape(4,0,-16,12,16,16))));
    public static final Block WOODEN_GUALUO = registerBlock("wooden_gualuo", new WallSideBlock(BaseBlockProperty.WOOD, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,13,16,16,15))));
    public static final Block LARGE_WOODEN_GUALUO = registerBlock("large_wooden_gualuo", new WallSideBlock(BaseBlockProperty.WOOD, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,13,16,16,15))));
    public static final Block LARGE_WOODEN_GUALUO_EDGE = registerBlock("large_wooden_gualuo_edge", new OrientableWallSideBlock(BaseBlockProperty.WOOD, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,13,16,16,15))));
    public static final Block CARVED_STONE_PANEL = registerBlock("carved_stone_panel", new CentralAxialBlock(BaseBlockProperty.STONE, 8));
    public static final Block CARVED_STONE_PANEL_EDGE = registerBlock("carved_stone_panel_edge", new SideAxialBlock(BaseBlockProperty.STONE, 8));
    public static final Block CARVED_STONE_PANEL_CENTER = registerBlock("carved_stone_panel_center", new CentralAxialBlock(BaseBlockProperty.STONE, 8));
    public static final Block CARVED_STONE_QUETI_PART = registerBlock("carved_stone_queti_part", new SideAxialBlock(BaseBlockProperty.STONE, ReShapeFunction.cardinalRotations(new RawVoxelShape(4,0,-9,12,16,31))));
    public static final Block CARVED_STONE_BEAM = registerBlock("carved_stone_beam", new SideAxialBlock(BaseBlockProperty.STONE, ReShapeFunction.cardinalRotations(new RawVoxelShape(3.5,0,-16,12.5,16,32))));
    public static final Block CARVED_STONE_QUETI = registerBlock("carved_stone_queti", new SideAxialBlock(BaseBlockProperty.STONE, ReShapeFunction.cardinalRotations(new RawVoxelShape(4,-16,0,12,16,16))));

    // PATTERNS

    public static final Block YELLOW_CARVED_PATTERN = registerBlock("yellow_carved_pattern", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-6,2,15,22,14,16))));
    public static final Block CIRCULAR_YELLOW_CARVED_PATTERN = registerBlock("circular_yellow_carved_pattern", new SixSideBlock(BaseBlockProperty.DYE));
    public static final Block YELLOW_CARVED_FANGXIN_EDGE_PATTERN = registerBlock("yellow_carved_fangxin_edge_pattern", new OrientableSixSideBlock(BaseBlockProperty.DYE));
    public static final Block YELLOW_CARVED_FANGXIN_PATTERN = registerBlock("yellow_carved_fangxin_pattern", new OrientableSixSideBlock(BaseBlockProperty.DYE));
    public static final Block LARGE_YELLOW_CARVED_PATTERN = registerBlock("large_yellow_carved_pattern", new SixSideBlock(BaseBlockProperty.DYE,
            ReShapeFunction.sixSideShape(new RawVoxelShape(-16,-16,15,32,32,16), new RawVoxelShape(-16,0,-16,32,1,32))));
    public static final Block MEDIUM_YELLOW_CARVED_PATTERN = registerBlock("medium_yellow_carved_pattern", new OrientableSixSideBlock(BaseBlockProperty.DYE,
            ReShapeFunction.orientedSixSideShape(new RawVoxelShape(-16,0,15,32,32,16), new RawVoxelShape(-16,0,-16,32,1,16))));
    public static final Block YELLOW_CARVED_ZHAOTOU_PATTERN = registerBlock("yellow_carved_zhaotou_pattern", new OrientableSixSideBlock(BaseBlockProperty.DYE));
    public static final Block LONG_YELLOW_CARVED_ZHAOTOU_PATTERN = registerBlock("long_yellow_carved_zhaotou_pattern", new OrientableSixSideBlock(BaseBlockProperty.DYE,
            ReShapeFunction.orientedSixSideShape(new RawVoxelShape(0,0,15,16,32,16), new RawVoxelShape(0,0,-16,16,1,16))));
    public static final Block CARVED_ZHAOTOU_PATTERN = registerBlock("carved_zhaotou_pattern", new OrientableSixSideBlock(BaseBlockProperty.DYE));
    public static final Block FLAME_ARCH_WALL_PATTERN = registerBlock("flame_arch_wall_pattern", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-4,0,15,20,16,16))));
    public static final Block GOLDEN_DRAGON_FANGXIN_PATTERN = registerBlock("golden_dragon_fangxin_pattern", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,0,15,32,16,16))));
    public static final Block BLUE_FANGXIN_PATTERN = registerBlock("blue_fangxin_pattern", new OrientableSixSideBlock(BaseBlockProperty.DYE));
    public static final Block BLUE_FANGXIN_PATTERN_EDGE = registerBlock("blue_fangxin_pattern_edge", new OrientableSixSideBlock(BaseBlockProperty.DYE));
    public static final Block GREEN_FANGXIN_PATTERN = registerBlock("green_fangxin_pattern", new OrientableSixSideBlock(BaseBlockProperty.DYE));
    public static final Block GREEN_FANGXIN_PATTERN_EDGE = registerBlock("green_fangxin_pattern_edge", new OrientableSixSideBlock(BaseBlockProperty.DYE));
    public static final Block LIGHT_BLUE_SU_STYLE_CAIHUA = registerBlock("light_blue_su_style_caihua", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-9,0,15,23,16,16))));
    public static final Block YELLOW_SU_STYLE_CAIHUA = registerBlock("yellow_su_style_caihua", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-9,0,15,23,16,16))));
    public static final Block LIGHT_YELLOW_SU_STYLE_CAIHUA = registerBlock("light_yellow_su_style_caihua", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-9,0,15,23,16,16))));
    public static final Block MEDIUM_SU_STYLE_CAIHUA = registerBlock("medium_su_style_caihua", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,8,15,32,32,16))));
    public static final Block LARGE_WHITE_SU_STYLE_CAIHUA = registerBlock("large_white_su_style_caihua", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,0,15,32,32,16))));
    public static final Block LARGE_GREEN_SU_STYLE_CAIHUA = registerBlock("large_green_su_style_caihua", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,0,15,32,32,16))));
    public static final Block LARGE_GRAY_SU_STYLE_CAIHUA = registerBlock("large_gray_su_style_caihua", new WallSideBlock(BaseBlockProperty.DYE, ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,0,15,32,32,16))));

    // CHUIHUA

    public static final Block BLUE_AND_GREEN_CHUIHUA = registerBlock("blue_and_green_chuihua",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.diagonal(new RawVoxelShape(2,2,2,14,16,14))).diagonallyPlaceable().noOcclusion().build());
    public static final Block CYAN_AND_YELLOW_CHUIHUA = registerBlock("cyan_and_yellow_chuihua",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.simpleShape(new RawVoxelShape(0,-16,0,16,16,16))).noOcclusion().build());
    public static final Block GILDED_DARK_OAK_CHUIHUA = registerBlock("gilded_dark_oak_chuihua", new SixSideBlock(BaseBlockProperty.WOOD,
            ReShapeFunction.sixSideShape(new RawVoxelShape(2,2,2,14,14,16), new RawVoxelShape(2,0,2,14,14,14)), true));

    // ROOF CHARM

    public static final Block GOLDEN_GLAZED_ROOF_CHARM = registerBlock("golden_glazed_roof_charm",
            DecorativeBlock.with(BaseBlockProperty.GLAZED).shaped(
                            ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,-16,16,0,32)), ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,-16,16,16,16)),
                                    ReShapeFunction.cardinalRotations(new RawVoxelShape(4.5,16,-7.5,11.5,29,-0.5)), ReShapeFunction.cardinalRotations(new RawVoxelShape(1,16,7,15,31,23))))
                    .directional().noOcclusion().build());
    public static final Block GOLDEN_GLAZED_ROOF_CHARM_ACCESSORY = registerBlock("golden_glazed_roof_charm_accessory", new WallSideBlock(BaseBlockProperty.GLAZED, ReShapeFunction.cardinalRotations(new RawVoxelShape(6,6,10,10,10,16))));
    public static final Block GREEN_GLAZED_ROOF_CHARM = registerBlock("green_glazed_roof_charm",
            DecorativeBlock.with(BaseBlockProperty.GLAZED).shaped(
                            ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,0,16,4,32)), ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,0,16,16,16)),
                                    ReShapeFunction.cardinalRotations(new RawVoxelShape(0,16,0,16,22,20)), ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,12,16,34,26))))
                    .directional().build());
    public static final Block DARK_PRISMARINE_CHIWEI = registerBlock("dark_prismarine_chiwei", DecorativeBlock.with(BaseBlockProperty.STONE)
                    .shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,-16,16,30,15))).directional().noOcclusion().build());

    // CEILING

    public static final Block QING_GOLDEN_DRAGON_CEILING = registerBlock("qing_golden_dragon_ceiling",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(BlockShapes.S16_H8).build());

    // RAILING

    public static final Block WHITE_MARBLE_RAILING = registerBlock("white_marble_railing", new RailingBlock(BaseBlockProperty.MARBLE, RailingBlock.MARBLE_SHAPES));
    public static final Block WHITE_MARBLE_RAILING_SLANT = registerBlock("white_marble_railing_slant", new RailingSlant(BaseBlockProperty.MARBLE, 4));
    public static final Block CARVED_WOODEN_RAILING = registerBlock("carved_wooden_railing", new RailingBlock(BaseBlockProperty.WOOD, RailingBlock.WOODEN_SHAPES));
    public static final Block RED_WOODEN_RAILING = registerBlock("red_wooden_railing", new CentralAxialBlock(BaseBlockProperty.WOOD, 2, 15));
    public static final Block RED_WOODEN_RAILING_EDGE = registerBlock("red_wooden_railing_edge", new CentralAxialBlock(BaseBlockProperty.WOOD, 2, 15));
    public static final Block WOODEN_RAILING = registerBlock("wooden_railing", new CentralAxialBlock(BaseBlockProperty.WOOD, 2, 15));
    public static final Block WOODEN_RAILING_VARIANT = registerBlock("wooden_railing_variant", new CentralAxialBlock(BaseBlockProperty.WOOD, 2, 15));

    // GUARDIAN LION

    public static final Block SMALL_WOODEN_GUARDIAN_LION = registerBlock("small_wooden_guardian_lion",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(BlockShapes.BACKWARD_3B_L).directional().noOcclusion().noFenceConnect().build());
    public static final Block SMALL_STONE_GUARDIAN_LION = registerBlock("small_stone_guardian_lion",
            DecorativeBlock.with(BaseBlockProperty.STONE).shaped(BlockShapes.BACKWARD_3B_L).directional().noOcclusion().noFenceConnect().build());
    public static final Block SMALL_JADE_GUARDIAN_LION = registerBlock("small_jade_guardian_lion",
            DecorativeBlock.with(BaseBlockProperty.JADE).shaped(BlockShapes.BACKWARD_3B_L).directional().noOcclusion().noFenceConnect().build());
    public static final Block SMALL_YELLOW_GLAZED_GUARDIAN_LION = registerBlock("small_yellow_glazed_guardian_lion",
            DecorativeBlock.with(BaseBlockProperty.GLAZED).shaped(BlockShapes.BACKWARD_3B_L).directional().noOcclusion().noFenceConnect().build());
    public static final Block SMALL_GREEN_GLAZED_GUARDIAN_LION = registerBlock("small_green_glazed_guardian_lion",
            DecorativeBlock.with(BaseBlockProperty.GLAZED).shaped(BlockShapes.BACKWARD_3B_L).directional().noOcclusion().noFenceConnect().build());
    public static final Block XUMI_STONE_MONOLITH = registerBlock("xumi_stone_monolith",
            DecorativeBlock.with(BaseBlockProperty.STONE).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(-8,0,-1,24,32,17))).directional().noOcclusion().noFenceConnect().build());

    // MISC DECORATIONS

    public static final Block CABBAGE_BASKET = registerBlock("cabbage_basket", new StackableHalfBlock(BaseBlockProperty.CROP));
    public static final Block CELERY_BASKET = registerBlock("celery_basket", new StackableHalfBlock(BaseBlockProperty.CROP));
    public static final Block ORANGE_BASKET = registerBlock("orange_basket", new StackableHalfBlock(BaseBlockProperty.CROP));
    public static final Block APPLE_BASKET = registerBlock("apple_basket", new StackableHalfBlock(BaseBlockProperty.CROP));
    public static final Block EGGPLANT_BASKET = registerBlock("eggplant_basket", new StackableHalfBlock(BaseBlockProperty.CROP));
    public static final Block PEAR_BASKET = registerBlock("pear_basket", new StackableHalfBlock(BaseBlockProperty.CROP));
    public static final Block BOOKSHELF = registerBlock("bookshelf", new BaseHorizontalDirectionalBlock(BaseBlockProperty.WOOD));
    public static final Block BOOKSHELF_VARIANT = registerBlock("bookshelf_variant", new BaseHorizontalDirectionalBlock(BaseBlockProperty.WOOD));

    /**
     * DECORATIONS
     */

    // STUDY

    public static final Block ABACUS = registerBlock("abacus",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(-1,0,3.5,17,1.5,12.5))).directional().diagonallyPlaceable().noCollision().build());
    public static final Block BRUSH_TOOLS = registerBlock("brush_tools",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(0.5,0,4,15.5,13,10))).directional().diagonallyPlaceable().noCollision().build());
    public static final Block BRUSH_AND_INKSTONE = registerBlock("brush_and_inkstone",
            DecorativeBlock.with(BaseBlockProperty.PAPER).shaped(BlockShapes.S16_H4).directional().noCollision().noOcclusion().build());
    public static final Block RULER = registerBlock("ruler",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(BlockShapes.S16_H1).directional().diagonallyPlaceable().noCollision().build());
    public static final Block PAINTING_SCROLL = registerBlock("painting_scroll",
            DecorativeBlock.with(BaseBlockProperty.PAPER).shaped(BlockShapes.S16_H4).directional().diagonallyPlaceable().noCollision().build());
    public static final Block BOOK_STACK = registerBlock("book_stack",
            DecorativeBlock.with(BaseBlockProperty.PAPER).shaped(BlockShapes.S16_H4).directional().noOcclusion().build());
    public static final Block PAPER = registerBlock("paper",
            DecorativeBlock.with(BaseBlockProperty.PAPER).shaped(BlockShapes.S16_H1).directional().diagonallyPlaceable().noOcclusion().noCollision().build());
    public static final Block BLUE_BOOK = registerBlock("blue_book",
            DecorativeBlock.with(BaseBlockProperty.PAPER).shaped(BlockShapes.S16_H2).directional().noOcclusion().noCollision().build());
    public static final Block BAMBOO_SLIPS = registerBlock("bamboo_slips",
            DecorativeBlock.with(BaseBlockProperty.SILK).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(7,0,2,14,6,14))).directional().diagonallyPlaceable().noCollision().noOcclusion().build());


    // ROOM DECO

    public static final Block BACK_CUSHION = registerBlock("back_cushion",
            DecorativeBlock.with(BaseBlockProperty.SILK).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(2,0,7,14,13,16))).directional().noCollision().noOcclusion().noFenceConnect().build());
    public static final Block CHESSBOARD = registerBlock("chessboard",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.diagonalSquare(32, 1)).directional().diagonallyPlaceable().noCollision().noOcclusion().build());
    public static final Block BLUE_AND_WHITE_PORCELAIN_VASE = registerBlock("blue_and_white_porcelain_vase",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.or(ReShapeFunction.diagonalSquare(8, 7), ReShapeFunction.diagonalSquare(5, 14))).diagonallyPlaceable().build());
    public static final Block LARGE_BLUE_AND_WHITE_PORCELAIN_VASE = registerBlock("large_blue_and_white_porcelain_vase",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.centeredSquare(12, 28)).directional().build());
    public static final Block SHORT_BLUE_AND_WHITE_PORCELAIN_POT = registerBlock("short_blue_and_white_porcelain_pot",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.centeredSquare(10, 10)).build());
    public static final Block TALL_BLUE_AND_WHITE_PORCELAIN_POT = registerBlock("tall_blue_and_white_porcelain_pot",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.diagonalSquare(6, 13)).directional().diagonallyPlaceable().build());
    public static final Block BLUE_AND_WHITE_PORCELAIN_BOWL = registerBlock("blue_and_white_porcelain_bowl",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.diagonalSquare(10, 6)).diagonallyPlaceable().build());
    public static final Block BRONZE_MIRROR = registerBlock("bronze_mirror",
            DecorativeBlock.with(BaseBlockProperty.BRONZE).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,11,16,18,13))).directional().build());
    public static final Block MEMORIAL_TABLET = registerBlock("memorial_tablet",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(1,0,11,15,16,14),180)).directional().diagonallyPlaceable().noCollision().noFenceConnect().build());
    public static final Block BRONZE_CENSER = registerBlock("bronze_censer",
            new Censer(DecorativeBlock.with(BaseBlockProperty.BRONZE).shaped(ReShapeFunction.diagonalSquare(10, 8)).directional().diagonallyPlaceable().noOcclusion().luminous(), new Vec3(0.5, 0.7, 0.5)));
    public static final Block ROYAL_CENSER = registerBlock("royal_censer",
            new Censer(DecorativeBlock.with(BaseBlockProperty.BRONZE).shaped(ReShapeFunction.centeredSquare(10, 15)).directional().luminous(), new Vec3(0.5, 0.5, 0.5)));
    public static final Block PORCELAIN_TEAPOT = registerBlock("porcelain_teapot",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.centeredSquare(10, 6)).directional().noCollision().build());
    public static final Block BOOTS = registerBlock("boots",
            DecorativeBlock.with(BaseBlockProperty.SILK).shaped(ReShapeFunction.centeredSquare(14, 8)).directional().noCollision().build());
    public static final Block JADE_PENDANT = registerBlock("jade_pendant",
            DecorativeBlock.with(BaseBlockProperty.JADE).shaped(ReShapeFunction.diagonalSquare(3, 16)).directional().diagonallyPlaceable().noCollision().build());
    public static final Block IMPERIAL_JADE_SEAL = registerBlock("imperial_jade_seal",
            DecorativeBlock.with(BaseBlockProperty.JADE).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(5,0,4,10,14,10))).directional().build());
    public static final Block LONG_PILLOW = registerBlock("long_pillow",
            DecorativeBlock.with(BaseBlockProperty.SILK).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(-4,0,4,20,8,12))).directional().diagonallyPlaceable().noOcclusion().build());
    public static final Block WIND_CHIME = registerBlock("wind_chime", new WindChime());

    // MISC OBJECTS

    public static final Block RED_SILK_FABRIC_ROLL = registerBlock("red_silk_fabric_roll",
            DecorativeBlock.with(BaseBlockProperty.SILK).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(-7,0,4,23,4,12))).directional().diagonallyPlaceable().noCollision().noOcclusion().build());
    public static final Block GREEN_SILK_FABRIC_ROLL = registerBlock("green_silk_fabric_roll",
            DecorativeBlock.with(BaseBlockProperty.SILK).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(-7,0,4,23,4,12))).directional().diagonallyPlaceable().noCollision().noOcclusion().build());
    public static final Block PURPLE_SILK_FABRIC_ROLL = registerBlock("purple_silk_fabric_roll",
            DecorativeBlock.with(BaseBlockProperty.SILK).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(-7,0,4,23,4,12))).directional().diagonallyPlaceable().noCollision().noOcclusion().build());
    public static final Block CLAY_DOLL_MALE = registerBlock("clay_doll_male",
            DecorativeBlock.with(BaseBlockProperty.TERRACOTTA).shaped(ReShapeFunction.diagonalSquare(8, 16)).directional().diagonallyPlaceable().build());
    public static final Block CLAY_DOLL_FEMALE = registerBlock("clay_doll_female",
            DecorativeBlock.with(BaseBlockProperty.TERRACOTTA).shaped(ReShapeFunction.diagonalSquare(8, 16)).directional().diagonallyPlaceable().build());
    public static final Block BOTTLE_GOURD = registerBlock("bottle_gourd",
            new BottleGourd(DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.diagonalSquare(7, 12)).directional().diagonallyPlaceable()));

    // FLOWER POTS

    public static final Block BLUE_AND_WHITE_PORCELAIN_FLOWERPOT = registerBlock("blue_and_white_porcelain_flowerpot",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.diagonalSquare(10, 17)).diagonallyPlaceable().build());
    public static final Block TERRACOTTA_FLOWERPOT = registerBlock("terracotta_flowerpot",
            DecorativeBlock.with(BaseBlockProperty.TERRACOTTA).shaped(ReShapeFunction.diagonalSquare(12, 17)).diagonallyPlaceable().noOcclusion().build());
    public static final Block BLACK_IRON_FLOWERPOT = registerBlock("black_iron_flowerpot",
            DecorativeBlock.with(BaseBlockProperty.IRON).shaped(ReShapeFunction.centeredSquare(8, 8)).directional().build());
    public static final Block BLUE_PORCELAIN_FLOWERPOT = registerBlock("blue_porcelain_flowerpot",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.centeredSquare(12, 17)).directional().build());

    // COURTYARD

    public static final Block SACK = registerBlock("sack",
            ContainerDecorativeBlock.with(BaseBlockProperty.SILK).content(ContainerType.COMMON_SMALL).shaped(ReShapeFunction.centeredSquare(6, 6)).directional().noCollision().pushReaction(PushReaction.BLOCK).build());
    public static final Block BAMBOO_TEA_BASKET = registerBlock("bamboo_tea_basket",
            ContainerDecorativeBlock.with(BaseBlockProperty.BAMBOO_WOOD).content(ContainerType.FOOD_REGULAR).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(1,0,2,15,24,16))).directional().diagonallyPlaceable().pushReaction(PushReaction.BLOCK).noOcclusion().build());
    public static final Block EMPTY_BAMBOO_TEA_BASKET = registerBlock("empty_bamboo_tea_basket",
            ContainerDecorativeBlock.with(BaseBlockProperty.BAMBOO_WOOD).content(ContainerType.FOOD_REGULAR).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(1,0,2,15,24,16))).directional().diagonallyPlaceable().pushReaction(PushReaction.BLOCK).noOcclusion().build());
    public static final Block STRAW_HAT = registerBlock("straw_hat",
            DecorativeBlock.with(BaseBlockProperty.FLAX).shaped(ReShapeFunction.diagonalSquare(14, 5)).diagonallyPlaceable().noCollision().noOcclusion().build());
    public static final Block KNIFE = registerBlock("knife",
            DecorativeBlock.with(BaseBlockProperty.IRON).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(6.5,0,-2.5,9.5,1,18.5))).directional().diagonallyPlaceable().noCollision().noOcclusion().build());
    public static final Block SUNDIAL = registerBlock("sundial",
            DecorativeBlock.with(BaseBlockProperty.STONE).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(-3,0,4.5,19,20,16))).directional().build());
    public static final Block BROOM = registerBlock("broom",
            DecorativeBlock.with(BaseBlockProperty.STONE).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(5,0,5,11,24,16.5))).directional().build());
    public static final Block STONE_PEDESTAL = registerBlock("stone_pedestal",
            DecorativeBlock.with(BaseBlockProperty.STONE).shaped(ReShapeFunction.diagonalSquare(12, 8)).diagonallyPlaceable().noOcclusion().build());
    public static final Block TERRACOTTA_POT = registerBlock("terracotta_pot",
            DecorativeBlock.with(BaseBlockProperty.TERRACOTTA).shaped(ReShapeFunction.diagonalSquare(12, 8)).directional().diagonallyPlaceable().noOcclusion().build());
    public static final Block CHINESE_HERBS_BAG = registerBlock("chinese_herbs_bag",
            DecorativeBlock.with(BaseBlockProperty.SILK).shaped(BlockShapes.S16_H8).directional().build());
    public static final Block FRUIT_BOX = registerBlock("fruit_box",
            ContainerDecorativeBlock.with(BaseBlockProperty.WOOD).content(ContainerType.FOOD_REGULAR).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(0,0,3,16,8,13))).directional().diagonallyPlaceable().build());
    public static final Block WOODEN_CRATE = registerBlock("wooden_crate",
            ContainerDecorativeBlock.with(BaseBlockProperty.WOOD).content(ContainerType.COMMON_REGULAR).shaped(BlockShapes.S16_H16).pushReaction(PushReaction.BLOCK).build());
    public static final Block GUNNY_SACK = registerBlock("gunny_sack",
            ContainerDecorativeBlock.with(BaseBlockProperty.FLAX).content(ContainerType.COMMON_REGULAR).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(3,0,2,15,13,14))).directional().pushReaction(PushReaction.BLOCK).build());
    public static final Block BRONZE_DING = registerBlock("bronze_ding",
            DecorativeBlock.with(BaseBlockProperty.BRONZE).shaped(
                            ReShapeFunction.or(ReShapeFunction.exclude(ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,-3,-7,23,16,23)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-4,1,-4,20,16,20))),/*main shape*/
                                    ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,-16,-5,1,-3,1)), ReShapeFunction.cardinalRotations(new RawVoxelShape(15,-16,-5,21,-3,1)),
                                    ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,-16,15,1,-3,21)),ReShapeFunction.cardinalRotations(new RawVoxelShape(15,-16,15,21,-3,21)), /*legs*/
                                    ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,16,3,-4,24,13)), ReShapeFunction.cardinalRotations(new RawVoxelShape(20,16,3,23,24,13)))) /*handles*/
                    .placeOffset(Direction.UP).directional().noOcclusion().build());
    public static final Block CARRIAGE = registerBlock("carriage",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(
                    ReShapeFunction.or(
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,4,20,23,7.5,16)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,5.5,16,23,9,12)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,7,12,23,10.5,8)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,8.5,8,23,12,4)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,10,4,23,13.5,0)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,11.5,0,23,15,-4)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,13,-4,23,16.5,-8)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,14.5,-8,23,18,-12)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,14.5,-14,21,32,-6)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(19,4,20,21,21.5,16)), ReShapeFunction.cardinalRotations(new RawVoxelShape(19,5.5,16,21,23,12)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(19,7,12,21,24.5,8)), ReShapeFunction.cardinalRotations(new RawVoxelShape(19,8.5,8,21,26,4)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(19,10,4,21,27.5,0)), ReShapeFunction.cardinalRotations(new RawVoxelShape(19,11.5,0,21,29,-4)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(19,13,-4,21,30.5,-8)), ReShapeFunction.cardinalRotations(new RawVoxelShape(19,14.5,-8,21,32,-12)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,4,20,-3,21.5,16)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,5.5,16,-3,23,12)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,7,12,-3,24.5,8)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,8.5,8,-3,26,4)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,10,4,-3,27.5,0)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,11.5,0,-3,29,-4)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,13,-4,-3,30.5,-8)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-5,14.5,-8,-3,32,-12)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-9,0,-9,-6,19.5,12)), ReShapeFunction.cardinalRotations(new RawVoxelShape(22,0,-9,25,19.5,12)))
            ).directional().noOcclusion().build());
    public static final Block WOODEN_POLES = registerBlock("wooden_poles",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,0,16,32,16)), ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,16,16,0,32)))).directional().noOcclusion().build());
    public static final Block TEAHOUSE_FLAG = registerBlock("teahouse_flag",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(7,-16,7,9,32,9)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-1.5,23.5,6,17.5,25,8)))).placeOffset(Direction.UP).directional().noOcclusion().build());
    public static final Block KNIFE_REST = registerBlock("knife_rest",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,0,0,32,16,16))).directional().noOcclusion().build());

    // WALL HANGING

    public static final Block LONG_HANGING_PAINTING = registerBlock("long_hanging_painting",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,15,16,28,16))));
    public static final Block WHITE_LANDSCAPE_PAINTING = registerBlock("white_landscape_painting",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(-3,-8,15,19,32,16))));
    public static final Block HORIZONTAL_OLD_LANDSCAPE_PAINTING = registerBlock("horizontal_old_landscape_painting",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(-12,0,15,28,16,16))));
    public static final Block HORIZONTAL_CALLIGRAPHY = registerBlock("horizontal_calligraphy",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(-9,0,15,25,14,16))));
    public static final Block HORIZONTAL_LANDSCAPE_PAINTING = registerBlock("horizontal_landscape_painting",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,0,14,32,32,16))));
    public static final Block LONG_YELLOW_HANGING_PAINTING = registerBlock("long_yellow_hanging_painting",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,14,16,32,16))));
    public static final Block DAMAGED_LANDSCAPE_PAINTING = registerBlock("damaged_landscape_painting",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,0,15,32,32,16))));
    public static final Block PORTRAIT = registerBlock("portrait",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,15,16,32,16))));
    public static final Block HANGING_PAINTING_FAN = registerBlock("hanging_painting_fan",
            new WallSideBlock(BaseBlockProperty.PAPER,  ReShapeFunction.cardinalRotations(new RawVoxelShape(-3,6,15,19,17,16))));
    public static final Block SINCERE_CALLIGRAPHY = registerBlock("sincere_calligraphy",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,14,16,32,16))));

    // FOOD

    public static final Block FOOD_HAMPER = registerBlock("food_hamper",
            ContainerDecorativeBlock.with(BaseBlockProperty.WOOD).content(ContainerType.FOOD_LARGE).shaped(ReShapeFunction.diagonalSquare(7, 15)).directional().diagonallyPlaceable().pushReaction(PushReaction.BLOCK).build());
    public static final Block PLATED_MOONCAKES = registerBlock("plated_mooncakes", //todo change stone slab plate to actual block
            ConsumableDecorativeBlock.with(BaseBlockProperty.PORCELAIN).bites(4).platedWith(() -> ModItems.BLUE_AND_WHITE_PORCELAIN_PLATE).food(ModFoods.MOONCAKE).shaped(BlockShapes.S16_H1).directional().build());
    public static final Block PLATED_MUNG_BEAN_CAKES = registerBlock("plated_mung_bean_cakes",
            ConsumableDecorativeBlock.with(BaseBlockProperty.GLAZED).bites(7).platedWith(() -> ModItems.BLACK_PORCELAIN_PLATE).food(ModFoods.MUNG_BEAN_CAKE)
                    .shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,2,16,1,14))).directional().noOcclusion().build());
    public static final Block PLATED_HAM = registerBlock("plated_ham",
            ConsumableDecorativeBlock.with(BaseBlockProperty.WOOD).bites(4).platedWith(() -> ModItems.POLISHED_OAK_PLANK).food(ModFoods.COOKED_MEAT).shaped(BlockShapes.S16_H1).directional().noOcclusion().build());
    public static final Block PLATED_FISH = registerBlock("plated_fish",
            ConsumableDecorativeBlock.with(BaseBlockProperty.WOOD).bites(3).platedWith(() -> ModItems.POLISHED_OAK_PLANK).food(ModFoods.FISH).shaped(BlockShapes.S16_H1).directional().noOcclusion().build());
    public static final Block SCATTERED_CARROTS = registerBlock("scattered_carrots",
            ConsumableDecorativeBlock.with(BaseBlockProperty.CROP).bites(2).platedWith(() -> ModItems.POLISHED_OAK_PLANK).food(Foods.CARROT)
                    .whenFinished((pState, pLevel, pPos, pPlayer) -> {pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL); pLevel.gameEvent(pPlayer, GameEvent.BLOCK_DESTROY, pPos);})
                    .shaped(BlockShapes.S16_H4).directional().noOcclusion().build());
    public static final Block WINE_POT = registerBlock("wine_pot",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.centeredSquare(6, 17)).directional().build());
    public static final Block XIAOLONGBAO = registerBlock("xiaolongbao",
            ConsumableDecorativeBlock.with(BaseBlockProperty.BAMBOO).bites(8).platedWith(() -> ModItems.POLISHED_OAK_PLANK).food(ModFoods.BAOZI)
//                    .whenFinished((pState, pLevel, pPos, pPlayer) -> {
//                        pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
//                        pLevel.gameEvent(pPlayer, GameEvent.BLOCK_DESTROY, pPos);
//                    })
                    .shaped(ReShapeFunction.of(state -> state.getValue(ModBlockStateProperties.BITES) <= 4 ? Block.box(0,0,0,16,8,16) : BlockShapes.S16_H16.apply(state))).noOcclusion().directional().build());

    //ICE

    public static final Block ICICLE = registerBlock("icicle", new Icicle(3));
    public static final Block LARGE_ICICLE = registerBlock("large_icicle", new Icicle(2, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,15,16,32,16))));

    // CELEBRATION

    public static final Block COUPLET = registerBlock("couplet", new OrientableWallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.sideOrientedShape(new RawVoxelShape(2,0,15,8,32,16))));
    public static final Block COUPLET_TOP = registerBlock("couplet_top", new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(6,2,15,26,8,16))));
    public static final Block FU_MARK = registerBlock("fu_mark", new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(9,7,15,23,21,16))));

    /**
     * FURNITURE
     */

    // CABINET

    public static final Block OAK_CABINET = registerBlock("oak_cabinet",
            ContainerDecorativeBlock.with(BaseBlockProperty.WOOD).content(ContainerType.COMMON_REGULAR).shaped(
                            ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(-1.5,0,2,17.5,14,14)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-2.5,14,1,18.5,16,15))))
                    .directional().noFenceConnect().pushReaction(PushReaction.BLOCK).build());
    public static final Block WARPED_CABINET = registerBlock("warped_cabinet",
            ContainerDecorativeBlock.with(BaseBlockProperty.WOOD).content(ContainerType.COMMON_REGULAR).shaped(
                            ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(-4.5,0,3,20.5,14,13)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-8,14,1,24,16,15))))
                    .directional().noFenceConnect().pushReaction(PushReaction.BLOCK).build());
    public static final Block EBONY_CABINET = registerBlock("ebony_cabinet",
            ContainerDecorativeBlock.with(BaseBlockProperty.WOOD).content(ContainerType.COMMON_LARGE).shaped(
                            ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(-6,0,0,22,30,16)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-7,30,-1,23,32,17))))
                    .directional().noFenceConnect().pushReaction(PushReaction.BLOCK).build());

    // TABLE

    public static final Block LARGE_TEA_TABLE = registerBlock("large_tea_table",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(-12,0,-3,28,8,19))).directional().build());
    public static final Block CHESS_TABLE = registerBlock("chess_table",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.centeredSquare(32, 4)).directional().diagonallyPlaceable().build());
    public static final Block HIGH_TABLE_WITH_WHITE_TOP = registerBlock("high_table_with_white_top",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.centeredSquare(12, 32)).directional().build());
    public static final Block CENSER_TABLE = registerBlock("censer_table",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.or(
                            ReShapeFunction.diagonal(new RawVoxelShape(4,0,4,12,2,12)), ReShapeFunction.diagonal(new RawVoxelShape(2,2,2,14,28,14)), ReShapeFunction.diagonal(new RawVoxelShape(3,28,3,13,32,13))))
                    .directional().diagonallyPlaceable().build());
    public static final Block PORCELAIN_INLAID_TABLE = registerBlock("porcelain_inlaid_table",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(BlockShapes.S16_H16).directional().noFenceConnect().build());
    public static final Block SMALL_TABLE = registerBlock("small_table",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.diagonalSquare(11, 16)).directional().diagonallyPlaceable().build());
    public static final Block SMALL_EBONY_TABLE = registerBlock("small_ebony_table",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.centeredSquare(13, 16)).directional().build());
    public static final Block LARGE_TABLE = registerBlock("large_table",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.centeredSquare(48, 16)).directional().build());
    public static final Block LONG_TABLE = registerBlock("long_table",
            new LongTableBlock(BaseBlockProperty.WOOD));

    // CHAIR

    public static final Block EBONY_CHAIR = registerBlock("ebony_chair",
            SeatDecorativeBlock.with(BaseBlockProperty.WOOD).seatOffset(new Vec3(0.0f, -0.1f, 0.0f)).shaped(ReShapeFunction.diagonalSquare(12, 9)).directional().diagonallyPlaceable().build());
    public static final Block CHAIR_WITH_YELLOW_CUSHION = registerBlock("chair_with_yellow_cushion",
            SeatDecorativeBlock.with(BaseBlockProperty.WOOD).seatOffset(new Vec3(0.0f, -0.1f, 0.0f)).shaped(ReShapeFunction.centeredSquare(13, 8)).directional().build());
    public static final Block PAINTED_CHAIR = registerBlock("painted_chair",
            SeatDecorativeBlock.with(BaseBlockProperty.WOOD).seatOffset(new Vec3(0.0f, -0.1f, 0.0f)).shaped(ReShapeFunction.centeredSquare(18, 10)).directional().build());
    public static final Block WOODEN_STOOL = registerBlock("wooden_stool",
            SeatDecorativeBlock.with(BaseBlockProperty.WOOD).seatOffset(new Vec3(0.0f, -0.1f, 0.0f)).shaped(ReShapeFunction.centeredSquare(16, 12)).directional().build());
    public static final Block PORCELAIN_INLAID_GRAND_CHAIR = registerBlock("porcelain_inlaid_grand_chair",
            SeatDecorativeBlock.with(BaseBlockProperty.WOOD).seatOffset(new Vec3(0.0f, -0.1f, 0.0f)).shaped(ReShapeFunction.centeredSquare(16, 12)).directional().build());
    public static final Block YELLOW_CUSHION = registerBlock("yellow_cushion",
            SeatDecorativeBlock.with(BaseBlockProperty.SILK).seatOffset(new Vec3(0.0f, -0.5f, 0.0f)).shaped(ReShapeFunction.eightRotations(new RawVoxelShape(2,0,1.5,14,4,13.5))).directional().diagonallyPlaceable().noOcclusion().build());
    public static final Block OAK_BED = registerBlock("oak_bed",
            SeatDecorativeBlock.with(BaseBlockProperty.WOOD).seatOffset(new Vec3(0.0f, -0.1f, 0.0f)).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(-8,0,0,24,8,16))).directional().build());
    public static final Block EBONY_BED = registerBlock("ebony_bed",
            SeatDecorativeBlock.with(BaseBlockProperty.WOOD).seatOffset(new Vec3(0.0f, 0.1f, 0.0f)).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(-15,0,-1,31,13,17))).directional().build());

    // SCREEN

    public static final Block LARGE_LANDSCAPE_PAINTING_SCREEN = registerBlock("large_landscape_painting_screen",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(
                    ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,-16,6,32,-9,10)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-13,-16,3,-9,-9,13)), ReShapeFunction.cardinalRotations(new RawVoxelShape(25,-16,3,29,-9,13)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-12,-9,7,28,13,9)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-15,13,7,31,19,9)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-10,19,7,26,26,9))))
                    .placeOffset(Direction.UP).directional().build());
    public static final Block PAINTED_SCREEN = registerBlock("painted_screen",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(
                    ReShapeFunction.or(ReShapeFunction.cardinalRotations(new RawVoxelShape(-5.5,-16,0,-2.5,-12,16)), ReShapeFunction.cardinalRotations(new RawVoxelShape(18.5,-16,0,21.5,-12,16)),
                            ReShapeFunction.cardinalRotations(new RawVoxelShape(-6,-12,7,22,5,9)), ReShapeFunction.cardinalRotations(new RawVoxelShape(-3,5,7,19,32,9))))
                    .placeOffset(Direction.UP).directional().build());

    /**
     * DOOR AND WINDOW
     */

    // DOOR

    public static final Block CARVED_WOODEN_DOOR = registerBlock("carved_wooden_door",
            new OpeningBlock(DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,7,16,32,9))).directional().noOcclusion()));
    public static final Block SONG_WOODEN_DOOR = registerBlock("song_wooden_door",
            new OpeningBlock(DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,7,16,32,9))).directional().noOcclusion()));

    // WINDOW

    public static final Block SONG_WOODEN_WINDOW = registerBlock("song_wooden_window",
            new OpeningBlock(DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,7,16,32,9))).directional().noOcclusion()));
    public static final Block ROYAL_ROSEWOOD_WINDOW = registerBlock("royal_rosewood_window",
            new OpeningBlock(DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,7,16,16,9))).directional().noOcclusion()));
    public static final Block TALL_ROYAL_ROSEWOOD_WINDOW = registerBlock("tall_royal_rosewood_window",
            new OpeningBlock(DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,7,16,32,9))).directional().noOcclusion()));
    public static final Block GLAZED_TILE_GRID_WINDOW = registerBlock("glazed_tile_grid_window",
            new DecorativeBlock(DecorativeBlock.with(BaseBlockProperty.GLAZED).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,-16,-2,32,32,18))).directional().noOcclusion()));
    public static final Block STONE_FLOWER_WINDOW = registerBlock("stone_flower_window", new BaseBlock(BaseBlockProperty.IRON));

    // DOOR DECO

    public static final Block KNOCKER = registerBlock("knocker",
            new WallSideBlock(BaseBlockProperty.COPPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(4,4,14,12,13,16))));
    public static final Block ROSEWOOD_PANEL = registerBlock("rosewood_panel",
            new SixSideBlock(BaseBlockProperty.WOOD, 3));
    public static final Block ROSEWOOD_DOOR_PANEL = registerBlock("rosewood_door_panel",
            new SixSideBlock(BaseBlockProperty.WOOD, 3));
    public static final Block LARGE_ROSEWOOD_PANEL = registerBlock("large_rosewood_panel",
            new OrientableSixSideBlock(BaseBlockProperty.WOOD, 3));
    public static final Block LARGE_ROSEWOOD_PANEL_EDGE = registerBlock("large_rosewood_panel_edge",
            new OrientableSixSideBlock(BaseBlockProperty.WOOD, 3));
    public static final Block CARVED_WOODEN_DOOR_PANEL = registerBlock("carved_wooden_door_panel",
            new OpeningBlock(DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,7,16,16,9))).directional().noOcclusion()));
    public static final Block PLAQUE = registerBlock("plaque",
            new DecorativeBlock(DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,0,7,32,15,15))).directional().noOcclusion()));
    public static final Block PAPER_STRIP_SEAL = registerBlock("paper_strip_seal",
            new WallSideBlock(BaseBlockProperty.PAPER, ReShapeFunction.cardinalRotations(new RawVoxelShape(7.5,1.5,15,24.5,18.5,16))));

    // WINDOW DECO

    public static final Block LARGE_BLUE_CURTAIN = registerBlock("large_blue_curtain",
            new WallSideBlock(BaseBlockProperty.SILK, ReShapeFunction.cardinalRotations(new RawVoxelShape(-16,0,15,32,16,16))));
    public static final Block MEDIUM_BLUE_CURTAIN = registerBlock("medium_blue_curtain",
            new WallSideBlock(BaseBlockProperty.SILK, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,15,32,16,16))));
    public static final Block SMALL_BLUE_CURTAIN = registerBlock("small_blue_curtain",
            new WallSideBlock(BaseBlockProperty.SILK, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,0,15,16,16,16))));
    public static final Block RED_CURTAIN = registerBlock("red_curtain",
            new OrientableWallSideBlock(BaseBlockProperty.SILK, ReShapeFunction.sideOrientedShape(new RawVoxelShape(0,-16,14,32,16,15))));
    public static final Block RED_CURTAIN_CORNER = registerBlock("red_curtain_corner",
            new OrientableWallSideBlock(BaseBlockProperty.SILK, ReShapeFunction.sideOrientedShape(new RawVoxelShape(0,-16,14,32,16,15))));
    public static final Block BAMBOO_CURTAIN = registerBlock("bamboo_curtain",
            new DecorativeBlock(DecorativeBlock.with(BaseBlockProperty.BAMBOO_WOOD).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(0,2,7,16,16,9))).directional().noOcclusion()));

    /**
     * PLANTS
     */

    // LOTUS


    public static final Block SMALL_LOTUS_LEAF = registerBlock("small_lotus_leaf",
            new AquaticPlantBlock(DecorativeBlock.with(BaseBlockProperty.LILY).shaped(BlockShapes.S16_H1).directional().diagonallyPlaceable().noCollision().noOcclusion()));
    public static final Block SMALL_DARK_GREEN_LOTUS_LEAF = registerBlock("small_dark_green_lotus_leaf",
            new AquaticPlantBlock(DecorativeBlock.with(BaseBlockProperty.LILY).shaped(BlockShapes.S16_H1).directional().diagonallyPlaceable().noCollision().noOcclusion()));
    public static final Block MEDIUM_LOTUS_LEAF = registerBlock("medium_lotus_leaf",
            new AquaticPlantBlock(DecorativeBlock.with(BaseBlockProperty.LILY).shaped(ReShapeFunction.centeredSquare(24, 1)).directional().diagonallyPlaceable().noCollision().noOcclusion()));
    public static final Block LARGE_LOTUS_LEAF = registerBlock("large_lotus_leaf",
            new AquaticPlantBlock(DecorativeBlock.with(BaseBlockProperty.LILY).shaped(ReShapeFunction.centeredSquare(32, 1)).directional().diagonallyPlaceable().noCollision().noOcclusion()));
    public static final Block TILTED_LOTUS_LEAF = registerBlock("tilted_lotus_leaf",
            new AquaticPlantBlock(DecorativeBlock.with(BaseBlockProperty.LILY).shaped(ReShapeFunction.centeredSquare(32, 16)).directional().noCollision().noOcclusion()));
    public static final Block SMALL_LOTUS_LEAF_CLUSTER = registerBlock("small_lotus_leaf_cluster",
            new AquaticPlantBlock(DecorativeBlock.with(BaseBlockProperty.LILY).shaped(BlockShapes.S16_H1).directional().diagonallyPlaceable().noCollision().noOcclusion()));
    public static final Block MEDIUM_LOTUS_LEAF_CLUSTER = registerBlock("medium_lotus_leaf_cluster",
            new AquaticPlantBlock(DecorativeBlock.with(BaseBlockProperty.LILY).shaped(ReShapeFunction.centeredSquare(32, 1)).directional().diagonallyPlaceable().noCollision().noOcclusion()));
    public static final Block LOTUS_BUD = registerBlock("lotus_bud",
            new AquaticPlantBlock(DecorativeBlock.with(BaseBlockProperty.LILY).shaped(ReShapeFunction.centeredSquare(8, 30)).directional().noCollision().noOcclusion()));
    public static final Block MEDIUM_LOTUS = registerBlock("medium_lotus",
            new AquaticPlantBlock(DecorativeBlock.with(BaseBlockProperty.LILY).shaped(ReShapeFunction.centeredSquare(42, 30)).directional().noCollision().noOcclusion()));

    // IVY

    public static final Block SMALL_RED_IVY = registerBlock("small_red_ivy",
            new WallSideBlock(BaseBlockProperty.PLANT, ReShapeFunction.sideShape(1)));
    public static final Block MEDIUM_RED_IVY = registerBlock("medium_red_ivy",
            new WallSideBlock(BaseBlockProperty.PLANT, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,15,16,16,16))));
    public static final Block LARGE_RED_IVY = registerBlock("large_red_ivy",
            new WallSideBlock(BaseBlockProperty.PLANT, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,15,16,32,16))));
    public static final Block SMALL_YELLOW_IVY = registerBlock("small_yellow_ivy",
            new WallSideBlock(BaseBlockProperty.PLANT, ReShapeFunction.sideShape(1)));
    public static final Block MEDIUM_YELLOW_IVY = registerBlock("medium_yellow_ivy",
            new WallSideBlock(BaseBlockProperty.PLANT, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,15,16,16,16))));
    public static final Block LARGE_YELLOW_IVY = registerBlock("large_yellow_ivy",
            new WallSideBlock(BaseBlockProperty.PLANT, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,15,16,32,16))));
    public static final Block SMALL_GREEN_IVY = registerBlock("small_green_ivy",
            new WallSideBlock(BaseBlockProperty.PLANT, ReShapeFunction.sideShape(1)));
    public static final Block MEDIUM_GREEN_IVY = registerBlock("medium_green_ivy",
            new WallSideBlock(BaseBlockProperty.PLANT, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,15,16,16,16))));
    public static final Block LARGE_GREEN_IVY = registerBlock("large_green_ivy",
            new WallSideBlock(BaseBlockProperty.PLANT, ReShapeFunction.cardinalRotations(new RawVoxelShape(0,-16,15,16,32,16))));

    // LEAVES

    public static final Block SMALL_LEAF_PILE = registerBlock("small_leaf_pile",
            DecorativeBlock.with(BaseBlockProperty.PLANT).shaped(BlockShapes.S16_H1).directional().noCollision().noOcclusion().build());
    public static final Block MEDIUM_LEAF_PILE = registerBlock("medium_leaf_pile",
            DecorativeBlock.with(BaseBlockProperty.PLANT).shaped(BlockShapes.S16_H1).directional().noCollision().noOcclusion().build());
    public static final Block LARGE_LEAF_PILE = registerBlock("large_leaf_pile",
            DecorativeBlock.with(BaseBlockProperty.PLANT).shaped(BlockShapes.S16_H1).directional().noCollision().noOcclusion().build());

    // BONSAI

    public static final Block SMALL_GREETING_PINE_BONSAI = registerBlock("small_greeting_pine_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(3,0,4,13,3,12))).directional().build());
    public static final Block MEDIUM_GREETING_PINE_BONSAI = registerBlock("medium_greeting_pine_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.centeredSquare(10, 4)).directional().noOcclusion().build());
    public static final Block LARGE_GREETING_PINE_BONSAI = registerBlock("large_greeting_pine_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(4,-16,5,12,11,13))).directional().placeOffset(Direction.UP).noOcclusion().build());
    public static final Block SMALL_WHITE_PORCELAIN_VASE_BONSAI = registerBlock("small_white_porcelain_vase_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.diagonalSquare(6, 5)).directional().diagonallyPlaceable().build());
    public static final Block MEDIUM_WHITE_PORCELAIN_VASE_BONSAI = registerBlock("medium_white_porcelain_vase_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.diagonalSquare(9f, 6.5f)).directional().diagonallyPlaceable().build());
    public static final Block LARGE_WHITE_PORCELAIN_VASE_BONSAI = registerBlock("large_white_porcelain_vase_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.or(ReShapeFunction.diagonalSquare(9f, 6.5f), ReShapeFunction.diagonalSquare(5, 14.5f))).directional().diagonallyPlaceable().noOcclusion().build());
    public static final Block SMALL_GREEN_PORCELAIN_VASE_BONSAI = registerBlock("small_green_porcelain_vase_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.centeredSquare(10, 9)).directional().build());
    public static final Block GREEN_PORCELAIN_VASE_BONSAI = registerBlock("green_porcelain_vase_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.or(ReShapeFunction.diagonalSquare(7, 6), ReShapeFunction.diagonalSquare(5, 8), ReShapeFunction.diagonalSquare(3.5f, 12.5f))).directional().diagonallyPlaceable().build());
    public static final Block RED_CORAL_BONSAI = registerBlock("red_coral_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.centeredSquare(10, 4)).directional().build());
    public static final Block RED_PLUM_BONSAI = registerBlock("red_plum_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(3,0,4,13,2,12))).directional().build());
    public static final Block BAMBOO_BONSAI = registerBlock("bamboo_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.cardinalRotations(new RawVoxelShape(2,0,4,14,3,12))).directional().noOcclusion().build()); //todo fix z-fighting
    public static final Block TALL_BLUE_VASE_BONSAI = registerBlock("tall_blue_vase_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(
                            ReShapeFunction.or(ReShapeFunction.diagonal(new RawVoxelShape(4,-16,4,12,-13,12)), ReShapeFunction.diagonal(new RawVoxelShape(5.5,-16,5.5,10.5,5,10.5)),
                                    ReShapeFunction.diagonal(new RawVoxelShape(4.5,-16,4.5,11.5,3,11.5))))
                    .directional().diagonallyPlaceable().placeOffset(Direction.UP).build());
    public static final Block TALL_BLUE_AND_WHITE_PORCELAIN_BONSAI = registerBlock("tall_blue_and_white_porcelain_bonsai",
            DecorativeBlock.with(BaseBlockProperty.PORCELAIN).shaped(ReShapeFunction.or(ReShapeFunction.diagonalSquare(6, 6), ReShapeFunction.diagonalSquare(4, 7.5f), ReShapeFunction.diagonalSquare(3, 12))).directional().diagonallyPlaceable().noOcclusion().build());
    public static final Block MEDIUM_BONSAI = registerBlock("medium_bonsai",
            DecorativeBlock.with(BaseBlockProperty.TERRACOTTA).shaped(ReShapeFunction.or(ReShapeFunction.diagonal(new RawVoxelShape(5,0,5,11,6,11)), ReShapeFunction.diagonal(new RawVoxelShape(4,6,4,12,12,12)))).directional().diagonallyPlaceable().noOcclusion().build());
    public static final Block LARGE_BONSAI = registerBlock("large_bonsai",
            DecorativeBlock.with(BaseBlockProperty.TERRACOTTA).shaped(ReShapeFunction.or(ReShapeFunction.diagonal(new RawVoxelShape(4,0,4,12,8,12)), ReShapeFunction.diagonal(new RawVoxelShape(3,8,3,13,15,13)))).directional().diagonallyPlaceable().noOcclusion().build());

    /**
     * LAMPS
     */

    //LANTERN

    public static final Block OCTAGONAL_PALACE_LANTERN = registerBlock("octagonal_palace_lantern",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(
                            ReShapeFunction.or(ReShapeFunction.simpleShape(new RawVoxelShape(2.5,4.5,2.5,13.5,18,13.5)), ReShapeFunction.simpleShape(new RawVoxelShape(0,18,0,16,25,16))))
                    .luminous().noOcclusion().placeOffset(Direction.DOWN).build());
    public static final Block SQUARE_PALACE_LANTERN = registerBlock("square_palace_lantern",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(
                    ReShapeFunction.or(ReShapeFunction.diagonal(new RawVoxelShape(2,-8.5,2,14,6,14)), ReShapeFunction.diagonal(new RawVoxelShape(0,6,0,16,11.5,16)))).diagonallyPlaceable().luminous().noOcclusion().build());
    public static final Block SMALL_RED_LANTERN = registerBlock("small_red_lantern",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.diagonalSquare(10, 10)).diagonallyPlaceable().luminous().build());
    public static final Block WHITE_SKY_LANTERN = registerBlock("white_sky_lantern",
            DecorativeBlock.with(BaseBlockProperty.BAMBOO).shaped(ReShapeFunction.centeredSquare(16, 24)).luminous().build());
    public static final Block RED_SKY_LANTERN = registerBlock("red_sky_lantern",
            DecorativeBlock.with(BaseBlockProperty.BAMBOO).shaped(ReShapeFunction.centeredSquare(16, 24)).luminous().build());
    public static final Block YELLOW_SKY_LANTERN = registerBlock("yellow_sky_lantern",
            DecorativeBlock.with(BaseBlockProperty.BAMBOO).shaped(ReShapeFunction.centeredSquare(16, 24)).luminous().build());

    // LAMP

    public static final Block STANDING_LAMP = registerBlock("standing_lamp",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(
                            ReShapeFunction.or(ReShapeFunction.diagonal(new RawVoxelShape(5,0,5,11,2,11)), ReShapeFunction.diagonal(new RawVoxelShape(7,2,7,9,21,9)), ReShapeFunction.diagonal(new RawVoxelShape(4,21,4,12,32,12))))
                    .diagonallyPlaceable().luminous().noOcclusion().build());
    public static final Block SMALL_STANDING_LAMP = registerBlock("small_standing_lamp",
            DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.or(ReShapeFunction.diagonal(new RawVoxelShape(7,0,7,9,5,9)), ReShapeFunction.diagonal(new RawVoxelShape(6,5,6,10,12,10)))).diagonallyPlaceable().luminous().build());
    public static final Block STONE_LAMP = registerBlock("stone_lamp",
            DecorativeBlock.with(BaseBlockProperty.STONE).shaped(
                            ReShapeFunction.or(ReShapeFunction.simpleShape(new RawVoxelShape(4,0,4,12,7,12)), ReShapeFunction.simpleShape(new RawVoxelShape(2,7,2,14,9,14)),
                                    ReShapeFunction.simpleShape(new RawVoxelShape(4,9,4,12,16,12)), ReShapeFunction.simpleShape(new RawVoxelShape(0,16,0,16,21.5,16))))
                    .luminous().build());


    // STREETLIGHT //todo rewrite collision logic
    public static final Block RED_LANTERN_STREETLIGHT = registerBlock("red_lantern_streetlight", new HangingLantern(HangingLantern.HangingLanternType.POLE));
    public static final Block HANGING_RED_LANTERN_STREETLIGHT = registerBlock("hanging_red_lantern_streetlight", new HangingLantern(HangingLantern.HangingLanternType.HANGING));
    public static final Block STREETLIGHT_POLE = registerBlock("streetlight_pole", DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(HangingLantern.POLE_ONLY).directional().noOcclusion().build());


    // CANDLESTICK

    public static final Block RED_CANDLE = registerBlock("red_candle",
            new CandleStick(DecorativeBlock.with(BaseBlockProperty.WOOD).shaped(ReShapeFunction.or(ReShapeFunction.diagonalSquare(3.5f, 4), ReShapeFunction.diagonalSquare(1.5f, 16)))
                    .directional().diagonallyPlaceable().luminous(), new Vec3(0.5,1.1,0.5)));
    public static final Block TRICOLOR_CANDLESTICK = registerBlock("tricolor_candlestick",
            new CandleStick(DecorativeBlock.with(BaseBlockProperty.IRON).shaped(
                            ReShapeFunction.or(ReShapeFunction.diagonal(new RawVoxelShape(5,0,5,11,2,11)), ReShapeFunction.diagonal(new RawVoxelShape(4,2,4,12,4,12)), ReShapeFunction.diagonal(new RawVoxelShape(7,0,7,9,18,9)),
                                    ReShapeFunction.diagonal(new RawVoxelShape(5.5,9,5.5,10.5,10,10.5)), ReShapeFunction.diagonal(new RawVoxelShape(6.7,9,6.7,9.3,13,9.3))))
                    .directional().diagonallyPlaceable().luminous(), new Vec3(0.5,1.25,0.5)));
    public static final Block JADE_CANDLESTICK = registerBlock("jade_candlestick",
            new CandleStick(DecorativeBlock.with(BaseBlockProperty.JADE).shaped(
                            ReShapeFunction.or(ReShapeFunction.diagonal(new RawVoxelShape(6,0,6,10,2,10)), ReShapeFunction.diagonal(new RawVoxelShape(6.5,2,6.5,9.5,4,9.5)), ReShapeFunction.diagonal(new RawVoxelShape(7.5,0,7.5,8.5,20,8.5)),
                                    ReShapeFunction.diagonal(new RawVoxelShape(7,5.5,7,9,7.5,9)), ReShapeFunction.diagonal(new RawVoxelShape(5.5,9,5.5,10.5,10.5,10.5)), ReShapeFunction.diagonal(new RawVoxelShape(7,15,7,9,16,9))))
                    .directional().diagonallyPlaceable().luminous(), new Vec3(0.5,1.3,0.5)));

    /**
     * MATERIALS
     */

    public static final Block JADE_ORE = registerBlock("jade_ore", new BaseOreBlock(2, 5));
    public static final Block DEEPSLATE_JADE_ORE = registerBlock("deepslate_jade_ore", new BaseOreBlock(2, 5));
    public static final Block MAGNESITE_ORE = registerBlock("magnesite_ore", new BaseOreBlock(2, 5));
    public static final Block DEEPSLATE_MAGNESITE_ORE = registerBlock("deepslate_magnesite_ore", new BaseOreBlock(2, 5));
    public static final Block HEMATITE_ORE = registerBlock("hematite_ore", new BaseOreBlock(1, 3));
    public static final Block DEEPSLATE_HEMATITE_ORE = registerBlock("deepslate_hematite_ore", new BaseOreBlock(1, 3));
    public static final Block NETHER_COBALT_ORE = registerBlock("nether_cobalt_ore", new BaseOreBlock(2, 5));
    public static final Block JADE_BLOCK = registerBlock("jade_block", new BaseBlock());
    public static final Block BRONZE_BLOCK = registerBlock("bronze_block", new BaseBlock());

    /**
     * TOOLS
     */

    public static final Block WOODWORKING_WORKBENCH = registerBlock("woodworking_workbench", new WoodworkingWorkBench());
    public static final Block BRICK_KILN = registerBlock("brick_kiln", new BrickKiln());
    public static final Block CHISEL_TABLE = registerBlock("chisel_table", new ChiselTableMedium());


    public static Block registerBlock(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, name), block);
    }

    public static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, name), new BlockItem(block, new Item.Properties())
        );
    }

    public static void registerModBlocks() {
        Ultramarine.info("Registering Mod Blocks for " + Ultramarine.MOD_ID);
    }
}
