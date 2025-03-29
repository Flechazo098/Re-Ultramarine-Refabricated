package org.voxelutopia.ultramarine.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
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

    public ChiselTableBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.CHISEL_TABLE, blockPos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return CONTAINER_TITLE;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ChiselTableMenu(containerId, inventory, this.storage);
    }

    public ChiselTableCombinedStorage getStorage() {
        return storage;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveAdditional(pTag, provider);

        ListTag itemListTag = new ListTag();
        for (int i = 0; i < 7; i++) {
            ItemStack stack = storage.getStackInSlot(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                stack.save(provider, itemTag);
                itemListTag.add(itemTag);
            }
        }
        pTag.put("Items", itemListTag);

        // 添加调试日志
        Ultramarine.LOGGER.debug("凿石台保存物品数据: {} 个物品", itemListTag.size());
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);

        ListTag itemListTag = pTag.getList("Items", 10);
        for (int i = 0; i < itemListTag.size(); ++i) {
            CompoundTag itemTag = itemListTag.getCompound(i);
            int slot = itemTag.getByte("Slot") & 255;
            if (slot >= 0 && slot < 7) {
                ItemStack stack = ItemStack.parseOptional(provider, itemTag);
                storage.setStackInSlot(slot, stack);

                // 添加调试日志
                Ultramarine.LOGGER.debug("凿石台加载物品: 槽位 {}, 物品 {}, 数量 {}",
                        slot, stack.getItem().getDescriptionId(), stack.getCount());
            }
        }

        // 添加调试日志
        Ultramarine.LOGGER.debug("凿石台加载物品数据: {} 个物品", itemListTag.size());
    }
}