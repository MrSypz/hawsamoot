package sypztep.hawsamoot.api.border;

import net.minecraft.util.Identifier;

public record BorderTemplate(int backgroundStartColor, int backgroundEndColor, int colorStart, int colorEnd, Identifier identifier) {
}
