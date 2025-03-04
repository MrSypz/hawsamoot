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
        return itemEntity != null && itemEntity.isOnGround();
    }

    /**
     * Checks if beam effect is enabled
     */
    public boolean isBeamEffectEnabled() {
        return isEnabled() && ModConfig.CONFIG.enableBeamEffect;
    }
    public void setBeamEffectEnabled(Boolean aBoolean) {
        ModConfig.CONFIG.enableBeamEffect = aBoolean;
    }
    /**
     * Checks if glow effect is enabled
     */
    public boolean isGlowEffectEnabled() {
        return isEnabled() && ModConfig.CONFIG.enableGlowEffect;
    }
    public void setGlowEffectEnabled(boolean aboolean) {
        ModConfig.CONFIG.enableGlowEffect = aboolean;
    }
    /**
     * Checks if enhanced text display is enabled
     */
    public boolean isEnhancedTextEnabled() {
        return isEnabled() && ModConfig.CONFIG.enableEnhancedText;
    }
    public void setEnhancedTextEnabled(boolean aBoolean) {
        ModConfig.CONFIG.enableEnhancedText = aBoolean;
    }
    /**
     * Gets the beam animation duration in ticks
     */
    public int getBeamAnimationDuration() {
        return ModConfig.CONFIG.beamAnimationDuration;
    }
    public void setBeamAnimationDuration(int duration) {
        ModConfig.CONFIG.beamAnimationDuration = duration;
    }
    public float getFadeDistance() {
        return ModConfig.CONFIG.fadeDistance;
    }
    public void setFadeDistance(float distance) {
        ModConfig.CONFIG.fadeDistance = distance;
    }

    /**
     * Gets the beam maximum height
     */
    public float getBeamMaxHeight() {
        return ModConfig.CONFIG.beamMaxHeight;
    }
    public void setBeamMaxHeight(float height) {
        ModConfig.CONFIG.beamMaxHeight = height;
    }
    /**
     * Gets the beam width
     */
    public float getBeamWidth() {
        return ModConfig.CONFIG.beamWidth;
    }
    public void setBeamWidth(float width) {
        ModConfig.CONFIG.beamWidth = width;
    }
    /**
     * Gets the beam alpha (transparency)
     */
    public float getBeamAlpha() {
        return ModConfig.CONFIG.beamAlpha;
    }
    public void setBeamAlpha(float alpha) {
        ModConfig.CONFIG.beamAlpha = alpha;
    }

    /**
     * Gets the glow animation duration in ticks
     */
    public int getGlowAnimationDuration() {
        return ModConfig.CONFIG.glowAnimationDuration;
    }
    public void setGlowAnimationDuration(int duration) {
        ModConfig.CONFIG.glowAnimationDuration = duration;
    }
    public float getInnerRotationSpeed() {
        return ModConfig.CONFIG.innerRotationSpeed;
    }
    public void setInnerRotationSpeed(float speed) {
        ModConfig.CONFIG.innerRotationSpeed = speed;
    }
    public float getOuterRotationSpeed() {
        return ModConfig.CONFIG.outerRotationSpeed;
    }
    public void setOuterRotationSpeed(float speed) {
        ModConfig.CONFIG.outerRotationSpeed = speed;
    }



    /**
     * Gets the glow effect size
     */
    public float getGlowSize() {
        return ModConfig.CONFIG.glowSize;
    }
    public void setGlowSize(float size) {
        ModConfig.CONFIG.glowSize = size;
    }
    public float getPulsePeriod() {
        return ModConfig.CONFIG.pulsePeriod;
    }
    public void setPulsePeriod(float period) {
        ModConfig.CONFIG.pulsePeriod = period;
    }
    /**
     * Gets the glow effect alpha (transparency)
     */
    public float getGlowAlpha() {
        return ModConfig.CONFIG.glowAlpha;
    }
    public void setGlowAlpha(float alpha) {
        ModConfig.CONFIG.glowAlpha = alpha;
    }
    public float getPulseAmp() {
        return ModConfig.CONFIG.pulseAmp;
    }
    public void setPulseAmp(float amp) {
        ModConfig.CONFIG.pulseAmp = amp;
    }
    public float getMaxFadeDistance() {
        return ModConfig.CONFIG.maxFadeDistance;
    }
    public void setMaxFadeDistance(float distance) {
        ModConfig.CONFIG.maxFadeDistance = distance;
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