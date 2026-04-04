package com.voxelutopia.ultramarine.init.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.voxelutopia.ultramarine.Ultramarine;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public final class ModVillagerTradeProvider implements DataProvider {
    private static final double DEFAULT_REPUTATION_DISCOUNT = 0.05;

    private final PackOutput.PathProvider villagerTradePathProvider;
    private final PackOutput.PathProvider tradeSetPathProvider;
    private final PackOutput.PathProvider villagerTradeTagPathProvider;

    public ModVillagerTradeProvider(FabricPackOutput output) {
        this.villagerTradePathProvider = output.createRegistryElementsPathProvider(Registries.VILLAGER_TRADE);
        this.tradeSetPathProvider = output.createRegistryElementsPathProvider(Registries.TRADE_SET);
        this.villagerTradeTagPathProvider = output.createRegistryTagsPathProvider(Registries.VILLAGER_TRADE);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        Map<Identifier, JsonElement> villagerTrades = new LinkedHashMap<>();
        addVillagerTrades(villagerTrades);
        futures.add(DataProvider.saveAll(output, Function.identity(), villagerTradePathProvider::json, villagerTrades));

        Map<Identifier, JsonElement> tradeSets = new LinkedHashMap<>();
        addTradeSets(tradeSets);
        futures.add(DataProvider.saveAll(output, Function.identity(), tradeSetPathProvider::json, tradeSets));

        Map<Identifier, JsonElement> villagerTradeTags = new LinkedHashMap<>();
        addVillagerTradeTags(villagerTradeTags);
        futures.add(DataProvider.saveAll(output, Function.identity(), villagerTradeTagPathProvider::json, villagerTradeTags));

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public String getName() {
        return "Ultramarine Villager Trades";
    }

    private static void addVillagerTrades(Map<Identifier, JsonElement> out) {
        // Mason
        out.put(id("mason/4/raw_hematite"), simpleTrade("minecraft:emerald", 5, null, "ultramarine:raw_hematite", 2, 12, 10, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("mason/4/magnesite"), simpleTrade("minecraft:emerald", 5, null, "ultramarine:magnesite", 2, 12, 10, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("mason/5/jade"), simpleTrade("minecraft:emerald", 10, null, "ultramarine:jade", 1, 6, 20, DEFAULT_REPUTATION_DISCOUNT));

        // Toolsmith
        out.put(id("toolsmith/4/bronze_ingot"), simpleTrade("minecraft:emerald", 6, null, "ultramarine:bronze_ingot", 1, 12, 10, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("toolsmith/5/carriage"), simpleTrade("minecraft:emerald", 40, null, "ultramarine:carriage", 1, 1, 10, DEFAULT_REPUTATION_DISCOUNT));

        // Farmer
        out.put(id("farmer/3/polished_rosewood_plank"), simpleTrade("minecraft:emerald", 4, null, "ultramarine:polished_rosewood_plank", 8, 12, 5, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("farmer/3/polished_ebony_plank"), simpleTrade("minecraft:emerald", 4, null, "ultramarine:polished_ebony_plank", 8, 12, 5, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("farmer/3/silk"), simpleTrade("minecraft:emerald", 30, null, "ultramarine:silk", 4, 4, 10, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("farmer/4/bamboo_tea_basket"), simpleTrade("minecraft:emerald", 20, "ultramarine:empty_bamboo_tea_basket", 1, "ultramarine:bamboo_tea_basket", 1, 4, 10, DEFAULT_REPUTATION_DISCOUNT));

        // Librarian
        out.put(id("librarian/4/xuan_paper"), simpleTrade("minecraft:emerald", 20, null, "ultramarine:xuan_paper", 4, 10, 20, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("librarian/5/porcelain_teapot"), simpleTrade("minecraft:emerald", 30, null, "ultramarine:porcelain_teapot", 1, 2, 20, DEFAULT_REPUTATION_DISCOUNT));

        // Cleric
        out.put(id("cleric/5/sundial"), simpleTrade("minecraft:emerald", 40, null, "ultramarine:sundial", 1, 1, 20, DEFAULT_REPUTATION_DISCOUNT));

        // Cook (custom profession)
        out.put(id("cook/1/porkchop_to_emerald"), simpleTrade("minecraft:porkchop", 15, null, "minecraft:emerald", 1, 12, 2, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/1/beef_to_emerald"), simpleTrade("minecraft:beef", 15, null, "minecraft:emerald", 1, 12, 2, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/1/chicken_to_emerald"), simpleTrade("minecraft:chicken", 20, null, "minecraft:emerald", 1, 12, 2, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/1/emerald_to_cooked_meat"), simpleTrade("minecraft:emerald", 8, null, "ultramarine:cooked_meat", 4, 12, 2, DEFAULT_REPUTATION_DISCOUNT));

        out.put(id("cook/2/mutton_to_emerald"), simpleTrade("minecraft:mutton", 15, null, "minecraft:emerald", 1, 12, 4, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/2/rabbit_to_emerald"), simpleTrade("minecraft:rabbit", 15, null, "minecraft:emerald", 1, 12, 4, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/2/emerald_to_grease"), simpleTrade("minecraft:emerald", 6, null, "ultramarine:grease", 2, 8, 5, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/2/emerald_to_fur"), simpleTrade("minecraft:emerald", 6, null, "ultramarine:fur", 2, 8, 5, DEFAULT_REPUTATION_DISCOUNT));

        out.put(id("cook/3/dried_kelp_to_emerald"), simpleTrade("minecraft:dried_kelp", 40, null, "minecraft:emerald", 2, 15, 8, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/3/sugar_to_emerald"), simpleTrade("minecraft:sugar", 40, null, "minecraft:emerald", 1, 20, 8, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/3/egg_to_emerald"), simpleTrade("minecraft:egg", 16, null, "minecraft:emerald", 1, 20, 8, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/3/emerald_to_mung_bean_cake"), simpleTrade("minecraft:emerald", 12, null, "ultramarine:mung_bean_cake", 4, 12, 10, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/3/emerald_to_mooncake"), simpleTrade("minecraft:emerald", 12, null, "ultramarine:mooncake", 4, 12, 10, DEFAULT_REPUTATION_DISCOUNT));

        out.put(id("cook/4/emerald_to_cabbage_basket"), simpleTrade("minecraft:emerald", 12, null, "ultramarine:cabbage_basket", 1, 3, 20, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/4/emerald_to_celery_basket"), simpleTrade("minecraft:emerald", 12, null, "ultramarine:celery_basket", 1, 3, 20, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/4/emerald_to_orange_basket"), simpleTrade("minecraft:emerald", 12, null, "ultramarine:orange_basket", 1, 3, 20, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/4/emerald_to_apple_basket"), simpleTrade("minecraft:emerald", 12, null, "ultramarine:apple_basket", 1, 3, 20, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/4/emerald_to_eggplant_basket"), simpleTrade("minecraft:emerald", 12, null, "ultramarine:eggplant_basket", 1, 3, 20, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/4/emerald_to_pear_basket"), simpleTrade("minecraft:emerald", 12, null, "ultramarine:pear_basket", 1, 3, 20, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/4/emerald_to_baozi"), simpleTrade("minecraft:emerald", 5, null, "ultramarine:baozi", 2, 6, 20, DEFAULT_REPUTATION_DISCOUNT));

        out.put(id("cook/5/emerald_to_xiaolongbao"), simpleTrade("minecraft:emerald", 25, null, "ultramarine:xiaolongbao", 1, 2, 30, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/5/emerald_to_wine_pot"), simpleTrade("minecraft:emerald", 30, null, "ultramarine:wine_pot", 1, 2, 20, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("cook/5/emerald_to_bottle_gourd"), simpleTrade("minecraft:emerald", 40, null, "ultramarine:bottle_gourd", 1, 1, 30, DEFAULT_REPUTATION_DISCOUNT));

        // Wandering trader (uncommon)
        out.put(id("wandering_trader/uncommon/incense"), simpleTrade("minecraft:emerald", 6, null, "ultramarine:incense", 1, 6, 30, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("wandering_trader/uncommon/silk"), simpleTrade("minecraft:emerald", 6, null, "ultramarine:silk", 1, 8, 30, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("wandering_trader/uncommon/jade"), simpleTrade("minecraft:emerald", 10, null, "ultramarine:jade", 1, 2, 30, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("wandering_trader/uncommon/painting_scroll"), simpleTrade("minecraft:emerald", 12, null, "ultramarine:painting_scroll", 1, 1, 30, DEFAULT_REPUTATION_DISCOUNT));
        out.put(id("wandering_trader/uncommon/blue_and_white_porcelain_vase"), simpleTrade("minecraft:emerald", 20, null, "ultramarine:blue_and_white_porcelain_vase", 1, 1, 30, DEFAULT_REPUTATION_DISCOUNT));
    }

    private static void addTradeSets(Map<Identifier, JsonElement> out) {
        // Custom profession cook.
        out.put(id("cook/level_1"), tradeSetTag("ultramarine:cook/level_1", 2.0, id("trade_set/cook/level_1")));
        out.put(id("cook/level_2"), tradeSetTag("ultramarine:cook/level_2", 2.0, id("trade_set/cook/level_2")));
        out.put(id("cook/level_3"), tradeSetTag("ultramarine:cook/level_3", 2.0, id("trade_set/cook/level_3")));
        out.put(id("cook/level_4"), tradeSetTag("ultramarine:cook/level_4", 2.0, id("trade_set/cook/level_4")));
        out.put(id("cook/level_5"), tradeSetTag("ultramarine:cook/level_5", 2.0, id("trade_set/cook/level_5")));
    }

    private static void addVillagerTradeTags(Map<Identifier, JsonElement> out) {
        // Add to vanilla professions (minecraft namespace tags).
        out.put(Identifier.fromNamespaceAndPath("minecraft", "mason/level_4"), tagValues(
                "ultramarine:mason/4/raw_hematite",
                "ultramarine:mason/4/magnesite"
        ));
        out.put(Identifier.fromNamespaceAndPath("minecraft", "mason/level_5"), tagValues(
                "ultramarine:mason/5/jade"
        ));

        out.put(Identifier.fromNamespaceAndPath("minecraft", "toolsmith/level_4"), tagValues(
                "ultramarine:toolsmith/4/bronze_ingot"
        ));
        out.put(Identifier.fromNamespaceAndPath("minecraft", "toolsmith/level_5"), tagValues(
                "ultramarine:toolsmith/5/carriage"
        ));

        out.put(Identifier.fromNamespaceAndPath("minecraft", "farmer/level_3"), tagValues(
                "ultramarine:farmer/3/polished_rosewood_plank",
                "ultramarine:farmer/3/polished_ebony_plank",
                "ultramarine:farmer/3/silk"
        ));
        out.put(Identifier.fromNamespaceAndPath("minecraft", "farmer/level_4"), tagValues(
                "ultramarine:farmer/4/bamboo_tea_basket"
        ));

        out.put(Identifier.fromNamespaceAndPath("minecraft", "librarian/level_4"), tagValues(
                "ultramarine:librarian/4/xuan_paper"
        ));
        out.put(Identifier.fromNamespaceAndPath("minecraft", "librarian/level_5"), tagValues(
                "ultramarine:librarian/5/porcelain_teapot"
        ));

        out.put(Identifier.fromNamespaceAndPath("minecraft", "cleric/level_5"), tagValues(
                "ultramarine:cleric/5/sundial"
        ));

        out.put(Identifier.fromNamespaceAndPath("minecraft", "wandering_trader/uncommon"), tagValues(
                "ultramarine:wandering_trader/uncommon/incense",
                "ultramarine:wandering_trader/uncommon/silk",
                "ultramarine:wandering_trader/uncommon/jade",
                "ultramarine:wandering_trader/uncommon/painting_scroll",
                "ultramarine:wandering_trader/uncommon/blue_and_white_porcelain_vase"
        ));

        // Custom profession cook tags.
        out.put(id("cook/level_1"), tagValues(
                "ultramarine:cook/1/porkchop_to_emerald",
                "ultramarine:cook/1/beef_to_emerald",
                "ultramarine:cook/1/chicken_to_emerald",
                "ultramarine:cook/1/emerald_to_cooked_meat"
        ));
        out.put(id("cook/level_2"), tagValues(
                "ultramarine:cook/2/mutton_to_emerald",
                "ultramarine:cook/2/rabbit_to_emerald",
                "ultramarine:cook/2/emerald_to_grease",
                "ultramarine:cook/2/emerald_to_fur"
        ));
        out.put(id("cook/level_3"), tagValues(
                "ultramarine:cook/3/dried_kelp_to_emerald",
                "ultramarine:cook/3/sugar_to_emerald",
                "ultramarine:cook/3/egg_to_emerald",
                "ultramarine:cook/3/emerald_to_mung_bean_cake",
                "ultramarine:cook/3/emerald_to_mooncake"
        ));
        out.put(id("cook/level_4"), tagValues(
                "ultramarine:cook/4/emerald_to_cabbage_basket",
                "ultramarine:cook/4/emerald_to_celery_basket",
                "ultramarine:cook/4/emerald_to_orange_basket",
                "ultramarine:cook/4/emerald_to_apple_basket",
                "ultramarine:cook/4/emerald_to_eggplant_basket",
                "ultramarine:cook/4/emerald_to_pear_basket",
                "ultramarine:cook/4/emerald_to_baozi"
        ));
        out.put(id("cook/level_5"), tagValues(
                "ultramarine:cook/5/emerald_to_xiaolongbao",
                "ultramarine:cook/5/emerald_to_wine_pot",
                "ultramarine:cook/5/emerald_to_bottle_gourd"
        ));
    }

    private static JsonObject simpleTrade(
            String wantsItemId,
            int wantsCount,
            String additionalWantsItemId,
            int additionalWantsCount,
            String givesItemId,
            int givesCount,
            int maxUses,
            int xp,
            double reputationDiscount
    ) {
        JsonObject root = new JsonObject();
        root.add("gives", itemStack(givesItemId, givesCount));
        root.addProperty("max_uses", maxUses);
        root.addProperty("reputation_discount", reputationDiscount);
        root.add("wants", itemStack(wantsItemId, wantsCount));

        if (additionalWantsItemId != null) {
            root.add("additional_wants", itemStack(additionalWantsItemId, additionalWantsCount));
        }

        root.addProperty("xp", xp);
        return root;
    }

    private static JsonObject simpleTrade(
            String wantsItemId,
            int wantsCount,
            String additionalWantsItemId,
            int additionalWantsCount,
            String givesItemId,
            int givesCount,
            int maxUses,
            int xp,
            float reputationDiscount
    ) {
        return simpleTrade(
                wantsItemId,
                wantsCount,
                additionalWantsItemId,
                additionalWantsCount,
                givesItemId,
                givesCount,
                maxUses,
                xp,
                (double) reputationDiscount
        );
    }

    private static JsonObject simpleTrade(
            String wantsItemId,
            int wantsCount,
            String additionalWantsItemId,
            String givesItemId,
            int givesCount,
            int maxUses,
            int xp,
            double reputationDiscount
    ) {
        if (additionalWantsItemId == null) {
            return simpleTrade(wantsItemId, wantsCount, null, 0, givesItemId, givesCount, maxUses, xp, reputationDiscount);
        }

        return simpleTrade(wantsItemId, wantsCount, additionalWantsItemId, 1, givesItemId, givesCount, maxUses, xp, reputationDiscount);
    }

    private static JsonObject tradeSetTag(String tradeTag, double amount, Identifier randomSequenceId) {
        JsonObject root = new JsonObject();
        root.addProperty("amount", amount);
        root.addProperty("random_sequence", randomSequenceId.toString());
        root.addProperty("trades", "#" + tradeTag);
        return root;
    }

    private static JsonObject itemStack(String itemId, int count) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", itemId);
        if (count != 1) {
            obj.addProperty("count", count);
        }
        return obj;
    }

    private static JsonObject tagValues(String... tradeIds) {
        JsonObject root = new JsonObject();
        JsonArray values = new JsonArray();
        for (String tradeId : tradeIds) {
            values.add(tradeId);
        }
        root.add("values", values);
        return root;
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Ultramarine.MOD_ID, path);
    }
}

