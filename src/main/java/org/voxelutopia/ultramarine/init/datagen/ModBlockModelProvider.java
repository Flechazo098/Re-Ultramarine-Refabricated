package org.voxelutopia.ultramarine.init.datagen;

import com.google.gson.JsonElement;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ModelProvider;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import org.voxelutopia.ultramarine.UltramarineDataGenerators;
import org.voxelutopia.ultramarine.common.block.*;
import org.voxelutopia.ultramarine.common.block.state.*;
import org.voxelutopia.ultramarine.init.registry.ModBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 方块模型数据生成器
 * @author Flechazo
 */
public class ModBlockModelProvider extends ModelProvider {
    
    private static final String BLOCK = "block/";
    private static final List<Block> NON_SIMPLE_BLOCKS = new ArrayList<>();
    private final PackOutput output;
    private BlockModelGenerators blockModelGenerators;
    private BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput;
    private Consumer<BlockStateGenerator> blockStateOutput;

    static {
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> (
                        block instanceof BaseWall ||
                        block instanceof BaseFence ||
                        block instanceof BaseDirectionalBlock ||
                        block instanceof BaseHorizontalDirectionalBlock ||
                        block instanceof BaseAxialBlock ||
                        block instanceof DecorativeBlock ||
                        block instanceof ContainerDecorativeBlock ||
                        block instanceof SeatDecorativeBlock ||
                        block instanceof ConsumableDecorativeBlock ||
                        block instanceof OpeningBlock ||
                        block instanceof RailingBlock ||
                        block instanceof RailingSlant ||
                        block instanceof WallSideBlock ||
                        block instanceof OrientableWallSideBlock ||
                        block instanceof StraightStairBlock ||
                        block instanceof RoofTiles ||
                        block instanceof CandleStick ||
                        block instanceof Censer ||
                        block instanceof BottleGourd ||
                        block instanceof Bracket
                ))
                .forEach(NON_SIMPLE_BLOCKS::add);
    }

    public ModBlockModelProvider(PackOutput output) {
        super(output);
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        // 创建路径提供器
        PackOutput.PathProvider blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        PackOutput.PathProvider modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");

        // 创建输出消费者
        modelOutput = (location, json) -> DataProvider.saveStable(cache, json.get(), modelPathProvider.json(location));
        blockStateOutput = (generator) -> DataProvider.saveStable(cache, generator.get(), blockStatePathProvider.json(generator.getBlock().builtInRegistryHolder().key().location()));

        // 初始化生成器
        blockModelGenerators = new BlockModelGenerators(blockStateOutput, modelOutput, (block) -> {});

        // 生成模型
        generateBlockModels();

        return CompletableFuture.completedFuture(null);
    }

    private void generateBlockModels() {
        // 生成基础方块
        generateBasicBlocks();
        
        // 生成特殊方块
        generateSpecialBlocks();
        
        // 生成装饰方块
        generateDecorativeBlocks();
        
        // 生成功能方块
        generateFunctionalBlocks();
    }

    private void generateBasicBlocks() {
        // 处理简单方块
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> !NON_SIMPLE_BLOCKS.contains(block))
                .forEach(this::generateSimpleBlock);

        // 处理楼梯
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof StairBlock)
                .forEach(this::generateStairsBlock);

        // 处理台阶
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof SlabBlock)
                .forEach(this::generateSlabBlock);

        // 处理墙
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof WallBlock)
                .forEach(this::generateWallBlock);

        // 处理栅栏
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof FenceBlock)
                .forEach(this::generateFenceBlock);
    }

    private void generateSpecialBlocks() {
        // 处理屋顶瓦片
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof RoofTiles)
                .forEach(this::generateRoofTilesBlock);

        // 处理栏杆
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof RailingBlock)
                .forEach(this::generateRailingBlock);

        // 处理支架
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof Bracket)
                .forEach(this::generateBracketBlock);

        // 处理手性方块
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof ChiralBlock)
                .forEach(this::generateChiralBlock);

        // 处理可定向方块
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof OrientableBlock)
                .forEach(this::generateOrientableBlock);

        // 处理可堆叠方块
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof StackableBlock)
                .forEach(this::generateStackableBlock);
    }

    private void generateDecorativeBlocks() {
        // 处理装饰性方块
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof DecorativeBlock)
                .forEach(this::generateDecorativeBlock);

        // 处理容器装饰方块
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof ContainerDecorativeBlock)
                .forEach(this::generateContainerDecorativeBlock);

        // 处理座椅装饰方块
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof SeatDecorativeBlock)
                .forEach(this::generateSeatDecorativeBlock);
    }

    private void generateFunctionalBlocks() {
        // 处理可消耗方块
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof ConsumableDecorativeBlock)
                .forEach(this::generateConsumableBlock);

        // 处理开启类方块
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof OpeningBlock)
                .forEach(this::generateOpeningBlock);

        // 处理蜡烛台
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof CandleStick)
                .forEach(this::generateCandleStickBlock);

        // 处理香炉
        BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof Censer)
                .forEach(this::generateCenserBlock);
    }

    private void generateSimpleBlock(Block block) {
        blockModelGenerators.createTrivialCube(block);
    }

    private void generateStairsBlock(Block block) {
        ResourceLocation texture = getBlockTexture(block);
        TexturedModel.Provider modelProvider = TexturedModel.CUBE_ALL.get(block);
        blockModelGenerators.createStairs(block, modelProvider, modelProvider, modelProvider);
    }

    private void generateSlabBlock(Block block) {
        ResourceLocation texture = getBlockTexture(block);
        TexturedModel.Provider modelProvider = TexturedModel.CUBE_ALL.get(block);
        blockModelGenerators.createSlab(block, modelProvider, modelProvider, modelProvider, modelProvider);
    }

    private void generateWallBlock(Block block) {
        ResourceLocation texture = getBlockTexture(block);
        TexturedModel.Provider modelProvider = TexturedModel.CUBE_ALL.get(block);
        blockModelGenerators.createWall(block, modelProvider, modelProvider, modelProvider);
    }

    private void generateFenceBlock(Block block) {
        ResourceLocation texture = getBlockTexture(block);
        TexturedModel.Provider modelProvider = TexturedModel.CUBE_ALL.get(block);
        blockModelGenerators.createFence(block, modelProvider, modelProvider);
    }

    private void generateRoofTilesBlock(Block block) {
        RoofTiles roofTiles = (RoofTiles) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成基础模型
        ModelTemplate template = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/roof_tile")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.property(ModBlockStateProperties.SHIFTED)
                .select(false, Variant.variant().with(VariantProperties.MODEL, template.create(
                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/roof_tile"),
                    TextureMapping.cube(texture),
                    modelOutput)))
                .select(true, Variant.variant().with(VariantProperties.MODEL, template.create(
                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/roof_tile_shifted"),
                    TextureMapping.cube(texture),
                    modelOutput))));
                    
        blockStateOutput.accept(generator);
    }

    private void generateRailingBlock(Block block) {
        RailingBlock railing = (RailingBlock) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成基础模型
        ModelTemplate template = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/railing")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiPartGenerator generator = MultiPartGenerator.multiPart(block)
            .with(Condition.condition().term(RailingBlock.UP, true),
                Variant.variant().with(VariantProperties.MODEL, template.create(
                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/railing_post"),
                        TextureMapping.cube(texture),
                        modelOutput)))
            .with(Condition.condition().term(RailingBlock.NORTH, true),
                Variant.variant().with(VariantProperties.MODEL, template.create(
                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/railing_side"),
                        TextureMapping.cube(texture),
                        modelOutput)))
            .with(Condition.condition().term(RailingBlock.EAST, true),
                Variant.variant().with(VariantProperties.MODEL, template.create(
                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/railing_side"),
                        TextureMapping.cube(texture),
                        modelOutput))
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .with(Condition.condition().term(RailingBlock.SOUTH, true),
                Variant.variant().with(VariantProperties.MODEL, template.create(
                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/railing_side"),
                        TextureMapping.cube(texture),
                        modelOutput))
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .with(Condition.condition().term(RailingBlock.WEST, true),
                Variant.variant().with(VariantProperties.MODEL, template.create(
                        ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/railing_side"),
                        TextureMapping.cube(texture),
                        modelOutput))
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
                
        blockStateOutput.accept(generator);
    }

    private void generateBracketBlock(Block block) {
        Bracket bracket = (Bracket) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成基础模型
        ModelTemplate template = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/bracket")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                .select(Direction.NORTH, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/bracket"),
                            TextureMapping.cube(texture),
                            modelOutput)))
                .select(Direction.EAST, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/bracket"),
                            TextureMapping.cube(texture),
                            modelOutput))
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.SOUTH, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/bracket"),
                            TextureMapping.cube(texture),
                            modelOutput))
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/bracket"),
                            TextureMapping.cube(texture),
                            modelOutput))
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
                    
        blockStateOutput.accept(generator);
    }

    private void generateDecorativeBlock(Block block) {
        DecorativeBlock decorative = (DecorativeBlock) block;
        ResourceLocation texture = getBlockTexture(block);
        TexturedModel.Provider modelProvider = TexturedModel.CUBE_ALL.get(block);
        
        if (decorative.isDirectional()) {
            // 生成方向性变体
            blockModelGenerators.createHorizontallyRotatedBlock(block, modelProvider);
        } else {
            // 生成普通方块
            blockModelGenerators.createTrivialCube(block);
        }
    }

    private void generateContainerDecorativeBlock(Block block) {
        ContainerDecorativeBlock container = (ContainerDecorativeBlock) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成容器模型
        ModelTemplate template = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/container")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
                .select(Direction.NORTH, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/container"),
                        TextureMapping.cube(texture),
                        modelOutput)))
                .select(Direction.EAST, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/container"),
                        TextureMapping.cube(texture),
                        modelOutput))
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(Direction.SOUTH, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/container"),
                        TextureMapping.cube(texture),
                        modelOutput))
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/container"),
                        TextureMapping.cube(texture),
                        modelOutput))
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
                    
        blockStateOutput.accept(generator);
    }

    private void generateSeatDecorativeBlock(Block block) {
        SeatDecorativeBlock seat = (SeatDecorativeBlock) block;
        ResourceLocation texture = getBlockTexture(block);
        TexturedModel.Provider modelProvider = TexturedModel.CUBE_ALL.get(block);
        
        // 生成座椅模型
        blockModelGenerators.createHorizontallyRotatedBlock(block, modelProvider);
    }

    private void generateConsumableBlock(Block block) {
        ConsumableDecorativeBlock consumable = (ConsumableDecorativeBlock) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成消耗状态模型
        for (int i = 0; i <= consumable.getMaxBites(); i++) {
            ModelTemplate template = new ModelTemplate(
                    Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/consumable_" + i)),
                    Optional.empty(),
                    TextureSlot.TEXTURE
            );
            
            template.create(
                    ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/" + block.getDescriptionId() + "_" + i),
                TextureMapping.cube(texture),
                modelOutput
            );
        }
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.property(ModBlockStateProperties.BITES)
                .generate(i -> Variant.variant()
                    .with(VariantProperties.MODEL, ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID,
                        "block/" + block.getDescriptionId() + "_" + Math.min(i, consumable.getMaxBites())))));
                        
        blockStateOutput.accept(generator);
    }

    private void generateOpeningBlock(Block block) {
        OpeningBlock opening = (OpeningBlock) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成开关状态模型
        ModelTemplate closedTemplate = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/opening_closed")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        ModelTemplate openTemplate = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/opening_open")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.properties(BlockStateProperties.OPEN, BlockStateProperties.DOOR_HINGE)
                .select(false, DoorHingeSide.LEFT, Variant.variant()
                    .with(VariantProperties.MODEL, closedTemplate.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/opening_closed_left"),
                        TextureMapping.cube(texture),
                        modelOutput)))
                .select(false, DoorHingeSide.RIGHT, Variant.variant()
                    .with(VariantProperties.MODEL, closedTemplate.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/opening_closed_right"),
                        TextureMapping.cube(texture),
                        modelOutput)))
                .select(true, DoorHingeSide.LEFT, Variant.variant()
                    .with(VariantProperties.MODEL, openTemplate.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/opening_open_left"),
                        TextureMapping.cube(texture),
                        modelOutput)))
                .select(true, DoorHingeSide.RIGHT, Variant.variant()
                    .with(VariantProperties.MODEL, openTemplate.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/opening_open_right"),
                        TextureMapping.cube(texture),
                        modelOutput))));
                        
        blockStateOutput.accept(generator);
    }

    private void generateCandleStickBlock(Block block) {
        CandleStick candleStick = (CandleStick) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成蜡烛台模型
        ModelTemplate template = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/candle_stick")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.property(BlockStateProperties.LIT)
                .select(false, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/candle_stick"),
                        TextureMapping.cube(texture),
                        modelOutput)))
                .select(true, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/candle_stick_lit"),
                        TextureMapping.cube(texture),
                        modelOutput))));
                        
        blockStateOutput.accept(generator);
    }

    private void generateCenserBlock(Block block) {
        Censer censer = (Censer) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成香炉模型
        ModelTemplate template = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/censer")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.property(BlockStateProperties.LIT)
                .select(false, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/censer"),
                        TextureMapping.cube(texture),
                        modelOutput)))
                .select(true, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/censer_lit"),
                        TextureMapping.cube(texture),
                        modelOutput))));
                        
        blockStateOutput.accept(generator);
    }

    /**
     * 生成手性方块的模型和状态
     * @param block 手性方块
     */
    private void generateChiralBlock(Block block) {
        ChiralBlock chiralBlock = (ChiralBlock) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成左右变体模型
        ModelTemplate leftTemplate = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/chiral_left")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        ModelTemplate rightTemplate = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/chiral_right")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.property(ModBlockStateProperties.CHIRAL_BLOCK_TYPE)
                .select(ChiralBlockType.LEFT, Variant.variant()
                    .with(VariantProperties.MODEL, leftTemplate.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/chiral_left"),
                        TextureMapping.cube(texture),
                        modelOutput)))
                .select(ChiralBlockType.RIGHT, Variant.variant()
                    .with(VariantProperties.MODEL, rightTemplate.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/chiral_right"),
                        TextureMapping.cube(texture),
                        modelOutput))));
                        
        blockStateOutput.accept(generator);
    }

    /**
     * 生成可定向方块的模型和状态
     * @param block 可定向方块
     */
    private void generateOrientableBlock(Block block) {
        OrientableBlock orientable = (OrientableBlock) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成基础模型
        ModelTemplate template = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/orientable")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.property(ModBlockStateProperties.ORIENTABLE_BLOCK_TYPE)
                .select(OrientableBlockType.NORTH, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/orientable"),
                        TextureMapping.cube(texture),
                        modelOutput)))
                .select(OrientableBlockType.EAST, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/orientable"),
                        TextureMapping.cube(texture),
                        modelOutput))
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(OrientableBlockType.SOUTH, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/orientable"),
                        TextureMapping.cube(texture),
                        modelOutput))
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(OrientableBlockType.WEST, Variant.variant()
                    .with(VariantProperties.MODEL, template.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/orientable"),
                        TextureMapping.cube(texture),
                        modelOutput))
                    .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270)));
                        
        blockStateOutput.accept(generator);
    }

    /**
     * 生成可堆叠方块的模型和状态
     * @param block 可堆叠方块
     */
    private void generateStackableBlock(Block block) {
        StackableBlock stackable = (StackableBlock) block;
        ResourceLocation texture = getBlockTexture(block);
        
        // 生成单层和双层模型
        ModelTemplate singleTemplate = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/stackable_single")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        ModelTemplate doubleTemplate = new ModelTemplate(
                Optional.of(ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/stackable_double")),
                Optional.empty(),
                TextureSlot.TEXTURE
        );
        
        // 生成变体
        MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(block)
            .with(PropertyDispatch.property(ModBlockStateProperties.STACKABLE_BLOCK_TYPE)
                .select(StackableBlockType.SINGLE, Variant.variant()
                    .with(VariantProperties.MODEL, singleTemplate.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/stackable_single"),
                        TextureMapping.cube(texture),
                        modelOutput)))
                .select(StackableBlockType.DOUBLE, Variant.variant()
                    .with(VariantProperties.MODEL, doubleTemplate.create(
                            ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID, "block/stackable_double"),
                        TextureMapping.cube(texture),
                        modelOutput))));
                        
        blockStateOutput.accept(generator);
    }

    /**
     * 获取方块的纹理资源位置
     * @param block 方块
     * @return 纹理资源位置
     */
    private ResourceLocation getBlockTexture(Block block) {
        return ResourceLocation.fromNamespaceAndPath(UltramarineDataGenerators.MOD_ID,
            BLOCK + block.builtInRegistryHolder().key().location().getPath());
    }

    /**
     * 创建水平朝向的属性分发器
     * @return 属性分发器
     */
    private PropertyDispatch createHorizontalFacingDispatch() {
        return PropertyDispatch.property(BlockStateProperties.HORIZONTAL_FACING)
            .select(Direction.NORTH, Variant.variant())
            .select(Direction.EAST, Variant.variant()
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
            .select(Direction.SOUTH, Variant.variant()
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
            .select(Direction.WEST, Variant.variant()
                .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270));
    }

    @Override
    public String getName() {
        return UltramarineDataGenerators.MOD_ID + " Block Models";
    }
}
