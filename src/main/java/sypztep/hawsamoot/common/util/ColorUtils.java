package sypztep.hawsamoot.common.util;

public class ColorUtils {
    public static int interpolateColor(int color1, int color2, float factor) {
        // Extract ARGB components from each color
        int a1 = (color1 >> 24) & 0xff;
        int r1 = (color1 >> 16) & 0xff;
        int g1 = (color1 >> 8) & 0xff;
        int b1 = color1 & 0xff;

        int a2 = (color2 >> 24) & 0xff;
        int r2 = (color2 >> 16) & 0xff;
        int g2 = (color2 >> 8) & 0xff;
        int b2 = color2 & 0xff;

        // Interpolate each component
        int a = (int) (a1 + factor * (a2 - a1));
        int r = (int) (r1 + factor * (r2 - r1));
        int g = (int) (g1 + factor * (g2 - g1));
        int b = (int) (b1 + factor * (b2 - b1));

        // Combine components back into a color
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
    public static float[] extractColorComponents(int color) {
        float alpha = ((color >> 24) & 0xFF) / 255.0f;
        float red = ((color >> 16) & 0xFF) / 255.0f;
        float green = ((color >> 8) & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;

        return new float[] { red, green, blue, alpha };
    }
    public static int applyAlpha(int color, int alpha) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        int originalAlpha = (color >> 24) & 0xFF;
        int newAlpha = (originalAlpha * alpha) / 255;

        return (newAlpha << 24) | (r << 16) | (g << 8) | b;
    }
}