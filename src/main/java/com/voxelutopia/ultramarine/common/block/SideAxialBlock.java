package com.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.init.data.RawVoxelShape;
import com.voxelutopia.ultramarine.init.data.shape.ReShapeFunction;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SideAxialBlock extends BaseHorizontalDirectionalBlock implements AxialBlock, SimpleWaterloggedBlock {

    public static final MapCodec<SideAxialBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BaseBlockProperty.CODEC.fieldOf("properties").forGetter(block -> block.property),
                    Codec.BOOL.fieldOf("hasCollision").forGetter(block -> block.hasCollision),
                    ReShapeFunction.CODEC.fieldOf("shapeFunction").forGetter(block -> block.shapeFunction)
            ).apply(instance, SideAxialBlock::new));

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final boolean hasCollision;
    private final ReShapeFunction shapeFunction;

    public SideAxialBlock(BaseBlockProperty property, boolean hasCollision, ReShapeFunction shapeFunction) {
        super(property);
        BlockState state = this.stateDefinition.any()
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(FACING, Direction.NORTH);
        this.registerDefaultState(state);
        this.shapeFunction = shapeFunction;
        this.hasCollision = hasCollision;
    }

    public SideAxialBlock(BaseBlockProperty property, int thickness, int height, boolean hasCollision) {
        this(property, hasCollision, ReShapeFunction.cardinalRotations(new RawVoxelShape((16 - thickness) / 2f, 0, 0, 16 - (16 - thickness) / 2f, height, 16)));
    }

    public SideAxialBlock(BaseBlockProperty property, int thickness) {
        this(property, thickness, 16, false);
    }

    public SideAxialBlock(BaseBlockProperty property, ReShapeFunction shape) {
        this(property, false, shape);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.shapeFunction.apply(pState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = this.defaultBlockState().setValue(WATERLOGGED, pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
        return state.setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return hasCollision ? this.getShape(pState, pLevel, pPos, pContext) : Shapes.empty();
    }

    @Override
    public Direction.Axis getAxis(BlockState pState) {
        Direction direction = pState.getValue(FACING);
        return direction.getAxis();
    }

    public BlockBehaviour.Properties properties() {
        return this.properties;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
