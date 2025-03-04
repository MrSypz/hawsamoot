package sypztep.hawsamoot;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.init.ModDataComponents;
import sypztep.hawsamoot.common.util.ModuleManager;


public class Hawsamoot implements ModInitializer {
    public static final String MOD_ID = "hawsamoot";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModConfig.load();

        ModDataComponents.init();
        ModuleManager.getInstance().initializeModules();

        logActiveModules();
    }

    private void logActiveModules() {
        ModuleManager moduleManager = ModuleManager.getInstance();

        LOGGER.info("Active modules:");
        moduleManager.getModules().forEach((id, module) -> {
            LOGGER.info("- {} ({}): {}", module.getName(), id, module.isEnabled() ? "Enabled" : "Disabled");
        });
    }
}
