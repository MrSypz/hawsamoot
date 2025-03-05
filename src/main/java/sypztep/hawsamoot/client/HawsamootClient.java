package sypztep.hawsamoot.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import sypztep.hawsamoot.common.config.ModConfig;

public class HawsamootClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        ModConfig.CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
