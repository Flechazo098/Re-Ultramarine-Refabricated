package org.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StraightStairBlock extends BaseHorizontalDirectionalBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<StraightStairBlock> CODEC = simpleCodec((properties) ->
            new StraightStairBlock(new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE)));

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    private static final VoxelShape BOTTOM_AABB = box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    private static final VoxelShape TOP_AABB = box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape EAST_SHAPE_BOTTOM = Shapes.or(BOTTOM_AABB, box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D));
    private static final VoxelShape NORTH_SHAPE_BOTTOM = Shapes.or(BOTTOM_AABB, box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D));
    private static final VoxelShape SOUTH_SHAPE_BOTTOM = Shapes.or(BOTTOM_AABB, box(0.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D));
    private static final VoxelShape WEST_SHAPE_BOTTOM = Shapes.or(BOTTOM_AABB, box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 16.0D));
    private static final VoxelShape EAST_SHAPE_TOP = Shapes.or(TOP_AABB, box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D));
    private static final VoxelShape NORTH_SHAPE_TOP = Shapes.or(TOP_AABB, box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D));
    private static final VoxelShape SOUTH_SHAPE_TOP = Shapes.or(TOP_AABB, box(0.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D));
    private static final VoxelShape WEST_SHAPE_TOP = Shapes.or(TOP_AABB, box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 16.0D));

    public StraightStairBlock(BlockBehaviour.Properties properties) {
        super(new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE));
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM)
                .setValue(WATERLOGGED, Boolean.FALSE));
    }

    public StraightStairBlock(BaseBlockProperty property) {
        this(property.properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        Half half = pState.getValue(HALF);
        if (half == Half.TOP) {
            return switch (direction) {
                case NORTH -> NORTH_SHAPE_TOP;
                case SOUTH -> SOUTH_SHAPE_TOP;
                case WEST -> WEST_SHAPE_TOP;
                default -> EAST_SHAPE_TOP;
            };
        } else {
            return switch (direction) {
                case NORTH -> NORTH_SHAPE_BOTTOM;
                case SOUTH -> SOUTH_SHAPE_BOTTOM;
                case WEST -> WEST_SHAPE_BOTTOM;
                default -> EAST_SHAPE_BOTTOM;
            };
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction = pContext.getHorizontalDirection();
        BlockPos blockpos = pContext.getClickedPos();
        FluidState fluidstate = pContext.getLevel().getFluidState(blockpos);
        return this.defaultBlockState()
                .setValue(FACING, direction)
                .setValue(HALF, getHalf(pContext))
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    private static Half getHalf(BlockPlaceContext pContext) {
        return pContext.getClickedFace() == Direction.DOWN ||
                pContext.getClickLocation().y - (double) pContext.getClickedPos().getY() > 0.5D
                ? Half.TOP : Half.BOTTOM;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(HALF, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
