package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public class TreeHeartToolMaterial {
    public static final TagKey<Item> REPAIRS_TREE_HEART_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "repairs_tree_heart_armor"));

    public static final TagKey<Block> INCORRECT_FOR_TREE_HEART_TOOL = TagKey.create(BuiltInRegistries.BLOCK.key(),
            Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "incorrect_for_tree_heart_tool"));

    public static final ToolMaterial TREE_HEART_TOOL_MATERIAL = new ToolMaterial(
            INCORRECT_FOR_TREE_HEART_TOOL, // incorrect blocks for drops
            4062, // durability
            15.0F, // speed
            5.0F, // attack damage bonus
            16, // enchantment value
            TreeHeartToolMaterial.REPAIRS_TREE_HEART_ARMOR // repair items
    );
}
