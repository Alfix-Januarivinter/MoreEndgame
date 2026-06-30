package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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

    public static final Item UNDEAD_SPIRIT = register("undead_spirit", Item::new, new Item.Properties());
    public static final Item TREE_HEART = register("tree_heart", Item::new, new Item.Properties());
    public static final Item CRYSTALLIZED_GEM = register("crystallized_gem", Item::new, new Item.Properties());

    public static final Item REAPER_SWORD = register(
            "reaper_sword",
            Item::new,
            new Item.Properties().sword(REAPER_TOOL_MATERIAL, 6f, -2f)
    );

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
        });
    }

    public static void initialize() {
        registerModItems();
        MoreEndgame.LOGGER.debug("More Endgame items initialized!");
    }

}