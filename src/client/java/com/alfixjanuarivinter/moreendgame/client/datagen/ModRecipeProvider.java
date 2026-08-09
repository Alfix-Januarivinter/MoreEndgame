package com.alfixjanuarivinter.moreendgame.client.datagen;

import com.alfixjanuarivinter.moreendgame.Items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {

            @Override
            public void buildRecipes() {

                SmithingTransformRecipeBuilder.smithing(
                                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.of(Items.NETHERITE_PICKAXE),
                                Ingredient.of(ModItems.CRYSTALLIZED_SCROLL),
                                RecipeCategory.TOOLS,
                                ModItems.CRYSTALLIZED_PICKAXE
                        )
                        .unlocks(getHasName(ModItems.CRYSTALLIZED_SCROLL), has(ModItems.CRYSTALLIZED_SCROLL))
                        .save(output, getItemName(ModItems.CRYSTALLIZED_PICKAXE) + "_smithing");

                SmithingTransformRecipeBuilder.smithing(
                                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.of(Items.NETHERITE_SWORD),
                                Ingredient.of(ModItems.REAPER_SCROLL),
                                RecipeCategory.TOOLS,
                                ModItems.REAPER_SWORD
                        )
                        .unlocks(getHasName(ModItems.REAPER_SCROLL), has(ModItems.REAPER_SCROLL))
                        .save(output, getItemName(ModItems.REAPER_SWORD) + "_smithing");

                SmithingTransformRecipeBuilder.smithing(
                                Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.of(Items.NETHERITE_AXE),
                                Ingredient.of(ModItems.TREE_SCROLL),
                                RecipeCategory.TOOLS,
                                ModItems.AXE_OF_THE_TREES
                        )
                        .unlocks(getHasName(ModItems.TREE_SCROLL), has(ModItems.TREE_SCROLL))
                        .save(output, getItemName(ModItems.AXE_OF_THE_TREES) + "_smithing");

                shaped(RecipeCategory.TOOLS, ModItems.REAPER_SCROLL)
                        .pattern("UUU")
                        .pattern("USU")
                        .pattern("UUU")
                        .define('U', ModItems.UNDEAD_SPIRIT)
                        .define('S', Items.PAPER)
                        .unlockedBy(getHasName(ModItems.UNDEAD_SPIRIT), has(ModItems.UNDEAD_SPIRIT))
                        .save(output);

                shaped(RecipeCategory.TOOLS, ModItems.TREE_SCROLL)
                        .pattern("UUU")
                        .pattern("USU")
                        .pattern("UUU")
                        .define('U', ModItems.TREE_HEART)
                        .define('S', Items.PAPER)
                        .unlockedBy(getHasName(ModItems.TREE_HEART), has(ModItems.TREE_HEART))
                        .save(output);

                shaped(RecipeCategory.TOOLS, ModItems.CRYSTALLIZED_SCROLL)
                        .pattern("UUU")
                        .pattern("USU")
                        .pattern("UUU")
                        .define('U', ModItems.CRYSTALLIZED_GEM)
                        .define('S', Items.PAPER)
                        .unlockedBy(getHasName(ModItems.CRYSTALLIZED_GEM), has(ModItems.CRYSTALLIZED_GEM))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "MoreEndgame Recipes";
    }
}