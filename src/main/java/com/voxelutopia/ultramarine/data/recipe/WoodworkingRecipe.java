package com.voxelutopia.ultramarine.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.data.registry.RecipeSerializerRegistry;
import com.voxelutopia.ultramarine.data.registry.RecipeTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.level.Level;

public class WoodworkingRecipe extends SingleItemRecipe {

    public WoodworkingRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult) {
        super(RecipeTypeRegistry.WOODWORKING, RecipeSerializerRegistry.WOODWORKING_SERIALIZER, pGroup, pIngredient, pResult);
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<WoodworkingRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(Ultramarine.MOD_ID, "woodworking");

        private static final Codec<WoodworkingRecipe> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(WoodworkingRecipe::getGroup),
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                ).apply(instance, (group, ingredient, result) ->
                        new WoodworkingRecipe(ID, group, ingredient, result))
        );

        @Override
        public Codec<WoodworkingRecipe> codec() {
            return CODEC;
        }

        @Override
        public WoodworkingRecipe fromNetwork(FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new WoodworkingRecipe(ID, group, ingredient, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, WoodworkingRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
