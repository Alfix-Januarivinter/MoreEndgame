package com.alfixjanuarivinter.moreendgame.Items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CrystallizedPickaxeItem extends Item {

    private static final int COOLDOWN_TICKS = 200;

    public CrystallizedPickaxeItem(Properties properties) {
        super(properties);
    }

    private void mineArea(Level level, Player player, ItemStack stack, BlockPos center) {
        Direction facing = Direction.fromYRot(player.getYRot());   // depth
        Direction widthDir = facing.getClockWise();                // left/right
        Direction heightDir = Direction.UP;                        // up/down

        int depthStart = -2, depthEnd = 1;   // 4 blocks deep (forward/back)
        int widthStart = -2, widthEnd = 1;   // 4 blocks wide
        int heightStart = -1, heightEnd = 2; // 4 blocks tall, shifted slightly upwards

        for (int d = depthStart; d <= depthEnd; d++) {
            for (int w = widthStart; w <= widthEnd; w++) {
                for (int h = heightStart; h <= heightEnd; h++) {
                    BlockPos pos = center.relative(facing, d)
                            .relative(widthDir, w)
                            .relative(heightDir, h);
                    BlockState state = level.getBlockState(pos);

                    if (state.isAir() || state.getDestroySpeed(level, pos) < 0) continue;

                    level.destroyBlock(pos, true, player);
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);

                    if (stack.isEmpty()) return;
                }
            }
        }

        level.playSound(null, center, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;
        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.PASS;
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        // Center 4 blocks in front of the player's eyes
        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        BlockPos center = BlockPos.containing(eye.add(look.scale(4.0)));

        mineArea(level, player, stack, center);
        player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResult useOn(net.minecraft.world.item.context.UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isShiftKeyDown()) return InteractionResult.PASS;
        ItemStack stack = context.getItemInHand();
        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.PASS;

        if (context.getLevel().isClientSide()) return InteractionResult.SUCCESS;

        BlockPos center = context.getClickedPos();
        mineArea(context.getLevel(), player, stack, center);
        player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
        return InteractionResult.CONSUME;
    }
}