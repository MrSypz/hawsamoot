package sypztep.hawsamoot;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sypztep.hawsamoot.common.init.ModDataComponents;


public class Hawsamoot implements ModInitializer {
    public static final String MODID = "hawsamoot";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }

    @Override
    public void onInitialize() {
        ModDataComponents.init();
    }
}
