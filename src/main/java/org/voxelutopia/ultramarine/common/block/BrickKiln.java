package org.voxelutopia.ultramarine.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.tile.BlockEntityHelper;
import org.voxelutopia.ultramarine.common.tile.BrickKilnBlockEntity;
import org.voxelutopia.ultramarine.init.registry.ModBlockEntities;

public class BrickKiln extends DecorativeBlock implements EntityBlock, BaseBlockPropertyHolder {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public BrickKiln() {
        super(DecorativeBlock.with(BaseBlockProperty.STONE)
                .shaped(Block.box(0,0,0, 16, 15, 16))
                .directional()
                .noOcclusion()
                .properties(BaseBlockProperty.STONE.properties.lightLevel(
                        (state) -> state.hasProperty(LIT) && state.getValue(LIT) ? 15 : 0
                )));

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, Boolean.FALSE));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BrickKilnBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null :
                BlockEntityHelper.createTickerHelper(pBlockEntityType, (BlockEntityType<? extends BrickKilnBlockEntity>) ModBlockEntities.BRICK_KILN, BrickKilnBlockEntity::serverTick);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
//            Ultramarine.LOGGER.debug("[客户端] 点击砖窑方块，位置：{}", pPos);
            return InteractionResult.SUCCESS;
        } else {
//            Ultramarine.LOGGER.info("[服务端] 玩家 {} 尝试打开砖窑 GUI，位置：{}", pPlayer.getName().getString(), pPos);

            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof BrickKilnBlockEntity) {
                MenuProvider menuProvider = pState.getMenuProvider(pLevel, pPos);

                if (menuProvider == null) {
                    Ultramarine.LOGGER.error("无法获取菜单提供器，位置：{}", pPos);
                    return InteractionResult.FAIL;
                }

                Ultramarine.LOGGER.debug("成功获取菜单提供器，准备打开界面...");
                pPlayer.openMenu(menuProvider);
                return InteractionResult.CONSUME;
            } else {
                Ultramarine.LOGGER.warn("在位置 {} 未找到砖窑方块实体", pPos);
            }
        }
        return InteractionResult.PASS;
    }

    // 添加菜单提供器方法
    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof BrickKilnBlockEntity furnace) {
                if (pLevel instanceof ServerLevel) {
                    // 直接使用 wrapHandlers() 返回的 FabricItemStorage，因为它应该实现了 Container 接口
                    Containers.dropContents(pLevel, pPos, furnace.wrapHandlers());
                    furnace.getRecipesToAwardAndPopExperience((ServerLevel)pLevel, Vec3.atCenterOf(pPos));
                }
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRand) {
        if (pState.getValue(LIT)) {
            double d0 = (double)pPos.getX() + 0.5D;
            double d1 = pPos.getY();
            double d2 = (double)pPos.getZ() + 0.5D;
            if (pRand.nextDouble() < 0.1D) {
                pLevel.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false); //todo custom sound event
            }

            Direction direction = pState.getValue(FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d3 = 0.52D;
            double d4 = pRand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
            double d6 = pRand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
            pLevel.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            pLevel.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(LIT); // 确保添加LIT属性

    }

    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public BaseBlockProperty getProperty() {
        return BaseBlockProperty.STONE;
    }

}