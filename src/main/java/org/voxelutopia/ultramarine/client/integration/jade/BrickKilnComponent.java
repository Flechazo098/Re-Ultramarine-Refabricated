package org.voxelutopia.ultramarine.client.integration.jade;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.tile.BrickKilnBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

public enum BrickKilnComponent implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

    INSTANCE;

    public static final ResourceLocation BRICK_KILN = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "brick_kiln");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        CompoundTag data = accessor.getServerData();
        if (!data.contains("cookProgress")) {
            return;
        }

        int cookProgress = data.getInt("cookProgress");
        int cookTotal = data.getInt("cookTotal");

        ListTag kilnItems = data.getList("kiln", Tag.TAG_COMPOUND);
        NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
        for (int i = 0; i < kilnItems.size(); i++)
            inventory.set(i, ItemStack.parseOptional(accessor.getLevel().registryAccess(), kilnItems.getCompound(i)));

        IElementHelper helper = IElementHelper.get();

        // 模仿熔炉的显示方式，将所有元素放在同一行
        // 主要输入物品
        tooltip.add(helper.item(inventory.get(0)));

        // 次要输入物品
        tooltip.append(helper.item(inventory.get(1)));

        // 添加间隔
        tooltip.append(helper.spacer(4, 0));

        // 添加进度条
        tooltip.append(helper.progress((float) cookProgress / cookTotal).translate(new Vec2(-2, 0)));

        // 添加输出物品
        tooltip.append(helper.item(inventory.get(3)));

        // 添加燃料信息（可选，如果需要）
        if (!inventory.get(2).isEmpty()) {
            tooltip.add(helper.item(inventory.get(2)));
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof BrickKilnBlockEntity kiln)) {
            return;
        }

        if (kiln.isEmpty()) {
            return;
        }

        // 保存物品栏数据
        ListTag items = new ListTag();
        items.add(kiln.getItem(BrickKilnBlockEntity.SLOT_INPUT_PRIMARY).saveOptional(accessor.getLevel().registryAccess()));
        items.add(kiln.getItem(BrickKilnBlockEntity.SLOT_INPUT_SECONDARY).saveOptional(accessor.getLevel().registryAccess()));
        items.add(kiln.getItem(BrickKilnBlockEntity.SLOT_FUEL).saveOptional(accessor.getLevel().registryAccess()));
        items.add(kiln.getItem(BrickKilnBlockEntity.SLOT_RESULT).saveOptional(accessor.getLevel().registryAccess()));
        data.put("kiln", items);

        // 保存进度数据
        data.putInt("cookProgress", kiln.dataAccess.get(BrickKilnBlockEntity.DATA_COOKING_PROGRESS));
        data.putInt("cookTotal", kiln.dataAccess.get(BrickKilnBlockEntity.DATA_COOKING_TOTAL_TIME));
        data.putInt("litProgress", kiln.dataAccess.get(BrickKilnBlockEntity.DATA_LIT_TIME));
        data.putInt("litTotal", kiln.dataAccess.get(BrickKilnBlockEntity.DATA_LIT_DURATION));
    }

    @Override
    public ResourceLocation getUid() {
        return BRICK_KILN;
    }
}