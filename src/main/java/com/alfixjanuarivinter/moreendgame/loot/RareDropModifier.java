package com.alfixjanuarivinter.moreendgame.loot;

import com.alfixjanuarivinter.moreendgame.Items.ModItems;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Set;

public class RareDropModifier {

    // 1. All Undead Mobs
    private static final Set<ResourceKey<LootTable>> UNDEAD_LOOT = Set.of(
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/zombie")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/husk")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/drowned")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/zombie_villager")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/skeleton")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/stray")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/bogged")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/zombified_piglin")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/phantom")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/wither_skeleton")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/wither"))
    );

    // 2. All Log Blocks
    private static final Set<ResourceKey<LootTable>> LOG_LOOT_TABLES = Set.of(
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/oak_log")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/spruce_log")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/birch_log")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/jungle_log")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/acacia_log")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/dark_oak_log")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/mangrove_log")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/cherry_log")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/pale_oak_log"))
    );

    // 3. Common Base Blocks (Stone / Deepslate)
    private static final Set<ResourceKey<LootTable>> BASE_STONE_LOOT = Set.of(
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/stone")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate"))
    );

    // 4. Common Ores
    private static final Set<ResourceKey<LootTable>> COMMON_ORE_LOOT = Set.of(
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/coal_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_coal_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/iron_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_iron_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/copper_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_copper_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/gold_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_gold_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/redstone_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_redstone_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/lapis_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_lapis_ore"))
    );

    // 5. Rare Ores (Diamond & Emerald)
    private static final Set<ResourceKey<LootTable>> RARE_ORE_LOOT = Set.of(
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/diamond_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_diamond_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/emerald_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_emerald_ore"))
    );

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {

            // --- UNDEAD SPIRITS ---
            // Undead Mobs: 1/500 (0.2%)
            if (UNDEAD_LOOT.contains(key)) {
                tableBuilder.pool(createSingleItemPool(ModItems.UNDEAD_SPIRIT, 0.002f));
            }

            // --- TREE HEARTS ---
            // Logs: 1/300 (~0.33%)
            if (LOG_LOOT_TABLES.contains(key)) {
                tableBuilder.pool(createSingleItemPool(ModItems.TREE_HEART, 0.00333f));
            }

            // --- CRYSTALLIZED GEMS ---
            // Stone & Deepslate: 1/5,000 (0.02%)
            if (BASE_STONE_LOOT.contains(key)) {
                tableBuilder.pool(createSingleItemPool(ModItems.CRYSTALLIZED_GEM, 0.0002f));
            }
            // Common Ores: 1/500 (0.2%)
            if (COMMON_ORE_LOOT.contains(key)) {
                tableBuilder.pool(createSingleItemPool(ModItems.CRYSTALLIZED_GEM, 0.002f));
            }
            // Rare Ores (Diamonds & Emeralds): 1/100 (1.0%)
            if (RARE_ORE_LOOT.contains(key)) {
                tableBuilder.pool(createSingleItemPool(ModItems.CRYSTALLIZED_GEM, 0.01f));
            }
        });
    }

    // Helper method to keep loot pool creation clean and concise
    private static LootPool createSingleItemPool(net.minecraft.world.item.Item item, float chance) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(item))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .build();
    }
}