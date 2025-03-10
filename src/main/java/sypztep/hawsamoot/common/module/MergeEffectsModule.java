package sypztep.hawsamoot.common.module;

import net.minecraft.entity.ItemEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import sypztep.hawsamoot.client.HawsamootClient;
import sypztep.hawsamoot.common.util.ConfigHolder;

public class MergeEffectsModule implements ConfigHolder {
    public MergeEffectsModule() {}
    @Override
    public boolean isEnabled() {
        return HawsamootClient.CONFIG.clientModule.mergeEffectsModule.enableMergeEffects;
    }

    public void playMergeEffects(ItemEntity targetEntity, ItemEntity sourceEntity) {

        World world = targetEntity.getWorld();
        if (world.isClient()) return;

        // Calculate the midpoint position between the two entities for the effect
        double midX = (targetEntity.getX() + sourceEntity.getX()) / 2;
        double midY = (targetEntity.getY() + sourceEntity.getY()) / 2 + 0.1;
        double midZ = (targetEntity.getZ() + sourceEntity.getZ()) / 2;

        // Spawn particles
        ((ServerWorld)world).spawnParticles(
                ParticleTypes.HAPPY_VILLAGER,
                midX, midY, midZ,
                2, // Reduced particle count for performance
                0.2, 0.2, 0.2,
                0.1
        );

        ((ServerWorld)world).spawnParticles(
                ParticleTypes.POOF,
                midX, midY, midZ,
                3,
                0.1, 0.1, 0.1,
                0.05
        );

        // Play sounds
        world.playSound(
                null,
                midX, midY, midZ,
                SoundEvents.ENTITY_ITEM_PICKUP,
                SoundCategory.NEUTRAL,
                0.5F,
                1.0F + world.random.nextFloat() * 0.4F
        );

        world.playSound(
                null,
                midX, midY, midZ,
                SoundEvents.BLOCK_AMETHYST_BLOCK_HIT,
                SoundCategory.NEUTRAL,
                0.6F,
                0.8F + world.random.nextFloat() * 0.4F
        );
    }
}