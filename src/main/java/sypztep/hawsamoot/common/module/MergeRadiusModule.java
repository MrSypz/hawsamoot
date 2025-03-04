package sypztep.hawsamoot.common.module;

import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.util.Module;

public class MergeRadiusModule implements Module {
    @Override
    public String getId() {
        return "merge_radius";
    }

    @Override
    public String getName() {
        return "Enhanced Merge Radius";
    }

    @Override
    public String getDescription() {
        return "Increases the radius at which items merge together";
    }

    @Override
    public boolean isEnabled() {
        return ModConfig.CONFIG.enableLargerMergeRadius;
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfig.CONFIG.enableLargerMergeRadius = enabled;
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean requiresServerSync() {
        return true;
    }

    /**
     * Gets the current merge radius
     */
    public double getMergeRadius() {
        return ModConfig.CONFIG.mergeRadius;
    }

    /**
     * Sets the merge radius
     */
    public void setMergeRadius(double radius) {
        if (radius >= 0.5 && radius <= 10.0) {
            ModConfig.CONFIG.mergeRadius = radius;
        }
    }
}