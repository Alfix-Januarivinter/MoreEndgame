package com.alfixjanuarivinter.moreendgame.mixin;

import com.alfixjanuarivinter.moreendgame.Items.ReaperSwordItem;
import com.alfixjanuarivinter.moreendgame.Items.AxeOfTheTreesItem;
import com.alfixjanuarivinter.moreendgame.Items.CrystallizedPickaxeItem;
import com.alfixjanuarivinter.moreendgame.util.ScrollAnvilHandler;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    @Shadow @Final private net.minecraft.world.inventory.DataSlot cost;

    public AnvilMenuMixin(MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition definition) {
        super(type, containerId, playerInventory, access, definition);
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void onCreateResult(CallbackInfo ci) {
        ItemStack leftStack = this.inputSlots.getItem(0);
        ItemStack rightStack = this.inputSlots.getItem(1);

        // System output to track if the anvil is even registering inputs
        if (!leftStack.isEmpty() && !rightStack.isEmpty()) {
            System.out.println("[MoreEndgame] Anvil checking: " + leftStack.getItem().toString() + " + " + rightStack.getItem().toString());
        }

        if (leftStack.getItem() instanceof ReaperSwordItem ||
                leftStack.getItem() instanceof AxeOfTheTreesItem ||
                leftStack.getItem() instanceof CrystallizedPickaxeItem) {

            ItemStack output = ScrollAnvilHandler.applyScroll(leftStack, rightStack.getItem());

            if (!output.isEmpty()) {
                this.resultSlots.setItem(0, output);
                this.cost.set(5);
                System.out.println("[MoreEndgame] Success! Outputting upgraded item.");
                ci.cancel();
            }
        }
    }
}