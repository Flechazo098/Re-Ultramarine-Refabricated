import re

# 第一个代码块（逐行 putBlock + 最后一行 putBlocks）
code1 = """
     BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ABACUS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BRUSH_TOOLS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BOTTLE_GOURD, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLACK_IRON_FLOWERPOT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUE_PORCELAIN_FLOWERPOT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEMORIAL_TABLET, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.KNOCKER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PORCELAIN_TEAPOT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BRONZE_CENSER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ROYAL_CENSER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.JADE_PENDANT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.IMPERIAL_JADE_SEAL, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TEAHOUSE_FLAG, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PAPER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LONG_HANGING_PAINTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DAMAGED_LANDSCAPE_PAINTING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HANGING_PAINTING_FAN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_WOODEN_GUARDIAN_LION, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_STONE_GUARDIAN_LION, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_JADE_GUARDIAN_LION, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_YELLOW_GLAZED_GUARDIAN_LION, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_GREEN_GLAZED_GUARDIAN_LION, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GUNNY_SACK, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PLATED_MUNG_BEAN_CAKES, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PLATED_FISH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WINE_POT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OCTAGONAL_PALACE_LANTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SQUARE_PALACE_LANTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STANDING_LAMP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_STANDING_LAMP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WHITE_SKY_LANTERN, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_SKY_LANTERN, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.YELLOW_SKY_LANTERN, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.STONE_LAMP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_WOODEN_RAILING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_WOODEN_RAILING_EDGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOODEN_RAILING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOODEN_RAILING_VARIANT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.YELLOW_CARVED_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CIRCULAR_YELLOW_CARVED_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.YELLOW_CARVED_FANGXIN_EDGE_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.YELLOW_CARVED_FANGXIN_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_YELLOW_CARVED_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_YELLOW_CARVED_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.YELLOW_CARVED_ZHAOTOU_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LONG_YELLOW_CARVED_ZHAOTOU_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FLAME_ARCH_WALL_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_DRAGON_FANGXIN_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUE_FANGXIN_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUE_FANGXIN_PATTERN_EDGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LIGHT_BLUE_SU_STYLE_CAIHUA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.YELLOW_SU_STYLE_CAIHUA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LIGHT_YELLOW_SU_STYLE_CAIHUA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_SU_STYLE_CAIHUA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_WHITE_SU_STYLE_CAIHUA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_GREEN_SU_STYLE_CAIHUA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_GRAY_SU_STYLE_CAIHUA, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREEN_FANGXIN_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREEN_FANGXIN_PATTERN_EDGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CARVED_ZHAOTOU_PATTERN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LONG_GILDED_DARK_OAK_QUETI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LONG_GILDED_DARK_OAK_QUETI_EDGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOODEN_QUETI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TALL_WOODEN_QUETI_EDGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOODEN_QUETI_EDGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SHORT_GLAZED_QUETI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_WOODEN_QUETI_EDGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOODEN_GUALUO, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_WOODEN_GUALUO, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_WOODEN_GUALUO_EDGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.THICK_CARVED_QUETI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_BLUE_CURTAIN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_BLUE_CURTAIN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_BLUE_CURTAIN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_CURTAIN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_CURTAIN_CORNER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GLAZED_TILE_GRID_WINDOW, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CARVED_WOODEN_DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SONG_WOODEN_DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SONG_WOODEN_WINDOW, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BAMBOO_CURTAIN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_CANDLE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WARPED_CABINET, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.EBONY_CABINET, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_TEA_TABLE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_LANDSCAPE_PAINTING_SCREEN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHESS_TABLE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.HIGH_TABLE_WITH_WHITE_TOP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CENSER_TABLE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PORCELAIN_INLAID_GRAND_CHAIR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PORCELAIN_INLAID_TABLE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHAIR_WITH_YELLOW_CUSHION, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PAINTED_CHAIR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_TABLE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_EBONY_TABLE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.OAK_BED, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PAINTED_SCREEN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOTUS_BUD, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_LOTUS, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TILTED_LOTUS_LEAF, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_LOTUS_LEAF, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_LOTUS_LEAF_CLUSTER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_LOTUS_LEAF_CLUSTER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_LOTUS_LEAF, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_DARK_GREEN_LOTUS_LEAF, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_LOTUS_LEAF, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_RED_IVY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_RED_IVY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_RED_IVY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_YELLOW_IVY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_YELLOW_IVY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_YELLOW_IVY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_GREEN_IVY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_GREEN_IVY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_GREEN_IVY, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_LEAF_PILE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_LEAF_PILE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_LEAF_PILE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_CORAL_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TALL_BLUE_VASE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BAMBOO_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_PLUM_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_GREETING_PINE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TALL_BLUE_AND_WHITE_PORCELAIN_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GREEN_PORCELAIN_VASE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_GREEN_PORCELAIN_VASE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_WHITE_PORCELAIN_VASE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_WHITE_PORCELAIN_VASE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LARGE_GREETING_PINE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_WHITE_PORCELAIN_VASE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MEDIUM_GREETING_PINE_BONSAI, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.WOODWORKING_WORKBENCH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BRICK_KILN, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHISEL_TABLE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.RED_LANTERN_STREETLIGHT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LONG_TABLE, RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.translucent(), ModBlocks.ICICLE, ModBlocks.LARGE_ICICLE);
"""

# 第二个代码块（两个 putBlocks）
code2 = """
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
                ModBlocks.LARGE_WHITE_PORCELAIN_VASE_BONSAI,
                ModBlocks.LARGE_WHITE_SU_STYLE_CAIHUA,
                ModBlocks.LARGE_WOODEN_GUALUO,
                ModBlocks.LARGE_WOODEN_GUALUO_EDGE,
                ModBlocks.LARGE_WOODEN_QUETI_EDGE,
                ModBlocks.LARGE_YELLOW_CARVED_PATTERN,
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
"""

def extract_modblocks(code):
    # 匹配 ModBlocks.XXX
    pattern = r'ModBlocks\.([A-Z_][A-Z0-9_]*)'
    matches = re.findall(pattern, code)
    return set(matches)

set1 = extract_modblocks(code1)
set2 = extract_modblocks(code2)

missing_in_code2 = set1 - set2
missing_in_code1 = set2 - set1

print("=== 比对结果 ===")
print(f"code1 中共有 {len(set1)} 个字段")
print(f"code2 中共有 {len(set2)} 个字段")

if missing_in_code2:
    print("\n❌ code2 缺少以下字段（存在于 code1 但不在 code2）：")
    for item in sorted(missing_in_code2):
        print(f"  - {item}")
else:
    print("\n✅ code2 包含了 code1 中的所有字段")

if missing_in_code1:
    print("\n⚠️ code1 缺少以下字段（存在于 code2 但不在 code1）：")
    for item in sorted(missing_in_code1):
        print(f"  - {item}")
else:
    print("\n✅ code1 包含了 code2 中的所有字段")