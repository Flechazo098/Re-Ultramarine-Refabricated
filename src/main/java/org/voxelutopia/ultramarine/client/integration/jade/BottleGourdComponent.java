package org.voxelutopia.ultramarine.client.integration.jade;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.alchemy.PotionContents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.tile.BottleGourdBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElementHelper;

import java.util.Optional;

public enum BottleGourdComponent implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(BottleGourdComponent.class);
    public static final ResourceLocation BOTTLE_GOURD = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "bottle_gourd");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig config) {
        LOGGER.debug("开始处理葫芦瓶工具提示，位置：{}", blockAccessor.getPosition());

        try {
            CompoundTag data = blockAccessor.getServerData();
            if (data.contains("BottleGourd", 10)) {
                CompoundTag tag = data.getCompound("BottleGourd");
                int charges = tag.getInt("Charges");
                ResourceLocation potionId = ResourceLocation.parse(tag.getString("Potion"));
                LOGGER.debug("获取到葫芦瓶数据 - 药水ID: {}, 剩余次数: {}", potionId, charges);

                // 创建药水物品并设置正确的药水类型
                ItemStack potionStack = new ItemStack(Items.POTION);
                var potionHolder = BuiltInRegistries.POTION.getHolder(potionId).orElseThrow();

                // 修复类型不匹配问题 - 直接使用potionHolder而不是Optional包装
                potionStack.set(DataComponents.POTION_CONTENTS,
                        new PotionContents(potionHolder));

                LOGGER.debug("已设置药水物品组件数据");

                // 获取药水名称 - 这里仍然使用Optional是因为Potion.getName方法需要Optional参数
                String potionKey = Potion.getName(Optional.of(potionHolder), "item.minecraft.potion.effect.");
                Component potionName = Component.translatable(potionKey);
                LOGGER.debug("解析药水名称成功: {} -> {}", potionKey, potionName.getString());

                IElementHelper helper = IElementHelper.get();
                IThemeHelper themeHelper = IThemeHelper.get();

                // 添加药水图标
                LOGGER.trace("添加药水图标");
                tooltip.add(helper.smallItem(potionStack));

                // 添加药水名称和数量
                LOGGER.trace("构建文本组件");
                tooltip.append(potionName);
                tooltip.append(helper.spacer(5, 0));
                tooltip.append(themeHelper.info(charges + "/" + BottleGourdBlockEntity.MAX_CHARGE));
            } else {
                LOGGER.debug("未找到葫芦瓶数据");
            }
        } catch (Exception e) {
            LOGGER.error("处理葫芦瓶工具提示时发生错误", e);
        }

        LOGGER.debug("工具提示处理完成");
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {
        LOGGER.debug("开始收集服务器端数据，位置：{}", blockAccessor.getPosition());

        try {
            if (blockAccessor.getBlockEntity() instanceof BottleGourdBlockEntity gourd) {
                LOGGER.debug("找到葫芦瓶实体，是否包含次数：{}", gourd.hasCharges());

                if (gourd.hasCharges()) {
                    CompoundTag compound = new CompoundTag();
                    int charges = gourd.getCharges();
                    ResourceLocation potionId = BuiltInRegistries.POTION.getKey(gourd.getPotion());

                    compound.putInt("Charges", charges);
                    compound.putString("Potion", potionId.toString());
                    tag.put("BottleGourd", compound);

                    LOGGER.info("发送葫芦瓶数据 - 药水: {}, 次数: {}/{}",
                            potionId, charges, BottleGourdBlockEntity.MAX_CHARGE);
                }
            } else {
                LOGGER.warn("目标方块实体不是葫芦瓶: {}", blockAccessor.getBlockEntity());
            }
        } catch (Exception e) {
            LOGGER.error("收集葫芦瓶数据时发生错误", e);
        }

        LOGGER.debug("服务器数据收集完成");
    }

    @Override
    public ResourceLocation getUid() {
        LOGGER.trace("获取组件UID: {}", BOTTLE_GOURD);
        return BOTTLE_GOURD;
    }
}