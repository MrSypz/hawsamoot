package sypztep.hawsamoot.mixin.itemmerge;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.hawsamoot.client.HawsamootClient;

import static net.minecraft.block.entity.HopperBlockEntity.transfer;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
    @Inject(method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z", at = @At("HEAD"), cancellable = true)
    private static void extract(Inventory inventory, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir) {
        ItemStack originalStack = itemEntity.getStack();

        if (originalStack.isEmpty()) {
            cir.setReturnValue(false);
            return;
        }

        ItemStack singleItemStack = originalStack.split(1);

        ItemStack remaining = transfer(null, inventory, singleItemStack, null);

        if (remaining.isEmpty()) {
            if (originalStack.getCount() > HawsamootClient.CONFIG.stackSizeModule.maxStackSize) {
                originalStack.setCount(HawsamootClient.CONFIG.stackSizeModule.maxStackSize);
            }
            itemEntity.setStack(originalStack);

            cir.setReturnValue(true);
        } else {
            originalStack.increment(1);
            itemEntity.setStack(originalStack);
            cir.setReturnValue(false);
        }
        if (originalStack.isEmpty()) itemEntity.discard();
    }
}

