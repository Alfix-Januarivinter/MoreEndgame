package com.alfixjanuarivinter.moreendgame.Items;

import com.alfixjanuarivinter.moreendgame.enchantment.CooldownHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CrystallizedPickaxeItem extends Item {

    private static final int BASE_COOLDOWN = 200;

    public CrystallizedPickaxeItem(Item.Properties properties) {
        super(properties);
    }

    private void mineArea(Level level, Player player, ItemStack stack, BlockPos center, boolean hasTreeScroll) {
        Direction facing = Direction.fromYRot(player.getYRot());
        Direction widthDir = facing.getClockWise();
        Direction heightDir = Direction.UP;

        int depthStart  = hasTreeScroll ? -3 : -2;
        int depthEnd    = hasTreeScroll ?  3 :  1;
        int widthStart  = hasTreeScroll ? -3 : -2;
        int widthEnd    = hasTreeScroll ?  3 :  1;
        int heightStart = hasTreeScroll ? -3 : -1;
        int heightEnd   = hasTreeScroll ?  3 :  2;

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

    private void handleAbilityActivation(Level level, Player player, ItemStack stack, BlockPos center) {
        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        // UPDATED: Mapped to unified lower-case keys
        boolean hasReaperScroll = tag.getBoolean("hasreaperscroll").orElse(false);
        boolean hasTreeScroll = tag.getBoolean("hastreescroll").orElse(false);

        int cooldown = CooldownHelper.getModifiedCooldown(level, stack, BASE_COOLDOWN);
        float scrollMultiplier = 1.0f;
        if (hasReaperScroll) scrollMultiplier -= 0.15f;
        if (hasTreeScroll) scrollMultiplier -= 0.15f;
        cooldown = (int) (cooldown * scrollMultiplier);
        player.getCooldowns().addCooldown(stack, Math.max(1, cooldown));

        if (level.isClientSide()) return;

        if (hasReaperScroll) {
            player.addEffect(new MobEffectInstance(MobEffects.HASTE, 120, 0));
        }

        mineArea(level, player, stack, center, hasTreeScroll);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;
        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.PASS;

        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Vec3 eye = player.getEyePosition();
        Vec3 look = player.getLookAngle().normalize();
        BlockPos center = BlockPos.containing(eye.add(look.scale(4.0)));

        handleAbilityActivation(level, player, stack, center);
        return InteractionResult.CONSUME;
    }

    @Override
    public InteractionResult useOn(net.minecraft.world.item.context.UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.isShiftKeyDown()) return InteractionResult.PASS;

        ItemStack stack = context.getItemInHand();
        if (player.getCooldowns().isOnCooldown(stack)) return InteractionResult.PASS;

        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockPos center = context.getClickedPos();
        handleAbilityActivation(level, player, stack, center);
        return InteractionResult.CONSUME;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay displayComponent, java.util.function.Consumer<Component> textConsumer, TooltipFlag tooltipFlag) {
        var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
        CompoundTag tag = customData.copyTag();

        boolean hasReaperScroll = tag.getBoolean("hasreaperscroll").orElse(false);
        boolean hasTreeScroll = tag.getBoolean("hastreescroll").orElse(false);

        if (hasReaperScroll || hasTreeScroll) {
            textConsumer.accept(Component.literal("Applied Upgrades:").withStyle(ChatFormatting.GOLD));
            if (hasReaperScroll) {
                textConsumer.accept(Component.literal(" - Reaper Scroll Upgrade (Gives Haste I)").withStyle(ChatFormatting.AQUA));
            }
            if (hasTreeScroll) {
                textConsumer.accept(Component.literal(" - Tree Scroll Upgrade (Increases Range to 7 Blocks)").withStyle(ChatFormatting.GREEN));
            }
        } else {
            textConsumer.accept(Component.literal("No upgrades applied.").withStyle(ChatFormatting.GRAY));
        }

        super.appendHoverText(stack, context, displayComponent, textConsumer, tooltipFlag);
    }
}