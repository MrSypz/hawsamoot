package sypztep.hawsamoot.common.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;
import sypztep.hawsamoot.Hawsamoot;
import sypztep.hawsamoot.api.border.BorderStyle;

import java.util.HashMap;
import java.util.Map;

/**
 * A scalable rarity border system that allows for custom rarity types and border styles.
 * Other mods can register their own rarity types and associated border styles.
 *
 */
public record RarityBorder(Identifier rarityId, String displayName) {
    private static final Map<Identifier, BorderStyle> RARITY_STYLES = new HashMap<>();

    public static final Codec<RarityBorder> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("rarity_id").forGetter(RarityBorder::rarityId),
                    Codec.STRING.fieldOf("display_name").forGetter(RarityBorder::displayName)
            ).apply(instance, RarityBorder::new)
    );

    // Default rarity types
    public static final RarityBorder COMMON = register("common", "Common", BorderStyle.COMMON);
    public static final RarityBorder UNCOMMON = register("uncommon", "Uncommon", BorderStyle.UNCOMMON);
    public static final RarityBorder RARE = register("rare", "Rare", BorderStyle.RARE);
    public static final RarityBorder EPIC = register("epic", "Epic", BorderStyle.EPIC);
    public static final RarityBorder LEGENDARY = register("legendary", "Legendary", BorderStyle.LEGENDARY);
    public static final RarityBorder MYTHIC = register("mythic", "Mythic", BorderStyle.MYTHIC);
    public static final RarityBorder CELESTIAL = register("celestial", "Celestial", BorderStyle.CELESTIAL);

    /**
     * Register a new rarity type with its associated border style
     */
    private static RarityBorder register(String path, String displayName, BorderStyle style) {
        return register(Hawsamoot.id(path), displayName, style);
    }
    /**
     * Register a new rarity type with custom namespace and border style
     */
    public static RarityBorder register(Identifier id, String displayName, BorderStyle style) {
        RarityBorder rarity = new RarityBorder(id, displayName);
        RARITY_STYLES.put(id, style);
        return rarity;
    }

    /**
     * Get the BorderStyle associated with this rarity
     */
    public BorderStyle toBorderStyle() {
        return RARITY_STYLES.getOrDefault(rarityId, BorderStyle.COMMON);
    }

    /**
     * Check if a rarity type is registered
     */
    public static boolean isRegistered(Identifier id) {
        return RARITY_STYLES.containsKey(id);
    }

    /**
     * Get a rarity by its identifier
     */
    public static RarityBorder byId(Identifier id) {
        if (isRegistered(id)) {
            return new RarityBorder(id, id.getPath());
        }
        return COMMON;
    }
}