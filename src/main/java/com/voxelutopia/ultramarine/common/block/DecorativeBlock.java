package com.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.common.block.state.ModBlockStateProperties;
import com.voxelutopia.ultramarine.init.data.shape.BlockShapes;
import com.voxelutopia.ultramarine.init.data.shape.ReShapeFunction;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@MethodsReturnNonnullByDefault
public class DecorativeBlock extends HorizontalDirectionalBlock implements BaseBlockPropertyHolder, DiagonallyPlaceable {

    public static final MapCodec<DecorativeBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Properties.CODEC.fieldOf("properties").forGetter(block -> block.properties),
                    Codec.BOOL.fieldOf("directional").forGetter(block -> block.directional),
                    Codec.BOOL.fieldOf("diagonallyPlaceable").forGetter(block -> block.diagonallyPlaceable),
                    Codec.BOOL.fieldOf("luminous").forGetter(block -> block.luminous),
                    Codec.BOOL.fieldOf("noCollision").forGetter(block -> block.noCollision),
                    Codec.BOOL.fieldOf("noFenceConnect").forGetter(block -> block.noFenceConnect)
            ).apply(instance, (properties, directional, diagonallyPlaceable, luminous, noCollision, noFenceConnect) ->
                    new DecorativeBlock.Builder(new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE))
                            .directional(directional)
                            .diagonallyPlaceable(diagonallyPlaceable)
                            .luminous(luminous)
                            .noCollision(noCollision)
                            .noFenceConnect(noFenceConnect)
                            .build()
            ));

    public static final DirectionProperty HORIZONTAL_FACING_SHIFT = ModBlockStateProperties.HORIZONTAL_FACING_SHIFT;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    private final BaseBlockProperty property;
    private final ReShapeFunction shape;
    private final boolean diagonallyPlaceable;
    private final boolean directional;
    private final boolean noCollision;
    private final boolean luminous;
    private final boolean noFenceConnect;
    protected StateDefinition<Block, BlockState> stateDefinition;

    public DecorativeBlock(BaseBlockProperty property, ReShapeFunction shape,
                           boolean directional, boolean diagonallyPlaceable,
                           boolean luminous, boolean noCollision, boolean noFenceConnect,
                           @Nullable Direction offset) {
        super(luminous ?
                property.properties.lightLevel((state) -> state.hasProperty(LIT) && state.getValue(LIT) ? 15 : 0) :
                property.properties);
        this.property = property;
        this.shape = shape;
        this.directional = directional;
        this.diagonallyPlaceable = diagonallyPlaceable;
        this.luminous = luminous;
        this.noCollision = noCollision;
        this.noFenceConnect = noFenceConnect;

        var stateDefinationBuilder = new StateDefinition.Builder<Block, BlockState>(this);
        createBlockStateDefinition(stateDefinationBuilder);
        stateDefinition = stateDefinationBuilder.create(Block::defaultBlockState, BlockState::new);
        BlockState state = this.getStateDefinition().any();
        if (isDiagonallyPlaceable()) state = state.setValue(DIAGONAL, false);
        if (isDirectional()) state = state.setValue(FACING, Direction.NORTH);
        if (isDiagonallyPlaceable() && isDirectional())
            state = state.setValue(HORIZONTAL_FACING_SHIFT, Direction.NORTH);
        if (isLuminous()) state = state.setValue(LIT, true);
        this.registerDefaultState(state);
    }

    public DecorativeBlock(Builder builder) {
        this(builder.property, builder.shape, builder.directional, builder.diagonallyPlaceable,
                builder.luminous, builder.noCollision, builder.noFenceConnect, builder.offset);
    }

//    protected DecorativeBlock(BlockBehaviour.Properties properties) {
//        super(properties);
//        this.property = new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE);
//        this.shape = BlockShapes.S16_H16;
//        this.directional = false;
//        this.diagonallyPlaceable = false;
//        this.luminous = false;
//        this.noCollision = false;
//        this.noFenceConnect = false;
//        this.offsetDirection = null;
//
//        var stateDefinationBuilder = new StateDefinition.Builder<Block, BlockState>(this);
//        createBlockStateDefinition(stateDefinationBuilder);
//        stateDefinition = stateDefinationBuilder.create(Block::defaultBlockState, BlockState::new);
//        this.registerDefaultState(this.stateDefinition.any());
//    }

    public static Builder with(BaseBlockProperty property) {
        return new Builder(property);
    }

    @Override
    public StateDefinition<Block, BlockState> getStateDefinition() {
        return stateDefinition;
    }

    @NotNull
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        BlockState state = setDiagonalStateForPlacement(this.defaultBlockState(), pContext);
        if (isDirectional() && isDiagonallyPlaceable()) {
            var directions = getMainAndShiftedDirections(pContext);
            state = state.setValue(FACING, directions.getLeft()).setValue(HORIZONTAL_FACING_SHIFT, directions.getRight())
                    .setValue(DIAGONAL, getDiagonalState(pContext));
        } else if (isDirectional() && !isDiagonallyPlaceable()) {
            state = state.setValue(FACING, pContext.getHorizontalDirection().getOpposite());
        } else if (!isDirectional() && isDiagonallyPlaceable()) {
            state = state.setValue(DIAGONAL, getDiagonalState(pContext));
        }
        if (isLuminous()) {
            state = state.setValue(LIT, true);
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        if (isDirectional()) pBuilder.add(FACING);
        if (isDiagonallyPlaceable()) pBuilder.add(DIAGONAL);
        if (isDirectional() && isDiagonallyPlaceable()) pBuilder.add(HORIZONTAL_FACING_SHIFT);
        if (isLuminous()) pBuilder.add(LIT);
    }

    @Override
    public void onPlace(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pOldState, boolean pIsMoving) {
//        if (offsetDirection != null) {
//            switch (offsetDirection) {
//                case DOWN -> {
//                    if (!pLevel.getBlockState(pPos.above()).isAir() && pLevel.getBlockState(pPos.below()).isAir()) {
//                        pLevel.removeBlock(pPos, pIsMoving);
//                        pLevel.setBlock(pPos.below(), pState, Block.UPDATE_ALL);
//                    }
//                }
//                case UP -> {
//                    if (!pLevel.getBlockState(pPos.below()).isAir() && pLevel.getBlockState(pPos.above()).isAir()) {
//                        pLevel.removeBlock(pPos, pIsMoving);
//                        pLevel.setBlock(pPos.above(), pState, Block.UPDATE_ALL);
//                    }
//                }
//            }
//        }
        //todo add config or toggle
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        BlockState newState = pState;
        if (pState.getBlock() instanceof DecorativeBlock decorativeBlock && decorativeBlock.isDirectional()) {
            newState = pState.setValue(FACING, pRot.rotate(pState.getValue(FACING)));
            if (decorativeBlock.isDiagonallyPlaceable()) {
                newState = newState.setValue(HORIZONTAL_FACING_SHIFT, pRot.rotate(pState.getValue(HORIZONTAL_FACING_SHIFT)));
            }
        }
        return newState;
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        BlockState newState = pState;
        if (pState.getBlock() instanceof DecorativeBlock decorativeBlock && decorativeBlock.isDirectional()) {
            newState = pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
            if (decorativeBlock.isDiagonallyPlaceable()) {
                newState = newState.rotate(pMirror.getRotation(pState.getValue(HORIZONTAL_FACING_SHIFT)));
            }
        }
        return newState;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.shape.apply(pState);
    }

    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return noCollision ? Shapes.empty() : getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

//    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
//        if (isLuminous()) return state.getValue(LIT) ? 14 : 0;
//        else return 0;
//    }

    @Override
    public BaseBlockProperty getProperty() {
        return property;
    }

    @Override
    public boolean isDiagonallyPlaceable() {
        return diagonallyPlaceable;
    }

    public boolean isLuminous() {
        return luminous;
    }

    public boolean isDirectional() {
        return directional;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return noFenceConnect ? BlockShapes.S16_H12.apply(pState) : super.getBlockSupportShape(pState, pReader, pPos);
    }

    @Override
    public float getShadeBrightness(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return 1.0f;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {
        public abstract T self();
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private final BaseBlockProperty property;
        private ReShapeFunction shape = BlockShapes.S16_H16;
        private boolean diagonallyPlaceable;
        private boolean directional;
        private boolean luminous;
        private boolean noCollision;
        private boolean noFenceConnect;
        private Direction offset = null;

        public Builder(BaseBlockProperty property) {
            this.property = property.copy();
        }

        public Builder properties(BlockBehaviour.Properties properties) {
            this.property.properties = properties;
            return this;
        }

        public Builder shaped(VoxelShape shape) {
            return shaped(ReShapeFunction.simpleShape(shape));
        }

        public Builder shaped(ReShapeFunction shape) {
            this.shape = shape;
            return this;
        }

        public Builder directional() {
            this.directional = true;
            return this;
        }

        public Builder directional(boolean directional) {
            this.directional = directional;
            return this;
        }

        public Builder diagonallyPlaceable() {
            this.diagonallyPlaceable = true;
            return this;
        }

        public Builder diagonallyPlaceable(boolean diagonallyPlaceable) {
            this.diagonallyPlaceable = diagonallyPlaceable;
            return this;
        }

        public Builder luminous() {
            this.luminous = true;
            // 确保在这里设置lightLevel
            this.property.properties = this.property.properties.lightLevel(
                    (state) -> state.hasProperty(LIT) && state.getValue(LIT) ? 15 : 0
            );
            return this;
        }

        public Builder luminous(boolean luminous) {
            this.luminous = luminous;
            if (luminous) {
                // 确保在这里设置lightLevel
                this.property.properties = this.property.properties.lightLevel(
                        (state) -> state.hasProperty(LIT) && state.getValue(LIT) ? 15 : 0
                );
            }
            return this;
        }

        public Builder noCollision() {
            this.noCollision = true;
            return this;
        }

        public Builder noCollision(boolean noCollision) {
            this.noCollision = noCollision;
            return this;
        }

        public Builder noOcclusion() {
            this.property.properties.noOcclusion();
            return this;
        }

        public Builder pushReaction(PushReaction reaction) {
            this.property.properties.pushReaction(reaction);
            return this;
        }

        public Builder noFenceConnect() {
            noFenceConnect = true;
            return this;
        }

        public Builder noFenceConnect(boolean noFenceConnect) {
            this.noFenceConnect = noFenceConnect;
            return this;
        }

        public Builder placeOffset(Direction direction) {
            offset = direction;
            return this;
        }

        public DecorativeBlock build() {
            return new DecorativeBlock(this);
        }

        @Override
        public Builder self() {
            return this;
        }
    }
}
