package sypztep.hawsamoot.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector2ic;
import sypztep.hawsamoot.api.border.BorderStyle;
import sypztep.hawsamoot.api.border.BorderTemplate;
import sypztep.hawsamoot.common.util.ColorUtils;
import sypztep.hawsamoot.mixin.DrawContextAccessor;

import java.util.List;
import java.util.function.Function;

/**
 * Handles rendering of styled borders for tooltips and other UI elements.
 * Updated for the latest Minecraft version.
 */
@Environment(EnvType.CLIENT)
public final class BorderHandler {
    public static void renderStyledTooltip(DrawContext context, TextRenderer textRenderer,
                                           List<TooltipComponent> components, int x, int y,
                                           TooltipPositioner positioner, BorderStyle borderStyle) {
        if (components.isEmpty()) {
            return;
        }

        BorderTemplate borderTemplate = borderStyle.getBorderTemplate();
        int bgStart = borderTemplate.backgroundStartColor();
        int bgEnd = borderTemplate.backgroundEndColor();
        int colorStart = borderTemplate.colorStart();
        int colorEnd = borderTemplate.colorEnd();
        Identifier identifier = borderTemplate.identifier();

        int defaultLineHeight = textRenderer.fontHeight + 2;

        int maxWidth = Math.max(components.stream()
                .mapToInt(component -> component != null ? component.getWidth(textRenderer) : 0)
                .max().orElse(0), 64);

        int[] heights = new int[components.size()];
        int totalHeight = 0;

        for (int i = 0; i < components.size(); i++) {
            TooltipComponent component = components.get(i);
            if (component != null) {
                int height = getComponentHeight(component, textRenderer);
                heights[i] = height > 0 ? height : defaultLineHeight;

                // Add spacing after first component
                if (i == 0 && components.size() > 1) {
                    totalHeight += heights[i] + 2; // Add extra spacing after title
                } else {
                    totalHeight += heights[i];
                }
            } else {
                heights[i] = defaultLineHeight;
                totalHeight += defaultLineHeight;
            }
        }

        totalHeight = Math.max(totalHeight, 16);

        Vector2ic position = positioner.getPosition(context.getScaledWindowWidth(),
                context.getScaledWindowHeight(), x, y, maxWidth, totalHeight);
        int posX = position.x();
        int posY = position.y();

        context.getMatrices().push();
        renderTooltipBackground(context, posX, posY, maxWidth, totalHeight, bgStart, bgEnd, colorStart, colorEnd);
        context.getMatrices().translate(0.0f, 0.0f, 400.0f);
        VertexConsumerProvider.Immediate vertexConsumers = ((DrawContextAccessor) context).getVertexConsumers();
        int currentY = posY;
        for (int i = 0; i < components.size(); i++) {
            TooltipComponent component = components.get(i);
            if (component != null) {
                Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
                component.drawText(textRenderer, posX, currentY, matrix, vertexConsumers);
                component.drawItems(textRenderer, posX, currentY, maxWidth, heights[i], context);
                currentY += heights[i];
                if (i == 0 && components.size() > 1) currentY += 2;

            } else currentY += defaultLineHeight;
        }

        int borderIndex = borderStyle.ordinal();

        context.getMatrices().translate(0.0f, 0.0f, 400.0f);

        Function<Identifier, RenderLayer> renderLayerProvider = RenderLayer::getGuiTextured;

        // Top-left corner
        drawTextureRegion(context, renderLayerProvider, identifier,
                posX - 6, posY - 6, 0, borderIndex * 16, 8, 8, 128, 128);

        // Top-right corner
        drawTextureRegion(context, renderLayerProvider, identifier,
                posX + maxWidth - 2, posY - 6, 56, borderIndex * 16, 8, 8, 128, 128);

        // Bottom-left corner
        drawTextureRegion(context, renderLayerProvider, identifier,
                posX - 6, posY + totalHeight - 2, 0, 8 + borderIndex * 16, 8, 8, 128, 128);

        // Bottom-right corner
        drawTextureRegion(context, renderLayerProvider, identifier,
                posX + maxWidth - 2, posY + totalHeight - 2, 56, 8 + borderIndex * 16, 8, 8, 128, 128);

        // Top border
        drawTextureRegion(context, renderLayerProvider, identifier,
                (posX - 6 + posX + maxWidth + 6) / 2 - 24, posY - 9, 8, borderIndex * 16, 48, 8, 128, 128);

        // Bottom border
        drawTextureRegion(context, renderLayerProvider, identifier,
                (posX - 6 + posX + maxWidth + 6) / 2 - 24, posY + totalHeight + 1, 8, 8 + borderIndex * 16, 48, 8, 128, 128);

        context.getMatrices().pop();
    }

    private static int getComponentHeight(TooltipComponent component, TextRenderer textRenderer) {
        try {
            int height = component.getHeight(textRenderer);
            if (height > 0) return height;
            if (component instanceof TooltipComponent) return textRenderer.fontHeight;
            return textRenderer.fontHeight;
        } catch (Exception e) {
            return textRenderer.fontHeight;
        }
    }

    private static void drawTextureRegion(DrawContext context, Function<Identifier, RenderLayer> renderLayerProvider,
                                          Identifier texture, int x, int y, int u, int v,
                                          int width, int height, int textureWidth, int textureHeight) {
        context.drawTexture(renderLayerProvider, texture, x, y, u, v, width, height, textureWidth, textureHeight, -1);
    }

    private static void renderTooltipBackground(DrawContext context, int x, int y, int width, int height,
                                                int bgStart, int bgEnd, int colorStart, int colorEnd) {
        int i = x - 3;
        int j = y - 3;
        int k = width + 6;
        int l = height + 6;
        renderHorizontalLine(context, i, j - 1, k, 400, bgStart);
        renderHorizontalLine(context, i, j + l, k, 400, bgStart);

        renderRectangleBackground(context, i, j, k, l, 400, bgStart, bgEnd);

        renderVerticalLine(context, i - 1, j, l, 400, bgStart);
        renderVerticalLine(context, i + k, j, l, 400, bgStart);
        renderBorder(context, i, j + 1, k, l, 400, colorStart, colorEnd);
        renderHorizontalLineWithCenterGradient(context, i, j + 12, k, 1, 400, colorStart, 0);
    }

    private static void renderBorder(DrawContext context, int x, int y, int width, int height, int z,
                                     int startColor, int endColor) {
        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, startColor);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z,
                                           int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height, z, startColor, endColor);
    }

    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int startColor) {
        context.fill(x, y, x + width, y + 1, z, startColor);
    }

    private static void renderRectangleBackground(DrawContext context, int x, int y, int width, int height,
                                                  int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + width, y + height, z, startColor, endColor);
    }

    public static void renderHorizontalLineWithCenterGradient(DrawContext context, int x, int y, int width, int height,
                                                              int z, int centerColor, int edgeColor) {
        int centerX = x + width / 2;
        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                int pixelX = x + dx;
                int pixelY = y + dy;
                float distance = (float) Math.abs(pixelX - centerX);
                float normalizedDistance = Math.min(distance / ((float) width / 2), 1.0f); // Normalize distance to [0, 1]
                int color = ColorUtils.interpolateColor(centerColor, edgeColor, normalizedDistance);
                context.fill(pixelX, pixelY, pixelX + 1, pixelY + 1, z, color);
            }
        }
    }
}