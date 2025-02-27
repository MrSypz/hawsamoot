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
    public static int lightenColor(int color, float amount) {
        int a = (color >> 24) & 0xff;
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = color & 0xff;

        r = (int) Math.min(r + (255 - r) * amount, 255);
        g = (int) Math.min(g + (255 - g) * amount, 255);
        b = (int) Math.min(b + (255 - b) * amount, 255);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int darkenColor(int color, float amount) {
        int a = (color >> 24) & 0xff;
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = color & 0xff;

        r = (int) Math.max(r * (1 - amount), 0);
        g = (int) Math.max(g * (1 - amount), 0);
        b = (int) Math.max(b * (1 - amount), 0);

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}