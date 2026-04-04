package com.voxelutopia.ultramarine.common.tile;

import com.voxelutopia.ultramarine.common.block.DecorativeBlock;
import com.voxelutopia.ultramarine.init.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.remainingTime = input.getIntOr("BurnTime", 0);
        this.lit = input.getBooleanOr("Lit", false);

        BlockState state = this.getBlockState();
        if (state.hasProperty(DecorativeBlock.LIT) && state.getValue(DecorativeBlock.LIT) != this.lit) {
            Level level = this.getLevel();
            if (level != null && !level.isClientSide()) {
                level.setBlock(this.getBlockPos(), state.setValue(DecorativeBlock.LIT, this.lit), Block.UPDATE_ALL);
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("BurnTime", remainingTime);
        output.putBoolean("Lit", lit);
    }
}
