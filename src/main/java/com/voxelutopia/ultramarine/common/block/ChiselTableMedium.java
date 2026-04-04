package com.voxelutopia.ultramarine.common.block;

import com.voxelutopia.ultramarine.common.menu.ChiselTableMenu;
import com.voxelutopia.ultramarine.init.data.shape.BlockShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ChiselTableMedium extends DecorativeBlock implements BaseBlockPropertyHolder {

    private static final Component CONTAINER_TITLE = Component.translatable("container.chisel_table");

    public ChiselTableMedium() {
        super(DecorativeBlock.with(BaseBlockProperty.paper()).shaped(BlockShapes.S16_H4).directional().noCollision().noOcclusion());
    }

    @Override
    public InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        if (pLevel.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            pPlayer.openMenu(this.getMenuProvider(pState, pLevel, pPos));
            return InteractionResult.CONSUME;
        }
    }

    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider((containerId, inventory, player) ->
                new ChiselTableMenu(containerId, inventory, ContainerLevelAccess.create(pLevel, pPos)), CONTAINER_TITLE);
    }

    @Override
    public BaseBlockProperty getProperty() {
        return BaseBlockProperty.wood();
    }
}
