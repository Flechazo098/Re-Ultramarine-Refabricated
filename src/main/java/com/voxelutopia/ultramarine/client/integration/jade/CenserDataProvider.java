package com.voxelutopia.ultramarine.client.integration.jade;

import com.voxelutopia.ultramarine.common.tile.CenserBlockEntity;
import net.minecraft.nbt.CompoundTag;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum CenserDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        if (blockAccessor.getBlockEntity() instanceof CenserBlockEntity censer) {
            compoundTag.putInt("BurnTime", censer.getRemainingTime());
        }
    }

    @Override
    public net.minecraft.resources.Identifier getUid() {
        return CenserComponent.CENSER_BURN_TIME;
    }
}

