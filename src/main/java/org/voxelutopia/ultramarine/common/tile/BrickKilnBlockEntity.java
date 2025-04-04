package org.voxelutopia.ultramarine.common.tile;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.voxelutopia.ultramarine.Ultramarine;
import org.voxelutopia.ultramarine.common.inventory.BrickKilnCombinedStorage;
import org.voxelutopia.ultramarine.common.menu.BrickKilnMenu;
import org.voxelutopia.ultramarine.common.recipe.CompositeSmeltingRecipe;
import org.voxelutopia.ultramarine.common.wrapper.RecipeWrapper;
import org.voxelutopia.ultramarine.init.registry.ModBlockEntities;
import org.voxelutopia.ultramarine.init.registry.ModRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity.getFuel;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("unused")
public class BrickKilnBlockEntity extends BlockEntity implements MenuProvider, RecipeCraftingHolder, Container {

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

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);


    private final BrickKilnCombinedStorage storage = new BrickKilnCombinedStorage();

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
        super(ModBlockEntities.BRICK_KILN, blockPos, blockState);

        this.storage.setBlockEntity(this);
        this.storage.getPrimaryInput().setBlockEntity(this);
        this.storage.getSecondaryInput().setBlockEntity(this);
        this.storage.getFuel().setBlockEntity(this);
        this.storage.getResult().setBlockEntity(this);
    }


    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, BrickKilnBlockEntity pBlockEntity) {
        boolean lit = pBlockEntity.isLit();
        boolean changed = false;

        ItemStack fuelItem = pBlockEntity.storage.getFuel().getItem(0);
        ItemStack primaryItem = pBlockEntity.storage.getPrimaryInput().getItem(0);
        ItemStack secondaryItem = pBlockEntity.storage.getSecondaryInput().getItem(0);
        ItemStack resultItem = pBlockEntity.storage.getResult().getItem(0);
        Optional<RecipeHolder<CompositeSmeltingRecipe>> recipeHolder = pLevel.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.COMPOSITE_SMELTING, new RecipeWrapper(pBlockEntity.storage), pLevel);
        CompositeSmeltingRecipe recipe = recipeHolder.map(RecipeHolder::value).orElse(null);

        if (pBlockEntity.isLit()) {
            --pBlockEntity.litTime;
        }

        if (recipe != null) {
            pBlockEntity.cookingTotalTime = recipe.getCookingTime();
        }

        // 检查是否需要消耗新的燃料
        if (!pBlockEntity.isLit() && !fuelItem.isEmpty() && recipe != null &&
                !primaryItem.isEmpty() && !secondaryItem.isEmpty()) {
            // 获取燃料燃烧时间
            int burnTime = pBlockEntity.getBurnDuration(fuelItem);
            if (burnTime > 0) {
                // 设置燃烧时间和持续时间
                pBlockEntity.litTime = burnTime;
                pBlockEntity.litDuration = burnTime;

                Item remainingItem = fuelItem.getItem().getCraftingRemainingItem();
                if (remainingItem != null) {
                    // 如果有剩余物品，设置为剩余物品
                    pBlockEntity.storage.getFuel().setItem(0, new ItemStack(remainingItem));
                } else {
                    // 否则减少燃料数量
                    fuelItem.shrink(1);
                    pBlockEntity.storage.getFuel().setItem(0, fuelItem);
                }

                changed = true;
            }
        }

        // 修改这部分代码，正确处理烧炼逻辑
        if (pBlockEntity.isLit() && recipe != null) {
            // 检查是否可以烧炼
            boolean canBurn = false;

            // 获取配方结果物品
            ItemStack recipeResult = recipe.getResultItem(pLevel.registryAccess());

            // 添加调试日志
            Ultramarine.LOGGER.debug("砖窑烧炼 - 配方结果: {}, 数量: {}",
                    recipeResult.getItem().getDescriptionId(),
                    recipeResult.getCount());

            // 检查输出槽是否可以接受结果
            if (resultItem.isEmpty()) {
                // 输出槽为空，可以烧炼
                canBurn = true;
            } else if (ItemStack.isSameItemSameComponents(resultItem, recipeResult)) {
                // 输出槽有相同物品，检查是否可以堆叠
                int newCount = resultItem.getCount() + recipeResult.getCount();
                int maxStackSize = Math.min(resultItem.getMaxStackSize(), 64);
                canBurn = newCount <= maxStackSize;

                Ultramarine.LOGGER.debug("砖窑烧炼 - 尝试堆叠，当前: {}, 新增: {}, 总计: {}, 最大: {}, 可堆叠: {}",
                        resultItem.getCount(), recipeResult.getCount(), newCount, maxStackSize, canBurn);
            }

            // 检查输入物品是否足够
            canBurn = canBurn && !primaryItem.isEmpty() && !secondaryItem.isEmpty();

            if (canBurn) {
                // 增加烧炼进度
                ++pBlockEntity.cookingProgress;

                if (pBlockEntity.cookingProgress >= pBlockEntity.cookingTotalTime) {
                    // 烧炼完成
                    pBlockEntity.cookingProgress = 0;

                    // 处理输出物品
                    if (resultItem.isEmpty()) {
                        // 输出槽为空，直接放入结果
                        pBlockEntity.storage.getResult().setItem(0, recipeResult.copy());
                        Ultramarine.LOGGER.debug("砖窑烧炼 - 输出槽为空，放入新物品: {}, 数量: {}",
                                recipeResult.getItem().getDescriptionId(), recipeResult.getCount());
                    } else {
                        // 输出槽有相同物品，堆叠
                        int newCount = resultItem.getCount() + recipeResult.getCount();
                        resultItem.setCount(newCount);
                        pBlockEntity.storage.getResult().setItem(0, resultItem);
                        Ultramarine.LOGGER.debug("砖窑烧炼 - 堆叠物品，新数量: {}", newCount);
                    }

                    // 消耗输入物品
                    primaryItem.shrink(1);
                    pBlockEntity.storage.getPrimaryInput().setItem(0, primaryItem);

                    secondaryItem.shrink(1);
                    pBlockEntity.storage.getSecondaryInput().setItem(0, secondaryItem);

                    Ultramarine.LOGGER.debug("砖窑烧炼 - 消耗材料，主材料剩余: {}, 副材料剩余: {}",
                            primaryItem.getCount(), secondaryItem.getCount());

                    // 记录使用的配方
                    pBlockEntity.setRecipeUsed(recipeHolder.orElse(null));

                    changed = true;
                }
            } else {
                // 不能烧炼，重置进度
                pBlockEntity.cookingProgress = 0;
            }
        } else if (!pBlockEntity.isLit() && pBlockEntity.cookingProgress > 0) {
            // 如果没有燃烧，进度慢慢减少
            pBlockEntity.cookingProgress = Mth.clamp(pBlockEntity.cookingProgress - BURN_COOL_SPEED, 0, pBlockEntity.cookingTotalTime);
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

    private int getBurnDuration(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            Item item = itemStack.getItem();
            return getFuel().getOrDefault(item, 0);
        }
    }

    private boolean canBurn(@Nullable CompositeSmeltingRecipe pRecipe, ItemStack fuel, ItemStack primary, ItemStack secondary, ItemStack resultPrev, int maxStackSize) {
        if (!primary.isEmpty() && !secondary.isEmpty() && pRecipe != null) {
            ItemStack result = pRecipe.assemble(new RecipeWrapper(new SimpleContainer(primary, secondary)), level.registryAccess());
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

    private boolean burn(@Nullable CompositeSmeltingRecipe pRecipe, BrickKilnBlockEntity entity, ItemStack fuel, ItemStack primary, ItemStack secondary, ItemStack resultPrev, int maxStackSize) {
        if (this.canBurn(pRecipe, fuel, primary, secondary, resultPrev, maxStackSize)) {
            ItemStack newResult = pRecipe.assemble(new RecipeWrapper(new SimpleContainer(primary, secondary)), level.registryAccess());
            if (resultPrev.isEmpty()) {
                storage.getResult().setItem(0, newResult.copy());
            } else if (ItemStack.isSameItemSameComponents(resultPrev, newResult)) {
                // 累加新结果的count到已有堆叠
                int total = resultPrev.getCount() + newResult.getCount();
                int maxStack = resultPrev.getMaxStackSize();
                resultPrev.setCount(Math.min(total, maxStack));
            }

            primary.shrink(1);
            secondary.shrink(1);
            storage.getPrimaryInput().setItem(0, primary);
            storage.getSecondaryInput().setItem(0, secondary);
            return true;
        } else {
            return false;
        }
    }

    private static int getTotalCookTime(Level pLevel, BrickKilnBlockEntity entity) {
        Optional<RecipeHolder<CompositeSmeltingRecipe>> recipeHolder = pLevel.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.COMPOSITE_SMELTING, new RecipeWrapper(wrapRecipe(entity)), pLevel);
        return recipeHolder.map(holder -> holder.value().getCookingTime()).orElse(200);
    }

    public void setRecipeUsed(@Nullable RecipeHolder<?> pRecipe) {
        if (pRecipe != null) {
            ResourceLocation resourcelocation = pRecipe.id();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    public void awardUsedRecipes(Player pPlayer) {
    }

    public void awardUsedRecipesAndPopExperience(ServerPlayer pPlayer) {
        if (!pPlayer.level().isClientSide()) {
            Collection<RecipeHolder<?>> list = this.getRecipesToAwardAndPopExperience((ServerLevel) pPlayer.level(), pPlayer.position());
            pPlayer.awardRecipes(list);
            this.recipesUsed.clear();
        }
    }

    public Collection<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel pLevel, Vec3 pos) {
        List<RecipeHolder<?>> list = Lists.newArrayList();

        for(Object2IntMap.Entry<ResourceLocation> entry : this.recipesUsed.object2IntEntrySet()) {
            pLevel.getRecipeManager().byKey(entry.getKey()).ifPresent((recipeHolder) -> {
                list.add(recipeHolder);
                createExperience(pLevel, pos, entry.getIntValue(), ((AbstractCookingRecipe)recipeHolder.value()).getExperience());
            });
        }
        return list;
    }

    private static void createExperience(ServerLevel pLevel, Vec3 p_155000_, int p_155001_, float p_155002_) {
        int i = Mth.floor((float)p_155001_ * p_155002_);
        float f = Mth.frac((float)p_155001_ * p_155002_);
        if (f != 0.0F && Math.random() < (double)f) {
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

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        // 直接使用方块实体位置创建菜单，不通过数据包
        return new BrickKilnMenu(containerId, inventory, this.getBlockPos(), this.storage, this.dataAccess);
    }
    public BrickKilnCombinedStorage wrapHandlers(){
        return this.storage;
    }

    private static Container wrapRecipe(BrickKilnBlockEntity entity){
        return entity.storage;
    }


    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        // 1. 保存物品 - 直接从storage中获取
        NonNullList<ItemStack> items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);

        // 获取各个槽位的物品
        ItemStack primaryInput = storage.getPrimaryInput().getItem(0);
        ItemStack secondaryInput = storage.getSecondaryInput().getItem(0);
        ItemStack fuel = storage.getFuel().getItem(0);
        ItemStack result = storage.getResult().getItem(0);

        items.set(SLOT_INPUT_PRIMARY, primaryInput);
        items.set(SLOT_INPUT_SECONDARY, secondaryInput);
        items.set(SLOT_FUEL, fuel);
        items.set(SLOT_RESULT, result);

        // 确保物品正确保存
        ContainerHelper.saveAllItems(tag, items, provider);

        // 2. 保存进度数据
        tag.putShort("BurnTime", (short)this.litTime);
        tag.putShort("CookTime", (short)this.cookingProgress);
        tag.putShort("CookTimeTotal", (short)this.cookingTotalTime);

        // 3. 保存配方使用记录
        CompoundTag recipesUsedTag = new CompoundTag();
        this.recipesUsed.forEach((id, count) -> recipesUsedTag.putInt(id.toString(), count));
        tag.put("RecipesUsed", recipesUsedTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        // 1. 加载物品
        NonNullList<ItemStack> items = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, provider);

        // 直接设置到storage对象的各个槽位中
        storage.getPrimaryInput().setItem(0, items.get(SLOT_INPUT_PRIMARY).copy());
        storage.getSecondaryInput().setItem(0, items.get(SLOT_INPUT_SECONDARY).copy());
        storage.getFuel().setItem(0, items.get(SLOT_FUEL).copy());
        storage.getResult().setItem(0, items.get(SLOT_RESULT).copy());

        // 2. 加载进度数据
        this.litTime = tag.getShort("BurnTime");
        this.cookingProgress = tag.getShort("CookTime");
        this.cookingTotalTime = tag.getShort("CookTimeTotal");
        this.litDuration = this.getBurnDuration(storage.getFuel().getItem(0));

        // 3. 加载配方使用记录
        CompoundTag recipesUsedTag = tag.getCompound("RecipesUsed");
        for (String key : recipesUsedTag.getAllKeys()) {
            this.recipesUsed.put(ResourceLocation.parse(key), recipesUsedTag.getInt(key));
        }
    }

    @Override
    public int getContainerSize() {
        return NUM_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        return storage.getPrimaryInput().getItem(0).isEmpty() &&
                storage.getSecondaryInput().getItem(0).isEmpty() &&
                storage.getFuel().getItem(0).isEmpty() &&
                storage.getResult().getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return switch (slot) {
            case SLOT_INPUT_PRIMARY -> storage.getPrimaryInput().getItem(0);
            case SLOT_INPUT_SECONDARY -> storage.getSecondaryInput().getItem(0);
            case SLOT_FUEL -> storage.getFuel().getItem(0);
            case SLOT_RESULT -> storage.getResult().getItem(0);
            default -> ItemStack.EMPTY;
        };
    }

    public NonNullList<ItemStack> getInventory () {
        return inventory;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (amount <= 0) return ItemStack.EMPTY;

        ItemStack current = getItem(slot);
        if (current.isEmpty()) return ItemStack.EMPTY;

        // 计算实际可以提取的数量
        int actualAmount = Math.min(amount, current.getCount());

        // 创建要返回的物品堆
        ItemStack result = current.copy();
        result.setCount(actualAmount);

        // 更新剩余物品
        current.shrink(actualAmount);
        setItem(slot, current);

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getItem(slot);
        setItem(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        switch (slot) {
            case SLOT_INPUT_PRIMARY -> storage.getPrimaryInput().setItem(0, stack);
            case SLOT_INPUT_SECONDARY -> storage.getSecondaryInput().setItem(0, stack);
            case SLOT_FUEL -> storage.getFuel().setItem(0, stack);
            case SLOT_RESULT -> storage.getResult().setItem(0, stack);
        }
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        storage.getPrimaryInput().setItem(0, ItemStack.EMPTY);
        storage.getSecondaryInput().setItem(0, ItemStack.EMPTY);
        storage.getFuel().setItem(0, ItemStack.EMPTY);
        storage.getResult().setItem(0, ItemStack.EMPTY);
    }

    // 设置每个槽位的最大物品数量
    @Override
    public int getMaxStackSize() {
        return 64;
    }

    //    @Override
//    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
//        if (side == null) return false;
//        return switch (side) {
//            case UP -> slot == SLOT_INPUT_PRIMARY || slot == SLOT_INPUT_SECONDARY;
//            case DOWN -> slot == SLOT_RESULT;
//            default -> slot == SLOT_FUEL;
//        };
//    }
//
//    @Override
//    public boolean canExtract(int slot, ItemStack stack, Direction side) {
//        if (side == Direction.DOWN) {
//            return slot == SLOT_RESULT;
//        }
//        return true;
//    }
//
//    @Override
//    public NonNullList<ItemStack> getItems() {
//        NonNullList<ItemStack> list = NonNullList.withSize(NUM_SLOTS, ItemStack.EMPTY);
//        list.set(SLOT_INPUT_PRIMARY, storage.getPrimaryInput().getItem(0));
//        list.set(SLOT_INPUT_SECONDARY, storage.getSecondaryInput().getItem(0));
//        list.set(SLOT_FUEL, storage.getFuel().getItem(0));
//        list.set(SLOT_RESULT, storage.getResult().getItem(0));
//        return list;
//    }
//
//    @Override
//    public void setStack(int slot, ItemStack stack) {
//        switch (slot) {
//            case SLOT_INPUT_PRIMARY -> storage.getPrimaryInput().setItem(0, stack);
//            case SLOT_INPUT_SECONDARY -> storage.getSecondaryInput().setItem(0, stack);
//            case SLOT_FUEL -> storage.getFuel().setItem(0, stack);
//            case SLOT_RESULT -> storage.getResult().setItem(0, stack);
//        }
//    }

    private void smelt(@Nullable RecipeHolder<?> recipeHolder) {
        if (recipeHolder != null && canSmelt(recipeHolder)) {
            ItemStack primaryInput = this.getItem(SLOT_INPUT_PRIMARY);
            ItemStack secondaryInput = this.getItem(SLOT_INPUT_SECONDARY);
            ItemStack resultItem = recipeHolder.value().getResultItem(this.level.registryAccess());

            // 添加调试日志
            Ultramarine.LOGGER.debug("砖窑合成 - 配方结果物品: {}, 数量: {}, 最大堆叠: {}",
                    resultItem.getItem().getDescriptionId(),
                    resultItem.getCount(),
                    resultItem.getMaxStackSize());

            ItemStack currentOutput = this.getItem(SLOT_RESULT);

            // 处理输出物品
            if (currentOutput.isEmpty()) {
                // 输出槽为空，直接放入结果
                this.setItem(SLOT_RESULT, resultItem.copy());
                Ultramarine.LOGGER.debug("砖窑合成 - 输出槽为空，放入新物品");
            } else if (ItemStack.isSameItemSameComponents(currentOutput, resultItem)) {
                // 输出槽已有相同物品，尝试堆叠
                int newCount = currentOutput.getCount() + resultItem.getCount();
                int maxStackSize = Math.min(currentOutput.getMaxStackSize(), 64);

                Ultramarine.LOGGER.debug("砖窑合成 - 尝试堆叠，新数量: {}, 最大堆叠: {}", newCount, maxStackSize);

                if (newCount <= maxStackSize) {
                    // 可以完全堆叠
                    currentOutput.setCount(newCount);
                    Ultramarine.LOGGER.debug("砖窑合成 - 完全堆叠，设置数量: {}", newCount);
                } else {
                    // 只能部分堆叠，达到最大堆叠数
                    currentOutput.setCount(maxStackSize);
                    Ultramarine.LOGGER.debug("砖窑合成 - 部分堆叠，设置最大数量: {}", maxStackSize);
                }
            }

            // 消耗输入物品
            if (primaryInput.getCount() > 0) {
                primaryInput.shrink(1);
            }
            if (secondaryInput.getCount() > 0 && recipeHolder.value() instanceof CompositeSmeltingRecipe) {
                secondaryInput.shrink(1);
            }
        }
    }

    // 同时修改canSmelt方法以检查输出槽是否可以接受更多物品
    private boolean canSmelt(@Nullable RecipeHolder<?> recipeHolder) {
        if (recipeHolder == null) {
            return false;
        } else if (this.getItem(SLOT_INPUT_PRIMARY).isEmpty()) {
            return false;
        } else {
            ItemStack resultItem = recipeHolder.value().getResultItem(this.level.registryAccess());

            if (resultItem.isEmpty()) {
                return false;
            } else {
                ItemStack outputItem = this.getItem(SLOT_RESULT);
                if (outputItem.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItemSameComponents(outputItem, resultItem)) {
                    return false;
                } else {
                    // 检查是否可以堆叠更多
                    int maxStackSize = Math.min(outputItem.getMaxStackSize(), 64);
                    return outputItem.getCount() + resultItem.getCount() <= maxStackSize;
                }
            }
        }
    }
}
