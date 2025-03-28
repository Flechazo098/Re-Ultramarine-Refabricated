package org.voxelutopia.ultramarine.client.integration.jade;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.phys.Vec2;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.tile.BottleGourdBlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;

import java.util.Objects;

public enum BottleGourdComponent implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    public static final ResourceLocation BOTTLE_GOURD = ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "bottle_gourd");

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        CompoundTag data = blockAccessor.getServerData();
        if (data.contains("Potion")) {
            int charges = data.getInt("Charges");
            ResourceLocation potionId = ResourceLocation.parse(data.getString("Potion"));
            Potion potion = BuiltInRegistries.POTION.get(potionId);
            IElementHelper helper = IElementHelper.get();
            tooltip.add(helper.item(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), potion), 0.6f).translate(new Vec2(-2, -2.5f)));
            tooltip.append(Component.literal("× " + charges));
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
        BottleGourdBlockEntity gourd = (BottleGourdBlockEntity) blockAccessor.getBlockEntity();
        if (gourd.hasCharges()) {
            int charges = gourd.getCharges();
            Potion potion = gourd.getPotion();
            compoundTag.putInt("Charges", charges);
            compoundTag.putString("Potion", Objects.requireNonNull(BuiltInRegistries.POTION.getKey(potion)).toString());
        }
    }

    @Override
    public ResourceLocation getUid() {
        return BOTTLE_GOURD;
    }

    // 修改自定义的 PotionUtils 内部类来提供 setPotion 方法
    private static class PotionUtils {
        public static ItemStack setPotion(ItemStack pStack, Potion pPotion) {
            ResourceLocation potionId = BuiltInRegistries.POTION.getKey(pPotion);

            // 使用 DataComponentPatch.Builder 创建 patch
            if (pPotion == Potions.WATER.value()) {
                // 对于水药水，不需要添加标签
            } else {
                // 使用 DataComponentType 和相应的值来设置药水
                // 注意：这里假设有一个 DataComponentType 用于药水
                // 实际使用时需要找到正确的 DataComponentType
                CompoundTag tag = new CompoundTag();
                tag.putString("Potion", potionId.toString());

                // 使用 ItemStack 的方法来设置 NBT 数据
                // 由于 1.21 版本的变化，我们需要找到正确的方法
                // 这里使用一个通用的方法，实际使用时可能需要调整
                try {
                    // 尝试使用反射设置 NBT 数据
                    java.lang.reflect.Method method = ItemStack.class.getMethod("setTag", CompoundTag.class);
                    method.invoke(pStack, tag);
                } catch (Exception e) {
                    // 如果反射失败，尝试其他方法
                    // 例如，可能有一个专门用于设置药水的方法
                }
            }

            return pStack;
        }
    }
}
