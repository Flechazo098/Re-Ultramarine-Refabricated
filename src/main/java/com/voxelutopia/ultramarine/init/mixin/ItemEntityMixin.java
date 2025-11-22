package com.voxelutopia.ultramarine.init.mixin;

import com.voxelutopia.ultramarine.init.event.ItemEvents;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    private int age;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ItemEntity self = (ItemEntity)(Object)this;
        if (!self.level().isClientSide() && ItemEvents.UPDATE_EVENT.invoker().onItemUpdate(self)) {
            this.age = 0;
        }
    }
}
