package sypztep.hawsamoot.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import sypztep.hawsamoot.Hawsamoot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(Hawsamoot.MODID + ".json");

    public static ModConfigData CONFIG = new ModConfigData();

    public static class ModConfigData {
        // Stack size module
        public boolean enableStackSizeIncrease = true;
        public int maxStackSize = 1024;

        // Merge radius module
        public boolean enableLargerMergeRadius = true;
        public double mergeRadius = 2.0;

        // Client Side
        // Custom name module
        public boolean enableCustomItemNames = true;
        public float yOffset = 10f;
        // Merge effects module
        public boolean enableMergeEffects = true;

        // Visual effects module
        public boolean enableVisualEffects = true;
        public boolean enableBeamEffect = true;
        public boolean enableGlowEffect = true;
        public boolean enableEnhancedText = true;

        // Visual effect properties
        public int beamAnimationDuration = 40;
        public float fadeDistance = 8f;
        public float maxFadeDistance = 10f;
        public float beamMaxHeight = 2.0f;
        public float beamWidth = 0.2f;
        public float beamAlpha = 0.5f;

        public float innerRotationSpeed = -0.25f;
        public float outerRotationSpeed = 0.15f;

        public int glowAnimationDuration = 40;
        public float pulsePeriod = 80f;
        public float pulseAmp = 0.1f;
        public float glowSize = 1.0f;
        public float glowAlpha = 0.5f;
    }

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                CONFIG = GSON.fromJson(Files.newBufferedReader(CONFIG_PATH), ModConfigData.class);
            } else {
                save(); // Create default config
            }
        } catch (IOException e) {
            System.err.println("Error loading config: " + e.getMessage());
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(CONFIG));
        } catch (IOException e) {
            System.err.println("Error saving config: " + e.getMessage());
        }
    }
}