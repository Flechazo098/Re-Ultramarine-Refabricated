package com.voxelutopia.ultramarine.init.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.voxelutopia.ultramarine.init.event.BlockEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {
    @ModifyExpressionValue(
            method = "place",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/BlockItem;getPlacementState(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private BlockState ultramarine$modifyPlacementState(BlockState original, BlockPlaceContext context) {
        BlockState modified = BlockEvents.PLACE.invoker().placeBlock(context.getLevel(), context.getClickedPos(), original, context.getPlayer());
        return modified != null ? modified : original;
    }
}
