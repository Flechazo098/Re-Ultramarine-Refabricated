package com.voxelutopia.ultramarine.data.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.data.registry.RecipeSerializerRegistry;
import com.voxelutopia.ultramarine.data.registry.RecipeTypeRegistry;
import com.voxelutopia.ultramarine.world.block.menu.ChiselTableMenu;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChiselTableRecipe implements Recipe<ChiselTableRecipe.ChiselTableRecipeInput> {


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
    public boolean matches(ChiselTableRecipeInput input, Level level) {
        Container container = input.container();

        ItemStack usedMaterial = container.getItem(ChiselTableMenu.SLOT_MATERIAL);
        ItemStack usedTemplate = container.getItem(ChiselTableMenu.SLOT_TEMPLATE);
        List<ItemStack> usedColors = Arrays.asList(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);

        for (int i = 0, j = ChiselTableMenu.SLOT_COLOR_START; j < ChiselTableMenu.SLOT_COLOR_END; i++, j++) {
            usedColors.set(i, container.getItem(j));
        }
        usedColors = usedColors.stream().filter(item -> !item.isEmpty()).collect(Collectors.toList());

        return material.test(usedMaterial) && template.test(usedTemplate) && compareColors(this.colors, usedColors);
    }


    @Override
    public ItemStack assemble(ChiselTableRecipeInput input, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.CHISEL_TABLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeTypeRegistry.CHISEL_TABLE;
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

    private static boolean compareColors(List<Ingredient> recipeColors, List<ItemStack> usedColors) {
        if (recipeColors.size() != usedColors.size()) return false;
        List<ItemStack> reversed = new ArrayList<>(usedColors);
        Collections.reverse(reversed);
        boolean fwd = true, rev = true;
        for (int i = 0; i < recipeColors.size(); i++) {
            fwd = fwd && recipeColors.get(i).test(usedColors.get(i));
            rev = rev && recipeColors.get(i).test(reversed.get(i));
        }
        return fwd || rev;
    }

    public static class Serializer implements RecipeSerializer<ChiselTableRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        private static final Codec<ChiselTableRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(ChiselTableRecipe::getId),
                Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
                Ingredient.CODEC.fieldOf("material").forGetter(ChiselTableRecipe::getMaterial),
                Ingredient.CODEC.fieldOf("template").forGetter(ChiselTableRecipe::getTemplate),
                Ingredient.CODEC.listOf().fieldOf("colors").forGetter(ChiselTableRecipe::getColors),
                ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        ).apply(instance, (id, group, material, template, colors, result) ->
                new ChiselTableRecipe(id, group, material, template, colors.toArray(new Ingredient[0]), result)));

        @Override
        public Codec<ChiselTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public ChiselTableRecipe fromNetwork(FriendlyByteBuf buffer) {
            ResourceLocation id = buffer.readResourceLocation();
            String group = buffer.readUtf();
            Ingredient material = Ingredient.fromNetwork(buffer);
            Ingredient template = Ingredient.fromNetwork(buffer);

            int colorCount = buffer.readVarInt();
            Ingredient[] colors = new Ingredient[colorCount];
            for (int i = 0; i < colorCount; i++) {
                colors[i] = Ingredient.fromNetwork(buffer);
            }

            ItemStack result = buffer.readItem();
            return new ChiselTableRecipe(id, group, material, template, colors, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ChiselTableRecipe recipe) {
            buffer.writeResourceLocation(recipe.getId());
            buffer.writeUtf(recipe.group);
            recipe.material.toNetwork(buffer);
            recipe.template.toNetwork(buffer);

            buffer.writeVarInt(recipe.colors.size());
            for (Ingredient color : recipe.colors) {
                color.toNetwork(buffer);
            }

            buffer.writeItem(recipe.result);
        }
    }

    public record ChiselTableRecipeInput(Container container) implements Container {
        @Override
        public int getContainerSize() {
            return container.getContainerSize();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public ItemStack getItem(int slot) {
            return container.getItem(slot);
        }

        @Override
        public ItemStack removeItem(int i, int j) {
            return null;
        }

        @Override
        public ItemStack removeItemNoUpdate(int i) {
            return null;
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
        public void clearContent() {

        }
    }
}