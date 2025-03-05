package sypztep.hawsamoot.common.module;

import sypztep.hawsamoot.client.HawsamootClient;
import sypztep.hawsamoot.common.util.ConfigHolder;

public class StackSizeModule implements ConfigHolder {
    public StackSizeModule() {}
    @Override
    public boolean isEnabled() {
        return HawsamootClient.CONFIG.stackSizeModule.enableStackSizeIncrease;
    }
    public int getMaxStackSize() {
        return HawsamootClient.CONFIG.stackSizeModule.maxStackSize;
    }
}