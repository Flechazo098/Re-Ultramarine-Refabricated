package org.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

import java.util.Map;

public class SideAxialBlock extends BaseHorizontalDirectionalBlock implements AxialBlock, SimpleWaterloggedBlock {

    public static final MapCodec<SideAxialBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BlockBehaviour.Properties.CODEC.fieldOf("properties").forGetter(SideAxialBlock::properties),
                    Codec.INT.fieldOf("thickness").forGetter(block -> block.thickness),
                    Codec.INT.fieldOf("height").forGetter(block -> block.height),
                    Codec.BOOL.fieldOf("hasCollision").forGetter(block -> block.hasCollision)
            ).apply(instance, SideAxialBlock::new));

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final int thickness;
    private final boolean hasCollision;
    private final int height;
    protected Map<Direction.Axis, VoxelShape> shapeByAxis;

    public SideAxialBlock(BlockBehaviour.Properties properties, int thickness, int height, boolean hasCollision) {
        super(new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE));
        BlockState state = this.stateDefinition.any()
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(FACING, Direction.NORTH);
        this.registerDefaultState(state);
        this.thickness = thickness;
        this.shapeByAxis = this.makeAxialShapes(thickness, height);
        this.height = height;
        this.hasCollision = hasCollision;
    }

    public SideAxialBlock(BaseBlockProperty property, int thickness) {
        this(property.properties, thickness, 16, false);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Direction direction = pState.getValue(FACING);
        if (direction == Direction.EAST || direction == Direction.WEST) return this.shapeByAxis.get(Direction.Axis.X);
        else return this.shapeByAxis.get(Direction.Axis.Z);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = this.defaultBlockState().setValue(WATERLOGGED, pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
        return state.setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(WATERLOGGED);
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

    public BlockBehaviour.Properties properties () {
        return super.properties();
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
