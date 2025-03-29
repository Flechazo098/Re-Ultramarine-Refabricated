package org.voxelutopia.ultramarine.init.datagen;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import org.jetbrains.annotations.NotNull;
import org.voxelutopia.ultramarine.UltramarineDataGenerators;
import org.voxelutopia.ultramarine.common.block.*;
import org.voxelutopia.ultramarine.common.block.state.*;
import org.voxelutopia.ultramarine.init.registry.ModBlocks;
import org.voxelutopia.ultramarine.util.helper.RegistryHelper;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;
import static org.voxelutopia.ultramarine.common.block.state.ModBlockStateProperties.*;

/**
 * @author Flechazo
 * @description Block model data generator for Ultramarine mod
 */
public class ModBlockModelProvider extends FabricModelProvider {

    public static final String BLOCK = "block/";

    private final Map<Pair<Direction, Direction>, Integer> rotations = Map.of(
            Pair.of(Direction.NORTH, Direction.EAST), 90,
            Pair.of(Direction.EAST, Direction.SOUTH), 90,
            Pair.of(Direction.SOUTH, Direction.WEST), 90,
            Pair.of(Direction.WEST, Direction.NORTH), 90,
            Pair.of(Direction.NORTH, Direction.WEST), -90,
            Pair.of(Direction.EAST, Direction.NORTH), -90,
            Pair.of(Direction.SOUTH, Direction.EAST), -90,
            Pair.of(Direction.WEST, Direction.SOUTH), -90
    );

    private final Map<Block, Integer> ROTATED_DECO = Map.of(
            ModBlocks.BRONZE_CENSER, 180,
            ModBlocks.ROYAL_CENSER, 180,
            ModBlocks.BOTTLE_GOURD, 180
    );

    private BlockModelGenerators blockModelGenerators;

    public ModBlockModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        this.blockModelGenerators = blockModelGenerators;
        registerStatesAndModels();
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        // Item models are handled separately
    }

    protected void registerStatesAndModels() {
        // Register basic blocks
        registerBasicBlocks();
        
        // Register roof tiles
        registerRoofTiles();
        
        // Register decorative blocks
        registerDecorativeBlocks();
        
        // Register special blocks
        registerSpecialBlocks();
        
        // Process remaining blocks with custom properties
        processRemainingBlocks();
    }

    private void registerBasicBlocks() {
        // Basic blocks
        simpleBlock(ModBlocks.CYAN_BRICKS);
        slabAndStairs(ModBlocks.CYAN_BRICKS, ModBlocks.CYAN_BRICK_SLAB, ModBlocks.CYAN_BRICK_STAIRS);
        wall(ModBlocks.CYAN_BRICKS, ModBlocks.CYAN_BRICK_WALL);
        
        simpleBlock(ModBlocks.BLACK_BRICKS);
        slabAndStairs(ModBlocks.BLACK_BRICKS, ModBlocks.BLACK_BRICK_SLAB, ModBlocks.BLACK_BRICK_STAIRS);
        wall(ModBlocks.BLACK_BRICKS, ModBlocks.BLACK_BRICK_WALL);
        
        // Add other basic blocks...
    }

    private void processRemainingBlocks() {
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof DecorativeBlock) {
                registerDecorativeBlock((DecorativeBlock) block);
            } else if (block instanceof SideBlock) {
                registerSideBlock((SideBlock) block);
            } else if (block instanceof RoofTiles) {
                processRoofTile((RoofTiles) block);
            }
        }
    }

    private void processRoofTile(RoofTiles block) {
        String color = getColorFromBlock(block);
        String blockName = name(block).toLowerCase();

        // 根据方块名称判断类型
        RoofTiles.RoofTileType type;
        if (blockName.contains("stairs")) {
            type = RoofTiles.RoofTileType.STAIRS;
        } else if (blockName.contains("edge")) {
            type = RoofTiles.RoofTileType.EDGE;
        } else {
            type = RoofTiles.RoofTileType.NORMAL;
        }
        
        // 根据类型调用相应的处理方法
        if (type == RoofTiles.RoofTileType.NORMAL || type == RoofTiles.RoofTileType.STAIRS) {
            shiftedTiles(block, color, type);
        } else {
            shiftedDirectionalBlock(block);
        }
    }

    private String getColorFromBlock(Block block) {
        String name = name(block).toLowerCase();
        if (name.contains("gray")) return "gray";
        if (name.contains("yellow")) return "yellow";
        if (name.contains("green")) return "green";
        if (name.contains("blue")) return "blue";
        if (name.contains("cyan")) return "cyan";
        if (name.contains("black")) return "black";
        return "gray"; // default color
    }

    private void registerRoofTiles() {
        // Gray roof tiles
        shiftedTiles(ModBlocks.GRAY_ROOF_TILES, "gray", RoofTiles.RoofTileType.NORMAL);
        shiftedTiles(ModBlocks.GRAY_ROOF_TILE_STAIRS, "gray", RoofTiles.RoofTileType.STAIRS);
        shiftedDirectionalBlock(ModBlocks.GRAY_ROOF_TILE_EDGE);

        // Yellow roof tiles
        shiftedTiles(ModBlocks.YELLOW_ROOF_TILES, "yellow", RoofTiles.RoofTileType.NORMAL);
        shiftedTiles(ModBlocks.YELLOW_ROOF_TILE_STAIRS, "yellow", RoofTiles.RoofTileType.STAIRS);
        shiftedTiles(ModBlocks.YELLOW_ROOF_TILE_EDGE, "yellow", RoofTiles.RoofTileType.EDGE);

        // Add other colored roof tiles...
    }

    private void shiftedTiles(Block block, String color, RoofTiles.RoofTileType type) {
        String basePath = "block/" + color + "_" + type.name().toLowerCase();
        ResourceLocation normalModel = modLoc(basePath);
        ResourceLocation shiftedModel = modLoc(basePath + "_shifted");
        
        // Create normal variant with particle texture
        TextureMapping textureMapping = new TextureMapping()
            .put(TextureSlot.ALL, normalModel)
            .put(TextureSlot.PARTICLE, mcLoc("block/" + color + "_concrete"));
        
        // Create shifted variant with particle texture
        TextureMapping shiftedTextureMapping = new TextureMapping()
            .put(TextureSlot.ALL, shiftedModel)
            .put(TextureSlot.PARTICLE, mcLoc("block/" + color + "_concrete"));
        
        // Generate models
        ModelTemplates.CUBE_ALL.create(normalModel, textureMapping, this.blockModelGenerators.modelOutput);
        ModelTemplates.CUBE_ALL.create(shiftedModel, shiftedTextureMapping, this.blockModelGenerators.modelOutput);
        
        // Generate block states with shifted property
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block);
        
        // Add variants for shifted and facing properties
        PropertyDispatch dispatch = PropertyDispatch.properties(RoofTiles.SHIFTED, RoofTiles.FACING)
            .generate((shifted, facing) -> {
                ResourceLocation modelLoc = shifted ? shiftedModel : normalModel;
                return Variant.variant()
                    .with(VariantProperties.MODEL, modelLoc)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[(int)facing.toYRot() / 90]);
            });
        
        generator.with(dispatch);
        
        // Accept the block state generator
        this.blockModelGenerators.blockStateOutput.accept(generator);
    }

    private void shiftedDirectionalBlock(Block block) {
        shiftedDirectionalBlock(block, 0);
    }

    private void shiftedDirectionalBlock(Block block, int rotation) {
        // Create property dispatch for SHIFTED and FACING properties
        PropertyDispatch dispatch = PropertyDispatch.properties(SHIFTED, HORIZONTAL_FACING)
            .generate((shifted, facing) -> {
                ResourceLocation modelLoc = shifted ? 
                    modLoc("block/" + name(block) + "_shifted") :
                    modLoc("block/" + name(block));
                    
                return Variant.variant()
                    .with(VariantProperties.MODEL, modelLoc)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[(rotation + (int)facing.toYRot() / 90) % 4]);
            });
            
        // Create and accept the block state generator
        this.blockModelGenerators.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(dispatch)
        );
    }

    private void registerDecorativeBlocks() {
        // Carved wood blocks
        registerCarvedWoodBlocks();
        
        // Pillars
        registerPillars();
        
        // Fangxin blocks
        registerFangxinBlocks();
        
        // Other decorative blocks
        registerOtherDecorativeBlocks();
    }

    private void registerCarvedWoodBlocks() {
        simpleBlock(ModBlocks.RED_CARVED_WOOD);
        carvedWoodenSlab(ModBlocks.RED_CARVED_WOODEN_SLAB, ModBlocks.RED_CARVED_WOOD);
        straightStairs(ModBlocks.RED_CARVED_WOODEN_STAIRS);
        
        // Add other carved wood variants...
    }

    private void registerPillars() {
        sideBottomTop(ModBlocks.CARVED_RED_PILLAR);
        sideBottomTop(ModBlocks.CARVED_RED_PILLAR_BASE);
        directionalSideEnd(ModBlocks.CARVED_RED_PILLAR_HEAD, sideLoc(ModBlocks.CARVED_RED_PILLAR_HEAD), modLoc(BLOCK + "carved_red_pillar"));
        
        // Add other pillar variants...
    }

    private void registerFangxinBlocks() {
        directionalSideEnd(ModBlocks.GREEN_FANGXIN, sideLoc(ModBlocks.GREEN_FANGXIN), endLoc(ModBlocks.GREEN_FANGXIN));
        directionalSideEnd(ModBlocks.GREEN_FANGXIN_EDGE, sideLoc(ModBlocks.GREEN_FANGXIN_EDGE), endLoc(ModBlocks.GREEN_FANGXIN));
        chiralWSMirror(ModBlocks.BLUE_FANGXIN_EDGE, mcLoc(BLOCK + "blue_concrete_powder"));
        
        // Add other fangxin variants...
    }

    private void registerSpecialBlocks() {
        // Register workbench
        ResourceLocation workbenchModel = blockLoc(ModBlocks.WOODWORKING_WORKBENCH);
        this.blockModelGenerators.blockStateOutput.accept(
            BlockModelGenerators.createRotatedVariant(ModBlocks.WOODWORKING_WORKBENCH, workbenchModel)
        );
        
        // Register ores
        simpleBlock(ModBlocks.JADE_ORE);
        simpleBlock(ModBlocks.MAGNESITE_ORE);
        simpleBlock(ModBlocks.HEMATITE_ORE);
    }

    private void carvedWoodenSlab(Block slab, Block full) {
        slabBlock((SlabBlock) slab, RegistryHelper.getBlockRegistryName(full), sideLoc(slab), blockLoc(full), blockLoc(full));
    }

    private void straightStairs(Block block) {
        ResourceLocation modelLoc = modLoc("block/" + name(block));
        
        // Create property dispatch for HALF and FACING properties
        PropertyDispatch dispatch = PropertyDispatch.properties(HALF, HORIZONTAL_FACING)
            .generate((half, facing) -> {
                int xRot = half == Half.BOTTOM ? 0 : 180;
                int yRot = ((int)facing.getClockWise().toYRot() - 90 + (half == Half.BOTTOM ? 0 : 180)) % 360;
                if (yRot < 0) yRot += 360;
                
                return Variant.variant()
                    .with(VariantProperties.MODEL, modelLoc)
                    .with(VariantProperties.X_ROT, VariantProperties.Rotation.values()[xRot / 90])
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[yRot / 90]);
            });
            
        // Create and accept the block state generator
        this.blockModelGenerators.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(dispatch)
        );
    }

    private void sideBottomTop(Block block) {
        String blockName = Objects.requireNonNull(RegistryHelper.getBlockRegistryName(block)).getPath();
        sideBottomTop(block, modLoc(BLOCK + blockName + "_side"), modLoc(BLOCK + blockName + "_bottom"), modLoc(BLOCK + blockName + "_top"));
    }

    private void sideBottomTop(Block block, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        String blockName = Objects.requireNonNull(RegistryHelper.getBlockRegistryName(block)).getPath();
        ResourceLocation modelLoc = modLoc("block/" + blockName);
        
        // Create texture mapping
        TextureMapping textureMapping = new TextureMapping()
            .put(TextureSlot.SIDE, side)
            .put(TextureSlot.BOTTOM, bottom)
            .put(TextureSlot.TOP, top)
            .put(TextureSlot.PARTICLE, side);
        
        // Generate model
        ModelTemplates.CUBE_BOTTOM_TOP.create(modelLoc, textureMapping, this.blockModelGenerators.modelOutput);
        
        // Generate block state
        this.blockModelGenerators.blockStateOutput.accept(
            BlockModelGenerators.createSimpleBlock(block, modelLoc)
        );
    }

    private void directionalSideEnd(Block block, ResourceLocation side, ResourceLocation end) {
        String blockName = name(block);
        ResourceLocation modelLoc = modLoc("block/" + blockName);
        
        // Create texture mapping
        TextureMapping textureMapping = new TextureMapping()
            .put(TextureSlot.SIDE, side)
            .put(TextureSlot.BOTTOM, end)
            .put(TextureSlot.TOP, end)
            .put(TextureSlot.PARTICLE, side);
        
        // Generate model
        ModelTemplates.CUBE_BOTTOM_TOP.create(modelLoc, textureMapping, this.blockModelGenerators.modelOutput);
        
        // Generate block state with rotation
        this.blockModelGenerators.blockStateOutput.accept(
            BlockModelGenerators.createRotatedVariant(block, modelLoc)
        );
    }

    private void chiralWSMirror(Block block, ResourceLocation end) {
        String path = name(block);
        ResourceLocation modelLoc = modLoc("block/" + path);
        ResourceLocation mirroredModelLoc = modLoc("block/" + path + "_mirrored");
        
        // Create texture mappings for both normal and mirrored variants
        TextureMapping normalMapping = new TextureMapping()
            .put(TextureSlot.UP, end)
            .put(TextureSlot.DOWN, end)
            .put(TextureSlot.NORTH, sideLoc(block))
            .put(TextureSlot.SOUTH, sideMirroredLoc(block))
            .put(TextureSlot.EAST, sideLoc(block))
            .put(TextureSlot.WEST, sideMirroredLoc(block))
            .put(TextureSlot.PARTICLE, sideLoc(block));
        
        TextureMapping mirroredMapping = new TextureMapping()
            .put(TextureSlot.UP, end)
            .put(TextureSlot.DOWN, end)
            .put(TextureSlot.NORTH, sideMirroredLoc(block))
            .put(TextureSlot.SOUTH, sideLoc(block))
            .put(TextureSlot.EAST, sideMirroredLoc(block))
            .put(TextureSlot.WEST, sideLoc(block))
            .put(TextureSlot.PARTICLE, sideLoc(block));
        
        // Generate models
        ModelTemplates.CUBE.create(modelLoc, normalMapping, this.blockModelGenerators.modelOutput);
        ModelTemplates.CUBE.create(mirroredModelLoc, mirroredMapping, this.blockModelGenerators.modelOutput);
        
        // Create property dispatch for CHIRAL_BLOCK_TYPE and HORIZONTAL_FACING
        PropertyDispatch dispatch = PropertyDispatch.properties(CHIRAL_BLOCK_TYPE, HORIZONTAL_FACING)
            .generate((chiralType, facing) -> {
                boolean isLeftOrTop = chiralType == ChiralBlockType.LEFT || chiralType == ChiralBlockType.TOP;
                ResourceLocation model = isLeftOrTop ? modelLoc : mirroredModelLoc;
                
                return Variant.variant()
                    .with(VariantProperties.MODEL, model)
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[(int)facing.toYRot() / 90]);
            });
            
        // Generate block state
        this.blockModelGenerators.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(dispatch)
        );
    }

    private ResourceLocation sideLoc(Block block) {
        return modLoc(BLOCK + name(block) + "_side");
    }

    private ResourceLocation sideMirroredLoc(Block block) {
        return modLoc(BLOCK + name(block) + "_side_mirrored");
    }

    private ResourceLocation endLoc(Block block) {
        return modLoc(BLOCK + name(block) + "_end");
    }

    private void registerDecorativeBlock(DecorativeBlock block) {
        if (!block.isDirectional() && !block.isDiagonallyPlaceable()) {
            simpleBlock(block);
        } else if (!block.isDirectional() && block.isDiagonallyPlaceable()) {
            diagonallyPlaceableBlock(block);
        } else if (block.isDirectional() && !block.isDiagonallyPlaceable()) {
            directionalBlock(block);
            } else {
            directionalAndDiagonalBlock(block);
        }
    }

    private void registerSideBlock(SideBlock block) {
        if (block instanceof WallSideBlock) {
            wallSideBlock((WallSideBlock) block);
        } else if (block instanceof SixSideBlock) {
            sixSideBlock((SixSideBlock) block);
        }
    }

    private void simpleBlock(Block block) {
        blockModelGenerators.createTrivialCube(block);
    }

    private void slabAndStairs(Block baseBlock, Block slabBlock, Block stairBlock) {
        ResourceLocation baseTexture = blockLoc(baseBlock);
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createSlab(
            slabBlock,
            baseTexture,
            baseTexture,
            baseTexture
        ));
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createStairs(
            stairBlock,
            baseTexture,
            baseTexture,
            baseTexture
        ));
    }

    private void wall(Block baseBlock, Block wallBlock) {
        ResourceLocation baseTexture = blockLoc(baseBlock);
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createWall(
            wallBlock,
            baseTexture,
            baseTexture,
            baseTexture
        ));
    }

    private void diagonallyPlaceableBlock(Block block) {
        ResourceLocation normalModel = blockLoc(block);
        ResourceLocation diagonalModel = modLoc(BLOCK + name(block) + "_diagonal");
        
        PropertyDispatch dispatch = PropertyDispatch.property(DIAGONAL)
            .select(false, Variant.variant().with(VariantProperties.MODEL, normalModel))
            .select(true, Variant.variant().with(VariantProperties.MODEL, diagonalModel));

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
            .with(dispatch));
    }

    private void directionalBlock(Block block) {
        BlockModelGenerators.createRotatedVariant(block, blockLoc(block));
    }

    private void directionalBlock(Block block, ModelTemplate modelTemplate) {
        ResourceLocation modelLoc = blockLoc(block);
        ResourceLocation diagonalModelLoc = modLoc(BLOCK + name(block) + "_diagonal");
        
        PropertyDispatch dispatch = PropertyDispatch.properties(DIAGONAL, HORIZONTAL_FACING, HORIZONTAL_FACING_SHIFT)
            .generate((diagonal, facing, shiftedFacing) -> {
                if (!diagonal) {
                    return Variant.variant()
                        .with(VariantProperties.MODEL, modelLoc)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[(int)facing.toYRot() / 90]);
            } else {
                    Pair<Direction, Direction> directions = Pair.of(facing, shiftedFacing);
                    int rotation = rotations.getOrDefault(directions, 0);
                    return Variant.variant()
                        .with(VariantProperties.MODEL, diagonalModelLoc)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[rotation / 90]);
                }
            });
            
        blockModelGenerators.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(dispatch)
        );
    }

    private void directionalAndDiagonalBlock(Block block) {
        ResourceLocation modelLoc = blockLoc(block);
        ResourceLocation diagonalModelLoc = modLoc(BLOCK + name(block) + "_diagonal");
        
        PropertyDispatch dispatch = PropertyDispatch.properties(DIAGONAL, HORIZONTAL_FACING, HORIZONTAL_FACING_SHIFT)
            .generate((diagonal, facing, shiftedFacing) -> {
                if (!diagonal) {
                    return Variant.variant()
                        .with(VariantProperties.MODEL, modelLoc)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[(int)facing.toYRot() / 90]);
                } else {
                    Pair<Direction, Direction> directions = Pair.of(facing, shiftedFacing);
                    int rotation = rotations.getOrDefault(directions, 0);
                    return Variant.variant()
                        .with(VariantProperties.MODEL, diagonalModelLoc)
                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[rotation / 90]);
                }
            });
            
        blockModelGenerators.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(dispatch)
        );
    }

    private void wallSideBlock(WallSideBlock block) {
        ResourceLocation modelLoc = modLoc(BLOCK + name(block));
        
        PropertyDispatch dispatch = PropertyDispatch.property(HORIZONTAL_FACING)
            .generate(facing -> Variant.variant()
                .with(VariantProperties.MODEL, modelLoc)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[(int)facing.toYRot() / 90]));
                
        blockModelGenerators.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(dispatch)
        );
    }

    private void sixSideBlock(SixSideBlock block) {
        ResourceLocation modelLoc = modLoc(BLOCK + name(block));
        
        PropertyDispatch dispatch = PropertyDispatch.property(FACING)
            .generate(facing -> {
                Variant variant = Variant.variant()
                    .with(VariantProperties.MODEL, modelLoc);
                    
                if (facing.getAxis().isVertical()) {
                    return variant.with(VariantProperties.X_ROT, 
                        facing == Direction.UP ? VariantProperties.Rotation.R0 : VariantProperties.Rotation.R180);
                } else {
                    return variant.with(VariantProperties.Y_ROT, 
                        VariantProperties.Rotation.values()[(int)facing.toYRot() / 90]);
                }
            });
            
        blockModelGenerators.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(dispatch)
        );
    }

    private ResourceLocation blockLoc(Block block) {
        return modLoc(BLOCK + name(block));
    }

    private ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, path);
    }

    private ResourceLocation mcLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", path);
    }

    private String name(Block block) {
        return Objects.requireNonNull(RegistryHelper.getBlockRegistryName(block)).getPath();
    }

    private ModelTemplate getTemplate(String path) {
        return new ModelTemplate(Optional.of(modLoc(path)), Optional.empty());
    }

    @NotNull
    @Override
    public String getName() {
        return UltramarineDataGenerators.MOD_ID + " Block Models";
    }

    private void registerOtherDecorativeBlocks() {
        // Register vegetable baskets
        registerVegetableBaskets();
        
        // Register railings
        registerRailings();
        
        // Register other decorative blocks
        registerMiscDecorativeBlocks();
    }

    private void registerVegetableBaskets() {
        vegetableBasket((StackableHalfBlock) ModBlocks.CABBAGE_BASKET);
        vegetableBasket((StackableHalfBlock) ModBlocks.CELERY_BASKET);
        vegetableBasket((StackableHalfBlock) ModBlocks.ORANGE_BASKET);
        vegetableBasket((StackableHalfBlock) ModBlocks.APPLE_BASKET);
        vegetableBasket((StackableHalfBlock) ModBlocks.EGGPLANT_BASKET);
        vegetableBasket((StackableHalfBlock) ModBlocks.PEAR_BASKET);
    }

    private void registerRailings() {
        railingBlock(ModBlocks.WHITE_MARBLE_RAILING);
        railingBlock(ModBlocks.CARVED_WOODEN_RAILING);
    }

    private void registerMiscDecorativeBlocks() {
        // Floor tiles
        ResourceLocation layeredModel = ModelTemplates.CUBE_ALL.create(
            ModBlocks.LAYERED_CYAN_FLOOR_TILES,
            TextureMapping.cube(modBlockLoc(ModBlocks.LAYERED_CYAN_FLOOR_TILES, "")),
            blockModelGenerators.modelOutput
        );
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createRotatedVariant(
            ModBlocks.LAYERED_CYAN_FLOOR_TILES, layeredModel
        ));

        ResourceLocation verticalModel = ModelTemplates.CUBE_ALL.create(
            ModBlocks.VERTICAL_CYAN_FLOOR_TILES,
            TextureMapping.cube(modBlockLoc(ModBlocks.VERTICAL_CYAN_FLOOR_TILES, "")),
            blockModelGenerators.modelOutput
        );
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createRotatedVariant(
            ModBlocks.VERTICAL_CYAN_FLOOR_TILES, verticalModel
        ));

        ResourceLocation mixedModel = ModelTemplates.CUBE_ALL.create(
            ModBlocks.MIXED_CYAN_FLOOR_TILES,
            TextureMapping.cube(modBlockLoc(ModBlocks.MIXED_CYAN_FLOOR_TILES, "")),
            blockModelGenerators.modelOutput
        );
        blockModelGenerators.blockStateOutput.accept(BlockModelGenerators.createRotatedVariant(
            ModBlocks.MIXED_CYAN_FLOOR_TILES, mixedModel
        ));
        
        // Other blocks
        blockModelGenerators.createTrivialCube(ModBlocks.CHISELED_CYAN_FLOOR_TILE);
        blockModelGenerators.createTrivialCube(ModBlocks.CUT_CYAN_FLOOR_TILES);
    }

    private void vegetableBasket(StackableHalfBlock block) {
        String blockName = Objects.requireNonNull(RegistryHelper.getBlockRegistryName(block)).getPath();
        
        // Create texture mappings
        TextureMapping singleMapping = new TextureMapping()
            .put(TextureSlot.SIDE, modLoc("block/vegetable_basket_side"))
            .put(TextureSlot.BOTTOM, modLoc("block/vegetable_basket_bottom"))
            .put(TextureSlot.TOP, modLoc(BLOCK + blockName + "_top"));
            
        TextureMapping doubleMapping = new TextureMapping()
            .put(TextureSlot.SIDE, modLoc("block/vegetable_basket_side"))
            .put(TextureSlot.BOTTOM, modLoc("block/vegetable_basket_bottom"))
            .put(TextureSlot.TOP, modLoc(BLOCK + blockName + "_top"));

        // Create models
        ResourceLocation singleModel = ModelTemplates.SLAB_BOTTOM.create(
            block,
            singleMapping,
            blockModelGenerators.modelOutput
        );
        
        ResourceLocation doubleModel = ModelTemplates.CUBE_BOTTOM_TOP.create(
            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, BLOCK + blockName + "_double"),
            doubleMapping,
            blockModelGenerators.modelOutput
        );

        // Create block state with property dispatch
        PropertyDispatch dispatch = PropertyDispatch.property(STACKABLE_BLOCK_TYPE)
            .select(StackableBlockType.SINGLE, Variant.variant().with(VariantProperties.MODEL, singleModel))
            .select(StackableBlockType.DOUBLE, Variant.variant().with(VariantProperties.MODEL, doubleModel));

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
            .with(dispatch));
    }

    private void railingBlock(Block block) {
        ResourceLocation pole = modBlockLoc(block, "pole");
        ResourceLocation side = modBlockLoc(block, "side");
        ResourceLocation panel = modBlockLoc(block, "panel");
        ResourceLocation poleShifted = modBlockLoc(block, "pole_shifted");
        ResourceLocation sideShifted = modBlockLoc(block, "side_shifted");
        ResourceLocation panelShifted = modBlockLoc(block, "panel_shifted");

        MultiPartGenerator multipart = MultiPartGenerator.multiPart(block);

        // Add pole variants
        multipart.with(Condition.condition()
                .term(BlockStateProperties.UP, true)
                .term(ModBlockStateProperties.SHIFTED, false),
                Variant.variant()
                    .with(VariantProperties.MODEL, pole));
                
        multipart.with(Condition.condition()
                .term(BlockStateProperties.UP, true)
                .term(ModBlockStateProperties.SHIFTED, true),
                Variant.variant()
                    .with(VariantProperties.MODEL, poleShifted));

        // Add side variants for each direction
        Direction[] directions = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        for (Direction dir : directions) {
            // Normal side
            multipart.with(
                Condition.condition()
                    .term(getDirectionProperty(dir), true)
                    .term(ModBlockStateProperties.SHIFTED, false),
                Variant.variant()
                    .with(VariantProperties.MODEL, side)
                    .with(VariantProperties.Y_ROT, getRotation(dir))
            );
            
            // Shifted side
            multipart.with(
                Condition.condition()
                    .term(getDirectionProperty(dir), true)
                    .term(ModBlockStateProperties.SHIFTED, true),
                Variant.variant()
                    .with(VariantProperties.MODEL, sideShifted)
                    .with(VariantProperties.Y_ROT, getRotation(dir))
            );
            
            // Normal panel
            multipart.with(
                Condition.condition()
                    .term(getDirectionProperty(dir), true)
                    .term(ModBlockStateProperties.SHIFTED, false),
                Variant.variant()
                    .with(VariantProperties.MODEL, panel)
                    .with(VariantProperties.Y_ROT, getRotation(dir))
            );
            
            // Shifted panel
            multipart.with(
                Condition.condition()
                    .term(getDirectionProperty(dir), true)
                    .term(ModBlockStateProperties.SHIFTED, true),
                Variant.variant()
                    .with(VariantProperties.MODEL, panelShifted)
                    .with(VariantProperties.Y_ROT, getRotation(dir))
            );
        }

        blockModelGenerators.blockStateOutput.accept(multipart);
    }

    private BooleanProperty getDirectionProperty(Direction dir) {
        return switch (dir) {
            case NORTH -> BlockStateProperties.NORTH;
            case SOUTH -> BlockStateProperties.SOUTH;
            case WEST -> BlockStateProperties.WEST;
            case EAST -> BlockStateProperties.EAST;
            default -> throw new IllegalArgumentException("Invalid direction: " + dir);
        };
    }

    private VariantProperties.Rotation getRotation(Direction dir) {
        return VariantProperties.Rotation.values()[(((int) dir.toYRot()) + 180) % 360 / 90];
    }

    private ResourceLocation modBlockLoc(Block block, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/" + name(block) + "_" + suffix);
    }

    /**
     * Helper method to create a horizontal block with rotation
     */
    private void horizontalBlock(Block block, ResourceLocation modelLoc, int rotation) {
        PropertyDispatch dispatch = PropertyDispatch.property(HORIZONTAL_FACING)
            .generate(facing -> Variant.variant()
                .with(VariantProperties.MODEL, modelLoc)
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.values()[((int)facing.toYRot() + rotation) % 360 / 90]));
                
        blockModelGenerators.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(dispatch)
        );
    }

    /**
     * Helper method to create a horizontal block without rotation
     */
    private void horizontalBlock(Block block, ResourceLocation modelLoc) {
        horizontalBlock(block, modelLoc, 0);
    }

    /**
     * Helper method to create a slab block with textures
     */
    private void slabBlock(SlabBlock block, ResourceLocation fullBlock, ResourceLocation side, ResourceLocation bottom, ResourceLocation top) {
        // Create texture mappings
        TextureMapping singleMapping = new TextureMapping()
            .put(TextureSlot.SIDE, side)
            .put(TextureSlot.BOTTOM, bottom)
            .put(TextureSlot.TOP, top);
            
        TextureMapping doubleMapping = new TextureMapping()
            .put(TextureSlot.SIDE, side)
            .put(TextureSlot.BOTTOM, bottom)
            .put(TextureSlot.TOP, top);

        // Create models
        ResourceLocation singleModel = ModelTemplates.SLAB_BOTTOM.create(
            block,
            singleMapping,
            blockModelGenerators.modelOutput
        );
        
        ResourceLocation topModel = ModelTemplates.SLAB_TOP.create(
            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, BLOCK + name(block) + "_top"),
            singleMapping,
            blockModelGenerators.modelOutput
        );
        
        ResourceLocation doubleModel = ModelTemplates.CUBE_BOTTOM_TOP.create(
            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, BLOCK + name(block) + "_double"),
            doubleMapping,
            blockModelGenerators.modelOutput
        );

        // Create block state with property dispatch
        PropertyDispatch dispatch = PropertyDispatch.property(SLAB_TYPE)
            .select(SlabType.BOTTOM, Variant.variant().with(VariantProperties.MODEL, singleModel))
            .select(SlabType.TOP, Variant.variant().with(VariantProperties.MODEL, topModel))
            .select(SlabType.DOUBLE, Variant.variant().with(VariantProperties.MODEL, doubleModel));

        blockModelGenerators.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(dispatch)
        );
    }
}