package com.voxelutopia.ultramarine.world.block.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.data.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class BottleGourdBlockEntity extends BlockEntity {

    public static final Codec<BottleGourdBlockEntity> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(entity -> entity.worldPosition),
                    BlockState.CODEC.fieldOf("blockState").forGetter(entity -> entity.getBlockState()),
                    BuiltInRegistries.POTION.byNameCodec().fieldOf("potion").forGetter(entity -> entity.potion),
                    Codec.INT.fieldOf("charges").forGetter(entity -> entity.charges),
                    Codec.BOOL.fieldOf("filled").forGetter(entity -> entity.filled)
            ).apply(instance, (pos, state, potion, charges, filled) -> {
                BottleGourdBlockEntity entity = new BottleGourdBlockEntity(pos, state);
                entity.potion = potion;
                entity.charges = charges;
                entity.filled = filled;
                return entity;
            })
    );


    public static final int MAX_CHARGE = 6;

    private Potion potion;
    private int charges;
    private boolean filled;

    public BottleGourdBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.BOTTLE_GOURD, pos, state);
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
        if (!filled || charges <= 0 || this.potion.equals(Potions.EMPTY)) return Optional.empty();
        else {
            Potion charge = this.potion;
            this.charges--;
            if (charges <= 0) {
                filled = false;
                this.potion = Potions.EMPTY;
            }
            return Optional.of(charge);
        }
    }

    public boolean hasCharges() {
        return (filled && charges > 0 && !potion.equals(Potions.EMPTY));
    }

    public int getCharges() {
        return this.charges;
    }

    public Potion getPotion() {
        return potion;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.potion = Potion.byName(pTag.getString("Potion"));
        this.charges = pTag.getInt("Charges");
        this.filled = pTag.getBoolean("Filled");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putString("Potion", BuiltInRegistries.POTION.getKey(this.potion).toString());
        pTag.putInt("Charges", this.charges);
        pTag.putBoolean("Filled", this.filled);
    }

}
