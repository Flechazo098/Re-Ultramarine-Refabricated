package org.voxelutopia.ultramarine.common.tile;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.NbtContents;
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
import net.minecraft.world.inventory.RecipeCraftingHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.voxelutopia.ultramarine.common.inventory.BrickKilnCombinedStorage;
import org.voxelutopia.ultramarine.common.inventory.FabricItemStorage;
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
public class BrickKilnBlockEntity extends BlockEntity implements MenuProvider, RecipeCraftingHolder {

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
    }

    public static void serverTick (Level pLevel, BlockPos pPos, BlockState pState, BrickKilnBlockEntity pBlockEntity){
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
    
        if (recipe != null){
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
    
        if (pBlockEntity.isLit() || !fuelItem.isEmpty() && (!primaryItem.isEmpty() && !secondaryItem.isEmpty())) {
            int maxStack = 64;
            if (! pBlockEntity.isLit() && recipe != null) {
                pBlockEntity.canBurn(recipe, fuelItem, primaryItem, secondaryItem, resultItem, maxStack);
            }
    
            if (pBlockEntity.isLit() && pBlockEntity.canBurn(recipe, fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                ++pBlockEntity.cookingProgress;
                if (pBlockEntity.cookingProgress == pBlockEntity.cookingTotalTime) {
                    pBlockEntity.cookingProgress = 0;
                    pBlockEntity.cookingTotalTime = getTotalCookTime(pLevel, pBlockEntity);
                    if (pBlockEntity.burn(recipe, pBlockEntity, fuelItem, primaryItem, secondaryItem, resultItem, maxStack)) {
                        pBlockEntity.setRecipeUsed(recipeHolder.orElse(null));
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

    private int getBurnDuration(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            Item item = itemStack.getItem();
            return (Integer)getFuel().getOrDefault(item, 0);
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
            } else if (resultPrev.is(newResult.getItem())) {
                resultPrev.grow(newResult.getCount());
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
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);
        this.litTime = pTag.getInt("BurnTime");
        this.cookingProgress = pTag.getInt("CookTime");
        this.cookingTotalTime = pTag.getInt("CookTimeTotal");
        this.litDuration = pTag.getInt("BurnDuration");

        ListTag itemListTag = pTag.getList("Items", 10);
        for (int i = 0; i < itemListTag.size(); ++i) {
            CompoundTag itemTag = itemListTag.getCompound(i);
            int slot = itemTag.getByte("Slot") & 255;
            ItemStack stack = ItemStack.parseOptional(provider, itemTag);
            switch (slot) {
                case SLOT_INPUT_PRIMARY -> storage.getPrimaryInput().setItem(0, stack);
                case SLOT_INPUT_SECONDARY -> storage.getSecondaryInput().setItem(0, stack);
                case SLOT_FUEL -> storage.getFuel().setItem(0, stack);
                case SLOT_RESULT -> storage.getResult().setItem(0, stack);
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveAdditional(pTag, provider);
        pTag.putInt("BurnTime", this.litTime);
        pTag.putInt("CookTime", this.cookingProgress);
        pTag.putInt("CookTimeTotal", this.cookingTotalTime);
        pTag.putInt("BurnDuration", this.litDuration);

        ListTag itemListTag = new ListTag();
        for (int i = 0; i < NUM_SLOTS; i++) {
            ItemStack stack = switch (i) {
                case SLOT_INPUT_PRIMARY -> storage.getPrimaryInput().getItem(0);
                case SLOT_INPUT_SECONDARY -> storage.getSecondaryInput().getItem(0);
                case SLOT_FUEL -> storage.getFuel().getItem(0);
                case SLOT_RESULT -> storage.getResult().getItem(0);
                default -> ItemStack.EMPTY;
            };
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                stack.save(provider, itemTag);
                itemListTag.add(itemTag);
            }
        }
        pTag.put("Items", itemListTag);
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
}
