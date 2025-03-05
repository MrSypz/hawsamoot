package sypztep.hawsamoot.common.module;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.hawsamoot.client.HawsamootClient;
import sypztep.hawsamoot.common.util.ConfigHolder;

public class CustomNameModule implements ConfigHolder {
    public CustomNameModule() {}
    public float getYOffset() {
        return HawsamootClient.CONFIG.clientModule.customNameModule.yOffset;
    }

    @Override
    public boolean isEnabled() {
        return HawsamootClient.CONFIG.clientModule.customNameModule.enableCustomItemNames;
    }

    public Text updateCustomName(ItemEntity entity) {
        if (!HawsamootClient.CONFIG.clientModule.customNameModule.enableCustomItemNames) return null;

        if (entity == null) return null;

        ItemStack stack = entity.getStack();
        if (stack.isEmpty()) return null;

        int count = stack.getCount();
        String itemName = stack.getName().getString();

        // Apply formatting from config
        Text countText = Text.literal(" x" + count + " ");
        if (HawsamootClient.CONFIG.clientModule.customNameModule.countBold) {
            countText = countText.copy().formatted(HawsamootClient.CONFIG.clientModule.customNameModule.countFormatting.getFormatting(), Formatting.BOLD);
        } else {
            countText = countText.copy().formatted(HawsamootClient.CONFIG.clientModule.customNameModule.countFormatting.getFormatting());
        }

        // Return formatted text with all custom options
        return Text.literal(HawsamootClient.CONFIG.clientModule.customNameModule.prefixText).formatted(HawsamootClient.CONFIG.clientModule.customNameModule.prefixFormatting.getFormatting())
                .append(countText)
                .append(Text.literal(itemName).formatted(HawsamootClient.CONFIG.clientModule.customNameModule.nameFormatting.getFormatting()));
    }
}