package sypztep.hawsamoot.mixin.itemmerge.client.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
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
import sypztep.hawsamoot.common.util.ModRenderLayer;
import sypztep.hawsamoot.common.util.ItemEntityGroundTimeAccessor;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity> {
    @Unique
    private final VisualEffectsModule visualModule = new VisualEffectsModule();

    @Unique
    private final CustomNameModule customNameModule = new CustomNameModule();

    protected ItemEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Inject(method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("RETURN"))
    public void onRender(ItemEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!visualModule.shouldRenderEffectsFor(entity)) {
            return;
        }

        if (!hasHitGround(entity)) {
            return;
        }

        if (visualModule.isBeamEffectEnabled()) {
            renderSquareBeam(matrices, vertexConsumers, entity);
        }

        if (visualModule.isGlowEffectEnabled()) {
            renderEnhancedGlow(matrices, vertexConsumers, entity, light);
        }

        Vec3d cameraPos = this.dispatcher.camera.getPos();
        Vec3d itemPos = new Vec3d(entity.getX(), entity.getY(), entity.getZ());
        double distance = cameraPos.distanceTo(itemPos);
        if (distance <= visualModule.getMaxFadeDistance() && visualModule.isEnhancedTextEnabled() && customNameModule.isEnabled()) {
            renderCustomTextWithBorder(matrices, vertexConsumers, entity, distance, visualModule.getMaxFadeDistance());
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
    private BorderTemplate getBorderTemplate(ItemEntity entity) {
        BorderStyle borderStyle = BorderRenderer.determineItemBorderStyle(entity.getStack());
        return borderStyle.getBorderTemplate();
    }

    @Unique
    private float[] getColorComponents(int color) {
        return ColorUtils.extractColorComponents(color);
    }

    @Unique
    private boolean hasHitGround(ItemEntity entity) {
        ItemEntityGroundTimeAccessor accessor = (ItemEntityGroundTimeAccessor) entity;
        return accessor.getGroundHitTime() != -1;
    }

    @Unique
    private float getTimeOnGround(ItemEntity entity) {
        ItemEntityGroundTimeAccessor accessor = (ItemEntityGroundTimeAccessor) entity;
        long groundHitTime = accessor.getGroundHitTime();

        if (groundHitTime == -1) {
            return 0f;
        }

        return (System.currentTimeMillis() - groundHitTime) / 1000f;
    }


    @Unique
    private float getAnimatedProgress(ItemEntity entity, float animationDuration) {
        float timeOnGround = getTimeOnGround(entity);
        float rawProgress = Math.min(timeOnGround / (animationDuration / 20f), 1.0f);
        return calculateEaseProgress(rawProgress);
    }

    @Unique
    private void renderEnhancedGlow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemEntity entity, int light) {
        float progress = getAnimatedProgress(entity, visualModule.getGlowAnimationDuration());
        if (progress < 0.1f) return;

        float baseSize = calculateGlowSize(entity, progress);
        float halfSize = baseSize / 2.0f;

        matrices.push();
        matrices.translate(0, 0.01f, 0);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));

        BorderTemplate borderTemplate = getBorderTemplate(entity);
        float[] startColors = getColorComponents(borderTemplate.colorStart());
        float[] endColors = getColorComponents(borderTemplate.colorEnd());

        float alpha = calculateGlowAlpha(entity, progress);

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
    private float calculateGlowSize(ItemEntity state, float progress) {
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
    private float calculateGlowAlpha(ItemEntity entity, float progress) {
        float baseAlpha = visualModule.getGlowAlpha() * progress;
        if (progress >= 1.0) {
            float timeOnGround = getTimeOnGround(entity);
            float pulseValue = visualModule.calculatePulse(
                    (int)(timeOnGround * 20f),
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
    private void renderSquareBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemEntity state) {
        if (state != null && !state.isOnGround()) return;

        float progress = getAnimatedProgress(state, visualModule.getBeamAnimationDuration());
        float height = visualModule.getBeamMaxHeight() * progress;

        if (height < 0.1f) return;

        matrices.push();

        float width = visualModule.getBeamWidth();
        float halfWidth = width / 2.0f;
        VertexContext context = new VertexContext(matrices, vertexConsumers);

        float alpha = visualModule.getBeamAlpha() * progress;
        float endAlpha = 0.0f;

        VertexConsumer consumer = vertexConsumers.getBuffer(visualModule.BEAM_NORMAL_LAYER);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float outerRotation = (state.age / 20.0f) * visualModule.getOuterRotationSpeed() * (float) Math.PI;
        float innerRotation = (state.age / 20.0f) * visualModule.getInnerRotationSpeed() * (float) Math.PI;

        BorderTemplate borderTemplate = getBorderTemplate(state);
        float[] bgStartComponents = getColorComponents(borderTemplate.colorStart());

        drawRotatingBeamSides(context, consumer, matrix, halfWidth, height,
                bgStartComponents[0], bgStartComponents[1], bgStartComponents[2],
                alpha, endAlpha, outerRotation);

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

        float x1 = -halfWidth * cos - (-halfWidth) * sin;
        float z1 = -halfWidth * sin + (-halfWidth) * cos;

        float x2 = halfWidth * cos - (-halfWidth) * sin;
        float z2 = halfWidth * sin + (-halfWidth) * cos;

        float x3 = halfWidth * cos - halfWidth * sin;
        float z3 = halfWidth * sin + halfWidth * cos;

        float x4 = -halfWidth * cos - halfWidth * sin;
        float z4 = -halfWidth * sin + halfWidth * cos;

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
                                            ItemEntity state, double distance, float maxDistance) {
        if (state != null && !state.isOnGround()) return;

        float distanceAlpha = 1.0f;
        if (distance > visualModule.getFadeDistance()) {
            distanceAlpha = (float)(1.0 - (distance - visualModule.getFadeDistance()) / (maxDistance - visualModule.getFadeDistance()));
        }

        float progress = getAnimatedProgress(state, visualModule.getBeamAnimationDuration());
        float combinedAlpha = progress * distanceAlpha;

        if (combinedAlpha < 0.1f) return;

        Text text = customNameModule.updateCustomName(state);
        if (text == null) return;

        int textAlpha = (int)(255 * combinedAlpha);

        matrices.push();

        Vec3d cameraPos = this.dispatcher.camera.getPos();
        Vec3d itemPos = new Vec3d(state.getX(), state.getY() - customNameModule.getYOffset(), state.getZ());
        Vec3d directionToCamera = cameraPos.subtract(itemPos).normalize();

        float heightOffset = 0.5f + 0.2f * (float)Math.sin((state.age / 20.0f) * 0.5);

        matrices.translate(
                directionToCamera.x * 0.3f,
                heightOffset + directionToCamera.y,
                directionToCamera.z * 0.3f
        );

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-this.dispatcher.camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.dispatcher.camera.getPitch()));

        float textScale = 0.025f * (0.8f + 0.2f * combinedAlpha);
        matrices.scale(-textScale, -textScale, textScale);

        float textWidth = this.getTextRenderer().getWidth(text);
        float borderWidth = 2.0f;

        int x1 = (int)(-textWidth/2 - borderWidth);
        int y1 = (int)(-borderWidth);
        int x2 = (int)(textWidth/2 + borderWidth);
        int y2 = (int)(this.getTextRenderer().fontHeight + borderWidth);

        BorderStyle borderStyle = BorderRenderer.determineItemBorderStyle(state.getStack());
        BorderTemplate borderTemplate = borderStyle.getBorderTemplate();
        Identifier identifier = borderTemplate.identifier();

        int bgStart = ColorUtils.applyAlpha(borderTemplate.backgroundStartColor(), textAlpha);
        int bgEnd = ColorUtils.applyAlpha(borderTemplate.backgroundEndColor(), textAlpha);
        int colorStart = ColorUtils.applyAlpha(borderTemplate.colorStart(), textAlpha);
        int colorEnd = ColorUtils.applyAlpha(borderTemplate.colorEnd(), textAlpha);

        VertexContext context = new VertexContext(matrices, vertexConsumers);
        WorldBorderRenderer.renderTooltipBackground(context, x1, y1, x2 - x1, y2 - y1, bgStart, bgEnd, colorStart, colorEnd);

        float textX = -textWidth / 2;
        float textY = 0;

        int enhancedLight = LightmapTextureManager.MAX_LIGHT_COORDINATE;

        int textColor = 0xFFFFFF | (textAlpha << 24);
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

        renderBorder(context, borderStyle, identifier, x1, y1, y2, textWidth);

        matrices.pop();
    }

    @Unique
    private void renderBorder(VertexContext context, BorderStyle borderStyle, Identifier identifier,
                              int x1, int y1, int y2, float textWidth) {
        int borderIndex = borderStyle.ordinal();
        Function<Identifier, RenderLayer> renderLayerProvider = ModRenderLayer::getGuiTextured;

        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                x1 - 6, y2 - 6, 0, borderIndex * 16, 8, 8, 128, 128);

        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                (int) (x1 + textWidth - 2), y2 - 6, 56, borderIndex * 16, 8, 8, 128, 128);

        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                x1 - 6, y2 + getTextRenderer().fontHeight - 2, 0, 8 + borderIndex * 16, 8, 8, 128, 128);

        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                (int) (x1 + textWidth - 2), y2 + getTextRenderer().fontHeight - 2, 56, 8 + borderIndex * 16, 8, 8, 128, 128);

        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                (int) ((x1 - 6 + x1 + textWidth + 6) / 2 - 24), y1 - y2 + 1, 8, borderIndex * 16, 48, 8, 128, 128);

        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                (int) ((x1 - 6 + x1 + textWidth + 6) / 2 - 24), y2 + 1, 8, 8 + borderIndex * 16, 48, 8, 128, 128);
    }
}