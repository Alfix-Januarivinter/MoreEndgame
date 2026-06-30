package com.alfixjanuarivinter.moreendgame.client.datagen;

import com.alfixjanuarivinter.moreendgame.Items.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                shaped(RecipeCategory.DECORATIONS, ModItems.REAPER_SWORD)
                        .pattern("UUU")
                        .pattern("USU")
                        .pattern("UUU")
                        .define('S', Items.NETHERITE_SWORD)
                        .define('U', ModItems.UNDEAD_SPIRIT)
                        .unlockedBy(getHasName(ModItems.UNDEAD_SPIRIT), has(ModItems.UNDEAD_SPIRIT))
                        .save(output);

            }
        };
    }

    @Override
    public String getName() {
        return "More Endgame Recipes";
    }
}