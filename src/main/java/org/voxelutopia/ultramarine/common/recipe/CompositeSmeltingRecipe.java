package org.voxelutopia.ultramarine.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.voxelutopia.ultramarine.common.tile.BrickKilnBlockEntity;
import org.voxelutopia.ultramarine.init.registry.ModRecipeSerializers;
import org.voxelutopia.ultramarine.init.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.List;

public class CompositeSmeltingRecipe implements Recipe<RecipeInput> {

    public static final MapCodec<CompositeSmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(CompositeSmeltingRecipe::getId),
            Codec.STRING.optionalFieldOf("group", "").forGetter(CompositeSmeltingRecipe::getGroup),
            Ingredient.CODEC.fieldOf("primary_ingredient").forGetter(CompositeSmeltingRecipe::getPrimaryIngredient),
            Ingredient.CODEC.fieldOf("secondary_ingredient").forGetter(CompositeSmeltingRecipe::getSecondaryIngredient),
            ItemStack.CODEC.fieldOf("result").forGetter(CompositeSmeltingRecipe::getResult),
            Codec.FLOAT.fieldOf("experience").forGetter(CompositeSmeltingRecipe::getExp),
            Codec.INT.fieldOf("cookingtime").forGetter(CompositeSmeltingRecipe::getCookingTime)
    ).apply(instance, CompositeSmeltingRecipe::new));


    public static final StreamCodec<RegistryFriendlyByteBuf, CompositeSmeltingRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                ResourceLocation.STREAM_CODEC.encode(buf, recipe.getId());
                ByteBufCodecs.STRING_UTF8.encode(buf, recipe.getGroup());
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getPrimaryIngredient());
                Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.getSecondaryIngredient());
                ItemStack.STREAM_CODEC.encode(buf, recipe.getResult());
                ByteBufCodecs.FLOAT.encode(buf, recipe.getExp());
                ByteBufCodecs.INT.encode(buf, recipe.getCookingTime());
            },
            buf -> {
                ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(buf);
                String group = ByteBufCodecs.STRING_UTF8.decode(buf);
                Ingredient primary = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                Ingredient secondary = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
                ItemStack result = ItemStack.STREAM_CODEC.decode(buf);
                float exp = ByteBufCodecs.FLOAT.decode(buf);
                int cookingTime = ByteBufCodecs.INT.decode(buf);
                return new CompositeSmeltingRecipe(id, group, primary, secondary, result, exp, cookingTime);
            }
    );
    protected final ResourceLocation id;
    protected final String group;
    protected final Ingredient primaryIngredient;
    protected final Ingredient secondaryIngredient;
    protected final ItemStack result;
    protected final float experience;
    protected final int cookingTime;

    public CompositeSmeltingRecipe(ResourceLocation pId, String pGroup, Ingredient primaryIngredient, Ingredient secondaryIngredient, ItemStack pResult, float pExperience, int pCookingTime) {
        this.id = pId;
        this.group = pGroup;
        this.primaryIngredient = primaryIngredient;
        this.secondaryIngredient = secondaryIngredient;
        this.result = pResult;
        this.experience = pExperience;
        this.cookingTime = pCookingTime;
    }

    public boolean partialMatch(Container pContainer, Level pLevel) {
        return primaryIngredient.or(secondaryIngredient).test(pContainer.getItem(0));
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ItemStack getResult() {
        return this.result;
    }

    @Override
    public ItemStack assemble(RecipeInput recipeInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return this.primaryIngredient.test(recipeInput.getItem(BrickKilnBlockEntity.SLOT_INPUT_PRIMARY)) &&
                this.secondaryIngredient.test(recipeInput.getItem(BrickKilnBlockEntity.SLOT_INPUT_SECONDARY));
    }


    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public Ingredient getPrimaryIngredient() {
        return primaryIngredient;
    }

    public Ingredient getSecondaryIngredient() {
        return secondaryIngredient;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.result.copy();
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

    public float getExp(){
        return experience;
    }

    public static class Serializer implements RecipeSerializer<CompositeSmeltingRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        private static final int defaultCookingTime = 200;

        protected Serializer() {}

        public CompositeSmeltingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            if (!pJson.has("result"))
                throw new JsonSyntaxException("Missing result, expected to find a string or object");

            String group = GsonHelper.getAsString(pJson, "group", "");
            Ingredient primaryIngredient = parseIngredient(pJson, "primary_ingredient");
            Ingredient secondaryIngredient = parseIngredient(pJson, "secondary_ingredient");

            ItemStack result;
            if (pJson.get("result").isJsonObject()) {
                JsonObject resultObj = GsonHelper.getAsJsonObject(pJson, "result");
                String itemId = GsonHelper.getAsString(resultObj, "item");
                ResourceLocation itemLocation = ResourceLocation.tryParse(itemId);
                Item item = BuiltInRegistries.ITEM.get(itemLocation);

                int count = GsonHelper.getAsInt(resultObj, "count", 1);
                result = new ItemStack(item, count);
            } else {
                String s1 = GsonHelper.getAsString(pJson, "result");
                ResourceLocation resourcelocation = ResourceLocation.tryParse(s1);
                result = new ItemStack(BuiltInRegistries.ITEM.get(resourcelocation));
            }
            float exp = GsonHelper.getAsFloat(pJson, "experience", 0.0F);
            int cookingTime = GsonHelper.getAsInt(pJson, "cookingtime", defaultCookingTime);
            return new CompositeSmeltingRecipe(pRecipeId, group, primaryIngredient, secondaryIngredient, result, exp, cookingTime);
        }

        @Nullable
        public CompositeSmeltingRecipe fromNetwork(ResourceLocation pRecipeId, RegistryFriendlyByteBuf pBuffer) {
            ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(pBuffer);
            String group = pBuffer.readUtf();
            Ingredient primary = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            Ingredient secondary = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            ItemStack result = ItemStack.STREAM_CODEC.decode(pBuffer);
            float exp = pBuffer.readFloat();
            int cookingTime = pBuffer.readVarInt();
            return new CompositeSmeltingRecipe(id, group, primary, secondary, result, exp, cookingTime);
        }


        public void toNetwork(RegistryFriendlyByteBuf pBuffer, CompositeSmeltingRecipe pRecipe) {
            ResourceLocation.STREAM_CODEC.encode(pBuffer, pRecipe.id);
            pBuffer.writeUtf(pRecipe.group);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.primaryIngredient); // 使用StreamCodec编码
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.secondaryIngredient);
            ItemStack.STREAM_CODEC.encode(pBuffer, pRecipe.result);
            pBuffer.writeFloat(pRecipe.experience);
            pBuffer.writeVarInt(pRecipe.cookingTime);
        }

        private static Ingredient parseIngredient(JsonObject json, String member) {
            JsonElement ingredientRaw = GsonHelper.isArrayNode(json, member) ? GsonHelper.getAsJsonArray(json, member) : GsonHelper.getAsJsonObject(json, member);

            // 如果是数组，处理多个物品的情况
            if (ingredientRaw.isJsonArray()) {
                List<ItemStack> items = new ArrayList<>();
                JsonArray array = ingredientRaw.getAsJsonArray();

                for (JsonElement element : array) {
                    if (element.isJsonObject()) {
                        JsonObject itemObj = element.getAsJsonObject();
                        String itemId = GsonHelper.getAsString(itemObj, "item");
                        ResourceLocation itemLoc = ResourceLocation.tryParse(itemId);
                        Item item = BuiltInRegistries.ITEM.get(itemLoc);
                        int count = GsonHelper.getAsInt(itemObj, "count", 1);
                        items.add(new ItemStack(item, count));
                    }
                }

                return Ingredient.of(items.toArray(new ItemStack[0]));
            }
            // 如果是对象，可能是标签或单个物品
            else if (ingredientRaw.isJsonObject()) {
                JsonObject obj = ingredientRaw.getAsJsonObject();

                // 处理标签
                if (obj.has("tag")) {
                    String tagId = GsonHelper.getAsString(obj, "tag");
                    ResourceLocation tagLoc = ResourceLocation.tryParse(tagId);
                    TagKey<Item> tag = TagKey.create(Registries.ITEM, tagLoc);
                    return Ingredient.of(tag);
                }
                // 处理单个物品
                else if (obj.has("item")) {
                    String itemId = GsonHelper.getAsString(obj, "item");
                    ResourceLocation itemLoc = ResourceLocation.tryParse(itemId);
                    Item item = BuiltInRegistries.ITEM.get(itemLoc);
                    int count = GsonHelper.getAsInt(obj, "count", 1);
                    return Ingredient.of(new ItemStack(item, count));
                }
            }

            throw new JsonSyntaxException("Invalid ingredient format: " + ingredientRaw);
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

}
