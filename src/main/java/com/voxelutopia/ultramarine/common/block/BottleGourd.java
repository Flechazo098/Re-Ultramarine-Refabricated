package com.voxelutopia.ultramarine.common.block;

import com.mojang.serialization.MapCodec;
import com.voxelutopia.ultramarine.common.tile.BottleGourdBlockEntity;
import com.voxelutopia.ultramarine.init.registry.ModBlockEntities;
import com.voxelutopia.ultramarine.util.helper.ItemHandlerHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class BottleGourd extends DecorativeBlock implements EntityBlock {

    public static final VoxelShape GOURD = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 14.0D, 11.0D);


    public BottleGourd(Builder builder) {
        super(builder);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack item, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (item.is(Items.POTION)) {
            var components = item.getComponents();
            var potionContents = components.get(DataComponents.POTION_CONTENTS);
            if (potionContents != null) {
                Optional<Holder<Potion>> optionalPotionHolder = potionContents.potion();
                if (optionalPotionHolder.isPresent()) {
                    var optionalBlockEntity = pLevel.getBlockEntity(pPos, ModBlockEntities.BOTTLE_GOURD);
                    if (optionalBlockEntity.isEmpty()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

                    BottleGourdBlockEntity blockEntity = optionalBlockEntity.get();
                    Holder<Potion> potionHolder = optionalPotionHolder.get();
                    Potion potion = potionHolder.value();
                    if (blockEntity.addPotionCharge(potion)) {
                        if (!pLevel.isClientSide()) {
                            if (!pPlayer.getAbilities().instabuild) {
                                item.shrink(1);
                                ItemHandlerHelper.giveItemToPlayer(pPlayer, new ItemStack(Items.GLASS_BOTTLE));
                            }
                        }
                        pLevel.playSound(null, pPos, SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 1.0f, 1.0f);
                        return ItemInteractionResult.sidedSuccess(pLevel.isClientSide());
                    }
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHit) {
        var optionalBlockEntity = pLevel.getBlockEntity(pPos, ModBlockEntities.BOTTLE_GOURD);
        if (optionalBlockEntity.isEmpty()) return InteractionResult.PASS;

        BottleGourdBlockEntity blockEntity = optionalBlockEntity.get();
        if (blockEntity.hasCharges()) {
            Optional<Potion> potion = blockEntity.takePotionCharge();
            if (potion.isPresent()) {
                if (!pLevel.isClientSide()) {
                    for (MobEffectInstance effect : potion.get().getEffects()) {
                        if (effect.getEffect().value().isInstantenous()) {
                            effect.getEffect().value().applyInstantenousEffect(pPlayer, pPlayer, pPlayer, effect.getAmplifier(), 1.0D);
                        } else {
                            pPlayer.addEffect(new MobEffectInstance(effect));
                        }
                    }
                }
                pLevel.playSound(null, pPlayer, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0f, 1.0f);
                return InteractionResult.sidedSuccess(pLevel.isClientSide());
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        pLevel.getBlockEntity(pPos, ModBlockEntities.BOTTLE_GOURD).ifPresent(entity -> {
            if (entity.hasCharges() && !pLevel.isClientSide()) {
                int charges = entity.getCharges();
                List<MobEffectInstance> effects = entity.getPotion().getEffects();
                if (!effects.isEmpty()) {
                    AreaEffectCloud areaeffectcloud = new AreaEffectCloud(pLevel, pPos.getX(), pPos.getY(), pPos.getZ());
                    areaeffectcloud.setRadius(0.5F * charges);
                    areaeffectcloud.setRadiusOnUse(-0.5F);
                    areaeffectcloud.setWaitTime(10);
                    areaeffectcloud.setDuration(areaeffectcloud.getDuration() * charges / 2);
                    areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

                    for (MobEffectInstance mobeffectinstance : effects) {
                        areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
                    }
                    pLevel.addFreshEntity(areaeffectcloud);
                }
            }
        });
        return pState;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BottleGourdBlockEntity(pPos, pState);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return null;
    }
}
