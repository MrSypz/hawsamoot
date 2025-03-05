package sypztep.hawsamoot.common.module;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.util.ConfigHolder;

public class CustomNameModule implements ConfigHolder {
    public CustomNameModule() {}
    public float getYOffset() {
        return ModConfig.CONFIG.clientModule.customNameModule.yOffset;
    }

    @Override
    public boolean isEnabled() {
        return ModConfig.CONFIG.clientModule.customNameModule.enableCustomItemNames;
    }

    public Text updateCustomName(ItemEntity entity) {
        if (entity == null) return null;

        ItemStack stack = entity.getStack();
        if (stack.isEmpty()) return null;

        int count = stack.getCount();
        String itemName = stack.getName().getString();

        // Return formatted text with count
        return Text.literal(">").formatted(Formatting.GOLD)
                .append(Text.literal(" x" + count + " ").formatted(Formatting.RED, Formatting.BOLD))
                .append(Text.literal(itemName).formatted(Formatting.WHITE));
    }

}