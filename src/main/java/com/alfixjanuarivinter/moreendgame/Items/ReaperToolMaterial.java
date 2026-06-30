package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public class ReaperToolMaterial {
    public static final TagKey<Item> REPAIRS_REAPER_ARMOR = TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "repairs_reaper_armor"));

    public static final TagKey<Block> INCORRECT_FOR_REAPER_TOOL = TagKey.create(BuiltInRegistries.BLOCK.key(),
            Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "incorrect_for_reaper_tool"));

    public static final ToolMaterial REAPER_TOOL_MATERIAL = new ToolMaterial(
            INCORRECT_FOR_REAPER_TOOL, // incorrect blocks for drops
            4062, // durability
            9.0F, // speed
            5.0F, // attack damage bonus
            16, // enchantment value
            ReaperToolMaterial.REPAIRS_REAPER_ARMOR // repair items
    );
}
