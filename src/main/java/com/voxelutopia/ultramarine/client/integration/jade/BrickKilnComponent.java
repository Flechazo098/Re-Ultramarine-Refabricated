package com.voxelutopia.ultramarine.client.integration.jade;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.common.tile.BrickKilnBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
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

        tooltip.add(helper.item(inventory.get(0)));

        tooltip.append(helper.item(inventory.get(1)));

        tooltip.append(helper.spacer(4, 0));

        tooltip.append(helper.progress((float) cookProgress / cookTotal).translate(new Vec2(-2, 0)));

        tooltip.append(helper.item(inventory.get(3)));

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

        ListTag items = new ListTag();
        items.add(kiln.getItem(BrickKilnBlockEntity.SLOT_INPUT_PRIMARY).saveOptional(accessor.getLevel().registryAccess()));
        items.add(kiln.getItem(BrickKilnBlockEntity.SLOT_INPUT_SECONDARY).saveOptional(accessor.getLevel().registryAccess()));
        items.add(kiln.getItem(BrickKilnBlockEntity.SLOT_FUEL).saveOptional(accessor.getLevel().registryAccess()));
        items.add(kiln.getItem(BrickKilnBlockEntity.SLOT_RESULT).saveOptional(accessor.getLevel().registryAccess()));
        data.put("kiln", items);

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