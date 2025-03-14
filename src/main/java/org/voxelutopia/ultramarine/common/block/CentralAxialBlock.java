package org.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class CentralAxialBlock extends Block implements AxialBlock, SimpleWaterloggedBlock {

    public static final MapCodec<CentralAxialBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BlockBehaviour.Properties.CODEC.fieldOf("properties").forGetter(CentralAxialBlock::properties),
                    Codec.INT.fieldOf("thickness").forGetter(block -> block.thickness),
                    Codec.INT.fieldOf("height").forGetter(block -> block.height),
                    Codec.BOOL.fieldOf("hasCollision").forGetter(block -> block.hasCollision)
            ).apply(instance, CentralAxialBlock::new));

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    private final int thickness;
    private final boolean hasCollision;
    private final int height;
    protected Map<Direction.Axis, VoxelShape> shapeByAxis;

    public CentralAxialBlock(BlockBehaviour.Properties properties, int thickness, int height, boolean hasCollision) {
        super(properties);
        BlockState state = this.stateDefinition.any()
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(AXIS, Direction.Axis.X);
        this.registerDefaultState(state);
        this.thickness = thickness;
        this.shapeByAxis = this.makeAxialShapes(thickness, height);
        this.height = height;
        this.hasCollision = hasCollision;
    }

    public CentralAxialBlock(BaseBlockProperty property, int thickness) {
        this(property.properties, thickness, 16, false);
    }

    public CentralAxialBlock(BaseBlockProperty property, int thickness, int height) {
        this(property.properties, thickness, height, true);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction.Axis axis = pState.getValue(AXIS);
        return this.shapeByAxis.get(axis);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = this.defaultBlockState().setValue(WATERLOGGED, pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
        return state.setValue(AXIS, pContext.getHorizontalDirection().getClockWise().getAxis());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED, AXIS);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.hasCollision ? getShape(pState, pLevel, pPos, pContext) : Shapes.empty();
    }

    @Override
    public Direction.Axis getAxis(BlockState pState) {
        return pState.getValue(AXIS);
    }

    public BlockBehaviour.Properties properties () {
        return super.properties();
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
