package sypztep.hawsamoot.common.init;

import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.hawsamoot.Hawsamoot;
import sypztep.hawsamoot.common.data.RarityBorder;

public class ModDataComponents {
    public static final ComponentType<RarityBorder> RARITY_BORDER = new ComponentType.Builder<RarityBorder>().codec(RarityBorder.CODEC).build();

    public static void init() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, Hawsamoot.id("rarity_border"), RARITY_BORDER);
    }
}
