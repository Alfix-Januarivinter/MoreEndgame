package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public class CrystallizedToolMaterial {
    public static final TagKey<Item> REPAIRS_CRYSTALLIZED_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "repairs_crystallized_armor"));

    public static final TagKey<Block> INCORRECT_FOR_CRYSTALLIZED_TOOL = TagKey.create(BuiltInRegistries.BLOCK.key(),
            Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "incorrect_for_crystallized_tool"));

    public static final ToolMaterial CRYSTALLIZED_TOOL_MATERIAL = new ToolMaterial(
            INCORRECT_FOR_CRYSTALLIZED_TOOL, // incorrect blocks for drops
            4062, // durability
            16.0F, // speed
            5.0F, // attack damage bonus
            16, // enchantment value
            CrystallizedToolMaterial.REPAIRS_CRYSTALLIZED_ARMOR // repair items
    );
}
