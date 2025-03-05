package sypztep.hawsamoot.common.module;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Identifier;
import sypztep.hawsamoot.Hawsamoot;
import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.util.ConfigHolder;

/**
 * Module that manages visual effects for item entities on the client.
 */
public class VisualEffectsModule implements ConfigHolder {
    public VisualEffectsModule() {}
    private static final Identifier GLOW_TEXTURE = Hawsamoot.id( "textures/misc/white.png");
    private RenderLayer createBeamColorLayer(boolean affectsOutline) {
        RenderLayer.MultiPhaseParameters params = RenderLayer.MultiPhaseParameters.builder()
                .program(RenderLayer.POSITION_COLOR_PROGRAM)
                .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
                .writeMaskState(affectsOutline ? RenderLayer.COLOR_MASK : RenderLayer.ALL_MASK)  // if true it can't see though water
                .cull(RenderLayer.DISABLE_CULLING)
                .lightmap(RenderLayer.ENABLE_LIGHTMAP)
                .build(false);

        return RenderLayer.of(
                "translucent_beam",
                VertexFormats.POSITION_COLOR,
                VertexFormat.DrawMode.QUADS,
                1536,
                false,
                true,
                params
        );
    }
    public RenderLayer BEAM_NORMAL_LAYER = createBeamColorLayer(true);
    @Override
    public boolean isEnabled() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.enableVisualEffects;
    }

    public boolean shouldRenderEffectsFor(ItemEntity itemEntity) {
        if (!isEnabled()) {
            return false;
        }

        // Only render for items on ground with custom names
        return itemEntity != null && itemEntity.isOnGround();
    }

    /**
     * Checks if beam effect is enabled
     */
    public boolean isBeamEffectEnabled() {
        return isEnabled() && ModConfig.CONFIG.clientModule.visualEffectsModule.beamEffectModule.enableBeamEffect;
    }
    public void setBeamEffectEnabled(boolean bl) {
        ModConfig.CONFIG.clientModule.visualEffectsModule.enableVisualEffects = bl;
    }
    /**
     * Checks if glow effect is enabled
     */
    public boolean isGlowEffectEnabled() {
        return isEnabled() && ModConfig.CONFIG.clientModule.visualEffectsModule.glowEffectModule.enableGlowEffect;
    }
    /**
     * Checks if enhanced text display is enabled
     */
    public boolean isEnhancedTextEnabled() {
        return isEnabled() && ModConfig.CONFIG.clientModule.visualEffectsModule.enableEnhancedText;
    }
    /**
     * Gets the beam animation duration in ticks
     */
    public int getBeamAnimationDuration() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.beamEffectModule.beamAnimationDuration;
    }

    public float getFadeDistance() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.beamEffectModule.fadeDistance;
    }

    public float getMaxFadeDistance() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.beamEffectModule.maxFadeDistance;
    }

    public float getBeamMaxHeight() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.beamEffectModule.beamMaxHeight;
    }

    public float getBeamWidth() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.beamEffectModule.beamWidth;
    }

    public float getBeamAlpha() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.beamEffectModule.beamAlpha;
    }

    public float getInnerRotationSpeed() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.beamEffectModule.innerRotationSpeed;
    }

    public float getOuterRotationSpeed() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.beamEffectModule.outerRotationSpeed;
    }

    // Glow Effect Accessors
    public int getGlowAnimationDuration() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.glowEffectModule.glowAnimationDuration;
    }

    public float getGlowSize() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.glowEffectModule.glowSize;
    }

    public float getGlowAlpha() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.glowEffectModule.glowAlpha;
    }

    public float getPulsePeriod() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.glowEffectModule.pulsePeriod;
    }

    public float getPulseAmp() {
        return ModConfig.CONFIG.clientModule.visualEffectsModule.glowEffectModule.pulseAmp;
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