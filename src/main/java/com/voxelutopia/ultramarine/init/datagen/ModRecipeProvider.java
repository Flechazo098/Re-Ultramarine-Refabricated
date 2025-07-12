//package org.voxelutopia.ultramarine.init.datagen;
//
//import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
//import net.minecraft.advancements.Criterion;
//import net.minecraft.advancements.critereon.InventoryChangeTrigger;
//import net.minecraft.core.HolderLookup;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.data.recipes.*;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.tags.ItemTags;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.level.ItemLike;
//import org.jetbrains.annotations.NotNull;
//import org.voxelutopia.ultramarine.UltramarineDataGenerators;
//import org.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;
//import org.voxelutopia.ultramarine.init.data.ModItemTags;
//import org.voxelutopia.ultramarine.init.registry.ModItems;
//import org.voxelutopia.ultramarine.util.helper.RegistryHelper;
//
//import java.util.Objects;
//import java.util.concurrent.CompletableFuture;
//
//@SuppressWarnings("unused")
//public class ModRecipeProvider extends RecipeProvider {
//
//    private final CompletableFuture<HolderLookup.Provider> registries;
//
//    public ModRecipeProvider(FabricDataOutput pGenerator, CompletableFuture<HolderLookup.Provider> registries) {
//        super(pGenerator, registries);
//        this.registries = registries;
//    }
//
//    private static void generateLampRecipes(@NotNull RecipeOutput recipeOutput) {
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.WHITE_CANDLE, 1)
//                .define('S', Items.STRING)
//                .define('G', ModItems.GREASE)
//                .pattern("S")
//                .pattern("G")
//                .unlockedBy(itemUnlockName(ModItems.GREASE), has(ModItems.GREASE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.OCTAGONAL_PALACE_LANTERN, 1)
//                .define('F', ModItems.WOODEN_FRAME)
//                .define('P', ModItemTags.POLISHED_PLANKS)
//                .define('C', ItemTags.CANDLES)
//                .pattern("FPF")
//                .pattern("PCP")
//                .pattern("FPF")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SQUARE_PALACE_LANTERN, 1)
//                .define('F', ModItems.WOODEN_FRAME)
//                .define('C', ItemTags.CANDLES)
//                .pattern(" F ")
//                .pattern("FCF")
//                .pattern(" F ")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STANDING_LAMP, 1)
//                .define('X', ModItems.XUAN_PAPER)
//                .define('C', ItemTags.CANDLES)
//                .define('W', ItemTags.PLANKS)
//                .define('F', ModItems.WOODEN_FRAME)
//                .define('P', ModItems.WOODEN_PARTS)
//                .pattern("XCX")
//                .pattern("PFP")
//                .pattern(" W ")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMALL_STANDING_LAMP, 1)
//                .define('X', ModItems.XUAN_PAPER)
//                .define('C', ItemTags.CANDLES)
//                .define('W', Items.STICK)
//                .define('F', ModItems.WOODEN_FRAME)
//                .define('P', ModItems.WOODEN_PARTS)
//                .pattern("XCX")
//                .pattern("PFP")
//                .pattern(" W ")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WHITE_SKY_LANTERN, 1)
//                .define('X', ModItems.XUAN_PAPER)
//                .define('C', ItemTags.CANDLES)
//                .define('D', Items.WHITE_DYE)
//                .define('F', ModItems.WOODEN_FRAME)
//                .pattern("XDX")
//                .pattern("XCX")
//                .pattern(" F ")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.YELLOW_SKY_LANTERN, 1)
//                .define('X', ModItems.XUAN_PAPER)
//                .define('C', ItemTags.CANDLES)
//                .define('D', Items.YELLOW_DYE)
//                .define('F', ModItems.WOODEN_FRAME)
//                .pattern("XDX")
//                .pattern("XCX")
//                .pattern(" F ")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RED_SKY_LANTERN, 1)
//                .define('X', ModItems.XUAN_PAPER)
//                .define('C', ItemTags.CANDLES)
//                .define('D', Items.RED_DYE)
//                .define('F', ModItems.WOODEN_FRAME)
//                .pattern("XDX")
//                .pattern("XCX")
//                .pattern(" F ")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SMALL_RED_LANTERN, 1)
//                .define('S', Items.STICK)
//                .define('C', ItemTags.CANDLES)
//                .define('D', Items.RED_DYE)
//                .define('P', Items.PAPER)
//                .pattern(" S ")
//                .pattern("PCP")
//                .pattern("DPD")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STONE_LAMP, 1)
//                .define('S', Items.STONE)
//                .define('C', ItemTags.CANDLES)
//                .define('W', Items.COBBLESTONE_WALL)
//                .define('T', Items.STONE_SLAB)
//                .pattern(" T ")
//                .pattern("WCW")
//                .pattern(" S ")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RED_CANDLE, 1)
//                .define('C', Items.RED_CANDLE)
//                .define('P', ModItems.WOODEN_PARTS)
//                .pattern("C")
//                .pattern("P")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TRICOLOR_CANDLESTICK, 1)
//                .define('C', Items.WHITE_CANDLE)
//                .define('P', ModItems.PORCELAIN_PARTS)
//                .pattern("C")
//                .pattern("P")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.JADE_CANDLESTICK, 1)
//                .define('C', Items.WHITE_CANDLE)
//                .define('P', ModItems.JADE_PARTS)
//                .pattern("C")
//                .pattern("P")
//                .unlockedBy(itemUnlockName(Items.CANDLE), has(Items.CANDLE))
//                .save(recipeOutput);
//    }
//
//    @NotNull
//    private static String itemUnlockName(ItemLike item) {
//        return "has_" + item;
//    }
//
//    public static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
//        return InventoryChangeTrigger.TriggerInstance.hasItems(item);
//    }
//
//    private static void quadComposeRecipe(Item part, Item combined, RecipeOutput recipeOutput) {
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, combined, 1)
//                .define('A', part)
//                .pattern("AA")
//                .pattern("AA")
//                .unlockedBy("has_" + RegistryHelper.getItemRegistryName(part).getPath(), has(part))
//                .save(recipeOutput);
//    }
//
//    private static void quadDecomposeRecipe(Item part, Item combined, RecipeOutput recipeOutput) {
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, part, 4).requires(combined)
//                .unlockedBy("has_" + RegistryHelper.getItemRegistryName(combined).getPath(), has(combined))
//                .save(recipeOutput);
//    }
//
//    private static void stonePolishing(Item raw, Item polished, RecipeOutput recipeOutput) {
//        String rawPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(raw)).getPath();
//        String polishedPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(polished)).getPath();
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, polished, 4)
//                .define('A', raw)
//                .pattern("AA")
//                .pattern("AA")
//                .unlockedBy("has_" + rawPath, has(raw))
//                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, polishedPath + "_from_crafting"));
//        SingleItemRecipeBuilder.stonecutting(Ingredient.of(raw), RecipeCategory.MISC, polished)
//                .unlockedBy("has_" + rawPath, has(raw))
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, polishedPath + "_from_stonecutting"));
//    }
//
//    private static void blockDyeing(Item block, Item dye, Item output, RecipeOutput recipeOutput) {
//        String blockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(block)).getPath();
//        String dyePath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(dye)).getPath();
//        String outputPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(output)).getPath();
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output).requires(block).requires(dye)
//                .unlockedBy("has_" + blockPath, has(block))
//                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, outputPath));
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, output, 8)
//                .define('B', block)
//                .define('D', dye)
//                .pattern("BBB")
//                .pattern("BDB")
//                .pattern("BBB")
//                .unlockedBy("has_" + blockPath, has(block))
//                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, outputPath + "_batch"));
//    }
//
//    private static void dust(Item input, Item output, RecipeOutput recipeOutput) {
//        SingleItemRecipeBuilder.stonecutting(Ingredient.of(input), RecipeCategory.MISC, output)
//                .unlockedBy("has_" + name(input), has(input))
//                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, name(output)));
//    }
//
//    private static void brickMixture(Item brick, int brickAmount, Item additive, Item output, RecipeOutput recipeOutput) {
//        String brickPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(brick)).getPath();
//        String additivePath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(additive)).getPath();
//        String outputPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(output)).getPath();
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output, brickAmount).requires(brick, brickAmount).requires(additive)
//                .unlockedBy("has_" + brickPath, has(brick))
//                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, outputPath));
//    }
//
//    private static void smeltingAndBlasting(Item input, Item output, RecipeOutput recipeOutput) {
//        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.MISC, output, 0.1F, 200)
//                .unlockedBy(itemUnlockName(input), has(input))
//                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, name(output) + "_from_smelting"));
//        SimpleCookingRecipeBuilder.blasting(Ingredient.of(input), RecipeCategory.MISC, output, 0.05F, 100)
//                .unlockedBy(itemUnlockName(input), has(input))
//                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, name(output) + "_from_blasting"));
//    }
//
//    private static void stoneSlabAndStairsRecipe(Item baseBlock, Item slabBlock, Item stairBlock, RecipeOutput recipeOutput) {
//        String baseBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(baseBlock)).getPath();
//        String stairsBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(stairBlock)).getPath();
//        String slabBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(slabBlock)).getPath();
//        String baseBlockAdvancement = "has_" + baseBlockPath;
//        Criterion< InventoryChangeTrigger.TriggerInstance > trigger = has(baseBlock);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stairBlock, 4)
//                .define('B', baseBlock)
//                .pattern("B  ")
//                .pattern("BB ")
//                .pattern("BBB")
//                .unlockedBy(baseBlockAdvancement, trigger)
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, stairsBlockPath + "_from_crafting"));
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, slabBlock, 6)
//                .define('B', baseBlock)
//                .pattern("BBB")
//                .unlockedBy(baseBlockAdvancement, trigger)
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, slabBlockPath + "_from_crafting"));
//        SingleItemRecipeBuilder.stonecutting(Ingredient.of(baseBlock), RecipeCategory.MISC, stairBlock)
//                .unlockedBy(baseBlockAdvancement, trigger)
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, stairsBlockPath + "_from_stonecutting"));
//        SingleItemRecipeBuilder.stonecutting(Ingredient.of(baseBlock), RecipeCategory.MISC, slabBlock, 2)
//                .unlockedBy(baseBlockAdvancement, trigger)
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, slabBlockPath + "_from_stonecutting"));
//    }
//
//    private static void woodSlabAndStairsRecipe(Item baseBlock, Item slabBlock, Item stairBlock, RecipeOutput recipeOutput) {
//        String baseBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(baseBlock)).getPath();
//        String stairsBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(stairBlock)).getPath();
//        String slabBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(slabBlock)).getPath();
//        String baseBlockAdvancement = "has_" + baseBlockPath;
//        Criterion< InventoryChangeTrigger.TriggerInstance > trigger = has(baseBlock);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stairBlock, 4)
//                .define('B', baseBlock)
//                .pattern("B  ")
//                .pattern("BB ")
//                .pattern("BBB")
//                .unlockedBy(baseBlockAdvancement, trigger)
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, stairsBlockPath + "_from_crafting"));
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, slabBlock, 6)
//                .define('B', baseBlock)
//                .pattern("BBB")
//                .unlockedBy(baseBlockAdvancement, trigger)
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, slabBlockPath + "_from_crafting"));
//        woodworking(Ingredient.of(baseBlock), stairBlock, 1, baseBlock, recipeOutput);
//        woodworking(Ingredient.of(baseBlock), slabBlock, 2, baseBlock, recipeOutput);
//    }
//
//    private static void wallRecipe(Item baseBlock, Item wallBlock, RecipeOutput recipeOutput) {
//        String baseBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(baseBlock)).getPath();
//        String wallBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(wallBlock)).getPath();
//        String baseBlockAdvancement = "has_" + baseBlockPath;
//        Criterion< InventoryChangeTrigger.TriggerInstance > trigger = has(baseBlock);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, wallBlock, 6)
//                .define('B', baseBlock)
//                .pattern("BBB")
//                .pattern("BBB")
//                .unlockedBy(baseBlockAdvancement, trigger)
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, wallBlockPath + "_from_crafting"));
//        SingleItemRecipeBuilder.stonecutting(Ingredient.of(baseBlock), RecipeCategory.MISC, wallBlock)
//                .unlockedBy(baseBlockAdvancement, trigger)
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, wallBlockPath + "_from_stonecutting"));
//    }
//
//    private static void fenceRecipe(Item baseBlock, Item fenceBlock, RecipeOutput recipeOutput) {
//        String baseBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(baseBlock)).getPath();
//        String fenceBlockPath = Objects.requireNonNull(RegistryHelper.getItemRegistryName(fenceBlock)).getPath();
//        String baseBlockAdvancement = "has_" + baseBlockPath;
//        Criterion< InventoryChangeTrigger.TriggerInstance > trigger = has(baseBlock);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, fenceBlock, 3)
//                .define('B', baseBlock)
//                .define('S', Items.STICK)
//                .pattern("BSB")
//                .pattern("BSB")
//                .unlockedBy(baseBlockAdvancement, trigger)
//                .save(recipeOutput,
//                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, fenceBlockPath + "_from_crafting"));
//        woodworking(Ingredient.of(baseBlock), fenceBlock, 1, baseBlock, recipeOutput);
//    }
//
//    private static void polishedPlankRecipe(Item planks, Item polishedPlank, RecipeOutput recipeOutput) {
//        woodworking(Ingredient.of(planks), polishedPlank, 2, planks, recipeOutput);
//    }
//
//    private static void roofTileBlocksRecipe(String color, RecipeOutput recipeOutput) {
//        Item tileItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, color + "_roof_tile"));
//        if (tileItem == null) return;
//
//        Item tileBlock = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, color + "_roof_tiles"));
//        if (tileBlock != null) {
//            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, tileBlock, 1)
//                    .define('T', tileItem)
//                    .pattern("TTT")
//                    .unlockedBy("has_" + color + "_roof_tile", has(tileItem))
//                    .save(recipeOutput);
//        }
//
//        tileBlock = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, color + "_roof_tile_stairs"));
//        if (tileBlock != null) {
//            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, tileBlock, 1)
//                    .define('T', tileItem)
//                    .pattern("T  ")
//                    .pattern("TT ")
//                    .pattern("TTT")
//                    .unlockedBy("has_" + color + "_roof_tile", has(tileItem))
//                    .save(recipeOutput);
//        }
//
//        tileBlock = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, color + "_roof_tile_edge"));
//        if (tileBlock != null) {
//            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, tileBlock, 1)
//                    .define('T', tileItem)
//                    .pattern("T  ")
//                    .pattern("TTT")
//                    .unlockedBy("has_" + color + "_roof_tile", has(tileItem))
//                    .save(recipeOutput);
//        }
//    }
//
//    private static void woodworking(Ingredient ingredient, ItemLike result, int count, Item unlockItem, RecipeOutput recipeOutput) {
//        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID,
//                RegistryHelper.getItemRegistryName(result).getPath() + "_from_woodworking");
//        WoodworkingRecipe recipe = new WoodworkingRecipe(id, "", ingredient, new ItemStack(result.asItem(), count)
//        );
//        recipe.finishRecipe(recipeOutput);
//    }
//
//    private static String name(Item item) {
//        return Objects.requireNonNull(RegistryHelper.getItemRegistryName(item)).getPath();
//    }
//
//    @Override
//    public void buildRecipes(RecipeOutput recipeOutput) {
//        //BUILDING BLOCKS
//        quadComposeRecipe(ModItems.CYAN_BRICK, ModItems.CYAN_BRICKS, recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.CYAN_BRICKS, ModItems.CYAN_BRICK_SLAB, ModItems.CYAN_BRICK_STAIRS, recipeOutput);
//        quadComposeRecipe(ModItems.BLACK_BRICK, ModItems.BLACK_BRICKS, recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.BLACK_BRICKS, ModItems.BLACK_BRICK_SLAB, ModItems.BLACK_BRICK_STAIRS, recipeOutput);
//        wallRecipe(ModItems.BLACK_BRICKS, ModItems.BLACK_BRICK_WALL, recipeOutput);
//        quadComposeRecipe(ModItems.BROWNISH_RED_STONE_BRICK, ModItems.BROWNISH_RED_STONE_BRICKS, recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.BROWNISH_RED_STONE_BRICKS, ModItems.BROWNISH_RED_STONE_BRICK_SLAB, ModItems.BROWNISH_RED_STONE_BRICK_STAIRS, recipeOutput);
//        wallRecipe(ModItems.BROWNISH_RED_STONE_BRICKS, ModItems.BROWNISH_RED_STONE_BRICK_WALL, recipeOutput);
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.PALE_YELLOW_STONE, 2)
//                .requires(Items.STONE)
//                .requires(Items.SAND)
//                .unlockedBy("has_" + Items.STONE, has(Items.STONE))
//                .save(recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.PALE_YELLOW_STONE, ModItems.PALE_YELLOW_STONE_SLAB, ModItems.PALE_YELLOW_STONE_STAIRS, recipeOutput);
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.VARIEGATED_ROCKS, 4)
//                .requires(Items.COBBLESTONE)
//                .requires(Items.GRANITE)
//                .requires(Items.DIORITE)
//                .requires(Items.ANDESITE)
//                .unlockedBy("has_" + Items.COBBLESTONE, has(Items.STONE))
//                .save(recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.VARIEGATED_ROCKS, ModItems.VARIEGATED_ROCK_SLAB, ModItems.VARIEGATED_ROCK_STAIRS, recipeOutput);
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.WEATHERED_STONE, 2)
//                .requires(Items.STONE)
//                .requires(Items.GRAVEL)
//                .unlockedBy("has_" + Items.STONE, has(Items.STONE))
//                .save(recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.WEATHERED_STONE, ModItems.WEATHERED_STONE_SLAB, ModItems.WEATHERED_STONE_STAIRS, recipeOutput);
//        stonePolishing(ModItems.WEATHERED_STONE, ModItems.POLISHED_WEATHERED_STONE, recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.POLISHED_WEATHERED_STONE, ModItems.POLISHED_WEATHERED_STONE_SLAB, ModItems.POLISHED_WEATHERED_STONE_STAIRS, recipeOutput);
//        wallRecipe(ModItems.POLISHED_WEATHERED_STONE, ModItems.POLISHED_WEATHERED_STONE_WALL, recipeOutput);
//        blockDyeing(Items.SMOOTH_STONE, Items.CYAN_DYE, ModItems.CYAN_FLOOR_TILE, recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.CYAN_FLOOR_TILE, ModItems.CYAN_FLOOR_TILE_SLAB, ModItems.CYAN_FLOOR_TILE_STAIRS, recipeOutput);
//        wallRecipe(ModItems.CYAN_FLOOR_TILE, ModItems.CYAN_FLOOR_TILE_WALL, recipeOutput);
//        blockDyeing(ModItems.CYAN_FLOOR_TILE, Items.WHITE_DYE, ModItems.LIGHT_CYAN_FLOOR_TILE, recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.LIGHT_CYAN_FLOOR_TILE, ModItems.LIGHT_CYAN_FLOOR_TILE_SLAB, ModItems.LIGHT_CYAN_FLOOR_TILE_STAIRS, recipeOutput);
//        wallRecipe(ModItems.LIGHT_CYAN_FLOOR_TILE, ModItems.LIGHT_CYAN_FLOOR_TILE_WALL, recipeOutput);
//        roofTileBlocksRecipe("gray", recipeOutput);
//        roofTileBlocksRecipe("yellow", recipeOutput);
//        roofTileBlocksRecipe("green", recipeOutput);
//        roofTileBlocksRecipe("blue", recipeOutput);
//        roofTileBlocksRecipe("cyan", recipeOutput);
//        roofTileBlocksRecipe("black", recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GREEN_GLAZED_TILES, 2)
//                .define('B', Items.GREEN_GLAZED_TERRACOTTA)
//                .define('T', ModItems.GREEN_ROOF_TILE)
//                .pattern(" T ")
//                .pattern("TBT")
//                .pattern(" T ")
//                .unlockedBy("has_" + Items.GREEN_GLAZED_TERRACOTTA, has(Items.GREEN_GLAZED_TERRACOTTA))
//                .save(recipeOutput);
//        stoneSlabAndStairsRecipe(ModItems.GREEN_GLAZED_TILES, ModItems.GREEN_GLAZED_TILE_SLAB, ModItems.GREEN_GLAZED_TILE_STAIRS, recipeOutput);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BAMBOO_MAT, 4)
//                .define('B', Items.BAMBOO)
//                .define('S', Items.STRING)
//                .pattern("BSB")
//                .pattern("BSB")
//                .pattern("BSB")
//                .unlockedBy("has_" + Items.BAMBOO, has(Items.BAMBOO))
//                .save(recipeOutput);
//        woodSlabAndStairsRecipe(ModItems.BAMBOO_MAT, ModItems.BAMBOO_MAT_SLAB, ModItems.BAMBOO_MAT_STAIRS, recipeOutput);
//        woodSlabAndStairsRecipe(ModItems.ROSEWOOD_PLANKS, ModItems.ROSEWOOD_SLAB, ModItems.ROSEWOOD_STAIRS, recipeOutput);
//        fenceRecipe(ModItems.ROSEWOOD_PLANKS, ModItems.ROSEWOOD_FENCE, recipeOutput);
//
//        //MATERIALS
//        woodworking(Ingredient.of(ItemTags.PLANKS), ModItems.WOODEN_FRAME, 2, Items.OAK_PLANKS, recipeOutput);
//        polishedPlankRecipe(Items.OAK_PLANKS, ModItems.POLISHED_OAK_PLANK, recipeOutput);
//        polishedPlankRecipe(Items.BIRCH_PLANKS, ModItems.POLISHED_BIRCH_PLANK, recipeOutput);
//        polishedPlankRecipe(Items.SPRUCE_PLANKS, ModItems.POLISHED_SPRUCE_PLANK, recipeOutput);
//        polishedPlankRecipe(Items.JUNGLE_PLANKS, ModItems.POLISHED_JUNGLE_PLANK, recipeOutput);
//        polishedPlankRecipe(Items.ACACIA_PLANKS, ModItems.POLISHED_ACACIA_PLANK, recipeOutput);
//        polishedPlankRecipe(Items.DARK_OAK_PLANKS, ModItems.POLISHED_DARK_OAK_PLANK, recipeOutput);
//        polishedPlankRecipe(Items.CRIMSON_PLANKS, ModItems.POLISHED_CRIMSON_PLANK, recipeOutput);
//        polishedPlankRecipe(Items.WARPED_PLANKS, ModItems.POLISHED_WARPED_PLANK, recipeOutput);
//        polishedPlankRecipe(ModItems.ROSEWOOD_PLANKS, ModItems.POLISHED_ROSEWOOD_PLANK, recipeOutput);
//        //polishedPlankRecipe(ItemRegistry.EBONY_PLANKS, ItemRegistry.POLISHED_EBONY_PLANK, recipeOutput);
//        dust(ModItems.RAW_HEMATITE, ModItems.HEMATITE_DUST, recipeOutput);
//        dust(ModItems.MAGNESITE, ModItems.MAGNESITE_DUST, recipeOutput);
//        dust(Items.PRISMARINE_SHARD, ModItems.PRISMARINE_DUST, recipeOutput);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.UNFIRED_CLAY_BRICK, 3)
//                .define('C', Items.CLAY_BALL)
//                .pattern("CCC")
//                .unlockedBy(itemUnlockName(Items.CLAY_BALL), has(Items.CLAY_BALL))
//                .save(recipeOutput);
//        brickMixture(ModItems.UNFIRED_CLAY_BRICK, 6, ModItems.PRISMARINE_DUST, ModItems.UNFIRED_RAW_CYAN_BRICK, recipeOutput);
//        brickMixture(ModItems.UNFIRED_CLAY_BRICK, 6, ModItems.HEMATITE_DUST, ModItems.UNFIRED_BLACK_BRICK, recipeOutput);
//        brickMixture(ModItems.UNFIRED_CLAY_BRICK, 6, Items.NETHER_WART, ModItems.UNFIRED_RAW_BROWNISH_RED_STONE_BRICK, recipeOutput);
//        smeltingAndBlasting(ModItems.UNFIRED_CLAY_BRICK, ModItems.FIRED_BRICK, recipeOutput);
//        smeltingAndBlasting(ModItems.UNFIRED_RAW_CYAN_BRICK, ModItems.CYAN_BRICK, recipeOutput);
//        smeltingAndBlasting(ModItems.UNFIRED_BLACK_BRICK, ModItems.BLACK_BRICK, recipeOutput);
//        smeltingAndBlasting(ModItems.UNFIRED_RAW_BROWNISH_RED_STONE_BRICK, ModItems.BROWNISH_RED_STONE_BRICK, recipeOutput);
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BRICK, 1)
//                .requires(ModItems.FIRED_BRICK)
//                .unlockedBy("has_" + ModItems.FIRED_BRICK, has(ModItems.FIRED_BRICK))
//                .save(recipeOutput);
//
//        //LAMPS
//        generateLampRecipes(recipeOutput);
//    }
//}
