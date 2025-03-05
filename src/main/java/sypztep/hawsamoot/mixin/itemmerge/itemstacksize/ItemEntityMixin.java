package sypztep.hawsamoot.mixin.itemmerge.itemstacksize;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import sypztep.hawsamoot.common.module.StackSizeModule;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Unique
    private static final StackSizeModule stackSizeModule = new StackSizeModule();

    @ModifyExpressionValue(method = "canMerge()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private int modifyMaxCount(int original) {
        return stackSizeModule.isEnabled() ? stackSizeModule.getMaxStackSize() : original;
    }

    @ModifyExpressionValue(method = "canMerge(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private static int modifyMaxCountInStaticCanMerge(int original) {
        return stackSizeModule.isEnabled() ? stackSizeModule.getMaxStackSize() : original;
    }

    @ModifyExpressionValue(method = "merge(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;I)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxCount()I"))
    private static int modifyMaxCountInMerge(int original) {
        return stackSizeModule.isEnabled() ? stackSizeModule.getMaxStackSize() : original;
    }

    @ModifyConstant(method = "merge(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V", constant = @Constant(intValue = 64))
    private static int modifyMergeMaxCount(int original) {
        return stackSizeModule.isEnabled() ? stackSizeModule.getMaxStackSize() : original;
    }
}
