package com.voxelutopia.ultramarine.common.recipe;

import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;

public interface WoodworkingRecipeAccess {
    default SelectableRecipe.SingleInputSet<StonecutterRecipe> ultramarine$woodworkingRecipes() {
        return SelectableRecipe.SingleInputSet.empty();
    }

    default void ultramarine$setWoodworkingRecipes(SelectableRecipe.SingleInputSet<StonecutterRecipe> recipes) {
    }
}
