package sypztep.hawsamoot.common.module;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.hawsamoot.common.config.ModConfig;
import sypztep.hawsamoot.common.util.Module;

public class CustomNameModule implements Module {
    @Override
    public String getId() {
        return "custom_name";
    }

    @Override
    public String getName() {
        return "Custom Item Names";
    }

    @Override
    public String getDescription() {
        return "Shows item count and name directly on dropped items";
    }

    @Override
    public boolean isEnabled() {
        return ModConfig.CONFIG.enableCustomItemNames;
    }

    @Override
    public void setEnabled(boolean enabled) {
        ModConfig.CONFIG.enableCustomItemNames = enabled;
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean requiresServerSync() {
        return false; // Client-side only
    }
    public float getYOffset() {
        return ModConfig.CONFIG.yOffset;
    }
    public void setYOffset(float yOffset) {
        ModConfig.CONFIG.yOffset = yOffset;
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