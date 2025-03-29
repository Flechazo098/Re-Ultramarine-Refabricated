package org.voxelutopia.ultramarine.client.integration.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.init.registry.ModItems;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum MalletToggleableComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    public static final ResourceLocation MALLET_TOGGLEABLE = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "mallet_toggleable");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        IElementHelper helper = IElementHelper.get();

        // 添加木槌图标，使用正常大小
        tooltip.add(helper.item(ModItems.WOODEN_MALLET.getDefaultInstance()));

        // 添加文本说明
        tooltip.append(helper.text(Component.translatable("gui.jade.plugin_ultramarine.mallet_toggleable")));
    }

    @Override
    public ResourceLocation getUid() {
        return MALLET_TOGGLEABLE;
    }
}