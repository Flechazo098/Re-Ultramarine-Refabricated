package org.voxelutopia.ultramarine.client.integration.jade;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.inventory.BrickKilnCombinedStorage;
import org.voxelutopia.ultramarine.common.tile.BrickKilnBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

public enum BrickKilnComponent implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private BrickKilnComponent() {
    }

    public static final ResourceLocation BRICK_KILN = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "brick_kiln");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag data = blockAccessor.getServerData();
        if (data.contains("CookTime")) {
            int progress = data.getInt("CookTime");
            ListTag items = data.getList("Items", 10);
            NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
            for (int i = 0; i < items.size(); ++ i) {
                inventory.set(i, ItemStack.parseOptional(blockAccessor.getLevel().registryAccess(), items.getCompound(i)));
            }
            IElementHelper helper = IElementHelper.get();
            int total = data.getInt("CookTimeTotal");
            float progressRatio = (float)progress / (float)total;

            // 修改布局以更好地显示物品和进度条
            IElement primaryInput = helper.item(inventory.get(0));
            IElement secondaryInput = helper.item(inventory.get(1));
            IElement fuel = helper.item(inventory.get(2));
            IElement result = helper.item(inventory.get(3));

            // 使用自定义进度条样式
            ResourceLocation arrowBase = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "textures/gui/arrow_base.png");
            ResourceLocation arrowProgress = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "textures/gui/arrow_progress.png");

            // 尝试使用纹理进度条，如果纹理不存在则使用默认进度条
            IElement progressBar;
            if (arrowBase != null && arrowProgress != null) {
                progressBar = helper.progress(
                        progressRatio,
                        arrowBase,
                        arrowProgress,
                        22, // 箭头宽度
                        16, // 箭头高度
                        false // 进度是否可以减少
                ).translate(new Vec2(0, 0));
            } else {
                progressBar = helper.progress(progressRatio).translate(new Vec2(0, 0));
            }

            // 添加物品和进度条到工具提示
            tooltip.add(primaryInput);
            tooltip.append(helper.spacer(2, 0));
            tooltip.append(secondaryInput);
            tooltip.append(helper.spacer(4, 0));
            tooltip.append(progressBar);
            tooltip.append(helper.spacer(4, 0));
            tooltip.append(result);

            // 如果有燃料，在下一行显示
            if (!inventory.get(2).isEmpty()) {
                tooltip.add(helper.spacer(16, 0));
                tooltip.append(fuel);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BlockEntity entity = blockAccessor.getBlockEntity();
        if (entity instanceof BrickKilnBlockEntity brickKiln) {
            // 从 BrickKilnBlockEntity 获取物品栏内容
            // 使用 wrapHandlers() 方法获取 BrickKilnCombinedStorage
            BrickKilnCombinedStorage storage = brickKiln.wrapHandlers();

            // 从存储中获取物品
            ItemStack primary = storage.getPrimaryInput().getItem(0);
            ItemStack secondary = storage.getSecondaryInput().getItem(0);
            ItemStack fuel = storage.getFuel().getItem(0);
            ItemStack result = storage.getResult().getItem(0);

            if (primary.isEmpty() && secondary.isEmpty() && fuel.isEmpty() && result.isEmpty()) return;

            ListTag items = new ListTag();
            // 使用 saveOptional 方法替代 save
            items.add(primary.saveOptional(blockAccessor.getLevel().registryAccess()));
            items.add(secondary.saveOptional(blockAccessor.getLevel().registryAccess()));
            items.add(fuel.saveOptional(blockAccessor.getLevel().registryAccess()));
            items.add(result.saveOptional(blockAccessor.getLevel().registryAccess()));
            compoundTag.put("Items", items);

            // 修改这里，传入 registryAccess 作为参数
            CompoundTag kilnTag = brickKiln.saveWithoutMetadata(blockAccessor.getLevel().registryAccess());
            compoundTag.putInt("CookTime", kilnTag.getInt("CookTime"));
            compoundTag.putInt("CookTimeTotal", kilnTag.getInt("CookTimeTotal"));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return BRICK_KILN;
    }
}