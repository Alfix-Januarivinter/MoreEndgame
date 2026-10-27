package com.alfixjanuarivinter.moreendgame.util;

import com.alfixjanuarivinter.moreendgame.Items.CrystallizedPickaxeItem;
import com.alfixjanuarivinter.moreendgame.Items.AxeOfTheTreesItem;
import com.alfixjanuarivinter.moreendgame.Items.ReaperSwordItem;
import com.alfixjanuarivinter.moreendgame.Items.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class ScrollAnvilHandler {

    public static ItemStack applyScroll(ItemStack tool, Item scroll) {
        ItemStack result = tool.copy();
        CustomData customData = result.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();
        Item toolItem = tool.getItem();

        if (scroll == ModItems.REAPER_SCROLL) {
            if (toolItem instanceof AxeOfTheTreesItem || toolItem instanceof CrystallizedPickaxeItem) {
                if (tag.getBoolean("hasreaperscroll").orElse(false)) return ItemStack.EMPTY;
                tag.putBoolean("hasreaperscroll", true);
            } else {
                return ItemStack.EMPTY;
            }

        } else if (scroll == ModItems.TREE_SCROLL) {
            if (toolItem instanceof ReaperSwordItem || toolItem instanceof CrystallizedPickaxeItem) {
                if (tag.getBoolean("hastreescroll").orElse(false)) return ItemStack.EMPTY;
                tag.putBoolean("hastreescroll", true);
            } else {
                return ItemStack.EMPTY;
            }

        } else if (scroll == ModItems.CRYSTALLIZED_SCROLL) {
            if (toolItem instanceof ReaperSwordItem || toolItem instanceof AxeOfTheTreesItem) {
                if (tag.getBoolean("hascrystallizedscroll").orElse(false)) return ItemStack.EMPTY;
                tag.putBoolean("hascrystallizedscroll", true);
            } else {
                return ItemStack.EMPTY;
            }

        } else {
            return ItemStack.EMPTY;
        }

        result.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return result;
    }
}