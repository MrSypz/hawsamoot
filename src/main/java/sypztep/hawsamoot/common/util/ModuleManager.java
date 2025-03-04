package sypztep.hawsamoot.common.util;

import sypztep.hawsamoot.Hawsamoot;
import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.module.*;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager();
    private final Map<String, Module> modules = new HashMap<>();

    private ModuleManager() {
        registerModule(new StackSizeModule());
        registerModule(new MergeRadiusModule());
        registerModule(new CustomNameModule());
        registerModule(new MergeEffectsModule());
        registerModule(new VisualEffectsModule());
    }

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    public void registerModule(Module module) {
        modules.put(module.getId(), module);
        Hawsamoot.LOGGER.info("Registered module: {}", module.getId());
    }

    public Module getModule(String id) {
        return modules.get(id);
    }

    public void setModuleEnabled(String id, boolean enabled) {
        Module module = modules.get(id);
        if (module != null) {
            module.setEnabled(enabled);
            ModConfig.save();
        }
    }

    public boolean isModuleEnabled(String id) {
        Module module = modules.get(id);
        return module != null && module.isEnabled();
    }

    public Map<String, Module> getModules() {
        return modules;
    }

    public void initializeModules() {
        modules.values().forEach(Module::initialize);
    }
}