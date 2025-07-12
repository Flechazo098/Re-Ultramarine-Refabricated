package com.voxelutopia.ultramarine.common.tile;

import com.voxelutopia.ultramarine.common.block.DecorativeBlock;
import com.voxelutopia.ultramarine.init.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class CenserBlockEntity extends BlockEntity {

    private int remainingTime = 0;
    private boolean lit = false;

    public CenserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CENSER, pos, state);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, CenserBlockEntity pBlockEntity) {
        if (pBlockEntity.remainingTime <= 0) {
            if (pBlockEntity.lit)
                pBlockEntity.finishIncense(pLevel, pPos, pState);
        } else {
            pBlockEntity.remainingTime--;
            if (pLevel.getGameTime() % 80 == 0) {
                pLevel.getEntitiesOfClass(LivingEntity.class, new AABB(pPos).inflate(10))
                        .forEach(e -> e.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0)));
            }
        }
    }

    public void lightIncense(Level pLevel, BlockPos pPos, BlockState pState) {
        this.lit = true;
        this.remainingTime = 1200;
        pLevel.setBlock(pPos, pState.setValue(DecorativeBlock.LIT, true), Block.UPDATE_ALL);
        this.setChanged();
    }

    public void finishIncense(Level pLevel, BlockPos pPos, BlockState pState) {
        this.lit = false;
        this.remainingTime = 0;
        pLevel.setBlock(pPos, pState.setValue(DecorativeBlock.LIT, false), Block.UPDATE_ALL);
        this.setChanged();
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);
        this.remainingTime = pTag.getInt("BurnTime");
        this.lit = pTag.getBoolean("Lit");

        // 确保方块状态与实体状态一致
        BlockState state = this.getBlockState();
        if (state.hasProperty(DecorativeBlock.LIT) && state.getValue(DecorativeBlock.LIT) != this.lit) {
            Level level = this.getLevel();
            if (level != null && !level.isClientSide()) {
                level.setBlock(this.getBlockPos(), state.setValue(DecorativeBlock.LIT, this.lit), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveAdditional(pTag, provider);
        pTag.putInt("BurnTime", remainingTime);
        pTag.putBoolean("Lit", lit);
    }
}
