package sypztep.hawsamoot.mixin.itemmerge.util;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.hawsamoot.common.util.ItemEntityGroundTimeAccessor;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin implements ItemEntityGroundTimeAccessor {
    @Unique
    private long groundHitTime = -1; // -1 means not on ground yet

    @Unique
    private boolean wasOnGroundLastTick = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity)(Object)this;
        boolean isOnGround = entity.isOnGround();

        if (!wasOnGroundLastTick && isOnGround) groundHitTime = System.currentTimeMillis();
         else if (!isOnGround) groundHitTime = -1; // Reset when no longer on ground

        wasOnGroundLastTick = isOnGround;
    }

    @Override
    @Unique
    public long getGroundHitTime() {
        return groundHitTime;
    }

    @Override
    @Unique
    public void setGroundHitTime(long time) {
        this.groundHitTime = time;
    }

    @Override
    @Unique
    public boolean getWasOnGroundLastTick() {
        return wasOnGroundLastTick;
    }

    @Override
    @Unique
    public void setWasOnGroundLastTick(boolean state) {
        this.wasOnGroundLastTick = state;
    }
}