package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.enchantment.CooldownHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

@SuppressWarnings("deprecation")
public class ReaperSwordItem extends Item {

    private static final float SWEEP_DAMAGE = 30.0F;
    private static final int SWEEP_RADIUS = 5;
    private static final int DURABILITY_COST = 30;
    private static final int BASE_COOLDOWN = 600;

    public ReaperSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }

        // 1. Calculate and set the cooldown identically on BOTH sides to prevent visual desync
        int cooldown = CooldownHelper.getModifiedCooldown(level, stack, BASE_COOLDOWN);
        player.getCooldowns().addCooldown(stack, cooldown);

        if (level.isClientSide()) {
            player.swing(hand);
            return InteractionResult.SUCCESS;
        }

        // Damage all mobs in a 5‑block radius (Server Side Only)
        AABB area = player.getBoundingBox().inflate(SWEEP_RADIUS);
        level.getEntitiesOfClass(LivingEntity.class, area,
                        entity -> entity != player && entity.isAlive())
                .forEach(entity -> {
                    entity.hurt(level.damageSources().playerAttack(player), SWEEP_DAMAGE);
                });

        // Durability cost
        stack.hurtAndBreak(DURABILITY_COST, player, EquipmentSlot.MAINHAND);

        // Sound & Particles
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 1.0F);

        if (player instanceof ServerPlayer serverPlayer) {
            SweepParticlePacket.sendToAll(serverPlayer);
        }

        player.swing(hand);
        return InteractionResult.CONSUME;
    }
}