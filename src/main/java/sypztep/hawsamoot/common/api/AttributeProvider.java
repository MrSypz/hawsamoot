package sypztep.hawsamoot.common.api;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a centralized registry system for entity attributes in Minecraft mods.
 * This class allows multiple mods to register their custom attributes and ensures they are properly
 * added to entities.
 *
 * @since 1.0.0
 */
public class AttributeProvider {
    private static final List<AttributeRegistration> ATTRIBUTES = new ArrayList<>();
    private static final List<ModAttributeRegistry> REGISTRIES = new ArrayList<>();

    /**
     * Registers a new attribute with the specified ID and attribute instance.
     *
     * @param id The unique identifier for the attribute
     * @param attribute The RegistryEntry<@EntityAttribute> instance to register
     * @throws IllegalArgumentException if id or attribute is null
     *
     * @example
     * <pre>{@code
     * RegistryEntry<@EntityAttribute> myAttribute = new ClampedEntityAttribute("attribute.name.generic.example", 0, 0.0, 100.0);
     * AttributeProvider.registerAttribute("mod_id.example_attribute", myAttribute);
     * }</pre>
     */
    public static void registerAttribute(String id, RegistryEntry<EntityAttribute> attribute, double baseValue) {
        if (id == null || attribute == null) {
            throw new IllegalArgumentException("ID and attribute must not be null");
        }
        ATTRIBUTES.add(new AttributeRegistration(id, attribute,baseValue));
    }
    public static void registerAttribute(String id, RegistryEntry<EntityAttribute> attribute) {
        registerAttribute(id, attribute, 0.0);
    }

    /**
     * Adds a new ModAttributeRegistry to the provider system.
     * This method should be called during mod initialization.
     *
     * @param registry The ModAttributeRegistry implementation to add
     * @throws IllegalArgumentException if registry is null
     *
     * @example
     * <pre>{@code
     * public class MyMod implements ModInitializer {
     *     @Override
     *     public void onInitialize() {
     *         AttributeProvider.addRegistry(new MyModAttributes());
     *     }
     * }
     * }</pre>
     */
    public static void addRegistry(ModAttributeRegistry registry) {
        if (registry == null) {
            throw new IllegalArgumentException("Registry must not be null");
        }
        REGISTRIES.add(registry);
    }

    /**
     * Retrieves all registered attributes from all registered ModAttributeRegistry instances.
     *
     * @return A List of all registered RegistryEntry<EntityAttribute>
     */
    public static List<AttributeRegistration> getAllAttributes() {
        List<AttributeRegistration> attributes = new ArrayList<>();
        for (ModAttributeRegistry registry : REGISTRIES) {
            attributes.addAll(registry.getAttributeRegistrations());
        }
        return attributes;
    }

    /**
     * Record class that holds the registration information for an attribute.
     *
     * @param id The unique identifier of the attribute
     * @param attribute The RegistryEntry<@EntityAttribute> instance
     * @param baseValue The base value for Attribute
     */
    public record AttributeRegistration(String id, RegistryEntry<EntityAttribute> attribute, double baseValue) {}
}