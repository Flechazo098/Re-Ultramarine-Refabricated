package org.voxelutopia.ultramarine.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.init.registry.ModRecipeSerializers;
import org.voxelutopia.ultramarine.init.registry.ModRecipeTypes;

public class WoodworkingRecipe extends SingleItemRecipe {

    public WoodworkingRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult) {
        super(ModRecipeTypes.WOODWORKING, ModRecipeSerializers.WOODWORKING_SERIALIZER, pGroup, pIngredient, pResult);
    }

    @Override
    public boolean matches(SingleRecipeInput recipeInput, Level level) {
        return this.ingredient.test(recipeInput.getItem(0));
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
            ResourceLocation.fromNamespaceAndPath(Ultramarine.MOD_ID, "woodworking");

        private final MapCodec<WoodworkingRecipe> codec = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(WoodworkingRecipe::getGroup),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
            ).apply(instance, (group, ingredient, result) ->
                new WoodworkingRecipe(ID, group, ingredient, result))
        );

        private final StreamCodec<RegistryFriendlyByteBuf, WoodworkingRecipe> streamCodec = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            WoodworkingRecipe::getGroup,
            Ingredient.CONTENTS_STREAM_CODEC,
            recipe -> recipe.ingredient,
            ItemStack.STREAM_CODEC,
            recipe -> recipe.result,
            (group, ingredient, result) -> new WoodworkingRecipe(ID, group, ingredient, result)
        );

        @Override
        public MapCodec<WoodworkingRecipe> codec() {
            return this.codec;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, WoodworkingRecipe> streamCodec() {
            return this.streamCodec;
        }
    }
}
