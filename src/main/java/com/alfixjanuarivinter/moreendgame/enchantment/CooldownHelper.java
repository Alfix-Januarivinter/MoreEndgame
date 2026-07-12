package com.alfixjanuarivinter.moreendgame.enchantment;

import com.alfixjanuarivinter.moreendgame.MoreEndgame;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class CooldownHelper {

    private static final ResourceKey<Enchantment> COOLDOWN_REDUCTION_KEY =
            ResourceKey.create(Registries.ENCHANTMENT,
                    Identifier.fromNamespaceAndPath(MoreEndgame.MOD_ID, "cooldown_reduction"));

    private static Holder<Enchantment> cachedHolder = null;

    public static void register() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            cachedHolder = server.registryAccess()
                    .lookupOrThrow(Registries.ENCHANTMENT)
                    .get(COOLDOWN_REDUCTION_KEY)          // returns Optional<Holder.Reference<Enchantment>>
                    .orElse(null);
        });
    }

    public static Holder<Enchantment> getEnchantmentHolder() {
        return getHolder();
    }

    private static Holder<Enchantment> getHolder() {
        if (cachedHolder == null) {
            throw new IllegalStateException(
                    "Cooldown Reduction enchantment not yet registered."
            );
        }
        return cachedHolder;
    }

    public static int getModifiedCooldown(ItemStack stack, int baseCooldown) {
        Holder<Enchantment> holder = getHolder();
        int level = EnchantmentHelper.getItemEnchantmentLevel(holder, stack);
        if (level > 0) {
            float multiplier = 1.0f - (0.05f * level);
            return Math.max(1, (int)(baseCooldown * multiplier));
        }
        return baseCooldown;
    }
}