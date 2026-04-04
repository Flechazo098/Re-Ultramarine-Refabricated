package com.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BaseHorizontalDirectionalBlock extends HorizontalDirectionalBlock implements BaseBlockPropertyHolder {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final MapCodec<BaseHorizontalDirectionalBlock> CODEC = simpleCodec((properties) ->
            new BaseHorizontalDirectionalBlock(new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE)));

    protected final BaseBlockProperty property;

    public BaseHorizontalDirectionalBlock(BaseBlock block) {
        this(block.getProperty());
    }

    public BaseHorizontalDirectionalBlock(BaseBlockProperty property) {
        super(property.properties());
        this.property = property;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    protected BaseHorizontalDirectionalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.property = new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public BaseBlockProperty getProperty() {
        return property;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
