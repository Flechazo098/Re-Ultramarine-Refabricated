package com.voxelutopia.ultramarine.common.tile;

import com.voxelutopia.ultramarine.init.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class BottleGourdBlockEntity extends BlockEntity {

    public static final int MAX_CHARGE = 6;
    private static final ResourceLocation EMPTY_POTION_ID = ResourceLocation.parse("empty");

    private Potion potion;
    private int charges;
    private boolean filled;

    public BottleGourdBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BOTTLE_GOURD, pos, state);
        this.potion = BuiltInRegistries.POTION.get(EMPTY_POTION_ID);
        this.charges = 0;
        this.filled = false;
    }

    public boolean addPotionCharge(Potion potion) {
        if (!filled) {
            this.potion = potion;
            this.charges = 1;
            this.filled = true;
            return true;
        } else if (potion.equals(this.potion) && this.charges < MAX_CHARGE) {
            this.charges++;
            return true;
        }
        return false;
    }

    public Optional<Potion> takePotionCharge() {
        if (!filled || charges <= 0 || this.potion.equals(BuiltInRegistries.POTION.get(EMPTY_POTION_ID)))
            return Optional.empty();
        else {
            Potion charge = this.potion;
            this.charges--;
            if (charges <= 0) {
                filled = false;
                this.potion = BuiltInRegistries.POTION.get(EMPTY_POTION_ID);
            }
            return Optional.of(charge);
        }
    }

    public boolean hasCharges() {
        return (filled && charges > 0 && !potion.equals(BuiltInRegistries.POTION.get(EMPTY_POTION_ID)));
    }

    public int getCharges() {
        return this.charges;
    }

    public Potion getPotion() {
        return potion;
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);
        ResourceLocation potionId = ResourceLocation.tryParse(pTag.getString("Potion"));
        this.potion = potionId != null ? BuiltInRegistries.POTION.get(potionId) : BuiltInRegistries.POTION.get(EMPTY_POTION_ID);
        this.charges = pTag.getInt("Charges");
        this.filled = pTag.getBoolean("Filled");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveAdditional(pTag, provider);
        ResourceLocation potionId = BuiltInRegistries.POTION.getKey(this.potion);
        pTag.putString("Potion", potionId != null ? potionId.toString() : EMPTY_POTION_ID.toString());
        pTag.putInt("Charges", this.charges);
        pTag.putBoolean("Filled", this.filled);
    }
}
