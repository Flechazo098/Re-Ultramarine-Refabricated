package com.voxelutopia.ultramarine.init.handler;

import com.voxelutopia.ultramarine.common.block.DecorativeBlock;
import com.voxelutopia.ultramarine.common.block.SnowRoofRidge;
import com.voxelutopia.ultramarine.common.tile.TravellingMerchant;
import com.voxelutopia.ultramarine.common.world.savedata.TravellingMerchantSpawnData;
import com.voxelutopia.ultramarine.init.data.ModBlockTags;
import com.voxelutopia.ultramarine.init.event.BlockEvents;
import com.voxelutopia.ultramarine.init.event.ItemEvents;
import com.voxelutopia.ultramarine.init.event.PlayerEvents;
import com.voxelutopia.ultramarine.init.registry.ModEntityTypes;
import com.voxelutopia.ultramarine.init.registry.ModItems;
import com.voxelutopia.ultramarine.init.registry.ModPoiTypes;
import com.voxelutopia.ultramarine.init.registry.ModVillagerProfessions;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Optional;

public class CommonEventHandler {

    public static void init() {
        breakSpeed();
        registerEntityAttributes();
        registerTravellingMerchantSpawn();
        registerBlockEvents();
        villagerTraders();
        itemConversion();
    }

    private static void itemConversion() {
        ItemEvents.UPDATE_EVENT.register(itemEntity -> {
            ItemStack item = itemEntity.getItem();
            if (itemEntity.isInWater() && item.is(ModItems.FIRED_BRICK) && itemEntity.getAge() >= 200) {
                itemEntity.setItem(new ItemStack(ModItems.CYAN_BRICK, item.getCount()));
                itemEntity.level().playSound(
                        null,
                        itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                        SoundEvents.LAVA_EXTINGUISH,
                        SoundSource.NEUTRAL,
                        0.5f, 1.0f
                );
                return true;
            }
            return false;
        });
    }

    private static void registerTravellingMerchantSpawn() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
            if (world.isClientSide()) return;
            if (world.dimension() != Level.OVERWORLD || world.getDayTime() % 24000 != 0) return;
            if (!world.getGameRules().getBoolean(GameRules.RULE_DO_TRADER_SPAWNING) ||
                    !world.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) ||
                    !world.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)) return;
            TravellingMerchantSpawnData spawnData = world.getDataStorage()
                    .computeIfAbsent(new SavedData.Factory<>(TravellingMerchantSpawnData::create, TravellingMerchantSpawnData::load, null), TravellingMerchantSpawnData.FILE_NAME);
            int spawnRoll = world.random.nextInt(100);
            if (spawnRoll < spawnData.getSpawnChance()) {
                spawnTrader(world);
                spawnData.resetSpawnChance();
            } else spawnData.increaseSpawnChance();
        });
    }

    private static void villagerTraders() {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 4, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 5), new ItemStack(ModItems.RAW_HEMATITE, 2), 12, 10, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 5), new ItemStack(ModItems.MAGNESITE, 2), 12, 10, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 5, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 10), new ItemStack(ModItems.JADE, 1), 6, 20, 0.05f));
        });

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.TOOLSMITH, 4, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 6), new ItemStack(ModItems.BRONZE_INGOT, 1), 12, 10, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.TOOLSMITH, 5, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 40), new ItemStack(ModItems.CARRIAGE, 1), 1, 10, 0.05f));
        });

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 3, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 4), new ItemStack(ModItems.POLISHED_ROSEWOOD_PLANK, 8), 12, 5, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 4), new ItemStack(ModItems.POLISHED_EBONY_PLANK, 8), 12, 5, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 30), new ItemStack(ModItems.SILK, 4), 4, 10, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 4, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 20), Optional.of(new ItemCost(ModItems.EMPTY_BAMBOO_TEA_BASKET, 1)), new ItemStack(ModItems.BAMBOO_TEA_BASKET, 1), 4, 10, 0.05f));
        });

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.LIBRARIAN, 4, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 20), new ItemStack(ModItems.XUAN_PAPER, 4), 10, 20, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.LIBRARIAN, 5, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 30), new ItemStack(ModItems.PORCELAIN_TEAPOT, 1), 2, 20, 0.05f));
        });

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 5, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 40), new ItemStack(ModItems.SUNDIAL, 1), 1, 20, 0.05f));
        });

        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.COOK, 1, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.PORKCHOP, 15), new ItemStack(Items.EMERALD, 1), 12, 2, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.BEEF, 15), new ItemStack(Items.EMERALD, 1), 12, 2, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.CHICKEN, 20), new ItemStack(Items.EMERALD, 1), 12, 2, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 8), new ItemStack(ModItems.COOKED_MEAT, 4), 12, 2, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.COOK, 2, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.MUTTON, 15), new ItemStack(Items.EMERALD, 1), 12, 4, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.RABBIT, 15), new ItemStack(Items.EMERALD, 1), 12, 4, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 6), new ItemStack(ModItems.GREASE, 2), 8, 5, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 6), new ItemStack(ModItems.FUR, 2), 8, 5, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.COOK, 3, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.DRIED_KELP, 40), new ItemStack(Items.EMERALD, 2), 15, 8, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.SUGAR, 40), new ItemStack(Items.EMERALD, 1), 20, 8, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EGG, 16), new ItemStack(Items.EMERALD, 1), 20, 8, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.MUNG_BEAN_CAKE, 4), 12, 10, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.MOONCAKE, 4), 12, 10, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.COOK, 4, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.CABBAGE_BASKET, 1), 3, 20, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.CELERY_BASKET, 1), 3, 20, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.ORANGE_BASKET, 1), 3, 20, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.APPLE_BASKET, 1), 3, 20, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.EGGPLANT_BASKET, 1), 3, 20, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.PEAR_BASKET, 1), 3, 20, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 5), new ItemStack(ModItems.BAOZI, 2), 6, 20, 0.05f));
        });
        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.COOK, 5, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 25), new ItemStack(ModItems.XIAOLONGBAO, 1), 2, 30, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 30), new ItemStack(ModItems.WINE_POT, 1), 2, 20, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 40), new ItemStack(ModItems.BOTTLE_GOURD, 1), 1, 30, 0.05f));
        });

        TradeOfferHelper.registerWanderingTraderOffers(2, factories -> {
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 6), new ItemStack(ModItems.INCENSE, 1), 6, 30, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 6), new ItemStack(ModItems.SILK, 1), 8, 30, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 10), new ItemStack(ModItems.JADE, 1), 2, 30, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 12), new ItemStack(ModItems.PAINTING_SCROLL, 1), 1, 30, 0.05f));
            factories.add(($1, $2) -> new MerchantOffer(new ItemCost(Items.EMERALD, 20), new ItemStack(ModItems.BLUE_AND_WHITE_PORCELAIN_VASE, 1), 1, 30, 0.05f));
        });
    }

    private static void breakSpeed() {
        PlayerEvents.BREAK_SPEED.register((player, state, pos, speed) -> {
            if (state.is(ModBlockTags.MINEABLE_WITH_SHEARS) &&
                    player.getItemInHand(player.getUsedItemHand()).is(Items.SHEARS)) {
                speed *= 4;
            }
            return speed;
        });

    }


    @SuppressWarnings("ConstantConditions")
    private static void spawnTrader(ServerLevel world) {
        ServerPlayer player = world.getRandomPlayer();
        if (player == null) return;
        BlockPos blockpos = player.blockPosition();
        world.getPoiManager().find((poi) -> poi.is(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getKey(ModPoiTypes.TRADE_POI)), (pos1) -> true, blockpos, 32, PoiManager.Occupancy.ANY).ifPresent(pos -> {
            BlockPos potentialSpawn = null;
            for (int i = 0; i < 10; ++i) {
                int x = pos.getX() + world.random.nextInt(4 * 2) - 4;
                int z = pos.getZ() + world.random.nextInt(4 * 2) - 4;
                int y = world.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                BlockPos rolledPos = new BlockPos(x, y, z);
                if (NaturalSpawner.isValidEmptySpawnBlock(world, rolledPos, world.getBlockState(rolledPos), world.getFluidState(rolledPos), EntityType.WANDERING_TRADER)) {
                    potentialSpawn = rolledPos;
                    break;
                }
            }
            if (potentialSpawn == null) return;

            boolean hasSpaceAtSpawn = true;
            for (BlockPos blockPos : BlockPos.betweenClosed(potentialSpawn, potentialSpawn.offset(1, 2, 1))) {
                if (!world.getBlockState(blockPos).getCollisionShape(world, blockPos).isEmpty()) {
                    hasSpaceAtSpawn = false;
                    break;
                }
            }

            if (hasSpaceAtSpawn && !world.getBiome(pos).is(Biomes.THE_VOID)) {
                TravellingMerchant merchant = ModEntityTypes.TRAVELLING_MERCHANT.spawn(world, potentialSpawn, MobSpawnType.EVENT);
                if (merchant == null) return;
                merchant.setDespawnDelay(12000);
                merchant.setWanderTarget(potentialSpawn);
                merchant.restrictTo(potentialSpawn, 8);
            }
        });
    }

    private static void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(ModEntityTypes.TRAVELLING_MERCHANT, TravellingMerchant.setCustomAttributes());
    }

    private static void registerBlockEvents() {

        BlockEvents.NEIGHBOR_NOTIFY.register((level, pos, state, notifiedSides, forceRedstoneUpdate) -> {
            if (state.is(Blocks.SNOW)) {
                BlockPos below = pos.below();
                BlockState blockBelow = level.getBlockState(below);

                if (blockBelow.getBlock() instanceof SnowRoofRidge ridge
                        && blockBelow.getValue(SnowRoofRidge.SNOW_LAYERS) < 15) {

                    ridge.handleSnow(blockBelow, level, below);

                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                    return false;
                }
            }

            return true;
        });

        BlockEvents.PLACE.register((level, pos, state, placer) -> {
            if (state != null && state.is(ModBlocks.BRUSH_AND_INKSTONE)) {
                BlockState below = level.getBlockState(pos.below());
                if (below.is(ModBlocks.PORCELAIN_INLAID_TABLE)) {
                    return ModBlocks.CHISEL_TABLE.defaultBlockState()
                            .setValue(DecorativeBlock.FACING, state.getValue(DecorativeBlock.FACING));
                }
            }
            return null;
        });

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClientSide()) return;
            if (state.is(ModBlocks.PORCELAIN_INLAID_TABLE) && world.getBlockState(pos.above()).is(ModBlocks.CHISEL_TABLE)) {
                world.setBlock(pos.above(), ModBlocks.BRUSH_AND_INKSTONE.defaultBlockState().setValue(DecorativeBlock.FACING, state.getValue(DecorativeBlock.FACING)), 3);
            }
        });
    }
}


