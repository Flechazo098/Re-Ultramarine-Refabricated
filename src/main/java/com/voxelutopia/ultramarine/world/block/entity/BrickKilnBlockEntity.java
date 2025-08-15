package com.voxelutopia.ultramarine.world.block.entity;

import com.voxelutopia.ultramarine.data.recipe.CompositeSmeltingRecipe;
import com.voxelutopia.ultramarine.data.registry.BlockEntityRegistry;
import com.voxelutopia.ultramarine.data.registry.RecipeTypeRegistry;
import com.voxelutopia.ultramarine.world.block.menu.BrickKilnMenu;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
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

    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();

    public BrickKilnBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.BRICK_KILN, blockPos, blockState);
        // 初始化物品数组
        Arrays.fill(items, ItemStack.EMPTY);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BrickKilnBlockEntity blockEntity) {
        boolean lit = blockEntity.isLit();
        boolean changed = false;

        ItemStack fuelItem = blockEntity.getItem(SLOT_FUEL);
        ItemStack primaryItem = blockEntity.getItem(SLOT_INPUT_PRIMARY);
        ItemStack secondaryItem = blockEntity.getItem(SLOT_INPUT_SECONDARY);
        ItemStack resultItem = blockEntity.getItem(SLOT_RESULT);

        CompositeSmeltingRecipe.CompositeSmeltingRecipeInput input =
                new CompositeSmeltingRecipe.CompositeSmeltingRecipeInput(primaryItem, secondaryItem);

        Optional<net.minecraft.world.item.crafting.RecipeHolder<CompositeSmeltingRecipe>> recipeHolder = level
                .getRecipeManager()
                .getRecipeFor(RecipeTypeRegistry.COMPOSITE_SMELTING, input, level);

        if (recipeHolder.isPresent()) {
            net.minecraft.world.item.crafting.RecipeHolder<CompositeSmeltingRecipe> holder = recipeHolder.get();
            CompositeSmeltingRecipe recipe = holder.value();

            if (blockEntity.isLit()) {
                --blockEntity.litTime;
            }

            blockEntity.cookingTotalTime = recipe.getCookingTime();

            if (blockEntity.isLit() || !fuelItem.isEmpty() && (!primaryItem.isEmpty() && !secondaryItem.isEmpty())) {
                int maxStack = 64;
                if (!blockEntity.isLit() && blockEntity.canBurn(recipe, fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                    blockEntity.litTime = FuelRegistry.INSTANCE.get(fuelItem.getItem());
                    blockEntity.litDuration = blockEntity.litTime;
                    if (blockEntity.isLit()) {
                        changed = true;
                        fuelItem.shrink(1);
                        blockEntity.setItem(SLOT_FUEL, fuelItem);
                    }
                }

                if (blockEntity.isLit() && blockEntity.canBurn(recipe, fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                    ++blockEntity.cookingProgress;
                    if (blockEntity.cookingProgress == blockEntity.cookingTotalTime) {
                        blockEntity.cookingProgress = 0;
                        blockEntity.cookingTotalTime = getTotalCookTime(level, blockEntity);

                        if (blockEntity.burn(recipe, blockEntity, fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                            blockEntity.setRecipeUsed(holder);
                        }
                        changed = true;
                    }
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
        ItemStack result = recipe.assemble(input, level.registryAccess());
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
        ItemStack newResult = recipe.assemble(input, level.registryAccess());
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
        return level.getRecipeManager().getRecipeFor(RecipeTypeRegistry.COMPOSITE_SMELTING, input, level)
                .map(holder -> holder.value().getCookingTime()).orElse(200);
    }

    public void setRecipeUsed(@Nullable net.minecraft.world.item.crafting.RecipeHolder<?> holder) {
        if (holder != null) {
            ResourceLocation id = holder.id();
            this.recipesUsed.addTo(id, 1);
        }
    }

    public void awardUsedRecipes(Player player) {
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
        if (!player.level().isClientSide()) {
            List<net.minecraft.world.item.crafting.RecipeHolder<?>> toAward = new ArrayList<>();
            for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
                player.level().getRecipeManager().byKey(entry.getKey()).ifPresent(holder -> {
                    toAward.add(holder);
                    Recipe<?> recipe = holder.value();
                    if (recipe instanceof AbstractCookingRecipe cooking) {
                        createExperience(player.serverLevel(), player.position(), entry.getIntValue(), cooking.getExperience());
                    }
                });
            }
            player.awardRecipes(toAward);
            this.recipesUsed.clear();
        }
    }

    public List<net.minecraft.world.item.crafting.RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 pos) {
        List<net.minecraft.world.item.crafting.RecipeHolder<?>> holders = new ArrayList<>();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent(holder -> {
                holders.add(holder);

                Recipe<?> recipe = holder.value();
                if (recipe instanceof AbstractCookingRecipe cooking) {
                    int used = entry.getIntValue();
                    createExperience(level, pos, used, cooking.getExperience());
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
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.litTime = tag.getInt("BurnTime");
        this.cookingProgress = tag.getInt("CookTime");
        this.cookingTotalTime = tag.getInt("CookTimeTotal");

        Arrays.fill(items, ItemStack.EMPTY);

        ListTag itemListTag = tag.getList("Items", 10);
        for (int i = 0; i < itemListTag.size(); ++i) {
            CompoundTag itemTag = itemListTag.getCompound(i);
            int j = itemTag.getByte("Slot") & 255;
            if (j < items.length) {
                items[j] = ItemStack.of(itemTag);
            }
        }

        ItemStack fuelItem = this.getItem(SLOT_FUEL);
        if (!fuelItem.isEmpty()) {
            Integer fuelValue = FuelRegistry.INSTANCE.get(fuelItem.getItem());
            this.litDuration = fuelValue != null ? fuelValue : 0;
        } else {
            this.litDuration = 0;
        }

        CompoundTag recipesTag = tag.getCompound("RecipesUsed");
        for (String s : recipesTag.getAllKeys()) {
            this.recipesUsed.put(ResourceLocation.tryParse(s), recipesTag.getInt(s));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BurnTime", this.litTime);
        tag.putInt("CookTime", this.cookingProgress);
        tag.putInt("CookTimeTotal", this.cookingTotalTime);

        ListTag itemListTag = new ListTag();
        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            if (!item.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                item.save(itemTag);
                itemTag.putByte("Slot", (byte) i);
                itemListTag.add(itemTag);
            }
        }
        if (!itemListTag.isEmpty()) {
            tag.put("Items", itemListTag);
        }

        CompoundTag recipesTag = new CompoundTag();
        this.recipesUsed.forEach((resourceLocation, count) -> recipesTag.putInt(resourceLocation.toString(), count));
        tag.put("RecipesUsed", recipesTag);
    }

    public Storage<ItemVariant> getItemStorage(@Nullable Direction direction) {
        if (direction == null) {
            return new CombinedStorage<>(List.of(
                    new BrickKilnSlotStorage(SLOT_INPUT_PRIMARY),
                    new BrickKilnSlotStorage(SLOT_INPUT_SECONDARY),
                    new BrickKilnSlotStorage(SLOT_FUEL),
                    new BrickKilnSlotStorage(SLOT_RESULT)
            ));
        }

        return switch (direction) {
            case UP ->
                    new CombinedStorage<>(List.of(
                            new BrickKilnSlotStorage(SLOT_INPUT_PRIMARY),
                            new BrickKilnSlotStorage(SLOT_INPUT_SECONDARY)
                    ));
            case DOWN ->
                    new BrickKilnSlotStorage(SLOT_RESULT) {
                        @Override
                        public boolean supportsInsertion() {
                            return false;
                        }
                    };
            default ->
                    new BrickKilnSlotStorage(SLOT_FUEL) {
                        @Override
                        protected boolean canInsert(ItemVariant itemVariant) {
                            Integer fuelValue = FuelRegistry.INSTANCE.get(itemVariant.getItem());
                            return fuelValue != null && fuelValue > 0;
                        }
                    };
        };
    }

    private class BrickKilnSlotStorage implements SingleSlotStorage<ItemVariant> {
        private final int slot;

        public BrickKilnSlotStorage(int slot) {
            this.slot = slot;
        }

        @Override
        public boolean isResourceBlank() {
            return getItem(slot).isEmpty();
        }

        @Override
        public ItemVariant getResource() {
            return ItemVariant.of(getItem(slot));
        }

        @Override
        public long getAmount() {
            return getItem(slot).getCount();
        }

        @Override
        public long getCapacity() {
            return getItem(slot).getMaxStackSize();
        }

        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            if (!canInsert(resource) || maxAmount <= 0) {
                return 0;
            }

            ItemStack currentStack = getItem(slot);

            if (currentStack.isEmpty()) {
                // 空槽位，直接插入
                int insertAmount = (int) Math.min(maxAmount, resource.getItem().getMaxStackSize());
                ItemStack newStack = resource.toStack(insertAmount);

                transaction.addCloseCallback((context, result) -> {
                    if (result.wasCommitted()) {
                        setItem(slot, newStack);
                    }
                });

                return insertAmount;
            } else if (resource.matches(currentStack)) {
                // 相同物品，尝试堆叠
                int spaceLeft = currentStack.getMaxStackSize() - currentStack.getCount();
                int insertAmount = (int) Math.min(maxAmount, spaceLeft);

                if (insertAmount > 0) {
                    ItemStack newStack = currentStack.copy();
                    newStack.grow(insertAmount);

                    transaction.addCloseCallback((context, result) -> {
                        if (result.wasCommitted()) {
                            setItem(slot, newStack);
                        }
                    });

                    return insertAmount;
                }
            }

            return 0;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            if (!canExtract(resource) || maxAmount <= 0) {
                return 0;
            }

            ItemStack currentStack = getItem(slot);

            if (resource.matches(currentStack)) {
                int extractAmount = (int) Math.min(maxAmount, currentStack.getCount());

                if (extractAmount > 0) {
                    ItemStack newStack = currentStack.copy();
                    newStack.shrink(extractAmount);

                    transaction.addCloseCallback((context, result) -> {
                        if (result.wasCommitted()) {
                            setItem(slot, newStack.isEmpty() ? ItemStack.EMPTY : newStack);
                        }
                    });

                    return extractAmount;
                }
            }

            return 0;
        }

        @Override
        public Iterator<StorageView<ItemVariant>> iterator() {
            return List.<StorageView<ItemVariant>>of(this).iterator();
        }

        protected boolean canInsert(ItemVariant itemVariant) {
            return switch (slot) {
                case SLOT_INPUT_PRIMARY, SLOT_INPUT_SECONDARY -> {
                    if (level == null) yield true;

                    CompositeSmeltingRecipe.CompositeSmeltingRecipeInput primaryInput =
                            new CompositeSmeltingRecipe.CompositeSmeltingRecipeInput(itemVariant.toStack(), ItemStack.EMPTY);
                    boolean canBePrimary = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.COMPOSITE_SMELTING).stream()
                            .anyMatch(recipeHolder -> recipeHolder.value().partialMatch(primaryInput, level));

                    CompositeSmeltingRecipe.CompositeSmeltingRecipeInput secondaryInput =
                            new CompositeSmeltingRecipe.CompositeSmeltingRecipeInput(ItemStack.EMPTY, itemVariant.toStack());
                    boolean canBeSecondary = level.getRecipeManager().getAllRecipesFor(RecipeTypeRegistry.COMPOSITE_SMELTING).stream()
                            .anyMatch(recipeHolder -> recipeHolder.value().partialMatch(secondaryInput, level));

                    yield canBePrimary || canBeSecondary;
                }
                case SLOT_FUEL -> {
                    Integer fuelValue = FuelRegistry.INSTANCE.get(itemVariant.getItem());
                    yield fuelValue != null && fuelValue > 0;
                }
                case SLOT_RESULT -> false;
                default -> false;
            };
        }

        protected boolean canExtract(ItemVariant itemVariant) {
            return switch (slot) {
                case SLOT_RESULT -> true;
                case SLOT_INPUT_PRIMARY, SLOT_INPUT_SECONDARY, SLOT_FUEL -> true;
                default -> false;
            };
        }
    }
}
