package sypztep.hawsamoot.common.module;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.hawsamoot.Hawsamoot;
import sypztep.hawsamoot.client.HawsamootClient;
import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.util.ConfigHolder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomNameModule implements ConfigHolder {
    public CustomNameModule() {}
    public float getYOffset() {
        return HawsamootClient.CONFIG.clientModule.customNameModule.yOffset;
    }
    @Override
    public boolean isEnabled() {
        return HawsamootClient.CONFIG.clientModule.customNameModule.enableCustomItemNames;
    }
    private static final Pattern NAME_PATTERN = Pattern.compile("^\\s*>\\s*x(\\d+)\\s+(.+)$");

    public Text updateCustomName(ItemEntity entity) {
        ModConfig.ClientModule.CustomNameModule config = HawsamootClient.CONFIG.clientModule.customNameModule;
        if (!HawsamootClient.CONFIG.clientModule.customNameModule.enableCustomItemNames) return null;

        ItemStack stack = entity.getStack();
        if (stack.isEmpty() || !entity.hasCustomName()) return null;

        String fullName = entity.getCustomName().getString();
        Matcher m = NAME_PATTERN.matcher(fullName);
        if (!m.find()) return null;

        String countStr = m.group(1);
        String itemName = m.group(2).trim();  // Trim whitespace from item name

        // Build text components
        Text prefix = Text.literal(config.prefixText)
                .formatted(config.prefixFormatting.getFormatting());

        Text count = Text.literal(" x" + countStr + " ")
                .formatted(config.countFormatting.getFormatting())
                .styled(style ->
                        config.countBold ? style.withBold(true) : style
                );

        Text name = Text.literal(itemName)
                .formatted(config.nameFormatting.getFormatting());

        return prefix.copy().append(count).append(name);
    }
}