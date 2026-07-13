package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.enchantment.CooldownHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class ReaperSwordItem extends Item {

    private static final float SWEEP_DAMAGE = 30.0F;
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

        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        boolean hasRegen = tag.getBoolean("hascrystallizedscroll").orElse(false);
        boolean hasTeleport = tag.getBoolean("hastreescroll").orElse(false);

        int cooldown = CooldownHelper.getModifiedCooldown(level, stack, BASE_COOLDOWN);

        float scrollMultiplier = 1.0f;
        if (hasRegen) scrollMultiplier -= 0.15f;
        if (hasTeleport) scrollMultiplier -= 0.15f;
        cooldown = (int) (cooldown * scrollMultiplier);

        player.getCooldowns().addCooldown(stack, Math.max(1, cooldown));

        if (hasTeleport && !level.isClientSide()) {
            Vec3 lookAngle = player.getLookAngle().normalize();
            Vec3 targetPos = player.position().add(lookAngle.scale(8.0));

            var pickResult = level.clip(new net.minecraft.world.level.ClipContext(
                    player.getEyePosition(), targetPos,
                    net.minecraft.world.level.ClipContext.Block.COLLIDER,
                    net.minecraft.world.level.ClipContext.Fluid.NONE, player));

            if (pickResult.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
                targetPos = pickResult.getLocation().subtract(lookAngle.scale(0.5));
            }

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.teleportTo(targetPos.x, targetPos.y, targetPos.z);
            }
        }

        if (level.isClientSide()) {
            player.swing(hand);
            return InteractionResult.SUCCESS;
        }

        int currentRadius = hasRegen ? 7 : 5;

        AABB area = player.getBoundingBox().inflate(currentRadius);
        level.getEntitiesOfClass(LivingEntity.class, area,
                        entity -> entity != player && entity.isAlive())
                .forEach(entity -> {
                    entity.hurt(level.damageSources().playerAttack(player), SWEEP_DAMAGE);
                });

        if (hasRegen) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 1));
        }

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

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag tooltipFlag) {
        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        // FIXED: Added .orElse(false) here as well
        boolean hasRegen = tag.getBoolean("hascrystallizedscroll").orElse(false);
        boolean hasTeleport = tag.getBoolean("hastreescroll").orElse(false);

        if (hasRegen || hasTeleport) {
            textConsumer.accept(Component.literal("Applied Upgrades:").withStyle(ChatFormatting.GOLD));

            if (hasRegen) {
                textConsumer.accept(Component.literal(" - Crystallized Scroll Upgrade (+ 1 Block Radius & Health Regen)").withStyle(ChatFormatting.AQUA));
            }
            if (hasTeleport) {
                textConsumer.accept(Component.literal(" - Tree Scroll Upgrade (Teleportation)").withStyle(ChatFormatting.GREEN));
            }
        } else {
            textConsumer.accept(Component.literal("No upgrades applied.").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, displayComponent, textConsumer, tooltipFlag);
    }
}