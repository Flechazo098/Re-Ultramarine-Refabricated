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
import org.jetbrains.annotations.NotNull;
import org.voxelutopia.ultramarine.common.menu.BrickKilnMenu;
import org.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class REIBrickKilnRecipeDisplay implements Display, Comparable<REIBrickKilnRecipeDisplay> {

    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final Optional<ResourceLocation> recipeID;
    private final float experience;
    private final int cookingTime;
    private int sortIndex = 0;

    public REIBrickKilnRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs,
                                     float experience, int cookingTime, Optional<ResourceLocation> recipeID) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.experience = experience;
        this.cookingTime = cookingTime;
        this.recipeID = recipeID;
    }

    public REIBrickKilnRecipeDisplay setSortIndex(int index) {
        this.sortIndex = index;
        return this;
    }

    @Override
    public int compareTo(REIBrickKilnRecipeDisplay other) {
        return Integer.compare(this.sortIndex, other.sortIndex);
    }

    public static REIBrickKilnRecipeDisplay of(RecipeHolder<CompositeSmeltingRecipe> recipeHolder) {
        CompositeSmeltingRecipe recipe = recipeHolder.value();

        // 创建固定大小的输入列表，确保槽位对应关系正确
        List<EntryIngredient> inputs = new ArrayList<>();

        // 填充空槽位，确保有足够的槽位
        for (int i = 0; i <= BrickKilnMenu.SLOT_FUEL; i++) {
            inputs.add(EntryIngredient.empty());
        }

        // 设置主要材料槽
        inputs.set(BrickKilnMenu.SLOT_INPUT_PRIMARY, EntryIngredients.ofIngredient(recipe.getPrimaryIngredient()));

        // 设置次要材料槽
        inputs.set(BrickKilnMenu.SLOT_INPUT_SECONDARY, EntryIngredients.ofIngredient(recipe.getSecondaryIngredient()));

        // 处理输出
        ItemStack resultItem = recipe.getResultItem(null);
        List<EntryIngredient> outputs = Collections.singletonList(
                resultItem.isEmpty() ? EntryIngredient.empty() : EntryIngredient.of(EntryStacks.of(resultItem))
        );

        return new REIBrickKilnRecipeDisplay(
                inputs,
                outputs,
                recipe.getExp(),
                recipe.getCookingTime(),
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
        return REIBrickKilnRecipeCategory.BRICK_KILN;
    }

    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return recipeID;
    }

    public float getExperience() {
        return experience;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public enum Serializer implements DisplaySerializer<REIBrickKilnRecipeDisplay> {

        INSTANCE;

        @Override
        public CompoundTag save(CompoundTag tag, REIBrickKilnRecipeDisplay display) {
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

            // 存储经验值和烹饪时间
            tag.putFloat("experience", display.experience);
            tag.putInt("cookingTime", display.cookingTime);

            // 存储排序索引
            tag.putInt("sortIndex", display.sortIndex);

            return tag;
        }

        @Override
        public REIBrickKilnRecipeDisplay read(CompoundTag tag) {
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

            // 读取经验值和烹饪时间
            float experience = tag.getFloat("experience");
            int cookingTime = tag.getInt("cookingTime");

            // 读取排序索引
            int sortIndex = tag.contains("sortIndex") ? tag.getInt("sortIndex") : 0;

            // 创建显示对象并设置排序索引
            return new REIBrickKilnRecipeDisplay(inputs, outputs, experience, cookingTime, recipeID)
                    .setSortIndex(sortIndex);
        }
    }
}