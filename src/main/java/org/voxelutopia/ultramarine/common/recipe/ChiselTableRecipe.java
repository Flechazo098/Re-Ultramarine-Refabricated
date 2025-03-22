package org.voxelutopia.ultramarine.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.voxelutopia.ultramarine.common.menu.ChiselTableMenu;
import org.voxelutopia.ultramarine.init.registry.ModRecipeSerializers;
import org.voxelutopia.ultramarine.init.registry.ModRecipeTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChiselTableRecipe implements Recipe<RecipeInput> {

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
    public NonNullList<ItemStack> getRemainingItems(RecipeInput inventory) {
        NonNullList<ItemStack> remains = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (template.test(stack)) {
                remains.set(i, stack.copyWithCount(1));
            }
        }
        return remains;
    }

    @Override
    public boolean matches (RecipeInput recipeInput, Level level) {
        ItemStack usedMaterial = recipeInput.getItem(ChiselTableMenu.SLOT_MATERIAL);
        ItemStack usedTemplate = recipeInput.getItem(ChiselTableMenu.SLOT_TEMPLATE);
        List<ItemStack> usedColors = Arrays.asList(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
        for (int i = 0, j = 2; j < recipeInput.size(); i++, j++){
            usedColors.set(i, recipeInput.getItem(j));
        }
        usedColors = usedColors.stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());
        return material.test(usedMaterial) && template.test(usedTemplate) && compareColors(this.colors, usedColors);
    }

    @Override
    public ItemStack assemble (RecipeInput recipeInput, HolderLookup.Provider provider) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem (HolderLookup.Provider provider) {
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

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CHISEL_TABLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.CHISEL_TABLE;
    }

    private static boolean compareColors(List<Ingredient> recipeColors, List<ItemStack> usedColors){
        if (recipeColors.size() != usedColors.size()) return false;
        List<ItemStack> usedColorsReverse = new ArrayList<>(usedColors);
        Collections.reverse(usedColorsReverse);
        boolean fwd = true, rvs = true;
        for (int i = 0; i < recipeColors.size(); i++){
            fwd = recipeColors.get(i).test(usedColors.get(i)) && fwd;
            rvs = recipeColors.get(i).test(usedColorsReverse.get(i)) && rvs;
        }
        return fwd || rvs;
    }

    public static class Serializer implements RecipeSerializer<ChiselTableRecipe>{

        public static final Serializer INSTANCE = new Serializer();

        protected Serializer() {}

        public ChiselTableRecipe fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
            if (!pJson.has("result"))
                throw new JsonSyntaxException("Missing result, expected to find a string or object");

            String group = GsonHelper.getAsString(pJson, "group", "");
            Ingredient material = parseIngredient(pJson, "material");
            Ingredient template = parseIngredient(pJson, "template");
            JsonArray colorsJson = GsonHelper.getAsJsonArray(pJson, "colors");
            Ingredient[] colors = new Ingredient[colorsJson.size()];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = parseIngredient(colorsJson.get(i).getAsJsonObject(), "item");
            }

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

            return new ChiselTableRecipe(pRecipeId, group, material, template, colors, result);
        }
        @Nullable
        public ChiselTableRecipe fromNetwork(ResourceLocation pRecipeId, RegistryFriendlyByteBuf pBuffer) {
            ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(pBuffer);
            String group = pBuffer.readUtf();
            Ingredient material = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            Ingredient template = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            int colorSize = pBuffer.readVarInt();
            Ingredient[] colors = new Ingredient[colorSize];
            for (int i = 0; i < colorSize; i++) {
                colors[i] = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            }
            ItemStack result = ItemStack.STREAM_CODEC.decode(pBuffer);
            return new ChiselTableRecipe(id, group, material, template, colors, result);
        }

        public void toNetwork(RegistryFriendlyByteBuf pBuffer, ChiselTableRecipe pRecipe) {
            ResourceLocation.STREAM_CODEC.encode(pBuffer, pRecipe.id);
            pBuffer.writeUtf(pRecipe.group);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.material);
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.template);
            pBuffer.writeVarInt(pRecipe.colors.size());
            for (Ingredient color : pRecipe.colors) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, color);
            }
            ItemStack.STREAM_CODEC.encode(pBuffer, pRecipe.result);
        }

        private static Ingredient parseIngredient(JsonObject json, String member) {
            JsonElement ingredientRaw = GsonHelper.isArrayNode(json, member) ?
                    GsonHelper.getAsJsonArray(json, member) :
                    GsonHelper.getAsJsonObject(json, member);

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
            } else if (ingredientRaw.isJsonObject()) {
                JsonObject obj = ingredientRaw.getAsJsonObject();

                if (obj.has("tag")) {
                    String tagId = GsonHelper.getAsString(obj, "tag");
                    ResourceLocation tagLoc = ResourceLocation.tryParse(tagId);
                    TagKey<Item> tag = TagKey.create(Registries.ITEM, tagLoc);
                    return Ingredient.of(tag);
                } else if (obj.has("item")) {
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
        public MapCodec<ChiselTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ChiselTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
