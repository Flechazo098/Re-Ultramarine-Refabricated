package org.voxelutopia.ultramarine.common.menu;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.inventory.ChiselTableCombinedStorage;
import org.voxelutopia.ultramarine.common.inventory.FabricItemStorage;
import org.voxelutopia.ultramarine.common.recipe.ChiselTableRecipe;
import org.voxelutopia.ultramarine.common.wrapper.RecipeWrapper;
import org.voxelutopia.ultramarine.init.data.ModItemTags;
import org.voxelutopia.ultramarine.init.registry.ModBlocks;
import org.voxelutopia.ultramarine.init.registry.ModMenuTypes;
import org.voxelutopia.ultramarine.init.registry.ModRecipeTypes;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Predicate;

public class ChiselTableMenu extends AbstractContainerMenu {

    public static final int SLOT_MATERIAL = 0;
    public static final int SLOT_TEMPLATE = 1;
    public static final int SLOT_COLOR_START = 2;
    public static final int SLOT_COLOR_END = 6;
    public static final int SLOT_RESULT = 6;
    private static final int INV_SLOT_START = 7;
    private static final int INV_SLOT_END = 34;
    private static final int USE_ROW_SLOT_START = 34;
    private static final int USE_ROW_SLOT_END = 43;

    private static final Predicate<ItemStack> IS_WOOD = i -> i.is(ItemTags.LOGS) || i.is(ModItemTags.POLISHED_PLANKS);
    private static final Predicate<ItemStack> IS_TEMPLATE = i -> i.is(ModItemTags.CHISEL_TEMPLATES);
    private static final Predicate<ItemStack> IS_COLOR = i -> i.is(ModItemTags.DYES) || i.is(ModItemTags.DYE_POWDER);

    private final ContainerLevelAccess access;
    private final Player player;
    private final FabricItemStorage storage;
    private final FabricItemStorage inventory;

    public ChiselTableMenu(int id, Inventory inventory){
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public ChiselTableMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(ModMenuTypes.CHISEL_TABLE, id);
        this.access = access;
        this.player = inventory.player;
        this.storage = new ChiselTableCombinedStorage();
        this.inventory = new InventoryFabricWrapper(inventory);

        this.addSlot(new MaterialSlot(storage, SLOT_MATERIAL, 26, 25));
        this.addSlot(new TemplateSlot(storage, SLOT_TEMPLATE, 53, 25));
        for (int i = SLOT_COLOR_START, j = 0; i < SLOT_COLOR_END; i++, j++){
            this.addSlot(new DyeSlot(storage, i, 26 + j * 18,52));
        }
        this.addSlot(new OutputSlot(storage, SLOT_RESULT, 130, 34));

        for(int r = 0; r < 3; ++r) {
            for(int c = 0; c < 9; ++c) {
                this.addSlot(new SlotFabricItemStorage(this.inventory, c + r * 9 + 9, 8 + c * 18, 84 + r * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new SlotFabricItemStorage(this.inventory, k, 8 + k * 18, 142));
        }
    }

    // 添加新的构造函数，接受ChiselTableCombinedStorage
    public ChiselTableMenu(int id, Inventory inventory, ChiselTableCombinedStorage storage) {
        super(ModMenuTypes.CHISEL_TABLE, id);
        this.access = ContainerLevelAccess.NULL;
        this.player = inventory.player;
        this.storage = storage;
        this.inventory = new InventoryFabricWrapper(inventory);

        this.addSlot(new MaterialSlot(storage, SLOT_MATERIAL, 26, 25));
        this.addSlot(new TemplateSlot(storage, SLOT_TEMPLATE, 53, 25));
        for (int i = SLOT_COLOR_START, j = 0; i < SLOT_COLOR_END; i++, j++){
            this.addSlot(new DyeSlot(storage, i, 26 + j * 18,52));
        }
        this.addSlot(new OutputSlot(storage, SLOT_RESULT, 130, 34));

        for(int r = 0; r < 3; ++r) {
            for(int c = 0; c < 9; ++c) {
                this.addSlot(new SlotFabricItemStorage(this.inventory, c + r * 9 + 9, 8 + c * 18, 84 + r * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new SlotFabricItemStorage(this.inventory, k, 8 + k * 18, 142));
        }
    }

    public void slotsChanged(SlotFabricItemStorage slot) {
        this.broadcastChanges();
        if (slot.index <= SLOT_RESULT){
            this.createResult();
        }
    }

    public void createResult() {
        Level level = player.level();
        RecipeInput ingredients = this.wrapIngredients();
    
        List<RecipeHolder<ChiselTableRecipe>> list = level.getRecipeManager().getRecipesFor(ModRecipeTypes.CHISEL_TABLE, ingredients, level);
    
        if (list.size() > 1) {
            Ultramarine.getLogger().warn("Duplicate chisel table recipe: ");
            list.forEach(holder -> Ultramarine.getLogger().warn(holder.id().getPath()));
            
            // Sort recipes by specificity (more specific recipes first)
            list.sort((a, b) -> {
                ChiselTableRecipe recipeA = a.value();
                ChiselTableRecipe recipeB = b.value();
                
                // Calculate recipe specificity based on ingredients
                int specificityA = calculateRecipeSpecificity(recipeA, ingredients);
                int specificityB = calculateRecipeSpecificity(recipeB, ingredients);
                
                // Higher specificity comes first
                return Integer.compare(specificityB, specificityA);
            });
        }
        if (list.isEmpty()) {
            this.storage.getResult().setItem(0, ItemStack.EMPTY);
        } else {
            ChiselTableRecipe recipe = list.getFirst().value();
            ItemStack resultItemStack = recipe.assemble(ingredients, level.registryAccess());
            this.storage.getResult().setItem(0, resultItemStack);
        }
    }

    /**
     * Calculates how specific a recipe is based on its ingredients.
     * Higher values mean more specific recipes.
     */
    private int calculateRecipeSpecificity(ChiselTableRecipe recipe, RecipeInput ingredients) {
        int specificity = 0;
        
        // Check if material uses specific items rather than tags
        if (recipe.getMaterial().getItems().length == 1) {
            specificity += 2;
        }
        
        // Check if template uses specific items rather than tags
        if (recipe.getTemplate().getItems().length == 1) {
            specificity += 2;
        }
        
        // Check colors - more colors and specific items increase specificity
        for (int i = 0; i < recipe.getColors().size(); i++) {
            Ingredient color = recipe.getColors().get(i);
            
            // Non-empty color slot increases specificity
            if (i < ingredients.size() && !ingredients.getItem(ChiselTableMenu.SLOT_COLOR_START + i).isEmpty()) {
                specificity += 1;
                
                // Specific item rather than tag adds more specificity
                if (color.getItems().length == 1) {
                    specificity += 1;
                }
            }
        }
        
        return specificity;
    }

    private RecipeInput wrapIngredients() {
        SimpleContainer container = new SimpleContainer(6);
        for (int i = 0; i < 6; i++) {
            container.setItem(i, this.storage.getStackInSlot(i));
        }
        return new RecipeWrapper(container);
    }

    protected void onTake(Player player, ItemStack itemStack, SlotFabricItemStorage slot) {
        for (int i = 0; i < SLOT_COLOR_END; i++) {
            ItemStack item = storage.getStackInSlot(i);
            if (i != SLOT_TEMPLATE && !item.isEmpty()) {
                item = item.copy();
                item.shrink(1);
                storage.setStackInSlot(i, item);
            }
        }
        this.slotsChanged(slot);
        this.broadcastChanges();
    }

    @Override
    public ItemStack quickMoveStack (Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemstack = slotItem.copy();
            if (pIndex == SLOT_RESULT) {
                slotItem.getItem().onCraftedBy(slotItem, pPlayer.level(), pPlayer);
                if (! this.moveItemStackTo(slotItem, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotItem, itemstack);
            } else if (pIndex > SLOT_RESULT) { // inv slots
                if (IS_WOOD.test(slotItem)) {
                    if (! this.moveItemStackTo(slotItem, SLOT_MATERIAL, SLOT_MATERIAL + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (IS_TEMPLATE.test(slotItem)) {
                    if (! this.moveItemStackTo(slotItem, SLOT_TEMPLATE, SLOT_TEMPLATE + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (IS_COLOR.test(slotItem)) {
                    if (! this.moveItemStackTo(slotItem, SLOT_COLOR_START, SLOT_COLOR_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < INV_SLOT_END) {
                    if (! this.moveItemStackTo(slotItem, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < USE_ROW_SLOT_END && ! this.moveItemStackTo(slotItem, INV_SLOT_START, INV_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (! this.moveItemStackTo(slotItem, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
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

    @Override
    public boolean stillValid (Player pPlayer) {
        return stillValid(this.access, pPlayer, ModBlocks.CHISEL_TABLE);
    }

    class OutputSlot extends SlotFabricItemStorage {

        public OutputSlot(FabricItemStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
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
            ChiselTableMenu.this.slotsChanged(this);
        }

        @Override
        public void onQuickCraft(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn) {
            int i = newStackIn.getCount() - oldStackIn.getCount();
            if (i > 0) {
                this.onQuickCraft(newStackIn, i);
            }
        }
    }

    class IngredientSlot extends SlotFabricItemStorage{

        public IngredientSlot(FabricItemStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public void setChanged() {
            super.setChanged();
            ChiselTableMenu.this.slotsChanged(this);
        }
    }

    class TemplateSlot extends IngredientSlot {

        public TemplateSlot(FabricItemStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return IS_TEMPLATE.test(stack);
        }


    }

    class MaterialSlot extends IngredientSlot {

        public MaterialSlot(FabricItemStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return IS_WOOD.test(stack);
        }

    }

    class DyeSlot extends IngredientSlot {

        public DyeSlot(FabricItemStorage itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@Nonnull ItemStack stack) {
            return IS_COLOR.test(stack);
        }

    }
}