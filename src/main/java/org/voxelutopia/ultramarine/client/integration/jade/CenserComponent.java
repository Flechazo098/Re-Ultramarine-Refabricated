package org.voxelutopia.ultramarine.client.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.tile.CenserBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum CenserComponent implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    public static final ResourceLocation CENSER_BURN_TIME = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "censer_burn_time");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getServerData().contains("BurnTime")) {
            int burnTime = blockAccessor.getServerData().getInt("BurnTime");
            if (burnTime > 0){
                tooltip.add(Component.translatable("gui.jade.plugin_ultramarine.censer_burn_time", burnTime / 20));
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return CENSER_BURN_TIME;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        CenserBlockEntity censer = (CenserBlockEntity) blockAccessor.getBlockEntity();
        compoundTag.putInt("BurnTime", censer.getRemainingTime());
    }
}
