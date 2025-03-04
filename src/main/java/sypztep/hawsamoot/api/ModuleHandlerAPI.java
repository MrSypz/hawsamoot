package sypztep.hawsamoot.api;

import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.module.MergeRadiusModule;
import sypztep.hawsamoot.common.module.StackSizeModule;
import sypztep.hawsamoot.common.util.ModuleManager;

public class ModuleHandlerAPI {

    /**
     * Enables or disables a module by its ID
     */
    public static void setModuleEnabled(String moduleId, boolean enabled) {
        ModuleManager.getInstance().setModuleEnabled(moduleId, enabled);
    }

    /**
     * Checks if a module is enabled
     */
    public static boolean isModuleEnabled(String moduleId) {
        return ModuleManager.getInstance().isModuleEnabled(moduleId);
    }

    /**
     * Sets the maximum stack size for items
     */
    public static void setMaxStackSize(int maxSize) {
        StackSizeModule module = (StackSizeModule) ModuleManager.getInstance().getModule("stack_size");
        if (module != null) {
            module.setMaxStackSize(maxSize);
            ModConfig.save();
        }
    }

    /**
     * Gets the current maximum stack size
     */
    public static int getMaxStackSize() {
        StackSizeModule module = (StackSizeModule) ModuleManager.getInstance().getModule("stack_size");
        return module != null ? module.getMaxStackSize() : 64;
    }

    /**
     * Sets the merge radius for items
     */
    public static void setMergeRadius(double radius) {
        MergeRadiusModule module = (MergeRadiusModule) ModuleManager.getInstance().getModule("merge_radius");
        if (module != null) {
            module.setMergeRadius(radius);
            ModConfig.save();
        }
    }

    /**
     * Gets the current merge radius
     */
    public static double getMergeRadius() {
        MergeRadiusModule module = (MergeRadiusModule) ModuleManager.getInstance().getModule("merge_radius");
        return module != null ? module.getMergeRadius() : 0.5;
    }

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
}
