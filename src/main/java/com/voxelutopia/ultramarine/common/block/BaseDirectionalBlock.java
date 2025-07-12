package com.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BaseDirectionalBlock extends DirectionalBlock implements BaseBlockPropertyHolder {

    public static final MapCodec<BaseDirectionalBlock> CODEC = simpleCodec((properties) ->
            new BaseDirectionalBlock(new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE)));

    protected final BaseBlockProperty property;

    public BaseDirectionalBlock(BaseBlock block) {
        this(block.getProperty());
    }

    public BaseDirectionalBlock(BaseBlockProperty property) {
        super(property.properties);
        this.property = property;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    protected BaseDirectionalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.property = new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        BlockState newState = pState;
        Direction facing = pState.getValue(FACING);
        if (facing.getAxis().isHorizontal()) {
            newState = newState.setValue(FACING, pRot.rotate(facing));
        }
        return newState;
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        BlockState newState = pState;
        Direction facing = pState.getValue(FACING);
        if (facing.getAxis().isHorizontal()) {
            newState = newState.rotate(pMirror.getRotation(facing));
        }
        return newState;
    }

    @Override
    public BaseBlockProperty getProperty() {
        return property;
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }
}
