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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WallSideBlock extends Block implements BaseBlockPropertyHolder, SimpleWaterloggedBlock, SideBlock {

    public static final MapCodec<WallSideBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BlockBehaviour.Properties.CODEC.fieldOf("properties").forGetter(block -> block.properties()),
                    Codec.INT.fieldOf("sideThickness").forGetter(block -> block.sideThickness)
            ).apply(instance, WallSideBlock::new));

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected final BaseBlockProperty property;
    private final Map<Direction, VoxelShape> shapeByDirection;
    private final int sideThickness;

    public WallSideBlock(BlockBehaviour.Properties properties, int sideThickness) {
        super(properties);
        this.property = new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE);
        this.sideThickness = sideThickness;
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
        this.shapeByDirection = faceShapeByDirection(sideThickness);
    }

    public WallSideBlock(BaseBlockProperty property) {
        this(property.properties, 1);
    }

    public WallSideBlock(BaseBlockProperty property, int sideThickness) {
        this(property.properties, sideThickness);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.shapeByDirection.get(pState.getValue(FACING));
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
