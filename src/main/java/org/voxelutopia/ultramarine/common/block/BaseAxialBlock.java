package org.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BaseAxialBlock extends Block implements AxialBlock, BaseBlockPropertyHolder {

    public static final MapCodec<BaseAxialBlock> CODEC = simpleCodec((properties) ->
            new BaseAxialBlock(new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE)));

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected final BaseBlockProperty property;

    public BaseAxialBlock(BaseBlock block) {
        this(block.getProperty());
    }

    public BaseAxialBlock(BaseBlockProperty property) {
        super(property.properties);
        this.property = property;
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    protected BaseAxialBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.property = new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(AXIS, pContext.getHorizontalDirection().getAxis());
    }

    @Override
    public BlockState rotate (BlockState pState, Rotation pRot) {
        BlockState newState = pState;
        if (pRot == Rotation.CLOCKWISE_90 || pRot == Rotation.COUNTERCLOCKWISE_90) {
            newState = switch (pState.getValue(AXIS)) {
                case X -> pState.setValue(AXIS, Direction.Axis.Z);
                case Z -> pState.setValue(AXIS, Direction.Axis.X);
                default -> newState;
            };
        }
        return newState;
    }

    @Override
    public BaseBlockProperty getProperty() {
        return property;
    }

    @Override
    public Direction.Axis getAxis(BlockState pState) {
        return pState.getValue(AXIS);
    }

    @Override
    protected MapCodec<BaseAxialBlock> codec() {
        return CODEC;
    }
}