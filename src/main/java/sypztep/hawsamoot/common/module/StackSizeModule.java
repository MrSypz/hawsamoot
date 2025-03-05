package sypztep.hawsamoot.common.module;

import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.util.ConfigHolder;

public class StackSizeModule implements ConfigHolder {
    public StackSizeModule() {}
    @Override
    public boolean isEnabled() {
        return ModConfig.CONFIG.stackSizeModule.enableStackSizeIncrease;
    }
    public int getMaxStackSize() {
        return ModConfig.CONFIG.stackSizeModule.maxStackSize;
    }
}