package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

import java.util.function.Function;

public class ModItems {
    public static final ToolMaterial REAPER_TOOL_MATERIAL = new ToolMaterial(
            ReaperToolMaterial.INCORRECT_FOR_REAPER_TOOL, // incorrect blocks for drops
            4062, // durability
            9.0F, // speed
            5.0F, // attack damage bonus
            16, // enchantment value
            ReaperToolMaterial.REPAIRS_REAPER_ARMOR // repair items
    );

    public static final ToolMaterial TREE_HEART_TOOL_MATERIAL = new ToolMaterial(
            TreeHeartToolMaterial.INCORRECT_FOR_TREE_HEART_TOOL, // incorrect blocks for drops
            4062, // durability
            15.0F, // speed
            5.0F, // attack damage bonus
            16, // enchantment value
            TreeHeartToolMaterial.REPAIRS_TREE_HEART_ARMOR // repair items
    );

    public static final ToolMaterial CRYSTALLIZED_TOOL_MATERIAL = new ToolMaterial(
            CrystallizedToolMaterial.INCORRECT_FOR_CRYSTALLIZED_TOOL, // incorrect blocks for drops
            4062, // durability
            16.0F, // speed
            5.0F, // attack damage bonus
            16, // enchantment value
            CrystallizedToolMaterial.REPAIRS_CRYSTALLIZED_ARMOR // repair items
    );

    public static final Item UNDEAD_SPIRIT = register("undead_spirit", Item::new, new Item.Properties());
    public static final Item TREE_HEART = register("tree_heart", Item::new, new Item.Properties());
    public static final Item CRYSTALLIZED_GEM = register("crystallized_gem", Item::new, new Item.Properties());

    public static final Item REAPER_SWORD = register(
            "reaper_sword",
            ReaperSwordItem::new,
            new Item.Properties().sword(REAPER_TOOL_MATERIAL, 6f, -2f)
                    .repairable(UNDEAD_SPIRIT)
    );

    public static final Item AXE_OF_THE_TREES = register(
            "axe_of_the_trees",
            AxeOfTheTreesItem::new,
            new Item.Properties().axe(TREE_HEART_TOOL_MATERIAL, 6f, -3f)
                    .repairable(TREE_HEART)
    );

    public static final Item CRYSTALLIZED_PICKAXE = register(
            "crystallized_pickaxe",
            CrystallizedPickaxeItem::new,
            new Item.Properties().pickaxe(CRYSTALLIZED_TOOL_MATERIAL, 2f, -2.8f)
                    .repairable(CRYSTALLIZED_GEM)
    );

    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MoreEndgame.MOD_ID, name));

        T item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.accept(UNDEAD_SPIRIT);
            entries.accept(TREE_HEART);
            entries.accept(CRYSTALLIZED_GEM);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.accept(REAPER_SWORD);
            entries.accept(AXE_OF_THE_TREES);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.accept(AXE_OF_THE_TREES);
            entries.accept(CRYSTALLIZED_PICKAXE);
        });
    }

    public static void initialize() {
        registerModItems();
        MoreEndgame.LOGGER.debug("More Endgame items initialized!");
    }

}