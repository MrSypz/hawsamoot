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
import sypztep.hawsamoot.common.util.ModuleManager;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin extends EntityRenderer<ItemEntity, ItemEntityRenderState> {
    @Unique
    private final VisualEffectsModule visualModule =
            (VisualEffectsModule) ModuleManager.getInstance().getModule("visual_effects");

    @Unique
    private final CustomNameModule customNameModule =
            (CustomNameModule) ModuleManager.getInstance().getModule("custom_name");


    protected ItemEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Unique
    private ItemEntity itemEntity;

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
    private void renderEnhancedGlow(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemEntityRenderState state, int light) {
        if (!isValid()) return;

        // Consistent progress calculation with beam rendering
        float rawProgress = Math.min(state.age / visualModule.getGlowAnimationDuration(), 1.0f);
        float progress = calculateEaseProgress(rawProgress);

        if (progress < 0.1f) return;

        // Dynamic sizing with consistent pulse mechanism
        float baseSize = calculateGlowSize(state, progress);
        float halfSize = baseSize / 2.0f;

        // Prepare rendering context
        matrices.push();
        matrices.translate(0, 0.01f, 0);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));

        // Determine border style and colors
        BorderStyle borderStyle = BorderRenderer.determineItemBorderStyle(itemEntity.getStack());
        BorderTemplate borderTemplate = borderStyle.getBorderTemplate();
        float[] startColors = ColorUtils.extractColorComponents(borderTemplate.colorStart());
        float[] endColors = ColorUtils.extractColorComponents(borderTemplate.colorEnd());

        // Calculate alpha with consistent approach
        float alpha = calculateGlowAlpha(state, progress);

        // Increase brightness by multiplying color components
        float brightnessMultiplier = 5f; // You can adjust this value
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
    private float calculateEaseProgress(float rawProgress) {
        return rawProgress == 0
                ? 0
                : (float) (rawProgress == 1
                ? 1
                : rawProgress < 0.5
                ? Math.pow(2, 20 * rawProgress - 10) / 2
                : (2 - Math.pow(2, -20 * rawProgress + 10)) / 2);
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

        Vector3f normal = new Vector3f(0, 0, 1);

        // Quad vertices with color gradient
        consumer.vertex(matrix, -halfSize, -halfSize, 0)
                .color(r, g, b, a)
                .texture(0, 1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal.x(), normal.y(), normal.z());

        consumer.vertex(matrix, halfSize, -halfSize, 0)
                .color((int)(endR * 255), (int)(endG * 255), (int)(endB * 255), a)
                .texture(1, 1)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal.x(), normal.y(), normal.z());

        consumer.vertex(matrix, halfSize, halfSize, 0)
                .color((int)(endR * 255), (int)(endG * 255), (int)(endB * 255), a)
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

        float rawProgress = Math.min(state.age / visualModule.getBeamAnimationDuration(), 1.0f);

        // Apply easing (cubic ease-in-out)
        float progress = rawProgress == 0
                ? 0
                : (float) (rawProgress == 1
                ? 1
                : rawProgress < 0.5 ? Math.pow(2, 20 * rawProgress - 10) / 2
                : (2 - Math.pow(2, -20 * rawProgress + 10)) / 2);

        // Set final height (2 blocks max)
        float height = visualModule.getBeamMaxHeight() * progress;

        // Skip rendering if no visible height yet
        if (height < 0.1f) return;

        // Save matrix state
        matrices.push();

        // Simplified beam properties
        float width = visualModule.getBeamWidth();
        float halfWidth = width / 2.0f;
        VertexContext context = new VertexContext(matrices, vertexConsumers);

        // White color with gradient - alpha depends on progress
        float alpha = visualModule.getBeamAlpha() * progress;  // Alpha increases with progress
        float endAlpha = 0.0f;

        // Get vertex consumer and matrix
        VertexConsumer consumer = vertexConsumers.getBuffer(visualModule.BEACON_NORMAL_LAYER);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float outerRotation = (state.age / 20.0f) * visualModule.getOuterRotationSpeed() * (float) Math.PI; // convert to radians
        float innerRotation = (state.age / 20.0f) * visualModule.getInnerRotationSpeed() * (float) Math.PI; // convert to radians

        // Get border color directly
        BorderStyle borderStyle = BorderRenderer.determineItemBorderStyle(itemEntity.getStack());
        BorderTemplate borderTemplate = borderStyle.getBorderTemplate();
        int bgStartColor = borderTemplate.colorStart();
        float[] bgStartComponents = ColorUtils.extractColorComponents(bgStartColor);
        float bgStartRed = bgStartComponents[0];
        float bgStartGreen = bgStartComponents[1];
        float bgStartBlue = bgStartComponents[2];


        // Draw the outer beam with border color
        drawRotatingBeamSides(context, consumer, matrix, halfWidth, height,
                bgStartRed, bgStartGreen, bgStartBlue, alpha, endAlpha, outerRotation);

        // Draw the inner beam with white color
        float innerWidth = halfWidth * 0.5f;
        drawRotatingBeamSides(context, consumer, matrix, innerWidth, height,
                bgStartRed, bgStartGreen, bgStartBlue, alpha * 0.8f, endAlpha, innerRotation);

        matrices.pop();
    }

    @Unique
    private void drawRotatingBeamSides(VertexContext context, VertexConsumer consumer, Matrix4f matrix,
                                       float halfWidth, float height,
                                       float r, float g, float b,
                                       float alpha, float endAlpha, float rotation) {
        float sin = (float) Math.sin(rotation);
        float cos = (float) Math.cos(rotation);

        float x1 = -halfWidth * cos - (-halfWidth) * sin;  // front-left
        float z1 = -halfWidth * sin + (-halfWidth) * cos;

        float x2 = halfWidth * cos - (-halfWidth) * sin;   // front-right
        float z2 = halfWidth * sin + (-halfWidth) * cos;

        float x3 = halfWidth * cos - halfWidth * sin;      // back-right
        float z3 = halfWidth * sin + halfWidth * cos;

        float x4 = -halfWidth * cos - halfWidth * sin;     // back-left
        float z4 = -halfWidth * sin + halfWidth * cos;
        context.fillGradient(consumer, matrix,
                x1, 0, z1, r, g, b, alpha,
                x1, height, z1, r, g, b, endAlpha,
                x2, height, z2, r, g, b, endAlpha,
                x2, 0, z2, r, g, b, alpha);

        context.fillGradient(consumer, matrix,
                x3, 0, z3, r, g, b, alpha,
                x3, height, z3, r, g, b, endAlpha,
                x4, height, z4, r, g, b, endAlpha,
                x4, 0, z4, r, g, b, alpha);

        context.fillGradient(consumer, matrix,
                x4, 0, z4, r, g, b, alpha,
                x4, height, z4, r, g, b, endAlpha,
                x1, height, z1, r, g, b, endAlpha,
                x1, 0, z1, r, g, b, alpha);

        context.fillGradient(consumer, matrix,
                x2, 0, z2, r, g, b, alpha,
                x2, height, z2, r, g, b, endAlpha,
                x3, height, z3, r, g, b, endAlpha,
                x3, 0, z3, r, g, b, alpha);
    }

    @Unique
    private void renderCustomTextWithBorder(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                            ItemEntityRenderState state, double distance, float maxDistance) {
        if (!isValid()) return;

        float distanceAlpha = 1.0f;
        if (distance > visualModule.getFadeDistance()) {
            distanceAlpha = (float)(1.0 - (distance - visualModule.getFadeDistance()) / (maxDistance - visualModule.getFadeDistance()));
        }

        float rawProgress = Math.min(state.age / visualModule.getBeamAnimationDuration(), 1.0f);

        float progress = rawProgress == 0
                ? 0
                : (float) (rawProgress == 1
                ? 1
                : rawProgress < 0.5 ? Math.pow(2, 20 * rawProgress - 10) / 2
                : (2 - Math.pow(2, -20 * rawProgress + 10)) / 2);

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

        // Get the camera position vector
        Vec3d cameraPos = this.dispatcher.camera.getPos();

        // Get the position of the item/entity being rendered
        Vec3d itemPos = new Vec3d(itemEntity.getX(), itemEntity.getY() - customNameModule.getYOffset(), itemEntity.getZ());

        // Calculate direction vector from item to camera (player)
        Vec3d directionToCamera = cameraPos.subtract(itemPos).normalize();

        // Calculate height oscillation as before
        float heightOffset = 0.5f + 0.2f * (float)Math.sin((state.age / 20.0f) * 0.5);

        // Apply translation: move along the direction to camera vector, plus some height
        matrices.translate(
                directionToCamera.x * 0.3f, // Move toward player on X axis
                heightOffset + directionToCamera.y, // Keep height oscillation + move toward player on Y axis
                directionToCamera.z * 0.3f  // Move toward player on Z axis
        );

        // Make text face the player
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-this.dispatcher.camera.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(this.dispatcher.camera.getPitch()));

        // Scale text appropriately - adjust size based on combined alpha
        float textScale = 0.025f * (0.8f + 0.2f * combinedAlpha);
        matrices.scale(-textScale, -textScale, textScale);

        // Get text width for centering and border
        float textWidth = this.getTextRenderer().getWidth(text);
        float borderWidth = 2.0f;

        // Convert to integers for the fill method
        int x1 = (int)(-textWidth/2 - borderWidth);
        int y1 = (int)(- borderWidth);
        int x2 = (int)(textWidth/2 + borderWidth);
        int y2 = (int)(this.getTextRenderer().fontHeight + borderWidth);
        BorderStyle borderStyle = BorderRenderer.determineItemBorderStyle(itemEntity.getStack());
        BorderTemplate borderTemplate = borderStyle.getBorderTemplate();
        Identifier identifier = borderTemplate.identifier();

        // Apply alpha to all colors
        int bgStart = ColorUtils.applyAlpha(borderTemplate.backgroundStartColor(), textAlpha);
        int bgEnd = ColorUtils.applyAlpha(borderTemplate.backgroundEndColor(), textAlpha);
        int colorStart = ColorUtils.applyAlpha(borderTemplate.colorStart(), textAlpha);
        int colorEnd = ColorUtils.applyAlpha(borderTemplate.colorEnd(), textAlpha);

        VertexContext context = new VertexContext(matrices, vertexConsumers);
        WorldBorderRenderer.renderTooltipBackground(context, x1, y1, x2 - x1, y2 - y1, bgStart, bgEnd, colorStart, colorEnd);

        // Calculate text position for centering
        float textX = -textWidth / 2;
        float textY = 0;

        // Draw text with glow effect (using emissive lighting)
        int enhancedLight = LightmapTextureManager.MAX_LIGHT_COORDINATE;

        // Draw main text with applied alpha
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
                (int) ((x1 - 6 + x1 + textWidth + 6) / 2 - 24), y1- y2 + 1, 8, borderIndex * 16, 48, 8, 128, 128);

        // Bottom border
        WorldBorderRenderer.drawTextureRegion(context, renderLayerProvider, identifier,
                (int) ((x1 - 6 + x1 + textWidth + 6) / 2 - 24), y2 + 1, 8, 8 + borderIndex * 16, 48, 8, 128, 128);

        matrices.pop();
    }
}
