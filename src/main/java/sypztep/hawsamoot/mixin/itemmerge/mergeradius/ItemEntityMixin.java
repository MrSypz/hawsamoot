package sypztep.hawsamoot.mixin.itemmerge.mergeradius;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import sypztep.hawsamoot.common.module.MergeRadiusModule;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Unique
    private final MergeRadiusModule mergeRadiusModule = new MergeRadiusModule();
    @ModifyArg(method = "tryMerge()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"), index = 0)
    private double modifyExpandX(double original) {
        return mergeRadiusModule.isEnabled() ? mergeRadiusModule.getMergeRadius() : original;
    }

    @ModifyArg(method = "tryMerge()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"), index = 2)
    private double modifyExpandZ(double original) {
        return mergeRadiusModule.isEnabled() ? mergeRadiusModule.getMergeRadius() : original;
    }
}
