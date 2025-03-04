package sypztep.hawsamoot.api;

import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.module.*;
import sypztep.hawsamoot.common.util.ModuleManager;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * API for external mods to interact with Hawsamoot's modules and configuration
 */
public class ModuleHandlerAPI {
    private static final ModuleManager MANAGER = ModuleManager.getInstance();

    /**
     * Module constants for easy reference
     */
    public static final class Modules {
        public static final String STACK_SIZE = "stack_size";
        public static final String MERGE_RADIUS = "merge_radius";
        public static final String CUSTOM_NAME = "custom_name";
        public static final String MERGE_EFFECTS = "merge_effects";
        public static final String VISUAL_EFFECTS = "visual_effects";
    }

    /**
     * Enables or disables a module by its ID
     */
    public static void setModuleEnabled(String moduleId, boolean enabled) {
        MANAGER.setModuleEnabled(moduleId, enabled);
    }

    /**
     * Checks if a module is enabled
     */
    public static boolean isModuleEnabled(String moduleId) {
        return MANAGER.isModuleEnabled(moduleId);
    }

    /**
     * Updates config after changes
     */
    public static void saveConfig() {
        ModConfig.save();
    }

    /**
     * Reloads config from file and updates all modules
     */
    public static void reloadConfig() {
        ModConfig.load();
        updateModulesFromConfig();
    }

    /**
     * Updates all modules from the current config
     */
    private static void updateModulesFromConfig() {
        ModConfig.ModConfigData config = ModConfig.CONFIG;

        // Update stack size module
        withModule(Modules.STACK_SIZE, StackSizeModule.class, module -> {
            module.setEnabled(config.enableStackSizeIncrease);
            module.setMaxStackSize(config.maxStackSize);
        });

        // Update merge radius module
        withModule(Modules.MERGE_RADIUS, MergeRadiusModule.class, module -> {
            module.setEnabled(config.enableLargerMergeRadius);
            module.setMergeRadius(config.mergeRadius);
        });

        // Update custom name module
        withModule(Modules.CUSTOM_NAME, CustomNameModule.class, module -> {
            module.setEnabled(config.enableCustomItemNames);
            module.setYOffset(config.yOffset);
        });

        // Update merge effects module
        withModule(Modules.MERGE_EFFECTS, MergeEffectsModule.class, module -> {
            module.setEnabled(config.enableMergeEffects);
        });

        // Update visual effects module
        withModule(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, module -> {
            module.setEnabled(config.enableVisualEffects);
            module.setBeamEffectEnabled(config.enableBeamEffect);
        });
    }

    // Helper methods for cleaner code

    /**
     * Execute an action with a module if it exists
     */
    private static <T> void withModule(String moduleId, Class<T> moduleClass, java.util.function.Consumer<T> action) {
        Object module = MANAGER.getModule(moduleId);
        if (moduleClass.isInstance(module)) {
            action.accept(moduleClass.cast(module));
        }
    }

    /**
     * Set a property on a module and save the config
     */
    private static <T, V> void setProperty(String moduleId, Class<T> moduleClass, BiConsumer<T, V> setter, V value) {
        withModule(moduleId, moduleClass, module -> {
            setter.accept(module, value);
            ModConfig.save();
        });
    }

    /**
     * Get a property from a module with a default fallback
     */
    private static <T, R> R getProperty(String moduleId, Class<T> moduleClass, Function<T, R> getter, R defaultValue) {
        Object module = MANAGER.getModule(moduleId);
        if (moduleClass.isInstance(module)) {
            return getter.apply(moduleClass.cast(module));
        }
        return defaultValue;
    }

    // Stack Size Module API

    public static void setStackSizeIncreaseEnabled(boolean enabled) {
        setModuleEnabled(Modules.STACK_SIZE, enabled);
    }

    public static boolean isStackSizeIncreaseEnabled() {
        return isModuleEnabled(Modules.STACK_SIZE);
    }

    public static void setMaxStackSize(int maxSize) {
        setProperty(Modules.STACK_SIZE, StackSizeModule.class, StackSizeModule::setMaxStackSize, maxSize);
    }

    public static int getMaxStackSize() {
        return getProperty(Modules.STACK_SIZE, StackSizeModule.class, StackSizeModule::getMaxStackSize, 64);
    }

    // Merge Radius Module API

    public static void setLargerMergeRadiusEnabled(boolean enabled) {
        setModuleEnabled(Modules.MERGE_RADIUS, enabled);
    }

    public static boolean isLargerMergeRadiusEnabled() {
        return isModuleEnabled(Modules.MERGE_RADIUS);
    }

    public static void setMergeRadius(double radius) {
        setProperty(Modules.MERGE_RADIUS, MergeRadiusModule.class, MergeRadiusModule::setMergeRadius, radius);
    }

    public static double getMergeRadius() {
        return getProperty(Modules.MERGE_RADIUS, MergeRadiusModule.class, MergeRadiusModule::getMergeRadius, 0.5);
    }

    // Custom Name Module API

    public static void setCustomItemNamesEnabled(boolean enabled) {
        setModuleEnabled(Modules.CUSTOM_NAME, enabled);
    }

    public static boolean areCustomItemNamesEnabled() {
        return isModuleEnabled(Modules.CUSTOM_NAME);
    }

    public static void setCustomNameYOffset(float offset) {
        setProperty(Modules.CUSTOM_NAME, CustomNameModule.class, CustomNameModule::setYOffset, offset);
    }

    public static float getCustomNameYOffset() {
        return getProperty(Modules.CUSTOM_NAME, CustomNameModule.class, CustomNameModule::getYOffset, 10f);
    }

    // Merge Effects Module API

    public static void setMergeEffectsEnabled(boolean enabled) {
        setModuleEnabled(Modules.MERGE_EFFECTS, enabled);
    }

    public static boolean areMergeEffectsEnabled() {
        return isModuleEnabled(Modules.MERGE_EFFECTS);
    }

    // Visual Effects Module API - Core settings

    public static void setVisualEffectsEnabled(boolean enabled) {
        setModuleEnabled(Modules.VISUAL_EFFECTS, enabled);
    }

    public static boolean areVisualEffectsEnabled() {
        return isModuleEnabled(Modules.VISUAL_EFFECTS);
    }

    public static void setBeamEffectEnabled(boolean enabled) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setBeamEffectEnabled, enabled);
    }

    public static boolean isBeamEffectEnabled() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::isBeamEffectEnabled, false);
    }

    public static void setGlowEffectEnabled(boolean enabled) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setGlowEffectEnabled, enabled);
    }

    public static boolean isGlowEffectEnabled() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::isGlowEffectEnabled, false);
    }

    public static void setEnhancedTextEnabled(boolean enabled) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setEnhancedTextEnabled, enabled);
    }

    public static boolean isEnhancedTextEnabled() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::isEnhancedTextEnabled, false);
    }

    // Visual Effects Module API - Beam properties

    public static void setBeamAnimationDuration(int duration) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setBeamAnimationDuration, duration);
    }

    public static int getBeamAnimationDuration() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getBeamAnimationDuration, 40);
    }

    public static void setFadeDistance(float distance) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setFadeDistance, distance);
    }

    public static float getFadeDistance() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getFadeDistance, 8f);
    }

    public static void setMaxFadeDistance(float distance) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setMaxFadeDistance, distance);
    }

    public static float getMaxFadeDistance() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getMaxFadeDistance, 10f);
    }

    public static void setBeamMaxHeight(float height) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setBeamMaxHeight, height);
    }

    public static float getBeamMaxHeight() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getBeamMaxHeight, 2.0f);
    }

    public static void setBeamWidth(float width) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setBeamWidth, width);
    }

    public static float getBeamWidth() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getBeamWidth, 0.2f);
    }

    public static void setBeamAlpha(float alpha) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setBeamAlpha, alpha);
    }

    public static float getBeamAlpha() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getBeamAlpha, 0.5f);
    }

    public static void setInnerRotationSpeed(float speed) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setInnerRotationSpeed, speed);
    }

    public static float getInnerRotationSpeed() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getInnerRotationSpeed, -0.25f);
    }

    public static void setOuterRotationSpeed(float speed) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setOuterRotationSpeed, speed);
    }

    public static float getOuterRotationSpeed() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getOuterRotationSpeed, 0.15f);
    }

    // Visual Effects Module API - Glow properties

    public static void setGlowAnimationDuration(int duration) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setGlowAnimationDuration, duration);
    }

    public static int getGlowAnimationDuration() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getGlowAnimationDuration, 40);
    }

    public static void setPulsePeriod(float period) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setPulsePeriod, period);
    }

    public static float getPulsePeriod() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getPulsePeriod, 80f);
    }

    public static void setPulseAmp(float amp) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setPulseAmp, amp);
    }

    public static float getPulseAmp() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getPulseAmp, 0.1f);
    }

    public static void setGlowSize(float size) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setGlowSize, size);
    }

    public static float getGlowSize() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getGlowSize, 1.0f);
    }

    public static void setGlowAlpha(float alpha) {
        setProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::setGlowAlpha, alpha);
    }

    public static float getGlowAlpha() {
        return getProperty(Modules.VISUAL_EFFECTS, VisualEffectsModule.class, VisualEffectsModule::getGlowAlpha, 0.5f);
    }
}