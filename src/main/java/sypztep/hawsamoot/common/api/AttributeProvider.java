package sypztep.hawsamoot.common.api;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.List;

public class AttributeProvider {
    private static final List<AttributeRegistration> ATTRIBUTES = new ArrayList<>();
    private static final List<ModAttributeRegistry> REGISTRIES = new ArrayList<>();

    public static void registerAttribute(String id, EntityAttribute attribute) {
        ATTRIBUTES.add(new AttributeRegistration(id, attribute));
    }

    public static void addRegistry(ModAttributeRegistry registry) {
        REGISTRIES.add(registry);
    }

    public static List<RegistryEntry<EntityAttribute>> getAllAttributes() {
        List<RegistryEntry<EntityAttribute>> attributes = new ArrayList<>();
        for (ModAttributeRegistry registry : REGISTRIES) {
            attributes.addAll(registry.getAttributes());
        }
        return attributes;
    }

    public record AttributeRegistration(String id, EntityAttribute attribute) {}
}