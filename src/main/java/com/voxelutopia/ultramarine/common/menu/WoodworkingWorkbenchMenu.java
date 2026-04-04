package com.voxelutopia.ultramarine.common.menu;

import com.voxelutopia.ultramarine.common.recipe.WoodworkingRecipeAccess;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import com.voxelutopia.ultramarine.init.registry.ModMenuTypes;
import com.voxelutopia.ultramarine.init.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WoodworkingWorkbenchMenu extends AbstractContainerMenu {

    public static final int INPUT_SLOT = 0;
    public static final int RESULT_SLOT = 1;
    private static final int INV_SLOT_START = 2;
    private static final int INV_SLOT_END = 29;
    private static final int USE_ROW_SLOT_START = 29;
    private static final int USE_ROW_SLOT_END = 38;

    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex = DataSlot.standalone();
    private final Level level;

    private SelectableRecipe.SingleInputSet<StonecutterRecipe> recipesForInput = SelectableRecipe.SingleInputSet.empty();
    private List<ItemStack> outputs = new ArrayList<>();
    private ItemStack input = ItemStack.EMPTY;
    private long lastSoundTime;

    final Slot inputSlot;
    final Slot resultSlot;
    final ResultContainer resultContainer = new ResultContainer();

    Runnable slotUpdateListener = () -> {
    };

    public final Container container = new SimpleContainer(1) {
        @Override
        public void setChanged() {
            super.setChanged();
            WoodworkingWorkbenchMenu.this.slotsChanged(this);
            WoodworkingWorkbenchMenu.this.slotUpdateListener.run();
        }
    };

    public WoodworkingWorkbenchMenu(int pId, Inventory inventory) {
        this(pId, inventory, ContainerLevelAccess.NULL);
    }

    public WoodworkingWorkbenchMenu(int id, Inventory inventory, final ContainerLevelAccess levelAccess) {
        super(ModMenuTypes.WOODWORKING_WORKBENCH, id);
        this.access = levelAccess;
        this.level = inventory.player.level();

        this.inputSlot = this.addSlot(new Slot(this.container, INPUT_SLOT, 20, 33));
        this.resultSlot = this.addSlot(new Slot(this.resultContainer, RESULT_SLOT, 143, 33) {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack itemStack) {
                itemStack.onCraftedBy(player, itemStack.getCount());
                WoodworkingWorkbenchMenu.this.resultContainer.awardUsedRecipes(player, this.getRelevantItems());

                ItemStack inputStack = WoodworkingWorkbenchMenu.this.inputSlot.remove(1);
                if (!inputStack.isEmpty()) {
                    WoodworkingWorkbenchMenu.this.setupResultSlot();
                }

                levelAccess.execute((level, pos) -> {
                    long l = level.getGameTime();
                    if (WoodworkingWorkbenchMenu.this.lastSoundTime != l) {
                        level.playSound(null, pos, ModSounds.WOODWORK, SoundSource.BLOCKS, 1.0F, 1.0F);
                        WoodworkingWorkbenchMenu.this.lastSoundTime = l;
                    }
                });
                super.onTake(player, itemStack);
            }

            private List<ItemStack> getRelevantItems() {
                return List.of(WoodworkingWorkbenchMenu.this.inputSlot.getItem());
            }
        });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex() {
        return this.selectedRecipeIndex.get();
    }

    public List<ItemStack> getOutputs() {
        return this.outputs;
    }

    public int getNumOutputs() {
        return this.outputs.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.outputs.isEmpty();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(this.access, pPlayer, ModBlocks.WOODWORKING_WORKBENCH);
    }

    @Override
    public boolean clickMenuButton(Player pPlayer, int pId) {
        if (this.selectedRecipeIndex.get() == pId) {
            return false;
        }
        if (this.isValidRecipeIndex(pId)) {
            this.selectedRecipeIndex.set(pId);
            this.setupResultSlot();
        }
        return true;
    }

    private boolean isValidRecipeIndex(int pId) {
        return pId >= 0 && pId < this.outputs.size();
    }

    @Override
    public void slotsChanged(Container pInventory) {
        ItemStack itemstack = this.inputSlot.getItem();
        if (!itemstack.is(this.input.getItem())) {
            this.input = itemstack.copy();
            this.setupRecipeList(itemstack);
        }
    }

    private void setupRecipeList(ItemStack inputStack) {
        this.selectedRecipeIndex.set(-1);
        this.resultSlot.set(ItemStack.EMPTY);
        this.outputs.clear();

        if (!inputStack.isEmpty()) {
            this.recipesForInput = ((WoodworkingRecipeAccess) this.level.recipeAccess()).ultramarine$woodworkingRecipes().selectByInput(inputStack);
        } else {
            this.recipesForInput = SelectableRecipe.SingleInputSet.empty();
        }

        ContextMap context = SlotDisplayContext.fromLevel(this.level);
        this.outputs = this.recipesForInput.entries().stream()
                .map(SelectableRecipe.SingleInputEntry::recipe)
                .map(SelectableRecipe::optionDisplay)
                .map(display -> display.resolveForFirstStack(context))
                .filter(stack -> !stack.isEmpty())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    void setupResultSlot() {
        if (!this.outputs.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            ItemStack resultStack = this.outputs.get(this.selectedRecipeIndex.get()).copy();
            if (resultStack.isItemEnabled(this.level.enabledFeatures())) {
                this.resultSlot.set(resultStack);
            } else {
                this.resultSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.resultSlot.set(ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    @Override
    public @NotNull MenuType<?> getType() {
        return ModMenuTypes.WOODWORKING_WORKBENCH;
    }

    public void registerUpdateListener(Runnable pListener) {
        this.slotUpdateListener = pListener;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultContainer && super.canTakeItemForPickAll(pStack, pSlot);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();

            if (pIndex == RESULT_SLOT) {
                item.onCraftedBy(itemstack1, pPlayer);
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (pIndex == INPUT_SLOT) {
                if (!this.moveItemStackTo(itemstack1, INV_SLOT_START, USE_ROW_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (((WoodworkingRecipeAccess) this.level.recipeAccess()).ultramarine$woodworkingRecipes().acceptsInput(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, INPUT_SLOT, RESULT_SLOT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= INV_SLOT_START && pIndex < INV_SLOT_END) {
                if (!this.moveItemStackTo(itemstack1, USE_ROW_SLOT_START, USE_ROW_SLOT_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= USE_ROW_SLOT_START && pIndex < USE_ROW_SLOT_END && !this.moveItemStackTo(itemstack1, INV_SLOT_START, INV_SLOT_END, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            }

            slot.setChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
            if (pIndex == RESULT_SLOT) {
                pPlayer.drop(itemstack1, false);
            }
            this.broadcastChanges();
        }

        return itemstack;
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((level, pos) -> this.clearContainer(pPlayer, this.container));
    }
}