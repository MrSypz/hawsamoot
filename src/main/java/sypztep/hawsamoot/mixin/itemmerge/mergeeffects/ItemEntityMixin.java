package sypztep.hawsamoot.mixin.itemmerge.mergeeffects;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.item.ItemStack;
import sypztep.hawsamoot.common.module.MergeEffectsModule;
import sypztep.hawsamoot.common.util.ModuleManager;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Unique
    private static final MergeEffectsModule mergeEffectsModule = (MergeEffectsModule) ModuleManager.getInstance().getModule("merge_effects");
    @Inject(method = "merge(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;merge(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)V"))
    private static void effectMerge(ItemEntity targetEntity, ItemStack targetStack, ItemEntity sourceEntity, ItemStack sourceStack, CallbackInfo ci) {
        if (mergeEffectsModule.isEnabled()) {
            mergeEffectsModule.playMergeEffects(targetEntity, sourceEntity);
        }
    }
}
