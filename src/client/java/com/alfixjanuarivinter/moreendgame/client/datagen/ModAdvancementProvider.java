package com.alfixjanuarivinter.moreendgame.client.datagen;

import com.alfixjanuarivinter.moreendgame.Items.ModItems;
import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.ConsumeItemTrigger;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {
    public ModAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider wrapperLookup, Consumer<AdvancementHolder> consumer) {
        HolderGetter<Item> itemGetter = wrapperLookup.lookupOrThrow(Registries.ITEM);
        AdvancementHolder getUndeadSpirit = Advancement.Builder.advancement()
                .display(
                        ModItems.UNDEAD_SPIRIT, // The display icon
                        Component.literal("Your First Undead Spirit"), // The title
                        Component.literal("I wounder what power it possesses"), // The description
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"), // Background image for the tab in the advancements page, if this is a root advancement (has no parent)
                        AdvancementType.TASK, // TASK, CHALLENGE, or GOAL
                        true, // Show the toast when completing it
                        true, // Announce it to chat
                        false // Hide it in the advancement tab until it's achieved
                )
                // "got_dirt" is the name referenced by other advancements when they want to have "requirements."
                .addCriterion("got_undead_spirit", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.UNDEAD_SPIRIT))
                // Give the advancement an id
                .save(consumer, Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "get_undead_spirit"));

        AdvancementHolder getTreeHeart = Advancement.Builder.advancement()
                .parent(getUndeadSpirit)
                .display(
                        ModItems.TREE_HEART, // The display icon
                        Component.literal("Your First Tree Heart"), // The title
                        Component.literal("Wait I didn't know trees had hearts"), // The description
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"), // Background image for the tab in the advancements page, if this is a root advancement (has no parent)
                        AdvancementType.TASK, // TASK, CHALLENGE, or GOAL
                        true, // Show the toast when completing it
                        true, // Announce it to chat
                        false // Hide it in the advancement tab until it's achieved
                )
                // "got_dirt" is the name referenced by other advancements when they want to have "requirements."
                .addCriterion("got_tree_heart", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TREE_HEART))
                // Give the advancement an id
                .save(consumer, Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "get_tree_heart"));

        AdvancementHolder getCrystallizedGem = Advancement.Builder.advancement()
                .parent(getTreeHeart)
                .display(
                        ModItems.CRYSTALLIZED_GEM, // The display icon
                        Component.literal("Your First Crystallized Gem"), // The title
                        Component.literal("Aren't diamonds crystallized gems"), // The description
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"), // Background image for the tab in the advancements page, if this is a root advancement (has no parent)
                        AdvancementType.TASK, // TASK, CHALLENGE, or GOAL
                        true, // Show the toast when completing it
                        true, // Announce it to chat
                        false // Hide it in the advancement tab until it's achieved
                )
                // "got_dirt" is the name referenced by other advancements when they want to have "requirements."
                .addCriterion("got_crystallized_gem", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CRYSTALLIZED_GEM))
                // Give the advancement an id
                .save(consumer, Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "get_crystallized_gem"));

        Advancement.Builder.advancement()
                .addCriterion("got_reaper_sword", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.REAPER_SWORD))
                .addCriterion("got_axe_of_the_trees", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.AXE_OF_THE_TREES))
                .addCriterion("got_crystallized_pickaxe", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CRYSTALLIZED_PICKAXE))
                // ...
                // #endregion multiple_criteria
                .parent(getCrystallizedGem)
                .display(
                        ModItems.REAPER_SWORD,
                        Component.literal("Unstoppable player"),
                        Component.literal("Got all 3 Ultimate tools"),
                        null, // Children don't need a background, the root advancement takes care of that
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .save(consumer, Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "ultimate_tools"));

    }
}