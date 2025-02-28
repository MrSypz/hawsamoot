package sypztep.hawsamoot.api;

import net.minecraft.util.Identifier;
import sypztep.hawsamoot.api.border.BorderStyle;
import sypztep.hawsamoot.common.data.RarityBorder;

public class RarityRegistry {
    public static RarityBorder registerRarity(String modId, String path, String displayName, BorderStyle style) {
        return RarityBorder.register(Identifier.of(modId, path), displayName, style);
    }
}