package com.voxelutopia.ultramarine.client.handler;

import com.voxelutopia.ultramarine.client.render.SeatEntityRenderer;
import com.voxelutopia.ultramarine.client.screen.BrickKilnScreen;
import com.voxelutopia.ultramarine.client.screen.ChiselTableScreen;
import com.voxelutopia.ultramarine.client.screen.ContainerDecorativeBlockScreen;
import com.voxelutopia.ultramarine.client.screen.WoodworkingWorkbenchScreen;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import com.voxelutopia.ultramarine.init.registry.ModEntityTypes;
import com.voxelutopia.ultramarine.init.registry.ModMenuTypes;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.WanderingTraderRenderer;

public class ModClientEventHandler {
    public static void onClientSetup() {
        setRenderLayers();
        registerMenuScreens();
        registerEntityRenderers();
    }

    private static void setRenderLayers() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ModBlocks.ABACUS,
                ModBlocks.BAMBOO_BONSAI,
                ModBlocks.BAMBOO_CURTAIN,
                ModBlocks.BLACK_IRON_FLOWERPOT,
                ModBlocks.BLUE_FANGXIN_PATTERN,
                ModBlocks.BLUE_FANGXIN_PATTERN_EDGE,
                ModBlocks.BLUE_PORCELAIN_FLOWERPOT,
                ModBlocks.BOTTLE_GOURD,
                ModBlocks.BRICK_KILN,
                ModBlocks.BRONZE_CENSER,
                ModBlocks.BRUSH_TOOLS,
                ModBlocks.CARVED_WOODEN_DOOR,
                ModBlocks.CARVED_ZHAOTOU_PATTERN,
                ModBlocks.CENSER_TABLE,
                ModBlocks.CHAIR_WITH_YELLOW_CUSHION,
                ModBlocks.CHESS_TABLE,
                ModBlocks.CHISEL_TABLE,
                ModBlocks.CIRCULAR_YELLOW_CARVED_PATTERN,
                ModBlocks.DAMAGED_LANDSCAPE_PAINTING,
                ModBlocks.EBONY_CABINET,
                ModBlocks.FLAME_ARCH_WALL_PATTERN,
                ModBlocks.GLAZED_TILE_GRID_WINDOW,
                ModBlocks.GOLDEN_DRAGON_FANGXIN_PATTERN,
                ModBlocks.GREEN_FANGXIN_PATTERN,
                ModBlocks.GREEN_FANGXIN_PATTERN_EDGE,
                ModBlocks.GREEN_PORCELAIN_VASE_BONSAI,
                ModBlocks.GUNNY_SACK,
                ModBlocks.HANGING_PAINTING_FAN,
                ModBlocks.HIGH_TABLE_WITH_WHITE_TOP,
                ModBlocks.IMPERIAL_JADE_SEAL,
                ModBlocks.JADE_PENDANT,
                ModBlocks.KNOCKER,
                ModBlocks.LARGE_BLUE_CURTAIN,
                ModBlocks.LARGE_BONSAI,
                ModBlocks.LARGE_GRAY_SU_STYLE_CAIHUA,
                ModBlocks.LARGE_GREEN_IVY,
                ModBlocks.LARGE_GREEN_SU_STYLE_CAIHUA,
                ModBlocks.LARGE_GREETING_PINE_BONSAI,
                ModBlocks.LARGE_LANDSCAPE_PAINTING_SCREEN,
                ModBlocks.LARGE_LEAF_PILE,
                ModBlocks.LARGE_LOTUS_LEAF,
                ModBlocks.LARGE_RED_IVY,
                ModBlocks.LARGE_TEA_TABLE,
                ModBlocks.LARGE_WHITE_PORCELAIN_VASE_BONSAI,
                ModBlocks.LARGE_WHITE_SU_STYLE_CAIHUA,
                ModBlocks.LARGE_WOODEN_GUALUO,
                ModBlocks.LARGE_WOODEN_GUALUO_EDGE,
                ModBlocks.LARGE_WOODEN_QUETI_EDGE,
                ModBlocks.LARGE_YELLOW_CARVED_PATTERN,
                ModBlocks.LARGE_YELLOW_IVY,
                ModBlocks.LIGHT_BLUE_SU_STYLE_CAIHUA,
                ModBlocks.LIGHT_YELLOW_SU_STYLE_CAIHUA,
                ModBlocks.LONG_GILDED_DARK_OAK_QUETI,
                ModBlocks.LONG_GILDED_DARK_OAK_QUETI_EDGE,
                ModBlocks.LONG_HANGING_PAINTING,
                ModBlocks.LONG_YELLOW_CARVED_ZHAOTOU_PATTERN,
                ModBlocks.LOTUS_BUD,
                ModBlocks.MEDIUM_BLUE_CURTAIN,
                ModBlocks.MEDIUM_BONSAI,
                ModBlocks.MEDIUM_GREEN_IVY,
                ModBlocks.MEDIUM_GREETING_PINE_BONSAI,
                ModBlocks.MEDIUM_LEAF_PILE,
                ModBlocks.MEDIUM_LOTUS,
                ModBlocks.MEDIUM_LOTUS_LEAF,
                ModBlocks.MEDIUM_LOTUS_LEAF_CLUSTER,
                ModBlocks.MEDIUM_RED_IVY,
                ModBlocks.MEDIUM_SU_STYLE_CAIHUA,
                ModBlocks.MEDIUM_WHITE_PORCELAIN_VASE_BONSAI,
                ModBlocks.MEDIUM_YELLOW_CARVED_PATTERN,
                ModBlocks.MEDIUM_YELLOW_IVY,
                ModBlocks.MEMORIAL_TABLET,
                ModBlocks.OAK_BED,
                ModBlocks.OCTAGONAL_PALACE_LANTERN,
                ModBlocks.PAINTED_CHAIR,
                ModBlocks.PAINTED_SCREEN,
                ModBlocks.PAPER,
                ModBlocks.PLATED_FISH,
                ModBlocks.PLATED_MUNG_BEAN_CAKES,
                ModBlocks.PORCELAIN_INLAID_GRAND_CHAIR,
                ModBlocks.PORCELAIN_INLAID_TABLE,
                ModBlocks.PORCELAIN_TEAPOT,
                ModBlocks.RED_CANDLE,
                ModBlocks.RED_CORAL_BONSAI,
                ModBlocks.RED_CURTAIN,
                ModBlocks.RED_CURTAIN_CORNER,
                ModBlocks.RED_LANTERN_STREETLIGHT,
                ModBlocks.RED_PLUM_BONSAI,
                ModBlocks.RED_WOODEN_RAILING,
                ModBlocks.RED_WOODEN_RAILING_EDGE,
                ModBlocks.ROYAL_CENSER,
                ModBlocks.SHORT_GLAZED_QUETI,
                ModBlocks.SMALL_BLUE_CURTAIN,
                ModBlocks.SMALL_DARK_GREEN_LOTUS_LEAF,
                ModBlocks.SMALL_EBONY_TABLE,
                ModBlocks.SMALL_GREEN_GLAZED_GUARDIAN_LION,
                ModBlocks.SMALL_GREEN_IVY,
                ModBlocks.SMALL_GREEN_PORCELAIN_VASE_BONSAI,
                ModBlocks.SMALL_GREETING_PINE_BONSAI,
                ModBlocks.SMALL_JADE_GUARDIAN_LION,
                ModBlocks.SMALL_LEAF_PILE,
                ModBlocks.SMALL_LOTUS_LEAF,
                ModBlocks.SMALL_LOTUS_LEAF_CLUSTER,
                ModBlocks.SMALL_RED_IVY,
                ModBlocks.SMALL_STANDING_LAMP,
                ModBlocks.SMALL_STONE_GUARDIAN_LION,
                ModBlocks.SMALL_TABLE,
                ModBlocks.SMALL_WHITE_PORCELAIN_VASE_BONSAI,
                ModBlocks.SMALL_WOODEN_GUARDIAN_LION,
                ModBlocks.SMALL_YELLOW_GLAZED_GUARDIAN_LION,
                ModBlocks.SMALL_YELLOW_IVY,
                ModBlocks.SONG_WOODEN_DOOR,
                ModBlocks.SONG_WOODEN_WINDOW,
                ModBlocks.SQUARE_PALACE_LANTERN,
                ModBlocks.STANDING_LAMP,
                ModBlocks.STONE_LAMP,
                ModBlocks.TALL_BLUE_AND_WHITE_PORCELAIN_BONSAI,
                ModBlocks.TALL_BLUE_VASE_BONSAI,
                ModBlocks.TALL_WOODEN_QUETI_EDGE,
                ModBlocks.TEAHOUSE_FLAG,
                ModBlocks.THICK_CARVED_QUETI,
                ModBlocks.TILTED_LOTUS_LEAF,
                ModBlocks.WARPED_CABINET,
                ModBlocks.WINE_POT,
                ModBlocks.WOODEN_GUALUO,
                ModBlocks.WOODEN_QUETI,
                ModBlocks.WOODEN_QUETI_EDGE,
                ModBlocks.WOODEN_RAILING,
                ModBlocks.WOODEN_RAILING_VARIANT,
                ModBlocks.WOODWORKING_WORKBENCH,
                ModBlocks.YELLOW_CARVED_FANGXIN_EDGE_PATTERN,
                ModBlocks.YELLOW_CARVED_FANGXIN_PATTERN,
                ModBlocks.YELLOW_CARVED_PATTERN,
                ModBlocks.YELLOW_CARVED_ZHAOTOU_PATTERN,
                ModBlocks.YELLOW_SU_STYLE_CAIHUA
        );

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(),
                ModBlocks.LONG_TABLE,
                ModBlocks.RED_SKY_LANTERN,
                ModBlocks.WHITE_SKY_LANTERN,
                ModBlocks.YELLOW_SKY_LANTERN,
                ModBlocks.ICICLE,
                ModBlocks.LARGE_ICICLE
        );
    }

    private static void registerMenuScreens() {
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X1, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X3, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_GENERIC_9X6, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_FOOD_9X3, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.CONTAINER_DECORATIVE_BLOCK_MENU_FOOD_9X6, ContainerDecorativeBlockScreen::new);
        MenuScreens.register(ModMenuTypes.BRICK_KILN, BrickKilnScreen::new);
        MenuScreens.register(ModMenuTypes.CHISEL_TABLE, ChiselTableScreen::new);
        MenuScreens.register(ModMenuTypes.WOODWORKING_WORKBENCH, WoodworkingWorkbenchScreen::new);
    }

    private static void registerEntityRenderers() {
        EntityRendererRegistry.register(ModEntityTypes.SEAT, SeatEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.TRAVELLING_MERCHANT, WanderingTraderRenderer::new);
    }
}
