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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChiselTableRecipe implements Recipe<ChiselTableRecipe.ChiselTableRecipeInput> {


    protected final ResourceLocation id;
    protected final String group;
    protected final Ingredient material;
    protected final Ingredient template;
    protected final List<Ingredient> colors;
    protected final ItemStack result;

    public ChiselTableRecipe(ResourceLocation pId, String pGroup, Ingredient material, Ingredient template, Ingredient[] colors, ItemStack pResult) {
        this.id = pId;
        this.group = pGroup;
        this.material = material;
        this.template = template;
        this.colors = new ArrayList<>();
        this.colors.addAll(Arrays.asList(colors));
        this.result = pResult;
    }


    @Override
    public boolean matches(ChiselTableRecipeInput input, Level level) {
        Container container = input.container();

        ItemStack usedMaterial = container.getItem(ChiselTableMenu.SLOT_MATERIAL);
        ItemStack usedTemplate = container.getItem(ChiselTableMenu.SLOT_TEMPLATE);
        List<ItemStack> usedColors = Arrays.asList(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);

        for (int i = 0, j = ChiselTableMenu.SLOT_COLOR_START; j < ChiselTableMenu.SLOT_COLOR_END; i++, j++) {
            usedColors.set(i, container.getItem(j));
        }
        usedColors = usedColors.stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());

        return material.test(usedMaterial) && template.test(usedTemplate) && compareColors(this.colors, usedColors);
    }


    @Override
    public ItemStack assemble(ChiselTableRecipeInput input, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CHISEL_TABLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.CHISEL_TABLE;
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

    public ResourceLocation getId() {
        return id;
    }

    private static boolean compareColors(List<Ingredient> recipeColors, List<ItemStack> usedColors) {
        if (recipeColors.size() != usedColors.size()) return false;
        List<ItemStack> reversed = new ArrayList<>(usedColors);
        Collections.reverse(reversed);
        boolean fwd = true, rev = true;
        for (int i = 0; i < recipeColors.size(); i++) {
            fwd = fwd && recipeColors.get(i).test(usedColors.get(i));
            rev = rev && recipeColors.get(i).test(reversed.get(i));
        }
        return fwd || rev;
    }

    public static class Serializer implements RecipeSerializer<ChiselTableRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        public static final MapCodec<ChiselTableRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ChiselTableRecipe::getId),
                Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                Ingredient.CODEC.fieldOf("material").forGetter(ChiselTableRecipe::getMaterial),
                Ingredient.CODEC.fieldOf("template").forGetter(ChiselTableRecipe::getTemplate),
                Ingredient.CODEC.listOf().fieldOf("colors").forGetter(ChiselTableRecipe::getColors),
                ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        ).apply(instance, (id, group, material, template, colors, result) ->
                new ChiselTableRecipe(id, group, material, template, colors.toArray(new Ingredient[0]), result)));

        public static final StreamCodec<RegistryFriendlyByteBuf, ChiselTableRecipe> STREAM_CODEC = StreamCodec.of(
                (buf, recipe) -> {
                    ResourceLocation.STREAM_CODEC.encode(buf, recipe.getId());
                    ByteBufCodecs.STRING_UTF8.encode(buf, recipe.group);
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.material);
                    Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.template);
                    ByteBufCodecs.INT.encode(buf, recipe.colors.size());
                    for (Ingredient color : recipe.colors) {
                        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, color);
                    }
                    ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                },
                buf -> {
                    ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(buf);
                    String group = ByteBufCodecs.STRING_UTF8.decode(buf);
                    Ingredient material = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                    Ingredient template = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                    int colorSize = ByteBufCodecs.INT.decode(buf);
                    Ingredient[] colors = new Ingredient[colorSize];
                    for (int i = 0; i < colorSize; i++) {
                        colors[i] = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                    }
                    ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                    return new ChiselTableRecipe(id, group, material, template, colors, result);
                }
        );

        @Override
        public MapCodec<ChiselTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChiselTableRecipe> streamCodec() {
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