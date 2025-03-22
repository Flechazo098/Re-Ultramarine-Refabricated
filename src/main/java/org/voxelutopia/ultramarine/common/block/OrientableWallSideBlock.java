package org.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.voxelutopia.ultramarine.common.block.state.ModBlockStateProperties;
import org.voxelutopia.ultramarine.common.block.state.OrientableBlockType;
import org.voxelutopia.ultramarine.init.data.shape.ReShapeFunction;

public class OrientableWallSideBlock extends WallSideBlock implements SideBlock {

    public static final MapCodec<OrientableWallSideBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BaseBlockProperty.CODEC.fieldOf("property").forGetter(block -> block.property),
                    ReShapeFunction.CODEC.fieldOf("shapeFunction").forGetter(block -> block.shapeFunction)
            ).apply(instance, OrientableWallSideBlock::new)
    );
    public static final EnumProperty<OrientableBlockType> TYPE = ModBlockStateProperties.ORIENTABLE_BLOCK_TYPE;
    private final ReShapeFunction shapeFunction;

    public OrientableWallSideBlock(BaseBlockProperty property, ReShapeFunction shapeFunction) {
        super(property);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(TYPE, OrientableBlockType.LEFT)
                .setValue(FACING, Direction.NORTH)
                .setValue(WATERLOGGED, false));
        this.shapeFunction = shapeFunction;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = this.defaultBlockState();
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());

        Direction faceDir = pContext.getClickedFace();
        Direction[] lookDirs = pContext.getNearestLookingDirections();
        if (faceDir.getAxis().isHorizontal()) {
            state = state.setValue(FACING, faceDir)
                    .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            for (Direction dir : lookDirs) {
                if (dir.getAxis().isHorizontal() && dir != faceDir && dir != faceDir.getOpposite()) {
                    if (dir == faceDir.getOpposite().getClockWise())
                        state = state.setValue(TYPE, OrientableBlockType.LEFT);
                    if (dir == faceDir.getOpposite().getCounterClockWise())
                        state = state.setValue(TYPE, OrientableBlockType.RIGHT);
                }
            }
            return state;
        }
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shapeFunction.apply(pState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(TYPE);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
}
