package com.voxelutopia.ultramarine.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.data.registry.RecipeSerializerRegistry;
import com.voxelutopia.ultramarine.data.registry.RecipeTypeRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
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
    public ItemStack assemble(CompositeSmeltingRecipeInput recipeInput, RegistryAccess provider) {
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
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.COMPOSITE_SMELTING_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.COMPOSITE_SMELTING;
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
        private static final Codec<CompositeSmeltingRecipe> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Ingredient.CODEC.fieldOf("primary_ingredient").forGetter(recipe -> recipe.primaryIngredient),
                        Ingredient.CODEC.fieldOf("secondary_ingredient").forGetter(recipe -> recipe.secondaryIngredient),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(recipe -> recipe.experience),
                        Codec.INT.optionalFieldOf("cookingtime", DEFAULT_COOKING_TIME).forGetter(recipe -> recipe.cookingTime)
                ).apply(instance, CompositeSmeltingRecipe::new)
        );

        protected Serializer() {
        }

        @Override
        public Codec<CompositeSmeltingRecipe> codec() {
            return CODEC;
        }

        @Override
        public CompositeSmeltingRecipe fromNetwork(FriendlyByteBuf buffer) {
            Ingredient primaryIngredient = Ingredient.fromNetwork(buffer);
            Ingredient secondaryIngredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float experience = buffer.readFloat();
            int cookingTime = buffer.readVarInt();
            return new CompositeSmeltingRecipe(primaryIngredient, secondaryIngredient, result, experience, cookingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CompositeSmeltingRecipe recipe) {
            recipe.primaryIngredient.toNetwork(buffer);
            recipe.secondaryIngredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
            buffer.writeFloat(recipe.experience);
            buffer.writeVarInt(recipe.cookingTime);
        }
    }

    // RecipeInput implementation for CompositeSmeltingRecipe
    public record CompositeSmeltingRecipeInput(ItemStack primaryItem, ItemStack secondaryItem) implements Container {

        @Override
        public ItemStack getItem(int index) {
            return switch (index) {
                case 0 -> primaryItem;
                case 1 -> secondaryItem;
                default -> ItemStack.EMPTY;
            };
        }

        @Override
        public ItemStack removeItem(int i, int j) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack removeItemNoUpdate(int i) {
            return ItemStack.EMPTY;
        }

        @Override
        public void setItem(int i, ItemStack itemStack) {

        }

        @Override
        public void setChanged() {

        }

        @Override
        public boolean stillValid(Player player) {
            return false;
        }

        @Override
        public int getContainerSize() {
            return 2;
        }

        @Override
        public boolean isEmpty() {
            return primaryItem.isEmpty() && secondaryItem.isEmpty();
        }

        @Override
        public void clearContent() {

        }
    }
}