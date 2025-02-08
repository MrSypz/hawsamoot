package sypztep.hawsamoot.common.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.LivingEntity;

@FunctionalInterface
public interface EntityAttributeCallback {
    Event<EntityAttributeCallback> EVENT = EventFactory.createArrayBacked(EntityAttributeCallback.class,
            (listeners) -> (entity, builder) -> {
                for (EntityAttributeCallback callback : listeners) {
                    callback.applyAttributes(entity, builder);
                }
            });

    /**
     * Applies custom attributes to an entity.
     *
     * @param entity The entity being initialized.
     * @param builder The attribute container builder for the entity.
     */
    void applyAttributes(LivingEntity entity, DefaultAttributeContainer.Builder builder);
}