package com.voxelutopia.ultramarine.world.block.entity;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.voxelutopia.ultramarine.data.recipe.CompositeSmeltingRecipe;
import com.voxelutopia.ultramarine.data.registry.BlockEntityRegistry;
import com.voxelutopia.ultramarine.data.registry.RecipeTypeRegistry;
import com.voxelutopia.ultramarine.world.block.menu.BrickKilnMenu;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.RecipeWrapper;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlottedStackStorage;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
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
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class BrickKilnBlockEntity extends BlockEntity implements MenuProvider, RecipeHolder, SlottedStackStorage {

    public static final Codec<BrickKilnBlockEntity> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(entity -> entity.worldPosition),
                    BlockState.CODEC.fieldOf("blockState").forGetter(entity -> entity.getBlockState()),
                    Codec.INT.fieldOf("litTime").forGetter(entity -> entity.litTime),
                    Codec.INT.fieldOf("litDuration").forGetter(entity -> entity.litDuration),
                    Codec.INT.fieldOf("cookingProgress").forGetter(entity -> entity.cookingProgress),
                    Codec.INT.fieldOf("cookingTotalTime").forGetter(entity -> entity.cookingTotalTime)
            ).apply(instance, (pos, state, litTime, litDuration, cookingProgress, cookingTotalTime) -> {
                BrickKilnBlockEntity entity = new BrickKilnBlockEntity(pos, state);
                entity.litTime = litTime;
                entity.litDuration = litDuration;
                entity.cookingProgress = cookingProgress;
                entity.cookingTotalTime = cookingTotalTime;
                return entity;
            })
    );

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


    private final ItemStackHandler ingredientsHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final ItemStackHandler fuelHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemVariant resource, int amount) {
            ItemStack stack = resource.toStack(amount);
            return FuelRegistry.INSTANCE.get(stack.getItem()) != null;
        }
    };

    private final ItemStackHandler resultHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemVariant resource, int count) {
            return false;
        }
    };

    private LazyOptional<ItemStackHandler> ingredientsLazyHandler = LazyOptional.empty();
    private LazyOptional<ItemStackHandler> fuelLazyHandler = LazyOptional.empty();
    private LazyOptional<ItemStackHandler> resultLazyHandler = LazyOptional.empty();

    protected final ContainerData dataAccess = new ContainerData() {
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
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, BrickKilnBlockEntity pBlockEntity) {
        boolean lit = pBlockEntity.isLit();
        boolean changed = false;

        ItemStack fuelItem = pBlockEntity.fuelHandler.getStackInSlot(0);
        ItemStack primaryItem = pBlockEntity.ingredientsHandler.getStackInSlot(0);
        ItemStack secondaryItem = pBlockEntity.ingredientsHandler.getStackInSlot(1);
        ItemStack resultItem = pBlockEntity.resultHandler.getStackInSlot(0);
        CompositeSmeltingRecipe recipe = pLevel.getRecipeManager().getRecipeFor(RecipeTypeRegistry.COMPOSITE_SMELTING, wrapRecipe(pBlockEntity), pLevel).orElse(null);

        if (pBlockEntity.isLit()) {
            --pBlockEntity.litTime;
        }

        if (recipe != null) {
            pBlockEntity.cookingTotalTime = recipe.getCookingTime();
        }

        if (pBlockEntity.isLit() || !fuelItem.isEmpty() && (!primaryItem.isEmpty() && !secondaryItem.isEmpty())) {

            int maxStack = 64;
            if (!pBlockEntity.isLit() && recipe != null && pBlockEntity.canBurn(recipe, fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                pBlockEntity.litTime = FuelRegistry.INSTANCE.get(fuelItem.getItem());
                pBlockEntity.litDuration = pBlockEntity.litTime;
                if (pBlockEntity.isLit()) {
                    changed = true;
                    fuelItem.shrink(1);
                    pBlockEntity.fuelHandler.setStackInSlot(0, fuelItem);
                    //no lava buckets
                }
            }

            if (pBlockEntity.isLit() && pBlockEntity.canBurn(recipe, fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                ++pBlockEntity.cookingProgress;
                if (pBlockEntity.cookingProgress == pBlockEntity.cookingTotalTime) {
                    pBlockEntity.cookingProgress = 0;
                    pBlockEntity.cookingTotalTime = getTotalCookTime(pLevel, pBlockEntity);
                    if (pBlockEntity.burn(recipe, pBlockEntity, fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                        pBlockEntity.setRecipeUsed(recipe);
                    }

                    changed = true;
                }
            } else {
                pBlockEntity.cookingProgress = 0;
            }
        } else if (!pBlockEntity.isLit() && pBlockEntity.cookingProgress > 0) {
            pBlockEntity.cookingProgress = Mth.clamp(pBlockEntity.cookingProgress - 2, 0, pBlockEntity.cookingTotalTime);
        }

        if (lit != pBlockEntity.isLit()) {
            changed = true;
            pState = pState.setValue(AbstractFurnaceBlock.LIT, pBlockEntity.isLit());
            pLevel.setBlock(pPos, pState, 3);
        }

        if (changed) {
            setChanged(pLevel, pPos, pState);
        }
    }

    private boolean canBurn(@Nullable Recipe<Container> pRecipe, ItemStack fuel, ItemStack primary, ItemStack secondary, ItemStack resultPrev, int maxStackSize) {
        if (!primary.isEmpty() && !secondary.isEmpty() && pRecipe != null) {
            ItemStack result = (pRecipe.assemble(new SimpleContainer(primary, secondary), level.registryAccess()));
            if (result.isEmpty()) {
                return false;
            } else {
                if (resultPrev.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(resultPrev, result)) {
                    return false;
                } else if (resultPrev.getCount() + result.getCount() <= maxStackSize && resultPrev.getCount() + result.getCount() <= resultPrev.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    return true;
                } else {
                    return resultPrev.getCount() + result.getCount() <= result.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }

    private boolean burn(Recipe<Container> pRecipe, BrickKilnBlockEntity entity, ItemStack fuel, ItemStack primary, ItemStack secondary, ItemStack resultPrev, int maxStackSize) {
        if (this.canBurn(pRecipe, fuel, primary, secondary, resultPrev, maxStackSize)) {
            ItemStack newResult = (pRecipe.assemble(new SimpleContainer(primary, secondary), level.registryAccess()));
            if (resultPrev.isEmpty()) {
                entity.resultHandler.setStackInSlot(0, newResult.copy());
            } else if (resultPrev.is(newResult.getItem())) {
                resultPrev.grow(newResult.getCount());
            }

            primary.shrink(1);
            secondary.shrink(1);
            entity.ingredientsHandler.setStackInSlot(0, primary);
            entity.ingredientsHandler.setStackInSlot(1, secondary);
            return true;
        } else {
            return false;
        }
    }

    private static int getTotalCookTime(Level pLevel, BrickKilnBlockEntity entity) {
        return pLevel.getRecipeManager().getRecipeFor(RecipeTypeRegistry.COMPOSITE_SMELTING, wrapRecipe(entity), pLevel).map(CompositeSmeltingRecipe::getCookingTime).orElse(200);
    }

    public void setRecipeUsed(@Nullable Recipe<?> pRecipe) {
        if (pRecipe != null) {
            ResourceLocation resourcelocation = pRecipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }

    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipes(Player pPlayer) {
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer pPlayer) {
        if (!pPlayer.level().isClientSide()) {
            List<Recipe<?>> list = this.getRecipesToAwardAndPopExperience((ServerLevel) pPlayer.level(), pPlayer.position());
            pPlayer.awardRecipes(list);
            this.recipesUsed.clear();
        }
    }

    public List<Recipe<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pos) {
        List<Recipe<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            pLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((p_155023_) -> {
                list.add(p_155023_);
                createExperience(pLevel, pos, entry.getIntValue(), ((AbstractCookingRecipe) p_155023_).getExperience());
            });
        }

        return list;
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
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player pPlayer) {
        return new BrickKilnMenu(pContainerId, this.worldPosition, pInventory,
                this, this.dataAccess);
    }

    private static RecipeWrapper wrapRecipe(BrickKilnBlockEntity entity) {
        return new RecipeWrapper(entity.ingredientsHandler);
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.litTime = pTag.getInt("BurnTime");
        this.cookingProgress = pTag.getInt("CookTime");
        this.cookingTotalTime = pTag.getInt("CookTimeTotal");

        ListTag itemListTag = pTag.getList("Items", 10);
        for (int i = 0; i < itemListTag.size(); ++i) {
            CompoundTag itemTag = itemListTag.getCompound(i);
            int j = itemTag.getByte("Slot") & 255;
            switch (j) {
                case SLOT_INPUT_PRIMARY -> this.ingredientsHandler.setStackInSlot(0, ItemStack.of(itemTag));
                case SLOT_INPUT_SECONDARY -> this.ingredientsHandler.setStackInSlot(1, ItemStack.of(itemTag));
                case SLOT_FUEL -> this.fuelHandler.setStackInSlot(0, ItemStack.of(itemTag));
                case SLOT_RESULT -> this.resultHandler.setStackInSlot(0, ItemStack.of(itemTag));
            }
        }

        this.litDuration = getBurnTime(this.fuelHandler.getStackInSlot(0));

        CompoundTag recipesTag = pTag.getCompound("RecipesUsed");

        for (String s : recipesTag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), recipesTag.getInt(s));
        }

    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("BurnTime", this.litTime);
        pTag.putInt("CookTime", this.cookingProgress);
        pTag.putInt("CookTimeTotal", this.cookingTotalTime);

        ListTag itemListTag = new ListTag();
        var items = this.wrapHandlers();
        for (int i = 0; i < items.getSlots().size(); i++) {
            ItemStack item = items.getStackInSlot(i);
            if (!item.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                item.save(itemTag);
                itemListTag.add(itemTag);
            }
        }
        if (!itemListTag.isEmpty()) {
            pTag.put("Items", itemListTag);
        }

        CompoundTag recipesTag = new CompoundTag();
        this.recipesUsed.forEach((resourceLocation, count) -> recipesTag.putInt(resourceLocation.toString(), count));
        pTag.put("RecipesUsed", recipesTag);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        createLazyHandlers();
    }

    private int getBurnTime(ItemStack stack) {
        if (stack.isEmpty()) return 0;
        return FuelRegistry.INSTANCE.get(stack.getItem());
    }


    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        ingredientsLazyHandler.invalidate();
        resultLazyHandler.invalidate();
        fuelLazyHandler.invalidate();
    }

    private void createLazyHandlers() {
        this.ingredientsLazyHandler = LazyOptional.of(() -> ingredientsHandler);
        this.resultLazyHandler = LazyOptional.of(() -> resultHandler);
        this.fuelLazyHandler = LazyOptional.of(() -> fuelHandler);
    }

    public ItemStackHandler getIngredientsHandler() {
        return ingredientsHandler;
    }

    public ItemStackHandler getFuelHandler() {
        return fuelHandler;
    }

    public ItemStackHandler getResultHandler() {
        return resultHandler;
    }

    @Override
    public int getSlotCount() {
        return NUM_SLOTS;
    }

    @Override
    public SingleSlotStorage<ItemVariant> getSlot(int slot) {
        return switch (slot) {
            case SLOT_INPUT_PRIMARY -> ingredientsHandler.getSlot(0);
            case SLOT_INPUT_SECONDARY -> ingredientsHandler.getSlot(1);
            case SLOT_FUEL -> fuelHandler.getSlot(0);
            case SLOT_RESULT -> resultHandler.getSlot(0);
            default -> null;
        };
    }

    @Override
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        long inserted = 0;

        // 尝试插入到ingredients slots
        if (inserted < maxAmount) {
            inserted += ingredientsHandler.insert(resource, maxAmount - inserted, transaction);
        }

        // 如果是燃料，尝试插入到fuel slot
        if (inserted < maxAmount && FuelRegistry.INSTANCE.get(resource.getItem()) != null) {
            inserted += fuelHandler.insert(resource, maxAmount - inserted, transaction);
        }

        return inserted;
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        long extracted = 0;

        // 从result handler提取
        extracted += resultHandler.extract(resource, maxAmount - extracted, transaction);

        // 如果还需要提取更多，从其他handlers提取
        if (extracted < maxAmount) {
            extracted += ingredientsHandler.extract(resource, maxAmount - extracted, transaction);
        }

        if (extracted < maxAmount) {
            extracted += fuelHandler.extract(resource, maxAmount - extracted, transaction);
        }

        return extracted;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return switch (slot) {
            case SLOT_INPUT_PRIMARY -> ingredientsHandler.getStackInSlot(0);
            case SLOT_INPUT_SECONDARY -> ingredientsHandler.getStackInSlot(1);
            case SLOT_FUEL -> fuelHandler.getStackInSlot(0);
            case SLOT_RESULT -> resultHandler.getStackInSlot(0);
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        switch (slot) {
            case SLOT_INPUT_PRIMARY -> ingredientsHandler.setStackInSlot(0, stack);
            case SLOT_INPUT_SECONDARY -> ingredientsHandler.setStackInSlot(1, stack);
            case SLOT_FUEL -> fuelHandler.setStackInSlot(0, stack);
            case SLOT_RESULT -> resultHandler.setStackInSlot(0, stack);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64; // 或者根据具体slot返回不同的限制
    }

    @Override
    public boolean isItemValid(int slot, ItemVariant resource, int count) {
        return switch (slot) {
            case SLOT_INPUT_PRIMARY, SLOT_INPUT_SECONDARY -> true;
            case SLOT_FUEL -> FuelRegistry.INSTANCE.get(resource.getItem()) != null;
            case SLOT_RESULT -> false;
            default -> false;
        };
    }

    // 添加wrapHandlers方法
    public ItemStackHandler wrapHandlers() {
        ItemStackHandler combined = new ItemStackHandler(NUM_SLOTS);
        combined.setStackInSlot(SLOT_INPUT_PRIMARY, ingredientsHandler.getStackInSlot(0));
        combined.setStackInSlot(SLOT_INPUT_SECONDARY, ingredientsHandler.getStackInSlot(1));
        combined.setStackInSlot(SLOT_FUEL, fuelHandler.getStackInSlot(0));
        combined.setStackInSlot(SLOT_RESULT, resultHandler.getStackInSlot(0));
        return combined;
    }
}
