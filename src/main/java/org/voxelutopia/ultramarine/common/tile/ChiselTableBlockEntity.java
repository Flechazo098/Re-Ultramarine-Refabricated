package org.voxelutopia.ultramarine.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.inventory.ChiselTableCombinedStorage;
import org.voxelutopia.ultramarine.common.menu.ChiselTableMenu;
import org.voxelutopia.ultramarine.init.registry.ModBlockEntities;

public class ChiselTableBlockEntity extends BlockEntity implements MenuProvider {

    private static final Component CONTAINER_TITLE = Component.translatable("container.chisel_table");
    private final ChiselTableCombinedStorage storage = new ChiselTableCombinedStorage();

    public ChiselTableBlockEntity (BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CHISEL_TABLE, blockPos, blockState);

        this.storage.setBlockEntity(this);
    }

    @Override
    public Component getDisplayName () {
        return CONTAINER_TITLE;
    }

    @Override
    public AbstractContainerMenu createMenu (int containerId, Inventory inventory, Player player) {
        return new ChiselTableMenu(containerId, inventory, this.storage);
    }

    public ChiselTableCombinedStorage getStorage () {
        return storage;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveAdditional(pTag, provider);

        // 1. 保存物品 - 直接从storage中获取
        NonNullList<ItemStack> items = NonNullList.withSize(7, ItemStack.EMPTY);

        // 获取各个槽位的物品
        ItemStack material = storage.getMaterial().getItem(0);
        ItemStack template = storage.getTemplate().getItem(0);
        ItemStack[] colors = new ItemStack[4];
        for (int i = 0; i < 4; i++) {
            colors[i] = storage.getColor(i).getItem(0);
        }
        ItemStack result = storage.getResult().getItem(0);

        // 设置到NonNullList中
        items.set(0, material);
        items.set(1, template);
        for (int i = 0; i < 4; i++) {
            items.set(2 + i, colors[i]);
        }
        items.set(6, result);

        // 确保物品正确保存
        ContainerHelper.saveAllItems(pTag, items, provider);

        // 添加详细的调试日志
        Ultramarine.LOGGER.debug("雕刻桌保存物品详情:");
        Ultramarine.LOGGER.debug("- 材料: {}", material.isEmpty() ? "空" : material.getItem().getDescriptionId() + " x" + material.getCount());
        Ultramarine.LOGGER.debug("- 模板: {}", template.isEmpty() ? "空" : template.getItem().getDescriptionId() + " x" + template.getCount());
        for (int i = 0; i < 4; i++) {
            Ultramarine.LOGGER.debug("- 颜色 {}: {}", i + 1, colors[i].isEmpty() ? "空" : colors[i].getItem().getDescriptionId() + " x" + colors[i].getCount());
        }
        Ultramarine.LOGGER.debug("- 结果: {}", result.isEmpty() ? "空" : result.getItem().getDescriptionId() + " x" + result.getCount());
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);

        // 1. 加载物品
        NonNullList<ItemStack> items = NonNullList.withSize(7, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, items, provider);

        // 2. 直接设置到storage对象的各个槽位中
        storage.getMaterial().setItem(0, items.get(0).copy());
        storage.getTemplate().setItem(0, items.get(1).copy());
        for (int i = 0; i < 4; i++) {
            storage.getColor(i).setItem(0, items.get(2 + i).copy());
        }
        storage.getResult().setItem(0, items.get(6).copy());

        // 3. 添加调试日志
        Ultramarine.LOGGER.debug("雕刻桌加载物品详情:");
        Ultramarine.LOGGER.debug("- 材料: {}", items.get(0).isEmpty() ? "空" : items.get(0).getItem().getDescriptionId() + " x" + items.get(0).getCount());
        Ultramarine.LOGGER.debug("- 模板: {}", items.get(1).isEmpty() ? "空" : items.get(1).getItem().getDescriptionId() + " x" + items.get(1).getCount());
        for (int i = 0; i < 4; i++) {
            Ultramarine.LOGGER.debug("- 颜色 {}: {}", i + 1, items.get(2 + i).isEmpty() ? "空" : items.get(2 + i).getItem().getDescriptionId() + " x" + items.get(2 + i).getCount());
        }
        Ultramarine.LOGGER.debug("- 结果: {}", items.get(6).isEmpty() ? "空" : items.get(6).getItem().getDescriptionId() + " x" + items.get(6).getCount());
        Ultramarine.LOGGER.debug("- 总物品数: {}", items.stream().filter(stack -> !stack.isEmpty()).count());
    }
    }