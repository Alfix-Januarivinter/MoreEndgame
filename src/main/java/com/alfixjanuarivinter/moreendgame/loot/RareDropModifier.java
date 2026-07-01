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

    // 1. Zombie loot table
    private static final ResourceKey<LootTable> ZOMBIE_LOOT =
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("entities/zombie"));

    // 2. All log block loot tables
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

    // 3. Stone, deepslate, and all ores
    private static final Set<ResourceKey<LootTable>> STONE_AND_ORE_LOOT = Set.of(
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/stone")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate")),
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
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_lapis_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/diamond_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_diamond_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/emerald_ore")),
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("blocks/deepslate_emerald_ore"))
    );

    public static void register() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            // 1. Zombie: 1/1000 chance
            if (ZOMBIE_LOOT.equals(key)) {
                LootPool pool = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.UNDEAD_SPIRIT))
                        .when(LootItemRandomChanceCondition.randomChance(0.001f))
                        .build();
                tableBuilder.pool(pool);
            }

            // 2. All logs: 1/1000 chance
            if (LOG_LOOT_TABLES.contains(key)) {
                LootPool pool = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.TREE_HEART))
                        .when(LootItemRandomChanceCondition.randomChance(0.001f))
                        .build();
                tableBuilder.pool(pool);
            }

            // 3. Stone, deepslate, and ores: 1/1000 chance
            if (STONE_AND_ORE_LOOT.contains(key)) {
                LootPool pool = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.CRYSTALLIZED_GEM))
                        .when(LootItemRandomChanceCondition.randomChance(0.001f))
                        .build();
                tableBuilder.pool(pool);
            }
        });
    }
}