package com.voxelutopia.ultramarine.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.init.registry.ModRecipeSerializers;
import com.voxelutopia.ultramarine.init.registry.ModRecipeTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CompositeSmeltingRecipe implements Recipe<CompositeSmeltingRecipe.CompositeSmeltingRecipeInput> {

    protected final Ingredient primaryIngredient;
    protected final Ingredient secondaryIngredient;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookingTime;

    public CompositeSmeltingRecipe(Ingredient primaryIngredient, Ingredient secondaryIngredient, ItemStack result, float experience, int cookingTime) {
        this.primaryIngredient = primaryIngredient;
        this.secondaryIngredient = secondaryIngredient;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(CompositeSmeltingRecipeInput input, Level level) {
        return this.primaryIngredient.test(input.primaryItem()) &&
                this.secondaryIngredient.test(input.secondaryItem());
    }

    public boolean partialMatch(CompositeSmeltingRecipeInput input, Level level) {
        return primaryIngredient.test(input.primaryItem()) || secondaryIngredient.test(input.secondaryItem());
    }

    @Override
    public ItemStack assemble(CompositeSmeltingRecipeInput recipeInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public Ingredient getPrimaryIngredient() {
        return primaryIngredient;
    }

    public Ingredient getSecondaryIngredient() {
        return secondaryIngredient;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.COMPOSITE_SMELTING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.COMPOSITE_SMELTING;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public float getExp() {
        return experience;
    }

    public static class Serializer implements RecipeSerializer<CompositeSmeltingRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        private static final int DEFAULT_COOKING_TIME = 200;

        // Codec for serialization
        public static final MapCodec<CompositeSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC.fieldOf("primary_ingredient").forGetter(recipe -> recipe.primaryIngredient),
                        Ingredient.CODEC.fieldOf("secondary_ingredient").forGetter(recipe -> recipe.secondaryIngredient),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(recipe -> recipe.experience),
                        Codec.INT.optionalFieldOf("cookingtime", DEFAULT_COOKING_TIME).forGetter(recipe -> recipe.cookingTime)
                ).apply(instance, CompositeSmeltingRecipe::new)
        );

        // StreamCodec for network serialization
        public static final StreamCodec<RegistryFriendlyByteBuf, CompositeSmeltingRecipe> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.primaryIngredient,
                Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.secondaryIngredient,
                ItemStack.STREAM_CODEC, recipe -> recipe.result,
                ByteBufCodecs.FLOAT, recipe -> recipe.experience,
                ByteBufCodecs.INT, recipe -> recipe.cookingTime,
                CompositeSmeltingRecipe::new
        );

        protected Serializer() {
        }

        @Override
        public MapCodec<CompositeSmeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CompositeSmeltingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    // RecipeInput implementation for CompositeSmeltingRecipe
    public record CompositeSmeltingRecipeInput(ItemStack primaryItem, ItemStack secondaryItem) implements RecipeInput {

        @Override
        public ItemStack getItem(int index) {
            return switch (index) {
                case 0 -> primaryItem;
                case 1 -> secondaryItem;
                default -> ItemStack.EMPTY;
            };
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public boolean isEmpty() {
            return primaryItem.isEmpty() && secondaryItem.isEmpty();
        }
    }
}