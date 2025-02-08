package sypztep.hawsamoot.common.api;

import java.util.List;

/**
 * Interface for mod attribute registries.
 * Implement this interface to create a registry of custom attributes for your mod.
 *
 * @since 1.0.0
 *
 * @example
 * <pre>{@code
 * public class MyModAttributes implements ModAttributeRegistry {
 *     private static final List<RegistryEntry<EntityAttribute>> REGISTERED_ATTRIBUTES = new ArrayList<>();
 *
 *     public static final RegistryEntry<EntityAttribute> MY_ATTRIBUTE = register(
 *         "my_mod.my_attribute",
 *         new ClampedEntityAttribute("attribute.name.my_mod.my_attribute", 0, 0.0, 100.0)
 *     );
 *
 *     @Override
 *     public List<RegistryEntry<EntityAttribute>> getAttributes() {
 *         return REGISTERED_ATTRIBUTES;
 *     }
 *
 *     private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
 *         RegistryEntry<EntityAttribute> entry = Registry.registerReference(
 *             Registries.ATTRIBUTE,
 *             MyMod.id(id),
 *             attribute
 *         );
 *         REGISTERED_ATTRIBUTES.add(entry);
 *         return entry;
 *     }
 * }
 * }</pre>
 */
public interface ModAttributeRegistry {
    /**
     * Gets all registered attributes from this registry.
     *
     * @return A List of RegistryEntry<EntityAttribute> containing all registered attributes
     */
    List<AttributeProvider.AttributeRegistration> getAttributeRegistrations();
}