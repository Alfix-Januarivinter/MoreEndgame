package com.alfixjanuarivinter.moreendgame.Items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class AxeOfTheTreesItem extends Item {

    private static final int MAX_LOGS = 256;
    private static final int COOLDOWN_TICKS = 600;

    public AxeOfTheTreesItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos clickedPos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        // Only activate when sneaking
        if (!player.isShiftKeyDown()) {
            return InteractionResult.PASS;   // allow normal axe usage (stripping, etc.)
        }

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockState clickedState = level.getBlockState(clickedPos);
        if (!clickedState.is(BlockTags.LOGS)) {
            return InteractionResult.PASS;
        }

        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }

        // Find all connected logs (BFS)
        Set<BlockPos> logs = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(clickedPos);

        while (!queue.isEmpty() && logs.size() < MAX_LOGS) {
            BlockPos current = queue.poll();
            if (!logs.add(current)) continue;

            BlockState state = level.getBlockState(current);
            if (!state.is(BlockTags.LOGS)) continue;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dy == 0 && dz == 0) continue;
                        BlockPos neighbor = current.offset(dx, dy, dz);
                        if (!logs.contains(neighbor) && level.getBlockState(neighbor).is(BlockTags.LOGS)) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }

        // Break every log and apply durability cost
        for (BlockPos pos : logs) {
            if (stack.isEmpty()) break;
            level.destroyBlock(pos, true, player);
            stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);   // Unbreaking respected
        }

        // Single wood‑breaking sound
        level.playSound(null, clickedPos, SoundEvents.WOOD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);

        player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);

        return InteractionResult.CONSUME;
    }
}