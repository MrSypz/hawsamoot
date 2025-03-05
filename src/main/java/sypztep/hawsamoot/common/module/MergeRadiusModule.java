package sypztep.hawsamoot.common.module;

import sypztep.hawsamoot.client.HawsamootClient;
import sypztep.hawsamoot.common.util.ConfigHolder;

public class MergeRadiusModule implements ConfigHolder {
    public MergeRadiusModule() {}
    @Override
    public boolean isEnabled() {
        return HawsamootClient.CONFIG.mergeModule.enableLargerMergeRadius;
    }

    public double getMergeRadius() {
        return HawsamootClient.CONFIG.mergeModule.mergeRadius;
    }
}