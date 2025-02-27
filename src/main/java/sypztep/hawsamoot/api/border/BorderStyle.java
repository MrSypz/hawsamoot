package sypztep.hawsamoot.api.border;

import net.minecraft.util.Identifier;
import sypztep.hawsamoot.Hawsamoot;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A flexible border styling system that replaces the rigid CritTier enum.
 * Allows for dynamic registration and lookup of border styles.
 */
public class BorderStyle {
    private static final Map<String, BorderStyle> REGISTRY = new HashMap<>();

    private static final String DEFAULT_BORDER_TEXTURE_PATH = "textures/gui/border/borders.png";
    private static final int DEFAULT_BG_COLOR = -267386864;

    public static final BorderStyle COMMON = register("common", "Common",
            new BorderTemplate(DEFAULT_BG_COLOR, 0xF01F2542, 0xF4C2C2CD, 0xF04f4f53,
                    Hawsamoot.id(DEFAULT_BORDER_TEXTURE_PATH)));

    public static final BorderStyle UNCOMMON = register("uncommon", "Uncommon",
            new BorderTemplate(DEFAULT_BG_COLOR, 0xF01F4225, 0xF43FC43A, 0xF0133F2A,
                    Hawsamoot.id(DEFAULT_BORDER_TEXTURE_PATH)));

    public static final BorderStyle RARE = register("rare", "Rare",
            new BorderTemplate(DEFAULT_BG_COLOR, 0xF01F3942, 0xF43589B6, 0xF0123141,
                    Hawsamoot.id(DEFAULT_BORDER_TEXTURE_PATH)));

    public static final BorderStyle EPIC = register("epic", "Epic",
            new BorderTemplate(DEFAULT_BG_COLOR, 0xF0620039, 0xF4C90076, 0xF064003B,
                    Hawsamoot.id(DEFAULT_BORDER_TEXTURE_PATH)));

    public static final BorderStyle LEGENDARY = register("legendary", "Legendary",
            new BorderTemplate(DEFAULT_BG_COLOR, 0xF05C5426, 0xF4DFB433, 0xF09C7D23,
                    Hawsamoot.id(DEFAULT_BORDER_TEXTURE_PATH)));

    public static final BorderStyle MYTHIC = register("mythic", "Mythic",
            new BorderTemplate(DEFAULT_BG_COLOR, 0xF0802A5C, 0xF4C217B5, 0xF0610B5A,
                    Hawsamoot.id(DEFAULT_BORDER_TEXTURE_PATH)));

    public static final BorderStyle CELESTIAL = register("celestial", "Celestial",
            new BorderTemplate(0xF4291523, 0xF0651626, 0xF4f44336, 0xF0a60a0a,
                    Hawsamoot.id(DEFAULT_BORDER_TEXTURE_PATH)));

    private final String id;
    private final String displayName;
    private final BorderTemplate borderTemplate;
    private final int ordinalValue;

    private BorderStyle(String id, String displayName, BorderTemplate borderTemplate, int ordinalValue) {
        this.id = id;
        this.displayName = displayName;
        this.borderTemplate = borderTemplate;
        this.ordinalValue = ordinalValue;
    }

    public static BorderStyle register(String id, String displayName, BorderTemplate template) {
        BorderStyle style = new BorderStyle(id, displayName, template, REGISTRY.size());
        REGISTRY.put(id, style);
        return style;
    }

    public static BorderStyle create(String id, String displayName,
                                     int bgStartColor, int bgEndColor,
                                     int colorStart, int colorEnd,
                                     Identifier texture) {
        BorderTemplate template = new BorderTemplate(
                bgStartColor, bgEndColor, colorStart, colorEnd, texture);
        return register(id, displayName, template);
    }
    public static BorderStyle byId(String id) {
        return REGISTRY.getOrDefault(id, COMMON);
    }

    public static Optional<BorderStyle> findByName(String name) {
        return REGISTRY.values().stream()
                .filter(style -> style.getDisplayName().equals(name))
                .findFirst();
    }

    public static BorderStyle byName(String name) {
        return findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Unknown BorderStyle name: " + name));
    }

    public static BorderStyle[] values() {
        return REGISTRY.values().toArray(new BorderStyle[0]);
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BorderTemplate getBorderTemplate() {
        return borderTemplate;
    }

    public int ordinal() {
        return ordinalValue;
    }
}