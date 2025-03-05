package sypztep.hawsamoot.mixin.itemmerge.client.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.hawsamoot.api.border.BorderStyle;
import sypztep.hawsamoot.api.border.BorderTemplate;
import sypztep.hawsamoot.client.render.util.BorderRenderer;
import sypztep.hawsamoot.client.render.util.VertexContext;
import sypztep.hawsamoot.client.render.util.WorldBorderRenderer;
import sypztep.hawsamoot.common.module.CustomNameModule;
import sypztep.hawsamoot.common.module.VisualEffectsModule;
import sypztep.hawsamoot.common.util.ColorUtils;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity, ItemEntityRenderState> {
    @Unique
    private final VisualEffectsModule visualModule = new VisualEffectsModule();

    @Unique
    private final CustomNameModule customNameModule = new CustomNameModule();

    @Unique
    private ItemEntity itemEntity;

    protected ItemEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Unique
    private boolean isValid() {
        return itemEntity != null && itemEntity.isOnGround();
    }

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    public void onUpdateRenderState(ItemEntity itemEntity, ItemEntityRenderState state, float tickDelta, CallbackInfo ci) {
        this.itemEntity = itemEntity;
    }

    @Inject(method = "render", at = @At("RETURN"))
    public void onRender(ItemEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!visualModule.shouldRenderEffectsFor(itemEntity)) {
            return;
        }

        if (visualModule.isBeamEffectEnabled()) {
            renderSquareBeam(matrices, vertexConsumers, state);
        }

        if (visualModule.isGlowEffectEnabled()) {
            renderEnhancedGlow(matrices, vertexConsumers, state, light);
        }

        Vec3d cameraPos = this.dispatcher.camera.getPos();
        Vec3d itemPos = new Vec3d(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ());
        double distance = cameraPos.distanceTo(itemPos);

        if (distance <= visualModule.getMaxFadeDistance() && visualModule.isEnhancedTextEnabled() && customNameModule.isEnabled()) {
            renderCustomTextWithBorder(matrices, vertexConsumers, state, distance, visualModule.getMaxFadeDistance());
        }
    }

    @Unique
    private float calculateEaseProgress(float rawProgress) {
        if (rawProgress <= 0) return 0;
        if (rawProgress >= 1) return 1;

        return rawProgress < 0.5
                ? (float) (Math.pow(2, 20 * rawProgress - 10) / 2)
                : (float) ((2 - Math.pow(2, -20 * rawProgress + 10)) / 2);
    }

    @Unique
    private BorderTemplate getBorderTemplate() {
        BorderStyle borderStyle = BorderRenderer.determineItemBorderStyle(itemEntity.getStack());
        return borderStyle.getBorderTemplate();
    }

    @Unique
    private float[] getColorComponents(int color) {
        return ColorUtils.extractColorComponents(color);
    }

    @Unique
    private float getAnimatedProgress(ItemEntityRenderState state, float animationDuration) {
        float rawProgress = Math.min(state.age / animationDuration, 1.0f);
        return calculateEaseProgress(rawProgress);
    }

    @Unique
    private void renderEnhancedGlow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemEntityRenderState state, int light) {
        if (!isValid()) return;

        float progress = getAnimatedProgress(state, visualModule.getGlowAnimationDuration());
        if (progress < 0.1f) return;

        // Dynamic sizing with consistent pulse mechanism
        float baseSize = calculateGlowSize(state, progress);
        float halfSize = baseSize / 2.0f;

        // Prepare rendering context
        matrices.push();
        matrices.translate(0, 0.01f, 0);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));

        // Determine border style and colors
        BorderTemplate borderTemplate = getBorderTemplate();
        float[] startColors = getColorComponents(borderTemplate.colorStart());
        float[] endColors = getColorComponents(borderTemplate.colorEnd());

        // Calculate alpha with consistent approach
        float alpha = calculateGlowAlpha(state, progress);

        // Increase brightness by multiplying color components
        float brightnessMultiplier = 5f;
        renderGlowQuad(matrices, vertexConsumers, light, halfSize,
                Math.min(startColors[0] * brightnessMultiplier, 1.0f),
                Math.min(startColors[1] * brightnessMultiplier, 1.0f),
                Math.min(startColors[2] * brightnessMultiplier, 1.0f),
                Math.min(endColors[0] * brightnessMultiplier, 1.0f),
                Math.min(endColors[1] * brightnessMultiplier, 1.0f),
                Math.min(endColors[2] * brightnessMultiplier, 1.0f),
                alpha);

        matrices.pop();
    }

    @Unique
    private float calculateGlowSize(ItemEntityRenderState state, float progress) {
        float baseSize = visualModule.getGlowSize() + 0.5f * progress;
        if (progress >= 1.0) {
            float pulsePeriod = 80f;
            float pulseTime = ((state.age - visualModule.getGlowAnimationDuration()) % pulsePeriod) / pulsePeriod;
            float pulseValue = 0.1f * (float) Math.sin(pulseTime * Math.PI * 2);
            baseSize += pulseValue;
        }
        return baseSize;
    }

    @Unique
    private float calculateGlowAlpha(ItemEntityRenderState state, float progress) {
        float baseAlpha = visualModule.getGlowAlpha() * progress;
        if (progress >= 1.0) {
            float pulseValue = visualModule.calculatePulse(
                    state.age,
                    visualModule.getGlowAnimationDuration(),
                    visualModule.getPulsePeriod(),
                    visualModule.getPulseAmp()
            );
            baseAlpha += pulseValue;
        }
        return baseAlpha;
    }

    @Unique
    private void renderGlowQuad(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                int light, float halfSize,
                                float startR, float startG, float startB,
                                float endR, float endG, float endB,
                                float alpha) {
        RenderLayer renderLayer = RenderLayer.getEntityTranslucentEmissive(visualModule.getGlowTexture());
        VertexConsumer consumer = vertexConsumers.getBuffer(renderLayer);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        int r = (int) (startR * 255);
        int g = (int) (startG * 255);
        int b = (int) (startB * 255);
        int a = (int) (alpha * 255);
        int endR_int = (int) (endR * 255);
        int endG_int = (int) (endG * 255);
        int endB_int = (int) (endB * 255);

        Vector3f normal = new Vector3f(0, 0, 1);

        // Quad vertices with color gradient
        consumer.vertex(matrix, -halfSize, -halfSize, 0)
                .color(r, g, b, a)
                .texture(0, 1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal.x(), normal.y(), normal.z());

        consumer.vertex(matrix, halfSize, -halfSize, 0)
                .color(endR_int, endG_int, endB_int, a)
                .texture(1, 1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal.x(), normal.y(), normal.z());

        consumer.vertex(matrix, halfSize, halfSize, 0)
                .color(endR_int, endG_int, endB_int, a)
                .texture(1, 0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal.x(), normal.y(), normal.z());

        consumer.vertex(matrix, -halfSize, halfSize, 0)
                .color(r, g, b, a)
                .texture(0, 0)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal.x(), normal.y(), normal.z());
    }

    @Unique
    private void renderSquareBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemEntityRenderState state) {
        if (!isValid()) return;

        float progress = getAnimatedProgress(state, visualModule.getBeamAnimationDuration());
        float height = visualModule.getBeamMaxHeight() * progress;

        if (height < 0.1f) return;

        matrices.push();

        float width = visualModule.getBeamWidth();
        float halfWidth = width / 2.0f;
        VertexContext context = new VertexContext(matrices, vertexConsumers);

        float alpha = visualModule.getBeamAlpha() * progress;
        float endAlpha = 0.0f;

        // Get vertex consumer and matrix
        VertexConsumer consumer = vertexConsumers.getBuffer(visualModule.BEAM_NORMAL_LAYER);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float outerRotation = (state.age / 20.0f) * visualModule.getOuterRotationSpeed() * (float) Math.PI;
        float innerRotation = (state.age / 20.0f) * visualModule.getInnerRotationSpeed() * (float) Math.PI;

        // Get border color directly
        BorderTemplate borderTemplate = getBorderTemplate();
        float[] bgStartComponents = getColorComponents(borderTemplate.colorStart());

        // Draw the outer beam with border color
        drawRotatingBeamSides(context, consumer, matrix, halfWidth, height,
                bgStartComponents[0], bgStartComponents[1], bgStartComponents[2],
                alpha, endAlpha, outerRotation);

        // Draw the inner beam with the same color but slightly dimmer
        float innerWidth = halfWidth * 0.5f;
        drawRotatingBeamSides(context, consumer, matrix, innerWidth, height,
                bgStartComponents[0], bgStartComponents[1], bgStartComponents[2],
                alpha * 0.8f, endAlpha, innerRotation);

        matrices.pop();
    }

    @Unique
    private void drawRotatingBeamSides(VertexContext context, VertexConsumer consumer, Matrix4f matrix,
                                       float halfWidth, float height,
                                       float r, float g, float b,
                                       float alpha, float endAlpha, float rotation) {
        float sin = (float) Math.sin(rotation);
        float cos = (float) Math.cos(rotation);

        // Calculate rotated corner positions
        float x1 = -halfWidth * cos - (-halfWidth) * sin;  // front-left
        float z1 = -halfWidth * sin + (-halfWidth) * cos;

        float x2 = halfWidth * cos - (-halfWidth) * sin;   // front-right
        float z2 = halfWidth * sin + (-halfWidth) * cos;

        float x3 = halfWidth * cos - halfWidth * sin;      // back-right
        float z3 = halfWidth * sin + halfWidth * cos;

        float x4 = -halfWidth * cos - halfWidth * sin;     // back-left
        float z4 = -halfWidth * sin + halfWidth * cos;

        // Draw the four sides of the beam
        drawBeamSide(context, consumer, matrix, x1, z1, x2, z2, height, r, g, b, alpha, endAlpha);
        drawBeamSide(context, consumer, matrix, x3, z3, x4, z4, height, r, g, b, alpha, endAlpha);
        drawBeamSide(context, consumer, matrix, x4, z4, x1, z1, height, r, g, b, alpha, endAlpha);
        drawBeamSide(context, consumer, matrix, x2, z2, x3, z3, height, r, g, b, alpha, endAlpha);
    }

    @Unique
    private void drawBeamSide(VertexContext context, VertexConsumer consumer, Matrix4f matrix,
                              float x1, float z1, float x2, float z2, float height,
                              float r, float g, float b, float alpha, float endAlpha) {
        context.fillGradient(consumer, matrix,
                x1, 0, z1, r, g, b, alpha,
                x1, height, z1, r, g, b, endAlpha,
                x2, height, z2, r, g, b, endAlpha,
                x2, 0, z2, r, g, b, alpha);
    }

    @Unique
    private void renderCustomTextWithBorder(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                            ItemEntityRenderState state, double distance, float maxDistance) {
        if (!isValid()) return;

        // Calculate alpha based on distance
        float distanceAlpha = 1.0f;
        if (distance > visualModule.getFadeDistance()) {
            distanceAlpha = (float)(1.0 - (distance - visualModule.getFadeDistance()) / (maxDistance - visualModule.getFadeDistance()));
        }

        // Get animation progress using the common method
        float progress = getAnimatedProgress(state, visualModule.getBeamAnimationDuration());

        // Combine animation progress with distance fading
        float combinedAlpha = progress * distanceAlpha;

        // Skip if not enough visibility
        if (combinedAlpha < 0.1f) return;

        Text text = customNameModule.updateCustomName(itemEntity);

        // Skip if no text
        if (text == null) return;

        // Calculate text alpha based on combined alpha value
        int textAlpha = (int)(255 * combinedAlpha);

        // Calculate position: above the item
        matrices.push();

        // Get the camera and item positions
        Vec3d cameraPos = this.dispatcher.camera.getPos();
        Vec3d itemPos = new Vec3d(itemEntity.getX(), itemEntity.getY() - customNameModule.getYOffset(), itemEntity.getZ());
        Vec3d directionToCamera = cameraPos.subtract(itemPos).normalize();

        // Calculate height oscillation
        float heightOffset = 0.5f + 0.2f * (float)Math.sin((state.age / 20.0f) * 0.5);

        // Apply translation: move along the direction to camera vector, plus some height
        matrices.translate(
                directionToCamera.x * 0.3f,
                heightOffset + directionToCamera.y,
                directionToCamera.z * 0.3f
        );

        // Make text face the player
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-this.dispatcher.camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.dispatcher.camera.getPitch()));

        // Scale text appropriately
        float textScale = 0.025f * (0.8f + 0.2f * combinedAlpha);
        matrices.scale(-textScale, -textScale, textScale);

        // Get text dimensions for border
        float textWidth = this.getTextRenderer().getWidth(text);
        float borderWidth = 2.0f;

        // Calculate border dimensions
        int x1 = (int)(-textWidth/2 - borderWidth);
        int y1 = (int)(- borderWidth);
        int x2 = (int)(textWidth/2 + borderWidth);
        int y2 = (int)(this.getTextRenderer().fontHeight + borderWidth);

        // Get border style and template
        BorderStyle borderStyle = BorderRenderer.determineItemBorderStyle(itemEntity.getStack());
        BorderTemplate borderTemplate = borderStyle.getBorderTemplate();
        Identifier identifier = borderTemplate.identifier();

        // Apply alpha to all colors
        int bgStart = ColorUtils.applyAlpha(borderTemplate.backgroundStartColor(), textAlpha);
        int bgEnd = ColorUtils.applyAlpha(borderTemplate.backgroundEndColor(), textAlpha);
        int colorStart = ColorUtils.applyAlpha(borderTemplate.colorStart(), textAlpha);
        int colorEnd = ColorUtils.applyAlpha(borderTemplate.colorEnd(), textAlpha);

        // Render background
        VertexContext context = new VertexContext(matrices, vertexConsumers);
        WorldBorderRenderer.renderTooltipBackground(context, x1, y1, x2 - x1, y2 - y1, bgStart, bgEnd, colorStart, colorEnd);

        // Text position for centering
        float textX = -textWidth / 2;
        float textY = 0;

        // Enhanced lighting for text
        int enhancedLight = LightmapTextureManager.MAX_LIGHT_COORDINATE;

        // Draw main text
        int textColor = 0xFFFFFF | (textAlpha << 24); // White text with alpha
        this.getTextRenderer().draw(
                text,
                textX,
                textY,
                textColor,
                false,
                matrices.peek().getPositionMatrix(),
                vertexConsumers,
                TextRenderer.TextLayerType.NORMAL,
                0,
                enhancedLight
        );

        // Draw border
        renderBorder(context, borderStyle, identifier, x1, y1, y2, textWidth);

        matrices.pop();
    }

    @Unique
    private void renderBorder(VertexContext context, BorderStyle borderStyle, Identifier identifier,
                              int x1, int y1, int y2, float textWidth) {
        int borderIndex = borderStyle.ordinal();
        Function<Identifier, RenderLayer> renderLayerProvider = RenderLayer::getGuiTextured;

        // Top-left corner
        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                x1 - 6, y2 - 6, 0, borderIndex * 16, 8, 8, 128, 128);

        // Top-right corner
        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                (int) (x1 + textWidth - 2), y2 - 6, 56, borderIndex * 16, 8, 8, 128, 128);

        // Bottom-left corner
        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                x1 - 6, y2 + getTextRenderer().fontHeight - 2, 0, 8 + borderIndex * 16, 8, 8, 128, 128);

        // Bottom-right corner
        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                (int) (x1 + textWidth - 2), y2 + getTextRenderer().fontHeight - 2, 56, 8 + borderIndex * 16, 8, 8, 128, 128);

        // Top border
        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                (int) ((x1 - 6 + x1 + textWidth + 6) / 2 - 24), y1 - y2 + 1, 8, borderIndex * 16, 48, 8, 128, 128);

        // Bottom border
        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                (int) ((x1 - 6 + x1 + textWidth + 6) / 2 - 24), y2 + 1, 8, 8 + borderIndex * 16, 48, 8, 128, 128);
    }
}