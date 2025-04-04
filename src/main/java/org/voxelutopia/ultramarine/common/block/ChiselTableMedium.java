package org.voxelutopia.ultramarine.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.voxelutopia.ultramarine.common.tile.ChiselTableBlockEntity;
import org.voxelutopia.ultramarine.init.data.shape.BlockShapes;

public class ChiselTableMedium extends DecorativeBlock implements BaseBlockPropertyHolder, EntityBlock {

    public ChiselTableMedium() {
        super(DecorativeBlock.with(BaseBlockProperty.PAPER).shaped(BlockShapes.S16_H4).directional().noCollision().noOcclusion());
    }

    @Override
    public InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            // 获取方块实体
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ChiselTableBlockEntity) {
                pPlayer.openMenu((ChiselTableBlockEntity) blockEntity);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ChiselTableBlockEntity(pos, state);
    }

    @Override
    public BaseBlockProperty getProperty() {
        return BaseBlockProperty.WOOD;
    }
}
