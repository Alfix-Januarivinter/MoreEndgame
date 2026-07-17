package com.alfixjanuarivinter.moreendgame.Blocks;

import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockStateComponent;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {

    public static final ResourceKey<Block> REAPER_BLOCK_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("moreendgame", "reaper_block"));
    public static final ResourceKey<Block> CRYSTALLIZED_BLOCK_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("moreendgame", "crystallized_block"));
    public static final ResourceKey<Block> TREE_HEART_BLOCK_KEY = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("moreendgame", "tree_heart_block"));

    public static final Block REAPER_BLOCK = registerBlock("reaper_block", REAPER_BLOCK_KEY,
            new ReaperBlock(BlockBehaviour.Properties.of()
                    .setId(REAPER_BLOCK_KEY)
                    .destroyTime(1.5F)
                    .explosionResistance(6.0F)
                    .sound(SoundType.STONE)));

    public static final Block CRYSTALLIZED_BLOCK = registerBlock("crystallized_block", CRYSTALLIZED_BLOCK_KEY,
            new CrystallizedBlock(BlockBehaviour.Properties.of()
                    .setId(CRYSTALLIZED_BLOCK_KEY)
                    .destroyTime(2.0F)
                    .explosionResistance(6.0F)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST)));

    public static final Block TREE_HEART_BLOCK = registerBlock("tree_heart_block", TREE_HEART_BLOCK_KEY,
            new TreeHeartBlock(BlockBehaviour.Properties.of()
                    .setId(TREE_HEART_BLOCK_KEY)
                    .destroyTime(2.0F)
                    .explosionResistance(4.0F)
                    .sound(SoundType.WOOD)));

    // FIXED: Added the missing Block Entity Type registry field
    public static final ResourceKey<BlockEntityType<?>> REAPER_BE_KEY = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath("moreendgame", "reaper_block_entity"));

    public static final BlockEntityType<ReaperBlockEntity> REAPER_BLOCK_ENTITY_TYPE = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            REAPER_BE_KEY,
            BlockEntityType.Builder.of(ReaperBlockEntity::new, REAPER_BLOCK).build(null)
    );

    private static <T extends Block> T registerBlock(String name, ResourceKey<Block> blockKey, T block) {
        Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("moreendgame", name));
        Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey)));
        return block;
    }

    public static void registerCreativeTabs() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(output -> {
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(output -> {
            output.accept(REAPER_BLOCK);

            // FIXED: Using ItemStacks + Data Components so variants actually show up uniquely
            for (TreeHeartBlock.TreeType type : TreeHeartBlock.TreeType.values()) {
                ItemStack stack = new ItemStack(TREE_HEART_BLOCK);
                stack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.EMPTY.with(TreeHeartBlock.TYPE, type));
                output.accept(stack);
            }

            for (CrystallizedBlock.Variant variant : CrystallizedBlock.Variant.values()) {
                ItemStack stack = new ItemStack(CRYSTALLIZED_BLOCK);
                stack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.EMPTY.with(CrystallizedBlock.VARIANT, variant));
                output.accept(stack);
            }
        });
    }

    public static void initialize() {
        registerCreativeTabs();
        MoreEndgame.LOGGER.debug("More Endgame blocks initialized!");
    }
}