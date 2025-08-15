package com.voxelutopia.ultramarine.world.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.data.registry.BlockEntityRegistry;
import com.voxelutopia.ultramarine.world.block.ContainerDecorativeBlock;
import com.voxelutopia.ultramarine.world.block.menu.ContainerDecorativeBlockMenu;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ContainerDecorativeBlockEntity extends RandomizableContainerBlockEntity {

    public static final Codec<ContainerDecorativeBlockEntity> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(entity -> entity.worldPosition),
                    BlockState.CODEC.fieldOf("blockState").forGetter(BlockEntity::getBlockState),
                    Codec.INT.fieldOf("rows").forGetter(entity -> entity.rows),
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(entity -> entity.block)
            ).apply(instance, (pos, state, rows, block) -> {
                ContainerDecorativeBlockEntity entity = new ContainerDecorativeBlockEntity(pos, state, rows);
                entity.block = block;
                // 兜底：如果block为null
                if (entity.block == null) {
                    entity.block = state.getBlock();
                }
                return entity;
            })
    );

    private NonNullList<ItemStack> items;
    private int rows = 3;
    private Block block;

    public ContainerDecorativeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.CONTAINER_DECORATIVE_BLOCK, pos, state);
        this.block = state.getBlock();
        this.items = NonNullList.withSize(this.rows * 9, ItemStack.EMPTY);
    }

    public ContainerDecorativeBlockEntity(BlockPos pos, BlockState state, int rows) {
        this(pos, state);
        this.rows = rows;
        this.items = NonNullList.withSize(rows * 9, ItemStack.EMPTY);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> pItemStacks) {
        this.items = pItemStacks;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container." + BuiltInRegistries.BLOCK.getKey(block).getPath());
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
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
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        if (!this.trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, this.items);
        }

        // 保存行数
        nbt.putInt("Rows", this.rows);

        // 保存方块信息
        if (this.block != null) {
            nbt.putString("BlockId", BuiltInRegistries.BLOCK.getKey(this.block).toString());
        }
    }

    public void load(CompoundTag nbt) {
        super.load(nbt);
        // 加载行数
        if (nbt.contains("Rows")) {
            this.rows = nbt.getInt("Rows");
        }

        // 加载方块信息
        if (nbt.contains("BlockId")) {
            String blockId = nbt.getString("BlockId");
            this.block = BuiltInRegistries.BLOCK.get(new ResourceLocation(blockId));
        } else if (this.level != null) {
            this.block = this.level.getBlockState(this.worldPosition).getBlock();
        }
        // 兜底：如果block依然为null
        if (this.block == null && this.level != null) {
            this.block = this.level.getBlockState(this.worldPosition).getBlock();
        }
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, this.items);
        }
    }
}
