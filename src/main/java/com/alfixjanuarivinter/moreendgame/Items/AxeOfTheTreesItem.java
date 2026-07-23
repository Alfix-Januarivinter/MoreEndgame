package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.enchantment.CooldownHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Consumer;

public class AxeOfTheTreesItem extends AxeItem {

    private static final int BASE_COOLDOWN = 600;

    public AxeOfTheTreesItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos origin = context.getClickedPos();
        BlockState state = level.getBlockState(origin);
        ItemStack stack = context.getItemInHand();
        var player = context.getPlayer();

        if (player == null || player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }

        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        // UPDATED: Mapped to unified lower-case keys
        boolean hasLeafScroll = tag.getBoolean("hascrystallizedscroll").orElse(false);
        boolean hasPlankScroll = tag.getBoolean("hasreaperscroll").orElse(false);

        boolean isLog = state.is(BlockTags.LOGS);
        boolean isPlank = state.is(BlockTags.PLANKS);

        if (!isLog && !(isPlank && hasPlankScroll)) {
            return InteractionResult.PASS;
        }

        int brokenCount = 1;

        if (!level.isClientSide()) {
            int maxBlocks = (isPlank && hasPlankScroll) ? 128 : 256;
            Queue<BlockPos> queue = new LinkedList<>();
            Set<BlockPos> visited = new HashSet<>();
            List<BlockPos> targets = new ArrayList<>();

            queue.add(origin);
            visited.add(origin);

            while (!queue.isEmpty() && targets.size() < maxBlocks) {
                BlockPos current = queue.poll();
                targets.add(current);

                for (BlockPos neighbor : BlockPos.betweenClosed(current.offset(-1, -1, -1), current.offset(1, 1, 1))) {
                    BlockPos immutableNeighbor = neighbor.immutable();
                    if (!visited.contains(immutableNeighbor)) {
                        BlockState neighborState = level.getBlockState(immutableNeighbor);
                        boolean match = isLog ? neighborState.is(BlockTags.LOGS) : neighborState.is(BlockTags.PLANKS);

                        if (match) {
                            visited.add(immutableNeighbor);
                            queue.add(immutableNeighbor);
                        }
                    }
                }
            }

            brokenCount = targets.size();
            targets.forEach(pos -> level.destroyBlock(pos, true, player));

            if (isLog && hasLeafScroll) {
                Queue<BlockPos> leafQueue = new LinkedList<>();
                Set<BlockPos> leafVisited = new HashSet<>();
                int leafCount = 0;

                for (BlockPos logPos : targets) {
                    leafQueue.add(logPos);
                }

                while (!leafQueue.isEmpty() && leafCount < 256) {
                    BlockPos currentLeaf = leafQueue.poll();
                    for (BlockPos neighbor : BlockPos.betweenClosed(currentLeaf.offset(-1, -1, -1), currentLeaf.offset(1, 1, 1))) {
                        BlockPos immutableNeighbor = neighbor.immutable();
                        if (!leafVisited.contains(immutableNeighbor)) {
                            BlockState neighborState = level.getBlockState(immutableNeighbor);
                            if (neighborState.is(BlockTags.LEAVES)) {
                                leafVisited.add(immutableNeighbor);
                                leafQueue.add(immutableNeighbor);
                                level.destroyBlock(immutableNeighbor, true, player);
                                leafCount++;
                            }
                        }
                    }
                }
            }
        }

        int cooldown = CooldownHelper.getModifiedCooldown(level, stack, BASE_COOLDOWN);
        float scrollMultiplier = 1.0f;
        if (hasLeafScroll) scrollMultiplier -= 0.15f;
        if (hasPlankScroll) scrollMultiplier -= 0.15f;
        cooldown = (int) (cooldown * scrollMultiplier);

        player.getCooldowns().addCooldown(stack, Math.max(1, cooldown));
        stack.hurtAndBreak(brokenCount, player, EquipmentSlot.MAINHAND);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag tooltipFlag) {
        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        boolean hasLeafScroll = tag.getBoolean("hascrystallizedscroll").orElse(false);
        boolean hasPlankScroll = tag.getBoolean("hasreaperscroll").orElse(false);

        if (hasLeafScroll || hasPlankScroll) {
            textConsumer.accept(Component.literal("Applied Upgrades:").withStyle(ChatFormatting.GOLD));

            if (hasLeafScroll) {
                textConsumer.accept(Component.literal(" - Crystallized Scroll Upgrade (Also destroys Leaves)").withStyle(ChatFormatting.GREEN));
            }
            if (hasPlankScroll) {
                textConsumer.accept(Component.literal(" - Reaper Scroll Upgrade (Destroys Planks)").withStyle(ChatFormatting.AQUA));
            }
        } else {
            textConsumer.accept(Component.literal("No upgrades applied.").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, displayComponent, textConsumer, tooltipFlag);
    }
}