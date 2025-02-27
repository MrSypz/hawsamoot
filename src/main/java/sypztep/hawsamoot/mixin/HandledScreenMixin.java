package sypztep.hawsamoot.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.hawsamoot.api.border.BorderStyle;
import sypztep.hawsamoot.common.tag.ModItemTags;
import sypztep.hawsamoot.util.BorderHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mixin to override the vanilla tooltip rendering with styled border tooltips.
 */
@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(
            method = "drawMouseoverTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/util/Identifier;)V"),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    protected void drawMouseoverTooltipMixin(DrawContext context, int x, int y, CallbackInfo ci, ItemStack stack) {
        if (client != null && !stack.isEmpty()) {
            List<Text> text = Screen.getTooltipFromItem(client, stack);

            // Convert Text to TooltipComponent
            List<TooltipComponent> components = text.stream()
                    .map(Text::asOrderedText)
                    .map(TooltipComponent::of)
                    .collect(Collectors.toList());

            // Add any tooltip data the item might have
            stack.getTooltipData().ifPresent(data -> {
                if (!components.isEmpty()) {
                    components.add(1, TooltipComponent.of(data));
                } else {
                    components.add(TooltipComponent.of(data));
                }
            });

            BorderStyle style = determineItemBorderStyle(stack);

            BorderHandler.renderStyledTooltip(
                    context,
                    this.textRenderer,
                    components,
                    x,
                    y,
                    HoveredTooltipPositioner.INSTANCE,
                    style
            );
            ci.cancel();
        }
    }

    @Unique
    private BorderStyle determineItemBorderStyle(ItemStack stack) {
        // Then check for mod tags (with priority)
        if (stack.isIn(ModItemTags.RARITY_CELESTIAL)) {
            return BorderStyle.CELESTIAL;
        } else if (stack.isIn(ModItemTags.RARITY_MYTHIC)) {
            return BorderStyle.MYTHIC;
        } else if (stack.isIn(ModItemTags.RARITY_LEGENDARY)) {
            return BorderStyle.LEGENDARY;
        } else if (stack.isIn(ModItemTags.RARITY_EPIC)) {
            return BorderStyle.EPIC;
        } else if (stack.isIn(ModItemTags.RARITY_RARE)) {
            return BorderStyle.RARE;
        } else if (stack.isIn(ModItemTags.RARITY_UNCOMMON)) {
            return BorderStyle.UNCOMMON;
        } else if (stack.isIn(ModItemTags.RARITY_COMMON)) {
            return BorderStyle.COMMON;
        }

        String rarity = stack.getRarity().asString().toLowerCase();
        return switch (rarity) {
            case "uncommon" -> BorderStyle.UNCOMMON;
            case "rare" -> BorderStyle.RARE;
            case "epic" -> BorderStyle.EPIC;
            default -> BorderStyle.COMMON;
        };
    }
}