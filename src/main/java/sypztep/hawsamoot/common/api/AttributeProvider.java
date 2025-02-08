package sypztep.hawsamoot.common.api;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import java.util.ArrayList;
import java.util.EnumSet;
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
     * Registers a new attribute with the specified targets.
     *
     * @param id The unique identifier for the attribute.
     * @param attribute The RegistryEntry<EntityAttribute> instance.
     * @param baseValue The base value of the attribute.
     * @param targets Where this attribute should apply (LIVING, PLAYER, or BOTH).
     */
    public static void registerAttribute(String id, RegistryEntry<EntityAttribute> attribute, double baseValue, EnumSet<Target> targets) {
        if (id == null || attribute == null) {
            throw new IllegalArgumentException("ID and attribute must not be null");
        }
        ATTRIBUTES.add(new AttributeRegistration(id, attribute, baseValue, targets));
    }

    public static void registerAttribute(String id, RegistryEntry<EntityAttribute> attribute, double baseValue) {
        registerAttribute(id, attribute, baseValue, EnumSet.of(Target.LIVING, Target.PLAYER)); // Default: Apply to both
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
     * Retrieves all attributes that should be applied to the given target type.
     *
     * @param target The target entity type (LIVING or PLAYER).
     * @return A list of attributes applicable to the target.
     */
    public static List<AttributeRegistration> getAttributesForTarget(Target target) {
        List<AttributeRegistration> applicableAttributes = new ArrayList<>();
        for (AttributeRegistration registration : getAllAttributes()) {
            if (registration.targets().contains(target)) {
                applicableAttributes.add(registration);
            }
        }
        return applicableAttributes;
    }

    /**
     * Enum to define where an attribute should apply.
     */
    public enum Target {
        LIVING, PLAYER
    }

    /**
     * Record class that holds the registration information for an attribute.
     *
     * @param id The unique identifier of the attribute
     * @param attribute The RegistryEntry<@EntityAttribute> instance
     * @param baseValue The base value for Attribute
     * @param targets target appply attribute to apply on Target
     * @see Target
     */
    public record AttributeRegistration(
            String id,
            RegistryEntry<EntityAttribute> attribute,
            double baseValue,
            EnumSet<Target> targets
    ) {}
}
