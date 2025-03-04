package sypztep.hawsamoot.common.module;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Identifier;
import sypztep.hawsamoot.Hawsamoot;
import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.util.Module;

/**
 * Module that manages visual effects for item entities on the client.
 */
public class VisualEffectsModule implements Module {
    private static final Identifier GLOW_TEXTURE = Hawsamoot.id( "textures/misc/white.png");
    private RenderLayer createBeaconColorLayer(boolean affectsOutline) {
        RenderLayer.MultiPhaseParameters params = RenderLayer.MultiPhaseParameters.builder()
                .program(RenderLayer.POSITION_COLOR_PROGRAM)
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                .writeMaskState(affectsOutline ? RenderLayer.COLOR_MASK : RenderLayer.ALL_MASK)  // if true it can't see though water
                .cull(RenderLayer.DISABLE_CULLING)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .build(false);

        return RenderLayer.of(
                "Yahooo",
                VertexFormats.POSITION_COLOR,
                VertexFormat.DrawMode.QUADS,
                1536,
                false,
                true,
                params
        );
    }
    public RenderLayer BEACON_NORMAL_LAYER = createBeaconColorLayer(true);

    @Override
    public String getId() {
        return "visual_effects";
    }

    @Override
    public String getName() {
        return "Visual Effects";
    }

    @Override
    public String getDescription() {
        return "Adds glowing effects, beams, and enhanced text displays to items on the ground";
    }

    @Override
    public boolean isEnabled() {
        return ModConfig.CONFIG.enableVisualEffects;
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfig.CONFIG.enableVisualEffects = enabled;
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean requiresServerSync() {
        return false; // Client-side only
    }

    /**
     * Checks if visual effects should be shown for an item entity
     */
    public boolean shouldRenderEffectsFor(ItemEntity itemEntity) {
        if (!isEnabled()) {
            return false;
        }

        // Only render for items on ground with custom names
        return itemEntity != null && itemEntity.isOnGround() && itemEntity.hasCustomName();
    }

    /**
     * Checks if beam effect is enabled
     */
    public boolean isBeamEffectEnabled() {
        return isEnabled() && ModConfig.CONFIG.enableBeamEffect;
    }

    /**
     * Checks if glow effect is enabled
     */
    public boolean isGlowEffectEnabled() {
        return isEnabled() && ModConfig.CONFIG.enableGlowEffect;
    }

    /**
     * Checks if enhanced text display is enabled
     */
    public boolean isEnhancedTextEnabled() {
        return isEnabled() && ModConfig.CONFIG.enableEnhancedText;
    }

    /**
     * Gets the beam animation duration in ticks
     */
    public int getBeamAnimationDuration() {
        return ModConfig.CONFIG.beamAnimationDuration;
    }
    public float getFadeDistance() {
        return ModConfig.CONFIG.fadeDistance;
    }

    /**
     * Gets the beam maximum height
     */
    public float getBeamMaxHeight() {
        return ModConfig.CONFIG.beamMaxHeight;
    }

    /**
     * Gets the beam width
     */
    public float getBeamWidth() {
        return ModConfig.CONFIG.beamWidth;
    }

    /**
     * Gets the beam alpha (transparency)
     */
    public float getBeamAlpha() {
        return ModConfig.CONFIG.beamAlpha;
    }

    /**
     * Gets the glow animation duration in ticks
     */
    public int getGlowAnimationDuration() {
        return ModConfig.CONFIG.glowAnimationDuration;
    }
    public float getInnerRotationSpeed() {
        return ModConfig.CONFIG.innerRotationSpeed;
    }
    public float getOuterRotationSpeed() {
        return ModConfig.CONFIG.outerRotationSpeed;
    }


    /**
     * Gets the glow effect size
     */
    public float getGlowSize() {
        return ModConfig.CONFIG.glowSize;
    }
    public float getPulsePeriod() {
        return ModConfig.CONFIG.pulsePeriod;
    }
    /**
     * Gets the glow effect alpha (transparency)
     */
    public float getGlowAlpha() {
        return ModConfig.CONFIG.glowAlpha;
    }
    public float getPulseAmp() {
        return ModConfig.CONFIG.pulseAmp;
    }
    public float getMaxFadeDistance() {
        return ModConfig.CONFIG.maxFadeDistance;
    }
    /**
     * Gets the glow effect texture
     */
    public Identifier getGlowTexture() {
        return GLOW_TEXTURE;
    }
    /**
     * Calculates a pulsating value for continuous animations
     * @param entityAge The age of the entity in ticks
     * @param animationDuration How long until full animation
     * @param pulsePeriod How long each pulse cycle lasts
     * @param pulseAmplitude How strong the pulse is
     * @return A sine-wave based pulsing value
     */
    public float calculatePulse(float entityAge, float animationDuration,
                                float pulsePeriod, float pulseAmplitude) {
        if (entityAge < animationDuration) {
            return 0; // No pulse during initial animation
        }

        float pulseTime = ((entityAge - animationDuration) % pulsePeriod) / pulsePeriod;
        return pulseAmplitude * (float) Math.sin(pulseTime * Math.PI * 2);
    }
}