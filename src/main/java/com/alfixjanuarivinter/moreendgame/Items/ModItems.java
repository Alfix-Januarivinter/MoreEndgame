package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;

import java.util.function.Function;

public class ModItems {
    public static final ToolMaterial REAPER_TOOL_MATERIAL = new ToolMaterial(
            ReaperToolMaterial.INCORRECT_FOR_REAPER_TOOL, 4062, 9.0F, 5.0F, 16, ReaperToolMaterial.REPAIRS_REAPER_ARMOR
    );

    public static final ToolMaterial TREE_HEART_TOOL_MATERIAL = new ToolMaterial(
            TreeHeartToolMaterial.INCORRECT_FOR_TREE_HEART_TOOL, 4062, 15.0F, 5.0F, 16, TreeHeartToolMaterial.REPAIRS_TREE_HEART_ARMOR
    );

    public static final ToolMaterial CRYSTALLIZED_TOOL_MATERIAL = new ToolMaterial(
            CrystallizedToolMaterial.INCORRECT_FOR_CRYSTALLIZED_TOOL, 4062, 16.0F, 5.0F, 16, CrystallizedToolMaterial.REPAIRS_CRYSTALLIZED_ARMOR
    );

    public static final Item UNDEAD_SPIRIT = register("undead_spirit", Item::new, new Item.Properties());
    public static final Item TREE_HEART = register("tree_heart", Item::new, new Item.Properties());
    public static final Item CRYSTALLIZED_GEM = register("crystallized_gem", Item::new, new Item.Properties());

    public static final Item REAPER_SWORD = register(
            "reaper_sword", ReaperSwordItem::new, new Item.Properties().sword(REAPER_TOOL_MATERIAL, 6f, -2f).repairable(UNDEAD_SPIRIT)
    );

    public static final Item AXE_OF_THE_TREES = register(
            "axe_of_the_trees",
            properties -> new AxeOfTheTreesItem(TREE_HEART_TOOL_MATERIAL, 6f, -3f, properties),
            new Item.Properties().axe(TREE_HEART_TOOL_MATERIAL, 6f, -3f).repairable(TREE_HEART)
    );

    public static final Item CRYSTALLIZED_PICKAXE = register(
            "crystallized_pickaxe", CrystallizedPickaxeItem::new, new Item.Properties().pickaxe(CRYSTALLIZED_TOOL_MATERIAL, 2f, -2.8f).repairable(CRYSTALLIZED_GEM)
    );

    public static final Item REAPER_SCROLL = register("reaper_scroll", Item::new, new Item.Properties().stacksTo(1));
    public static final Item TREE_SCROLL = register("tree_scroll", Item::new, new Item.Properties().stacksTo(1));
    public static final Item CRYSTALLIZED_SCROLL = register("crystallized_scroll", Item::new, new Item.Properties().stacksTo(1));

    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, name));

        T item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void registerModItems() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(output -> {
            output.accept(UNDEAD_SPIRIT);
            output.accept(TREE_HEART);
            output.accept(CRYSTALLIZED_GEM);
            var lookupProvider = output.getContext().holders();
            lookupProvider.lookup(Registries.ENCHANTMENT).flatMap(enchantmentRegistry -> enchantmentRegistry.get(ResourceKey.create(
                    Registries.ENCHANTMENT,
                    Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "cooldown_reduction")
            ))).ifPresent(enchantmentHolder -> {
                int maxLevel = enchantmentHolder.value().getMaxLevel();

                for (int level = 1; level <= maxLevel; level++) {
                    ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
                    book.enchant(enchantmentHolder, level);
                    output.accept(book);
                }
            });
            output.accept(REAPER_SCROLL);
            output.accept(TREE_SCROLL);
            output.accept(CRYSTALLIZED_SCROLL);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> {
            output.accept(REAPER_SWORD);
            output.accept(AXE_OF_THE_TREES);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(output -> {
            output.accept(AXE_OF_THE_TREES);
            output.accept(CRYSTALLIZED_PICKAXE);
        });
    }

    public static void initialize() {
        registerModItems();
        MoreEndgame.LOGGER.debug("More Endgame items initialized!");
    }

}