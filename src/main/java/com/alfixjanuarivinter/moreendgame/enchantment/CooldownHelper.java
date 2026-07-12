package com.alfixjanuarivinter.moreendgame.enchantment;

import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class CooldownHelper {

    public static final ResourceKey<Enchantment> COOLDOWN_REDUCTION_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "cooldown_reduction"));

    public static int getModifiedCooldown(Level level, ItemStack stack, int baseCooldown) {
        // Safe context-based lookup that handles both singleplayer servers and client instances
        var registry = level.registryAccess().lookup(Registries.ENCHANTMENT).orElse(null);
        if (registry == null) return baseCooldown;

        return registry.get(COOLDOWN_REDUCTION_KEY).map(enchantmentHolder -> {
            int levelInt = EnchantmentHelper.getItemEnchantmentLevel(enchantmentHolder, stack);
            if (levelInt > 0) {
                float multiplier = 1.0f - (0.05f * levelInt);
                return Math.max(1, (int)(baseCooldown * multiplier));
            }
            return baseCooldown;
        }).orElse(baseCooldown);
    }
}