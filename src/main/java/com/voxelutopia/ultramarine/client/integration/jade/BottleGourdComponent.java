package com.voxelutopia.ultramarine.client.integration.jade;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.common.tile.BottleGourdBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.IElementHelper;

import java.util.Optional;

public enum BottleGourdComponent implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    public static final ResourceLocation BOTTLE_GOURD = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "bottle_gourd");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig config) {

        CompoundTag data = blockAccessor.getServerData();
        if (data.contains("BottleGourd", 10)) {
            CompoundTag tag = data.getCompound("BottleGourd");
            int charges = tag.getInt("Charges");
            ResourceLocation potionId = ResourceLocation.parse(tag.getString("Potion"));

            ItemStack potionStack = new ItemStack(Items.POTION);
            var potionHolder = BuiltInRegistries.POTION.getHolder(potionId).orElseThrow();

            potionStack.set(DataComponents.POTION_CONTENTS,
                    new PotionContents(potionHolder));

            String potionKey = Potion.getName(Optional.of(potionHolder), "item.minecraft.potion.effect.");
            Component potionName = Component.translatable(potionKey);

            IElementHelper helper = IElementHelper.get();
            IThemeHelper themeHelper = IThemeHelper.get();

            tooltip.add(helper.smallItem(potionStack));

            tooltip.append(potionName);
            tooltip.append(helper.spacer(5, 0));
            tooltip.append(themeHelper.info(charges + "/" + BottleGourdBlockEntity.MAX_CHARGE));
        }
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor blockAccessor) {

        if (blockAccessor.getBlockEntity() instanceof BottleGourdBlockEntity gourd) {
            if (gourd.hasCharges()) {
                CompoundTag compound = new CompoundTag();
                int charges = gourd.getCharges();
                ResourceLocation potionId = BuiltInRegistries.POTION.getKey(gourd.getPotion());

                compound.putInt("Charges", charges);
                compound.putString("Potion", potionId.toString());
                tag.put("BottleGourd", compound);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return BOTTLE_GOURD;
    }
}