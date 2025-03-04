package sypztep.hawsamoot.client.render.util;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class WorldBorderRenderer {
    public static void renderTooltipBackground(VertexContext context, int x, int y, int width, int height,
                                                int bgStart, int bgEnd, int colorStart, int colorEnd) {
        int i = x - 3;
        int j = y - 3;
        int k = width + 6;
        int l = height + 6;
        int z = 1;

        renderRectangleBackground(context, i, j, k, l, z, bgStart, bgEnd);
        renderBorder(context, i, j + 1, k, l, 0, colorStart, colorEnd);
    }
    public static void drawTextureRegion(VertexContext context, Function<Identifier, RenderLayer> renderLayerProvider,
                                          Identifier texture, int x, int y, int u, int v,
                                          int width, int height, int textureWidth, int textureHeight) {
        context.drawTexture(renderLayerProvider, texture, x, y, u, v, width, height, textureWidth, textureHeight, -1);
    }
    private static void renderBorder(VertexContext context, int x, int y, int width, int height, int z,
                                     int startColor, int endColor) {
        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, startColor);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }

    private static void renderVerticalLine(VertexContext context, int x, int y, int height, int z,
                                           int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height, z, startColor, endColor);
    }

    private static void renderHorizontalLine(VertexContext context, int x, int y, int width, int z, int startColor) {
        context.fill(x, y, x + width, y + 1, z, startColor);
    }

    private static void renderRectangleBackground(VertexContext context, int x, int y, int width, int height,
                                                  int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + width, y + height, z, startColor, endColor);
    }
}
