package com.voxelutopia.ultramarine.common.tile;

import com.google.common.collect.ImmutableList;
import com.voxelutopia.ultramarine.init.registry.ModItems;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class TravellingMerchant extends WanderingTrader {

    private static final List<MerchantOffer> TRADE_OPTIONS_SELL = new ArrayList<>();
    private static final List<MerchantOffer> TRADE_OPTIONS_BUY = new ArrayList<>();

    public TravellingMerchant(EntityType<? extends TravellingMerchant> entityType, Level level) {
        super(entityType, level);
        var sell = new ArrayList<>(TRADE_OPTIONS_SELL);
        Collections.shuffle(sell);
        var buy = new ArrayList<>(TRADE_OPTIONS_BUY);
        Collections.shuffle(buy);
        var offers = new MerchantOffers();
        offers.addAll(sell.subList(0, 4));
        offers.addAll(buy.subList(0, 2));
        this.offers = offers;
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.27d)
                .add(Attributes.FOLLOW_RANGE, 32d);
    }

    @Override
    public @NotNull MerchantOffers getOffers() {
        if (offers != null) {
            return offers;
        }
        return null;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, serverEntity);
    }

    public static List<MerchantOffer> getTradeOptions() {
        return Stream.of(TRADE_OPTIONS_SELL, TRADE_OPTIONS_BUY).flatMap(Collection::stream).toList();
    }

    static {
        TRADE_OPTIONS_SELL.addAll(List.of(
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 10), new ItemStack(ModItems.INCENSE), 8, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 4), new ItemStack(ModItems.XUAN_PAPER), 20, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 6), new ItemStack(ModItems.SILK), 20, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 8), new ItemStack(Items.LAPIS_LAZULI), 10, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 8), new ItemStack(Items.PRISMARINE_SHARD), 10, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 15), new ItemStack(ModItems.RED_SILK_FABRIC_ROLL), 5, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 15), new ItemStack(ModItems.GREEN_SILK_FABRIC_ROLL), 5, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 15), new ItemStack(ModItems.PURPLE_SILK_FABRIC_ROLL), 5, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 20), new ItemStack(ModItems.CLAY_DOLL_MALE), 5, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 20), new ItemStack(ModItems.CLAY_DOLL_FEMALE), 5, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 30), new ItemStack(ModItems.PAINTING_SCROLL), 5, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.COPPER_CASH_COIN, 10), new ItemStack(ModItems.CHINESE_HERBS_BAG), 5, 5, 0.05f)
        ));
        TRADE_OPTIONS_BUY.addAll(List.of(
                new MerchantOffer(new ItemCost(Items.AMETHYST_SHARD), new ItemStack(ModItems.COPPER_CASH_COIN, 6), 8, 5, 0.05f),
                new MerchantOffer(new ItemCost(Items.ENDER_PEARL), new ItemStack(ModItems.COPPER_CASH_COIN, 3), 10, 5, 0.05f),
                new MerchantOffer(new ItemCost(Items.BOOK), new ItemStack(ModItems.COPPER_CASH_COIN, 3), 5, 5, 0.05f),
                new MerchantOffer(new ItemCost(Items.NAUTILUS_SHELL), new ItemStack(ModItems.COPPER_CASH_COIN, 20), 2, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.POLISHED_EBONY_PLANK), new ItemStack(ModItems.COPPER_CASH_COIN, 4), 10, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.POLISHED_ROSEWOOD_PLANK), new ItemStack(ModItems.COPPER_CASH_COIN, 4), 10, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.BLUE_AND_WHITE_PORCELAIN_PIECE), new ItemStack(ModItems.COPPER_CASH_COIN, 6), 8, 5, 0.05f),
                new MerchantOffer(new ItemCost(ModItems.JADE), new ItemStack(ModItems.COPPER_CASH_COIN, 15), 2, 5, 0.05f)
        ));
    }
}
