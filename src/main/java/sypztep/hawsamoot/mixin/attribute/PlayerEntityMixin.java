package sypztep.hawsamoot.mixin.attribute;

import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.hawsamoot.common.api.AttributeProvider;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void registryExtraStats(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        DefaultAttributeContainer.Builder builder = cir.getReturnValue();

        for (AttributeProvider.AttributeRegistration registration : AttributeProvider.getAttributesForTarget(AttributeProvider.Target.PLAYER)) {
            builder.add(registration.attribute(), registration.baseValue());
        }

        cir.setReturnValue(builder);
    }
}