package com.voxelutopia.ultramarine.common.menu;

import com.voxelutopia.ultramarine.Ultramarine;
import com.voxelutopia.ultramarine.common.recipe.ChiselTableRecipe;
import com.voxelutopia.ultramarine.init.data.ModItemTags;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import com.voxelutopia.ultramarine.init.registry.ModMenuTypes;
import com.voxelutopia.ultramarine.init.registry.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ChiselTableMenu extends AbstractContainerMenu {

    // 统一槽位定义，与旧版本保持一致
    public static final int SLOT_MATERIAL = 0;
    public static final int SLOT_TEMPLATE = 1;
    public static final int SLOT_COLOR_START = 2;
    public static final int SLOT_COLOR_END = 6;
    public static final int SLOT_RESULT = 6;
    public static final int INV_SLOT_START = 7;
    public static final int INV_SLOT_END = 34;
    public static final int USE_ROW_SLOT_START = 34;
    public static final int USE_ROW_SLOT_END = 43;

    private final ContainerLevelAccess access;
    private final Player player;
    private final Container crafting = new SimpleContainer(6);
    private final Container result = new SimpleContainer(1);

    private static final Predicate<ItemStack> IS_WOOD = i -> i.is(ItemTags.LOGS) || i.is(ModItemTags.POLISHED_PLANKS);
    private static final Predicate<ItemStack> IS_TEMPLATE = i -> i.is(ModItemTags.CHISEL_TEMPLATES);
    private static final Predicate<ItemStack> IS_COLOR = i -> i.is(ModItemTags.DYES) || i.is(ModItemTags.DYE_POWDER);

    public ChiselTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public ChiselTableMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(ModMenuTypes.CHISEL_TABLE, containerId);
        this.access = access;
        this.player = playerInventory.player;

        this.addSlot(new MaterialSlot(crafting, SLOT_MATERIAL, 26, 25));

        this.addSlot(new TemplateSlot(crafting, SLOT_TEMPLATE, 53, 25));

        for (int i = 0; i < 4; i++) {
            this.addSlot(new DyeSlot(crafting, SLOT_COLOR_START + i, 26 + i * 18, 52));
        }

        this.addSlot(new OutputSlot(result, 0, 130, 34));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public void slotsChanged(Container container) {
        this.broadcastChanges();
        if (container == this.crafting) {
            this.createResult();
        }
    }

    public void createResult() {
        Level level = player.level();

        ChiselTableRecipe.ChiselTableRecipeInput input = new ChiselTableRecipe.ChiselTableRecipeInput(this.crafting);

        List<RecipeHolder<ChiselTableRecipe>> matchingRecipes = level.getRecipeManager().getRecipesFor(ModRecipeTypes.CHISEL_TABLE, input, level);

        if (matchingRecipes.size() > 1) {
            Ultramarine.getLogger().warn("Found {} matching chisel table recipes for current input:", matchingRecipes.size());
            matchingRecipes.forEach(recipe -> Ultramarine.getLogger().warn("  - {}", recipe.id()));
        }

        if (matchingRecipes.isEmpty()) {
            this.result.setItem(0, ItemStack.EMPTY);
        } else {
            ChiselTableRecipe recipe = matchingRecipes.getFirst().value();
            ItemStack resultItemStack = recipe.assemble(input, level.registryAccess());
            this.result.setItem(0, resultItemStack);
        }
    }

    protected void onTake(Player player, ItemStack itemStack, Slot slot) {
        for (int i = 0; i < SLOT_COLOR_END; i++) {
            ItemStack item = crafting.getItem(i);
            if (i != SLOT_TEMPLATE) {
                item.shrink(1);
            }
            crafting.setItem(i, item);
        }
        this.slotsChanged(this.crafting);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();
            if (pIndex == SLOT_RESULT) {
                slotItem.getItem().onCraftedBy(slotItem, pPlayer.level(), pPlayer);
                if (!this.moveItemStackTo(slotItem, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotItem, itemstack);
            } else if (pIndex > SLOT_RESULT) { // inv slots
                if (IS_WOOD.test(slotItem)) {
                    if (!this.moveItemStackTo(slotItem, SLOT_MATERIAL, SLOT_MATERIAL + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (IS_TEMPLATE.test(slotItem)) {
                    if (!this.moveItemStackTo(slotItem, SLOT_TEMPLATE, SLOT_TEMPLATE + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (IS_COLOR.test(slotItem)) {
                    if (!this.moveItemStackTo(slotItem, SLOT_COLOR_START, SLOT_COLOR_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < INV_SLOT_END) {
                    if (!this.moveItemStackTo(slotItem, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < USE_ROW_SLOT_END && !this.moveItemStackTo(slotItem, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotItem, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }
            slot.setChanged();

            if (slotItem.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, slotItem);
            this.broadcastChanges();
        }
        return itemstack;
    }

    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.result && super.canTakeItemForPickAll(pStack, pSlot);
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.access.execute((level, blockPos) -> this.clearContainer(pPlayer, this.crafting));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, ModBlocks.CHISEL_TABLE);
    }

    class OutputSlot extends Slot {

        public OutputSlot(Container container, int index, int xPosition, int yPosition) {
            super(container, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(Player pPlayer, ItemStack pStack) {
            super.onTake(pPlayer, pStack);
            ChiselTableMenu.this.onTake(pPlayer, pStack, this);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            ChiselTableMenu.this.slotsChanged(this.container);
        }

        @Override
        public void onQuickCraft(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {
            int i = newStackIn.getCount() - oldStackIn.getCount();
            if (i > 0) {
                this.onQuickCraft(newStackIn, i);
            }
        }
    }

    class IngredientSlot extends Slot {

        public IngredientSlot(Container container, int index, int xPosition, int yPosition) {
            super(container, index, xPosition, yPosition);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            ChiselTableMenu.this.slotsChanged(this.container);
        }
    }

    class TemplateSlot extends IngredientSlot {

        public TemplateSlot(Container container, int index, int xPosition, int yPosition) {
            super(container, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return IS_TEMPLATE.test(stack);
        }
    }

    class MaterialSlot extends IngredientSlot {

        public MaterialSlot(Container container, int index, int xPosition, int yPosition) {
            super(container, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return IS_WOOD.test(stack);
        }
    }

    class DyeSlot extends IngredientSlot {

        public DyeSlot(Container container, int index, int xPosition, int yPosition) {
            super(container, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return IS_COLOR.test(stack);
        }
    }
}