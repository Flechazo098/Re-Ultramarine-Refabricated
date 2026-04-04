//package com.voxelutopia.ultramarine.client.integration.rei;
//
//import com.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;
//import me.shedaniel.rei.api.common.category.CategoryIdentifier;
//import me.shedaniel.rei.api.common.display.Display;
//import me.shedaniel.rei.api.common.display.DisplaySerializer;
//import me.shedaniel.rei.api.common.entry.EntryIngredient;
//import me.shedaniel.rei.api.common.util.EntryIngredients;
//import me.shedaniel.rei.api.common.util.EntryStacks;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeHolder;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//public class REIWoodworkingRecipeDisplay implements Display, Comparable<REIWoodworkingRecipeDisplay> {
//
//    private final List<EntryIngredient> inputs;
//    private final List<EntryIngredient> outputs;
//    private final Optional<ResourceLocation> recipeID;
//    private int sortIndex = 0;
//
//    public REIWoodworkingRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> recipeID) {
//        this.inputs = inputs;
//        this.outputs = outputs;
//        this.recipeID = recipeID;
//    }
//
//    public REIWoodworkingRecipeDisplay setSortIndex(int index) {
//        this.sortIndex = index;
//        return this;
//    }
//
//    @Override
//    public int compareTo(REIWoodworkingRecipeDisplay other) {
//        return Integer.compare(this.sortIndex, other.sortIndex);
//    }
//
//    public static REIWoodworkingRecipeDisplay of(RecipeHolder<WoodworkingRecipe> recipeHolder) {
//        WoodworkingRecipe recipe = recipeHolder.value();
//
//        List<EntryIngredient> inputs = Collections.singletonList(
//                EntryIngredients.ofIngredient(recipe.getIngredients().getFirst())
//        );
//
//        ItemStack resultItem = recipe.getResultItem(null);
//        List<EntryIngredient> outputs = Collections.singletonList(
//                resultItem.isEmpty() ? EntryIngredient.empty() : EntryIngredient.of(EntryStacks.of(resultItem))
//        );
//
//        return new REIWoodworkingRecipeDisplay(
//                inputs,
//                outputs,
//                Optional.of(recipeHolder.id())
//        );
//    }
//
//    @Override
//    public List<EntryIngredient> getInputEntries() {
//        return inputs;
//    }
//
//    @Override
//    public List<EntryIngredient> getOutputEntries() {
//        return outputs;
//    }
//
//    @Override
//    public CategoryIdentifier<?> getCategoryIdentifier() {
//        return REIWoodworkingRecipeCategory.WOODWORKING;
//    }
//
//    @Override
//    public Optional<ResourceLocation> getDisplayLocation() {
//        return recipeID;
//    }
//
//    public enum Serializer implements DisplaySerializer<REIWoodworkingRecipeDisplay> {
//
//        INSTANCE;
//
//        @Override
//        public CompoundTag save(CompoundTag tag, REIWoodworkingRecipeDisplay display) {
//            ListTag inputs = new ListTag();
//            display.inputs.forEach(ingredient -> inputs.add(ingredient.saveIngredient()));
//            tag.put("inputs", inputs);
//
//            ListTag outputs = new ListTag();
//            display.outputs.forEach(ingredient -> outputs.add(ingredient.saveIngredient()));
//            tag.put("outputs", outputs);
//
//            display.recipeID.ifPresent(id -> tag.putString("recipeID", id.toString()));
//
//            tag.putInt("sortIndex", display.sortIndex);
//
//            return tag;
//        }
//
//        @Override
//        public REIWoodworkingRecipeDisplay read(CompoundTag tag) {
//            List<EntryIngredient> inputs = new ArrayList<>();
//            tag.getList("inputs", Tag.TAG_LIST).forEach(nbtElement ->
//                    inputs.add(EntryIngredient.read((ListTag) nbtElement)));
//
//            List<EntryIngredient> outputs = new ArrayList<>();
//            tag.getList("outputs", Tag.TAG_LIST).forEach(nbtElement ->
//                    outputs.add(EntryIngredient.read((ListTag) nbtElement)));
//
//            Optional<ResourceLocation> recipeID = tag.contains("recipeID")
//                    ? Optional.of(ResourceLocation.parse(tag.getString("recipeID")))
//                    : Optional.empty();
//
//            int sortIndex = tag.contains("sortIndex") ? tag.getInt("sortIndex") : 0;
//
//            return new REIWoodworkingRecipeDisplay(inputs, outputs, recipeID)
//                    .setSortIndex(sortIndex);
//        }
//    }
//}