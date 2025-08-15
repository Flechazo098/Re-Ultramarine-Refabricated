package com.voxelutopia.ultramarine.world.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.data.shape.BlockShapes;
import com.voxelutopia.ultramarine.data.shape.ShapeFunction;
import com.voxelutopia.ultramarine.world.block.state.ModBlockStateProperties;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
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

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
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
                    new Builder(new BaseBlockProperty(properties, BaseBlockProperty.BlockMaterial.STONE))
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
    private final ShapeFunction shapeFunction;
    private final boolean diagonallyPlaceable;
    private final boolean directional;
    private final boolean noCollision;
    private final boolean luminous;
    private final boolean noFenceConnect;
    private final @Nullable Direction offsetDirection;
    protected StateDefinition<Block, BlockState> stateDefinition;

    public DecorativeBlock(BaseBlockProperty property, ShapeFunction shapeFunction,
                           boolean directional, boolean diagonallyPlaceable,
                           boolean luminous, boolean noCollision, boolean noFenceConnect,
                           @Nullable Direction offset) {
        super(luminous ?
                property.properties.lightLevel((state) -> state.hasProperty(LIT) && state.getValue(LIT) ? 15 : 0) :
                property.properties);
        this.property = property;
        this.shapeFunction = shapeFunction;
        this.directional = directional;
        this.diagonallyPlaceable = diagonallyPlaceable;
        this.luminous = luminous;
        this.noCollision = noCollision;
        this.noFenceConnect = noFenceConnect;
        this.offsetDirection = offset;

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
        this(builder.property, builder.shapeFunction, builder.directional, builder.diagonallyPlaceable,
                builder.luminous, builder.noCollision, builder.noFenceConnect, builder.offset);
    }

    public static Builder with(BaseBlockProperty property) {
        return new Builder(property);
    }

    @Override
    public StateDefinition<Block, BlockState> getStateDefinition() {
        return stateDefinition;
    }

    @NotNull
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        if (isDirectional()) pBuilder.add(FACING);
        if (isDiagonallyPlaceable()) pBuilder.add(DIAGONAL);
        if (isDirectional() && isDiagonallyPlaceable()) pBuilder.add(HORIZONTAL_FACING_SHIFT);
        if (isLuminous()) pBuilder.add(LIT);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        /*
        if (offsetDirection != null) {
            switch (offsetDirection) {
                case DOWN -> {
                    if (!pLevel.getBlockState(pPos.above()).isAir() && pLevel.getBlockState(pPos.below()).isAir()) {
                        pLevel.removeBlock(pPos, pIsMoving);
                        pLevel.setBlock(pPos.below(), pState, Block.UPDATE_ALL);
                    }
                }
                case UP -> {
                    if (!pLevel.getBlockState(pPos.below()).isAir() && pLevel.getBlockState(pPos.above()).isAir()) {
                        pLevel.removeBlock(pPos, pIsMoving);
                        pLevel.setBlock(pPos.above(), pState, Block.UPDATE_ALL);
                    }
                }
            }
        }

         */
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
        return this.shapeFunction.apply(pState);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return noCollision ? Shapes.empty() : getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

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
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {
        public abstract T self();
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private final BaseBlockProperty property;
        private ShapeFunction shapeFunction = BlockShapes.S16_H16;
        private boolean diagonallyPlaceable;
        private boolean directional;
        private boolean luminous;
        private boolean noCollision;
        private boolean noFenceConnect;
        private Direction offset = null;

        public Builder(BaseBlockProperty property) {
            this.property = property.copy();
        }

        public Builder shaped(VoxelShape shape) {
            return shaped(ShapeFunction.simpleShape(shape));
        }

        public Builder shaped(ShapeFunction shapeFunction) {
            this.shapeFunction = shapeFunction;
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

        public Builder directional() {
            this.directional = true;
            return this;
        }

        public Builder directional(boolean directional) {
            this.directional = directional;
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

        public Builder noOcclusion() {
            this.property.properties.noOcclusion();
            return this;
        }
        public Builder noCollision(boolean noCollision) {
            this.noCollision = noCollision;
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

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return noFenceConnect ? BlockShapes.S16_H12.apply(pState) : super.getBlockSupportShape(pState, pReader, pPos);
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0f;
    }
}
