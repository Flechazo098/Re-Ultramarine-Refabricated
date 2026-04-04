package com.voxelutopia.ultramarine.common.tile;

import com.voxelutopia.ultramarine.common.menu.BrickKilnMenu;
import com.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;
import com.voxelutopia.ultramarine.init.registry.ModBlockEntities;
import com.voxelutopia.ultramarine.init.registry.ModRecipeTypes;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class BrickKilnBlockEntity extends BlockEntity implements MenuProvider {

    public static final int SLOT_INPUT_PRIMARY = 0;
    public static final int SLOT_INPUT_SECONDARY = 1;
    public static final int SLOT_FUEL = 2;
    public static final int SLOT_RESULT = 3;
    public static final int DATA_LIT_TIME = 0;
    public static final int DATA_LIT_DURATION = 1;
    public static final int DATA_COOKING_PROGRESS = 2;
    public static final int DATA_COOKING_TOTAL_TIME = 3;
    public static final int NUM_DATA_VALUES = 4;
    public static final int NUM_SLOTS = 4;
    public static final int BURN_TIME_STANDARD = 200;
    public static final int BURN_COOL_SPEED = 2;

    private static final Component CONTAINER_TITLE = Component.translatable("container.brick_kiln");

    int litTime;
    int litDuration;
    int cookingProgress;
    int cookingTotalTime;

    private final ItemStack[] items = new ItemStack[NUM_SLOTS];

    public final ContainerData dataAccess = new ContainerData() {
        public int get(int key) {
            return switch (key) {
                case DATA_LIT_TIME -> BrickKilnBlockEntity.this.litTime;
                case DATA_LIT_DURATION -> BrickKilnBlockEntity.this.litDuration;
                case DATA_COOKING_PROGRESS -> BrickKilnBlockEntity.this.cookingProgress;
                case DATA_COOKING_TOTAL_TIME -> BrickKilnBlockEntity.this.cookingTotalTime;
                default -> 0;
            };
        }

        public void set(int key, int value) {
            switch (key) {
                case DATA_LIT_TIME -> BrickKilnBlockEntity.this.litTime = value;
                case DATA_LIT_DURATION -> BrickKilnBlockEntity.this.litDuration = value;
                case DATA_COOKING_PROGRESS -> BrickKilnBlockEntity.this.cookingProgress = value;
                case DATA_COOKING_TOTAL_TIME -> BrickKilnBlockEntity.this.cookingTotalTime = value;
            }
        }

        public int getCount() {
            return 4;
        }
    };

    private final Object2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Object2IntOpenHashMap<>();

    public BrickKilnBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.BRICK_KILN, blockPos, blockState);
        Arrays.fill(items, ItemStack.EMPTY);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BrickKilnBlockEntity blockEntity) {
        boolean lit = blockEntity.isLit();
        boolean changed = false;

        ItemStack fuelItem = blockEntity.getItem(SLOT_FUEL);
        ItemStack primaryItem = blockEntity.getItem(SLOT_INPUT_PRIMARY);
        ItemStack secondaryItem = blockEntity.getItem(SLOT_INPUT_SECONDARY);
        ItemStack resultItem = blockEntity.getItem(SLOT_RESULT);

        CompositeSmeltingRecipe.CompositeSmeltingRecipeInput input = new CompositeSmeltingRecipe.CompositeSmeltingRecipeInput(primaryItem, secondaryItem);
        RecipeHolder<CompositeSmeltingRecipe> recipe = null;
        if (level instanceof ServerLevel serverLevel) {
            recipe = serverLevel.recipeAccess().getRecipeFor(ModRecipeTypes.COMPOSITE_SMELTING, input, serverLevel).orElse(null);
        }

        if (blockEntity.isLit()) {
            --blockEntity.litTime;
        }

        if (recipe != null) {
            blockEntity.cookingTotalTime = recipe.value().getCookingTime();
        }

        if (blockEntity.isLit() || !fuelItem.isEmpty() && (!primaryItem.isEmpty() && !secondaryItem.isEmpty())) {
            int maxStack = 64;
            if (!blockEntity.isLit() && recipe != null && blockEntity.canBurn(recipe.value(), fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                blockEntity.litTime = level.fuelValues().burnDuration(fuelItem);
                blockEntity.litDuration = blockEntity.litTime;
                if (blockEntity.isLit()) {
                    changed = true;
                    fuelItem.shrink(1);
                    blockEntity.setItem(SLOT_FUEL, fuelItem);
                }
            }

            if (recipe != null && blockEntity.isLit() && blockEntity.canBurn(recipe.value(), fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                ++blockEntity.cookingProgress;
                if (blockEntity.cookingProgress == blockEntity.cookingTotalTime) {
                    blockEntity.cookingProgress = 0;
                    blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);
                    if (blockEntity.burn(recipe.value(), blockEntity, fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                        blockEntity.setRecipeUsed(recipe);
                    }

                    changed = true;
                }
            } else {
                blockEntity.cookingProgress = 0;
            }
        } else if (blockEntity.cookingProgress > 0) {
            blockEntity.cookingProgress = Mth.clamp(blockEntity.cookingProgress - 2, 0, blockEntity.cookingTotalTime);
        }

        if (lit != blockEntity.isLit()) {
            changed = true;
            state = state.setValue(AbstractFurnaceBlock.LIT, blockEntity.isLit());
            level.setBlock(pos, state, 3);
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    public ItemStack getItem(int slot) {
        return slot >= 0 && slot < items.length ? items[slot] : ItemStack.EMPTY;
    }

    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < items.length) {
            items[slot] = stack;
            setChanged();
        }
    }

    public boolean isEmpty() {
        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean canBurn(@Nullable CompositeSmeltingRecipe recipe, ItemStack fuel, ItemStack primary, ItemStack secondary, ItemStack resultPrev, int maxStackSize) {
        if (recipe == null || primary.isEmpty() || secondary.isEmpty()) return false;

        CompositeSmeltingRecipe.CompositeSmeltingRecipeInput input = new CompositeSmeltingRecipe.CompositeSmeltingRecipeInput(primary, secondary);
        ItemStack result = recipe.assemble(input);
        if (result.isEmpty()) {
            return false;
        } else {
            if (resultPrev.isEmpty()) {
                return true;
            } else if (!ItemStack.isSameItem(resultPrev, result)) {
                return false;
            } else if (resultPrev.getCount() + result.getCount() <= maxStackSize && resultPrev.getCount() + result.getCount() <= resultPrev.getMaxStackSize()) {
                return true;
            } else {
                return resultPrev.getCount() + result.getCount() <= result.getMaxStackSize();
            }
        }
    }

    private boolean burn(CompositeSmeltingRecipe recipe, BrickKilnBlockEntity entity, ItemStack fuel, ItemStack primary, ItemStack secondary, ItemStack resultPrev, int maxStackSize) {
        if (!canBurn(recipe, fuel, primary, secondary, resultPrev, maxStackSize)) return false;
        CompositeSmeltingRecipe.CompositeSmeltingRecipeInput input = new CompositeSmeltingRecipe.CompositeSmeltingRecipeInput(primary, secondary);
        ItemStack newResult = recipe.assemble(input);
        if (resultPrev.isEmpty()) {
            entity.setItem(SLOT_RESULT, newResult.copy());
        } else if (resultPrev.is(newResult.getItem())) {
            resultPrev.grow(newResult.getCount());
        }

        primary.shrink(1);
        secondary.shrink(1);
        entity.setItem(SLOT_INPUT_PRIMARY, primary);
        entity.setItem(SLOT_INPUT_SECONDARY, secondary);
        return true;
    }

    private static int getTotalCookTime(Level level, BrickKilnBlockEntity entity) {
        CompositeSmeltingRecipe.CompositeSmeltingRecipeInput input =
                new CompositeSmeltingRecipe.CompositeSmeltingRecipeInput(
                        entity.getItem(SLOT_INPUT_PRIMARY),
                        entity.getItem(SLOT_INPUT_SECONDARY)
                );
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess().getRecipeFor(ModRecipeTypes.COMPOSITE_SMELTING, input, serverLevel)
                    .map(holder -> holder.value().getCookingTime())
                    .orElse(200);
        }
        return 200;
    }

    public void setRecipeUsed(@Nullable RecipeHolder<?> holder) {
        if (holder != null) {
            ResourceKey<Recipe<?>> id = holder.id();
            this.recipesUsed.addTo(id, 1);
        }
    }

    public void awardUsedRecipes(Player player) {
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
        if (player.level() instanceof ServerLevel level && !level.isClientSide()) {
            List<RecipeHolder<?>> toAward = new ArrayList<>();
            for (Object2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.recipesUsed.object2IntEntrySet()) {
                level.recipeAccess().byKey(entry.getKey()).ifPresent(holder -> {
                    toAward.add(holder);
                    Recipe<?> recipe = holder.value();
                    if (recipe instanceof AbstractCookingRecipe cooking) {
                        createExperience(level, player.position(), entry.getIntValue(), cooking.experience());
                    }
                });
            }
            player.awardRecipes(toAward);
            this.recipesUsed.clear();
        }
    }


    public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 pos) {
        List<RecipeHolder<?>> holders = new ArrayList<>();

        for (Object2IntMap.Entry<ResourceKey<Recipe<?>>> entry : this.recipesUsed.object2IntEntrySet()) {
            level.recipeAccess().byKey(entry.getKey()).ifPresent(holder -> {
                holders.add(holder);

                Recipe<?> recipe = holder.value();
                if (recipe instanceof CompositeSmeltingRecipe cooking) {
                    int used = entry.getIntValue();
                    createExperience(level, pos, used, cooking.getExp());
                }
            });
        }

        return holders;
    }


    private static void createExperience(ServerLevel pLevel, Vec3 p_155000_, int p_155001_, float p_155002_) {
        int i = Mth.floor((float) p_155001_ * p_155002_);
        float f = Mth.frac((float) p_155001_ * p_155002_);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        ExperienceOrb.award(pLevel, p_155000_, i);
    }

    private boolean isLit() {
        return this.litTime > 0;
    }

    @Override
    public Component getDisplayName() {
        return CONTAINER_TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new BrickKilnMenu(containerId, this.worldPosition, inventory, this, this.dataAccess);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.litTime = input.getIntOr("BurnTime", 0);
        this.cookingProgress = input.getIntOr("CookTime", 0);
        this.cookingTotalTime = input.getIntOr("CookTimeTotal", 0);

        Arrays.fill(items, ItemStack.EMPTY);

        for (ValueInput child : input.childrenListOrEmpty("Items")) {
            int slot = child.getByteOr("Slot", (byte) 0) & 255;
            if (slot >= items.length) {
                continue;
            }
            ItemStack itemStack = child.read("Item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
            items[slot] = itemStack;
        }

        ItemStack fuelItem = this.getItem(SLOT_FUEL);
        if (!fuelItem.isEmpty()) {
            Level level = this.getLevel();
            this.litDuration = level == null ? 0 : level.fuelValues().burnDuration(fuelItem);
        } else {
            this.litDuration = 0;
        }

        this.recipesUsed.clear();
        for (ValueInput child : input.childrenListOrEmpty("RecipesUsed")) {
            String idString = child.getStringOr("id", "");
            if (idString.isEmpty()) {
                continue;
            }
            Identifier id = Identifier.tryParse(idString);
            if (id == null) {
                continue;
            }
            int count = child.getIntOr("count", 0);
            if (count > 0) {
                this.recipesUsed.put(ResourceKey.create(Registries.RECIPE, id), count);
            }
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("BurnTime", this.litTime);
        output.putInt("CookTime", this.cookingProgress);
        output.putInt("CookTimeTotal", this.cookingTotalTime);

        ValueOutput.ValueOutputList itemsOut = output.childrenList("Items");
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (item.isEmpty()) {
                continue;
            }
            ValueOutput child = itemsOut.addChild();
            child.putByte("Slot", (byte) i);
            child.store("Item", ItemStack.OPTIONAL_CODEC, item);
        }

        ValueOutput.ValueOutputList recipesOut = output.childrenList("RecipesUsed");
        this.recipesUsed.forEach((id, count) -> {
            if (count <= 0) return;
            ValueOutput child = recipesOut.addChild();
            child.putString("id", id.identifier().toString());
            child.putInt("count", count);
        });
    }
}
