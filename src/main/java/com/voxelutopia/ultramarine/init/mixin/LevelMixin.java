package com.voxelutopia.ultramarine.init.mixin;

import com.voxelutopia.ultramarine.init.event.BlockEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@Mixin(Level.class)
public class LevelMixin {

    @Inject(method = "updateNeighborsAt", at = @At("HEAD"), cancellable = true)
    private void onUpdateNeighborsAt(BlockPos pos, Block block, CallbackInfo ci) {
        Level self = (Level) (Object) this;

        boolean allowed = BlockEvents.NEIGHBOR_NOTIFY.invoker().onNeighborNotify(
                self,
                pos,
                self.getBlockState(pos),
                EnumSet.allOf(Direction.class),
                false
        );

        if (!allowed) ci.cancel();
    }
}
