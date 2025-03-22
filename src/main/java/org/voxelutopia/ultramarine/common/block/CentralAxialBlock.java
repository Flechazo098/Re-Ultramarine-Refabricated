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
import org.voxelutopia.ultramarine.init.data.RawVoxelShape;
import org.voxelutopia.ultramarine.init.data.shape.ReShapeFunction;

import java.util.Map;

public class CentralAxialBlock extends Block implements AxialBlock, SimpleWaterloggedBlock {

    public static final MapCodec<CentralAxialBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BaseBlockProperty.CODEC.fieldOf("properties").forGetter(block -> block.property),
                    ReShapeFunction.CODEC.fieldOf("shapeFunction").forGetter(block -> block.shapeFunction),
                    Codec.BOOL.fieldOf("hasCollision").forGetter(block -> block.hasCollision)
            ).apply(instance, CentralAxialBlock::new));

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    private final boolean hasCollision;
    protected Map<Direction.Axis, VoxelShape> shapeByAxis;
    private final ReShapeFunction shapeFunction;
    private final BaseBlockProperty property;

    public CentralAxialBlock(BaseBlockProperty property, ReShapeFunction shapeFunction, boolean hasCollision) {
        super(property.properties);
        this.property = property;
        BlockState state = this.stateDefinition.any()
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(AXIS, Direction.Axis.X);
        this.registerDefaultState(state);
        this.shapeFunction = shapeFunction;
        this.hasCollision = hasCollision;
    }

    public CentralAxialBlock(BaseBlockProperty property, int thickness, int height, boolean hasCollision) {
        this(property, ReShapeFunction.axialRotations(new RawVoxelShape(0, 0, (16-thickness)/2f, 16, height, 16-(16-thickness)/2f)), hasCollision);
    }

    public CentralAxialBlock(BaseBlockProperty property, int thickness) {
        this(property, thickness, 16, false);
    }

    public CentralAxialBlock(BaseBlockProperty property, int thickness, int height) {
        this(property, thickness, height, true);
    }

    public CentralAxialBlock(BaseBlockProperty property, ReShapeFunction shapeFunction){
        this(property, shapeFunction, false);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shapeFunction.apply(pState);
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

    public BaseBlockProperty getProperty() {
        return property;
    }

    public BlockBehaviour.Properties properties() {
        return super.properties();
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
