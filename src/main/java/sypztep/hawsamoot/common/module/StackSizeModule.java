package sypztep.hawsamoot.common.module;

import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.util.Module;

public class StackSizeModule implements Module {
    @Override
    public String getId() {
        return "stack_size";
    }

    @Override
    public String getName() {
        return "Enhanced Stack Size";
    }

    @Override
    public String getDescription() {
        return "Increases the maximum stack size for items";
    }

    @Override
    public boolean isEnabled() {
        return ModConfig.CONFIG.enableStackSizeIncrease;
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfig.CONFIG.enableStackSizeIncrease = enabled;
    }

    @Override
    public void initialize() {
        // Nothing to initialize
    }

    @Override
    public boolean requiresServerSync() {
        return true;
    }

    /**
     * Gets the current maximum stack size
     */
    public int getMaxStackSize() {
        return ModConfig.CONFIG.maxStackSize;
    }

    /**
     * Sets the maximum stack size
     */
    public void setMaxStackSize(int maxSize) {
        if (maxSize >= 64 && maxSize <= 9999) {
            ModConfig.CONFIG.maxStackSize = maxSize;
        }
    }
}