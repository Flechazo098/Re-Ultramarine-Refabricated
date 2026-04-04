package com.voxelutopia.ultramarine.init.mixin;

import com.voxelutopia.ultramarine.init.event.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.redstone.Orientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {

    @Inject(method = "updateNeighborsAt", at = @At("HEAD"), cancellable = true)
    private void onUpdateNeighborsAt(BlockPos pos, Block block, CallbackInfo ci) {
        ServerLevel self = (ServerLevel) (Object) this;

        boolean allowed = BlockEvents.NEIGHBOR_NOTIFY.invoker().onNeighborNotify(
                self,
                pos,
                self.getBlockState(pos),
                EnumSet.allOf(Direction.class),
                false
        );

        if (!allowed) {
            ci.cancel();
        }
    }

    @Inject(method = "updateNeighborsAtExceptFromFacing", at = @At("HEAD"), cancellable = true)
    private void onUpdateNeighbors(BlockPos pos, Block blockObject, Direction skipDirection, Orientation orientation, CallbackInfo ci) {
        ServerLevel self = (ServerLevel) (Object) this;

        EnumSet<Direction> directions = EnumSet.allOf(Direction.class);
        directions.remove(skipDirection);

        boolean allowed = BlockEvents.NEIGHBOR_NOTIFY.invoker().onNeighborNotify(
                self,
                pos,
                self.getBlockState(pos),
                directions,
                false
        );

        if (!allowed) {
            ci.cancel();
        }
    }
}
