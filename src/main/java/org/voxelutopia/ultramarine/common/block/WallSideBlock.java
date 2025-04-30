package org.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.voxelutopia.ultramarine.init.data.shape.ReShapeFunction;


public class WallSideBlock extends Block implements BaseBlockPropertyHolder, SimpleWaterloggedBlock, SideBlock {

    public static final MapCodec<WallSideBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BaseBlockProperty.CODEC.fieldOf("property").forGetter(block -> block.property),
                    Codec.INT.fieldOf("sideThickness").forGetter(block -> block.sideThickness)
            ).apply(instance, WallSideBlock::new) // 使用主构造函数
    );
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected final BaseBlockProperty property;
    private final ReShapeFunction shapeFunction;
    private final int sideThickness;

    public WallSideBlock(BaseBlockProperty property, int sideThickness) {
        super(property.copy().properties.noOcclusion().noCollission());
        this.property = property;
        this.sideThickness = sideThickness;
        this.shapeFunction = ReShapeFunction.sideShape(sideThickness);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }


    public WallSideBlock(BaseBlockProperty property, ReShapeFunction shapeFunction) {
        super(property.copy().properties.noOcclusion().noCollission());
        this.property = property;
        this.sideThickness = 1;
        this.shapeFunction = shapeFunction;
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
    }

    public WallSideBlock(BaseBlockProperty property) {
        this(property, 1);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.shapeFunction.apply(pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = this.defaultBlockState();
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());

        Direction direction = pContext.getClickedFace();
        if (direction.getAxis().isHorizontal()) {
            return state.setValue(FACING, direction)
                    .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }


    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public BaseBlockProperty getProperty() {
        return property;
    }

    public BlockBehaviour.Properties properties () {
        return this.properties;
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
