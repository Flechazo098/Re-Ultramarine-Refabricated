package org.voxelutopia.ultramarine.client.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.voxelutopia.ultramarine.common.recipe.WoodworkingRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class REIWoodworkingRecipeDisplay implements Display, Comparable<REIWoodworkingRecipeDisplay> {

    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final Optional<ResourceLocation> recipeID;
    private int sortIndex = 0; // 添加排序索引字段

    public REIWoodworkingRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> recipeID) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.recipeID = recipeID;
    }

    // 添加设置排序索引的方法
    public REIWoodworkingRecipeDisplay setSortIndex(int index) {
        this.sortIndex = index;
        return this;
    }

    // 实现Comparable接口的比较方法
    @Override
    public int compareTo(REIWoodworkingRecipeDisplay other) {
        return Integer.compare(this.sortIndex, other.sortIndex);
    }

    public static REIWoodworkingRecipeDisplay of(RecipeHolder<WoodworkingRecipe> recipeHolder) {
        WoodworkingRecipe recipe = recipeHolder.value();

        // 处理输入
        List<EntryIngredient> inputs = Collections.singletonList(
                EntryIngredients.ofIngredient(recipe.getIngredients().get(0))
        );

        // 处理输出
        ItemStack resultItem = recipe.getResultItem(null);
        List<EntryIngredient> outputs = Collections.singletonList(
                resultItem.isEmpty() ? EntryIngredient.empty() : EntryIngredient.of(EntryStacks.of(resultItem))
        );

        return new REIWoodworkingRecipeDisplay(
                inputs,
                outputs,
                Optional.of(recipeHolder.id())
        );
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return inputs;
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return REIWoodworkingRecipeCategory.WOODWORKING;
    }

    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return recipeID;
    }

    public enum Serializer implements DisplaySerializer<REIWoodworkingRecipeDisplay> {

        INSTANCE;

        @Override
        public CompoundTag save(CompoundTag tag, REIWoodworkingRecipeDisplay display) {
            // 存储配方输入
            ListTag inputs = new ListTag();
            display.inputs.forEach(ingredient -> inputs.add(ingredient.saveIngredient()));
            tag.put("inputs", inputs);

            // 存储配方输出
            ListTag outputs = new ListTag();
            display.outputs.forEach(ingredient -> outputs.add(ingredient.saveIngredient()));
            tag.put("outputs", outputs);

            // 存储配方ID
            display.recipeID.ifPresent(id -> tag.putString("recipeID", id.toString()));

            // 存储排序索引
            tag.putInt("sortIndex", display.sortIndex);

            return tag;
        }

        @Override
        public REIWoodworkingRecipeDisplay read(CompoundTag tag) {
            // 读取输入
            List<EntryIngredient> inputs = new ArrayList<>();
            tag.getList("inputs", Tag.TAG_LIST).forEach(nbtElement ->
                    inputs.add(EntryIngredient.read((ListTag) nbtElement)));

            // 读取输出
            List<EntryIngredient> outputs = new ArrayList<>();
            tag.getList("outputs", Tag.TAG_LIST).forEach(nbtElement ->
                    outputs.add(EntryIngredient.read((ListTag) nbtElement)));

            // 读取配方ID
            Optional<ResourceLocation> recipeID = tag.contains("recipeID")
                    ? Optional.of(ResourceLocation.parse(tag.getString("recipeID")))
                    : Optional.empty();

            // 读取排序索引
            int sortIndex = tag.contains("sortIndex") ? tag.getInt("sortIndex") : 0;

            // 创建显示对象并设置排序索引
            return new REIWoodworkingRecipeDisplay(inputs, outputs, recipeID)
                    .setSortIndex(sortIndex);
        }
    }
}