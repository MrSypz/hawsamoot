package sypztep.hawsamoot.common.util;

import org.jetbrains.annotations.Nullable;

/**
 * Interface for all library modules
 */
public interface Module {
    /**
     * Gets the unique ID of this module
     */
    String getId();

    /**
     * Gets the display name of this module
     */
    String getName();

    /**
     * Gets the description of this module
     */
    @Nullable
    String getDescription();

    /**
     * Checks if this module is enabled
     */
    boolean isEnabled();

    /**
     * Sets whether this module is enabled
     */
    void setEnabled(boolean enabled);

    /**
     * Initializes this module
     */
    void initialize();

    /**
     * Checks if this module requires server sync
     *  False for Client Side
     */
    boolean requiresServerSync();
}