package sypztep.hawsamoot.mixin.itemmerge.itemstacksize;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import sypztep.hawsamoot.common.module.StackSizeModule;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique
    private static final StackSizeModule stackSizeModule = new StackSizeModule();
    @ModifyExpressionValue(method = "method_57371", at = @At(value = "CONSTANT", args = "intValue=99"))
    private static int modifyMaxStackSize(int original) {
        return stackSizeModule.isEnabled() ? stackSizeModule.getMaxStackSize() : original;
    }
}