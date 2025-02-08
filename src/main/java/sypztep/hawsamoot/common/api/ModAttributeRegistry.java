package sypztep.hawsamoot.common.api;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.List;

public interface ModAttributeRegistry {
    List<RegistryEntry<EntityAttribute>> getAttributes();
}