package com.voxelutopia.ultramarine.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.common.menu.ChiselTableMenu;
import com.voxelutopia.ultramarine.init.registry.ModRecipeSerializers;
import com.voxelutopia.ultramarine.init.registry.ModRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChiselTableRecipe implements Recipe<ChiselTableRecipe.ChiselTableRecipeInput> {
    protected final String group;
    protected final Ingredient material;
    protected final Ingredient template;
    protected final List<Ingredient> colors;
    protected final ItemStack result;

    public ChiselTableRecipe(String pGroup, Ingredient material, Ingredient template, List<Ingredient> colors, ItemStack pResult) {
        this.group = pGroup;
        this.material = material;
        this.template = template;
        this.colors = colors;
        this.result = pResult;
    }

    @Override
    public boolean matches(ChiselTableRecipe.ChiselTableRecipeInput pContainer, @NotNull Level pLevel) {
        ItemStack usedMaterial = pContainer.getItem(ChiselTableMenu.SLOT_MATERIAL);
        ItemStack usedTemplate = pContainer.getItem(ChiselTableMenu.SLOT_TEMPLATE);
        List<ItemStack> usedColors = Arrays.asList(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        for (int i = 0, j = 2; j < pContainer.size(); i++, j++) {
            usedColors.set(i, pContainer.getItem(j));
        }
        usedColors = usedColors.stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());
        return material.test(usedMaterial) && template.test(usedTemplate) && compareColors(this.colors, usedColors);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull ChiselTableRecipe.ChiselTableRecipeInput pContainer, HolderLookup.@NotNull Provider registryAccess) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registryAccess) {
        return result.copy();
    }

    public Ingredient getMaterial() {
        return material;
    }

    public Ingredient getTemplate() {
        return template;
    }

    public List<Ingredient> getColors() {
        return colors;
    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CHISEL_TABLE_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModRecipeTypes.CHISEL_TABLE;
    }

    private static boolean compareColors(List<Ingredient> recipeColors, List<ItemStack> usedColors) {
        if (recipeColors.size() != usedColors.size()) return false;
        List<ItemStack> usedColorsReverse = new ArrayList<>(usedColors);
        Collections.reverse(usedColorsReverse);
        boolean fwd = true, rvs = true;
        for (int i = 0; i < recipeColors.size(); i++) {
            fwd = recipeColors.get(i).test(usedColors.get(i)) && fwd;
            rvs = recipeColors.get(i).test(usedColorsReverse.get(i)) && rvs;
        }
        return fwd || rvs;
    }

    public enum Serializer implements RecipeSerializer<ChiselTableRecipe> {
        INSTANCE;

        public static final MapCodec<ChiselTableRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ChiselTableRecipe::getGroup),
                Ingredient.CODEC.fieldOf("material").forGetter(ChiselTableRecipe::getMaterial),
                Ingredient.CODEC.fieldOf("template").forGetter(ChiselTableRecipe::getTemplate),
                Codec.list(Ingredient.CODEC).fieldOf("colors").forGetter(ChiselTableRecipe::getColors),
                ItemStack.CODEC.fieldOf("result").forGetter(ChiselTableRecipe::getResult)
        ).apply(i, ChiselTableRecipe::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, ChiselTableRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8, ChiselTableRecipe::getGroup,
                        Ingredient.CONTENTS_STREAM_CODEC, ChiselTableRecipe::getMaterial,
                        Ingredient.CONTENTS_STREAM_CODEC, ChiselTableRecipe::getTemplate,
                        ByteBufCodecs.<RegistryFriendlyByteBuf, Ingredient>list().apply(Ingredient.CONTENTS_STREAM_CODEC), ChiselTableRecipe::getColors,
                        ItemStack.STREAM_CODEC, ChiselTableRecipe::getResult,
                        ChiselTableRecipe::new
                );

        @Override
        public @NotNull MapCodec<ChiselTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, ChiselTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public record ChiselTableRecipeInput(Container container) implements RecipeInput {
        @Override
        public ItemStack getItem(int slot) {
            return container.getItem(slot);
        }

        @Override
        public int size() {
            return container.getContainerSize();
        }
    }
}