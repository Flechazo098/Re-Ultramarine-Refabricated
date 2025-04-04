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
import org.voxelutopia.ultramarine.common.menu.ChiselTableMenu;
import org.voxelutopia.ultramarine.common.recipe.ChiselTableRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class REIChiselTableRecipeDisplay implements Display, Comparable<REIChiselTableRecipeDisplay>{

    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final Optional<ResourceLocation> recipeID;
    private int sortIndex = 0;

    public REIChiselTableRecipeDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> recipeID) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.recipeID = recipeID;
    }

    public REIChiselTableRecipeDisplay setSortIndex(int index) {
        this.sortIndex = index;
        return this;
    }

    @Override
    public int compareTo(REIChiselTableRecipeDisplay other) {
        return Integer.compare(this.sortIndex, other.sortIndex);
    }

    public static REIChiselTableRecipeDisplay of(RecipeHolder<ChiselTableRecipe> recipeHolder) {
        ChiselTableRecipe recipe = recipeHolder.value();

        List<EntryIngredient> inputs = new ArrayList<>();

        for (int i = 0; i < ChiselTableMenu.SLOT_COLOR_END; i++) {
            inputs.add(EntryIngredient.empty());
        }

        inputs.set(ChiselTableMenu.SLOT_MATERIAL, EntryIngredients.ofIngredient(recipe.getMaterial()));

        inputs.set(ChiselTableMenu.SLOT_TEMPLATE, EntryIngredients.ofIngredient(recipe.getTemplate()));

        List<EntryIngredient> colorEntries = recipe.getColors().stream()
                .map(EntryIngredients::ofIngredient)
                .toList();

        for (int i = 0; i < colorEntries.size() && i < 4; i++) {
            inputs.set(ChiselTableMenu.SLOT_COLOR_START + i, colorEntries.get(i));
        }

        ItemStack resultItem = recipe.getResultItem(null);
        List<EntryIngredient> outputs = Collections.singletonList(
                resultItem.isEmpty() ? EntryIngredient.empty() : EntryIngredient.of(EntryStacks.of(resultItem))
        );

        return new REIChiselTableRecipeDisplay(
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
        return REIChiselTableRecipeCategory.CHISEL_TABLE;
    }

    @Override
    public Optional<ResourceLocation> getDisplayLocation() {
        return recipeID;
    }


    public enum Serializer implements DisplaySerializer<REIChiselTableRecipeDisplay> {

        INSTANCE;

        @Override
        public CompoundTag save(CompoundTag tag, REIChiselTableRecipeDisplay display) {
            ListTag inputs = new ListTag();
            display.inputs.forEach(ingredient -> inputs.add(ingredient.saveIngredient()));
            tag.put("inputs", inputs);

            ListTag outputs = new ListTag();
            display.outputs.forEach(ingredient -> outputs.add(ingredient.saveIngredient()));
            tag.put("outputs", outputs);

            display.recipeID.ifPresent(id -> tag.putString("recipeID", id.toString()));

            tag.putInt("sortIndex", display.sortIndex);

            return tag;
        }

        @Override
        public REIChiselTableRecipeDisplay read(CompoundTag tag) {
            List<EntryIngredient> inputs = new ArrayList<>();
            tag.getList("inputs", Tag.TAG_LIST).forEach(nbtElement ->
                    inputs.add(EntryIngredient.read((ListTag) nbtElement)));

            List<EntryIngredient> outputs = new ArrayList<>();
            tag.getList("outputs", Tag.TAG_LIST).forEach(nbtElement ->
                    outputs.add(EntryIngredient.read((ListTag) nbtElement)));

            Optional<ResourceLocation> recipeID = tag.contains("recipeID")
                    ? Optional.of(ResourceLocation.parse(tag.getString("recipeID")))
                    : Optional.empty();

            int sortIndex = tag.contains("sortIndex") ? tag.getInt("sortIndex") : 0;

            return new REIChiselTableRecipeDisplay(inputs, outputs, recipeID)
                    .setSortIndex(sortIndex);
        }
    }
}