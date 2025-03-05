package sypztep.hawsamoot.common.module;

import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.util.ConfigHolder;

public class MergeRadiusModule implements ConfigHolder {
    public MergeRadiusModule() {}
    @Override
    public boolean isEnabled() {
        return ModConfig.CONFIG.mergeModule.enableLargerMergeRadius;
    }

    public double getMergeRadius() {
        return ModConfig.CONFIG.mergeModule.mergeRadius;
    }
}