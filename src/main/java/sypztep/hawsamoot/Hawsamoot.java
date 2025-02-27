package sypztep.hawsamoot;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Hawsamoot implements ModInitializer {
    public static final String MOD_ID = "hawsamoot";
    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
    }
}
