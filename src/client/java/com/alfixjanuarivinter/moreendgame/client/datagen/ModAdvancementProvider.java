package com.alfixjanuarivinter.moreendgame.client.datagen;

import com.alfixjanuarivinter.moreendgame.Items.ModItems;
import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registryLookup
    ) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(
            HolderLookup.Provider wrapperLookup,
            Consumer<AdvancementHolder> consumer
    ) {
        AdvancementHolder getUndeadSpirit = Advancement.Builder.advancement()
                .rootDisplay(
                        ModItems.UNDEAD_SPIRIT,
                        Component.literal("Your First Undead Spirit"),
                        Component.literal("I wounder what power it possesses"),
                        Identifier.withDefaultNamespace("gui/advancements/backgrounds/adventure"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion(
                        "got_undead_spirit",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.UNDEAD_SPIRIT)
                )
                .save(consumer, Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "get_undead_spirit"));

        AdvancementHolder getTreeHeart = Advancement.Builder.advancement()
                .parent(getUndeadSpirit)
                .display(
                        ModItems.TREE_HEART,
                        Component.literal("Your First Tree Heart"),
                        Component.literal("Wait I didn't know trees had hearts"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion(
                        "got_tree_heart",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.TREE_HEART)
                )
                .save(consumer, Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "get_tree_heart"));

        AdvancementHolder getCrystallizedGem = Advancement.Builder.advancement()
                .parent(getTreeHeart)
                .display(
                        ModItems.CRYSTALLIZED_GEM,
                        Component.literal("Your First Crystallized Gem"),
                        Component.literal("Aren't diamonds crystallized gems"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion(
                        "got_crystallized_gem",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CRYSTALLIZED_GEM)
                )
                .save(consumer, Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "get_crystallized_gem"));

        Advancement.Builder.advancement()
                .addCriterion(
                        "got_reaper_sword",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.REAPER_SWORD)
                )
                .addCriterion(
                        "got_axe_of_the_trees",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.AXE_OF_THE_TREES)
                )
                .addCriterion(
                        "got_crystallized_pickaxe",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.CRYSTALLIZED_PICKAXE)
                )
                .parent(getCrystallizedGem)
                .display(
                        ModItems.REAPER_SWORD,
                        Component.literal("Unstoppable player"),
                        Component.literal("Got all 3 Ultimate tools"),
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .save(consumer, Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "ultimate_tools"));
    }
}
