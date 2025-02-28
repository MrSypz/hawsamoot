package sypztep.hawsamoot.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import sypztep.hawsamoot.common.data.RarityBorder;
import sypztep.hawsamoot.common.init.ModDataComponents;

/**
 * Utility class for handling item rarity operations.
 */
public class RarityHelper {
    /**
     * Sets the rarity of an item using a predefined RarityBorder
     */
    public static void setRarity(ItemStack stack, RarityBorder rarity) {
        stack.set(ModDataComponents.RARITY_BORDER, rarity);
    }

    /**
     * Sets the rarity of an item using a rarity ID
     */
    public static void setRarity(ItemStack stack, Identifier rarityId) {
        setRarity(stack, RarityBorder.byId(rarityId));
    }

    /**
     * Gets the rarity of an item, returning COMMON if none is set
     */
    public static RarityBorder getRarity(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.RARITY_BORDER, RarityBorder.COMMON);
    }
}