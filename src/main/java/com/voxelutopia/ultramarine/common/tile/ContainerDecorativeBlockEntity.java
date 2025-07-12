package com.voxelutopia.ultramarine.common.tile;

import com.voxelutopia.ultramarine.common.block.ContainerDecorativeBlock;
import com.voxelutopia.ultramarine.common.menu.ContainerDecorativeBlockMenu;
import com.voxelutopia.ultramarine.init.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ContainerDecorativeBlockEntity extends RandomizableContainerBlockEntity {

    private NonNullList<ItemStack> items;
    private int rows = 3;
    private Block block;

    public ContainerDecorativeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CONTAINER_DECORATIVE_BLOCK, pos, state);
        this.block = state.getBlock();
        this.items = NonNullList.withSize(this.rows * 9, ItemStack.EMPTY);
    }

    public ContainerDecorativeBlockEntity(BlockPos pos, BlockState state, int rows) {
        this(pos, state);
        this.rows = rows;
        this.items = NonNullList.withSize(rows * 9, ItemStack.EMPTY);
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItemStacks) {
        this.items = pItemStacks;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container." + BuiltInRegistries.BLOCK.getKey(block).getPath());
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        if (block instanceof ContainerDecorativeBlock container) {
            return container.getContainerType().createMenu(pContainerId, pInventory, this);
        }
        return ContainerDecorativeBlockMenu.genericThreeRows(pContainerId, pInventory, this);
    }

    @Override
    public int getContainerSize() {
        return rows * 9;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.saveAdditional(nbt, provider);
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.items, provider);
        }

        // 保存行数
        nbt.putInt("Rows", this.rows);

        // 保存方块信息
        if (this.block != null) {
            nbt.putString("BlockId", BuiltInRegistries.BLOCK.getKey(this.block).toString());
        }
    }


    @Override
    protected void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);

        // 加载行数
        if (nbt.contains("Rows")) {
            this.rows = nbt.getInt("Rows");
        }

        // 加载方块信息
        if (nbt.contains("BlockId")) {
            String blockId = nbt.getString("BlockId");
            this.block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockId));
        } else {
            // 如果没有保存的方块信息，使用当前方块状态
            if (this.level != null) {
                this.block = this.level.getBlockState(this.worldPosition).getBlock();
            }
        }

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.items, provider);
        }
    }
}
