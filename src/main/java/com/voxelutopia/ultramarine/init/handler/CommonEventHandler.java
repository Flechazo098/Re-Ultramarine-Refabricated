package com.voxelutopia.ultramarine.init.handler;

import com.voxelutopia.ultramarine.common.block.DecorativeBlock;
import com.voxelutopia.ultramarine.common.block.SnowRoofRidge;
import com.voxelutopia.ultramarine.common.tile.TravellingMerchant;
import com.voxelutopia.ultramarine.common.world.savedata.TravellingMerchantSpawnData;
import com.voxelutopia.ultramarine.init.data.ModBlockTags;
import com.voxelutopia.ultramarine.init.event.BlockEvents;
import com.voxelutopia.ultramarine.init.event.ItemEvents;
import com.voxelutopia.ultramarine.init.event.PlayerEvents;
import com.voxelutopia.ultramarine.init.registry.ModBlocks;
import com.voxelutopia.ultramarine.init.registry.ModEntityTypes;
import com.voxelutopia.ultramarine.init.registry.ModItems;
import com.voxelutopia.ultramarine.init.registry.ModPoiTypes;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;

public class CommonEventHandler {

    public static void init() {
        breakSpeed();
        registerEntityAttributes();
        registerTravellingMerchantSpawn();
        registerBlockEvents();
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
        ServerTickEvents.START_LEVEL_TICK.register(world -> {
            if (world.isClientSide()) return;
            if (world.dimension() != Level.OVERWORLD || world.getOverworldClockTime() % 24000 != 0) return;
            if (!world.getGameRules().get(GameRules.SPAWN_WANDERING_TRADERS) ||
                    !world.getGameRules().get(GameRules.SPAWN_MOBS) ||
                    !world.getGameRules().get(GameRules.ADVANCE_TIME)) return;
            TravellingMerchantSpawnData spawnData = world.getDataStorage()
                    .computeIfAbsent(TravellingMerchantSpawnData.TYPE);
            int spawnRoll = world.getRandom().nextInt(100);
            if (spawnRoll < spawnData.getSpawnChance()) {
                spawnTrader(world);
                spawnData.resetSpawnChance();
            } else spawnData.increaseSpawnChance();
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
    public static void spawnTrader(ServerLevel world) {
        ServerPlayer player = world.getRandomPlayer();
        if (player == null) return;
        BlockPos blockpos = player.blockPosition();
        world.getPoiManager().find((poi) -> poi.is(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getKey(ModPoiTypes.TRADE_POI)), (pos1) -> true, blockpos, 32, PoiManager.Occupancy.ANY).ifPresent(pos -> {
            BlockPos potentialSpawn = null;
            for (int i = 0; i < 10; ++i) {
                int x = pos.getX() + world.getRandom().nextInt(4 * 2) - 4;
                int z = pos.getZ() + world.getRandom().nextInt(4 * 2) - 4;
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
                TravellingMerchant merchant = ModEntityTypes.TRAVELLING_MERCHANT.spawn(world, potentialSpawn, EntitySpawnReason.EVENT);
                if (merchant == null) return;
                merchant.setDespawnDelay(12000);
                merchant.setWanderTarget(potentialSpawn);
                merchant.setHomeTo(potentialSpawn, 8);
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


