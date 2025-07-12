package com.voxelutopia.ultramarine.init.data;

import com.google.common.base.Suppliers;
import com.voxelutopia.ultramarine.init.registry.ModItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public enum ModTiers implements Tier {
    BLUE_AND_WHITE_PORCELAIN(3, 16, 14.0F, 5.0F, 24, BlockTags.NEEDS_STONE_TOOL,
            () -> Ingredient.of(ModItems.BLUE_AND_WHITE_PORCELAIN_PIECE)
    );

    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    private final TagKey<Block> incorrectBlocks;

    ModTiers(
            int level,
            int uses,
            float speed,
            float damage,
            int enchantmentValue,
            TagKey<Block> incorrectBlocks,
            Supplier<Ingredient> repairIngredient
    ) {
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.incorrectBlocks = incorrectBlocks;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    @Override
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.incorrectBlocks;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return damage;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Deprecated
    public int getLevel() {
        return 0;
    }
}