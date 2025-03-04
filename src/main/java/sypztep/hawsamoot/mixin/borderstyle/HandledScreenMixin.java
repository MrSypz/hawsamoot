package sypztep.hawsamoot.mixin.borderstyle;

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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.hawsamoot.api.border.BorderStyle;
import sypztep.hawsamoot.client.render.util.BorderRenderer;

import java.util.List;
import java.util.stream.Collectors;

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

            List<TooltipComponent> components = text.stream()
                    .map(Text::asOrderedText)
                    .map(TooltipComponent::of)
                    .collect(Collectors.toList());

            stack.getTooltipData().ifPresent(data -> {
                if (!components.isEmpty()) {
                    components.add(1, TooltipComponent.of(data));
                } else {
                    components.add(TooltipComponent.of(data));
                }
            });

            BorderStyle style = BorderRenderer.determineItemBorderStyle(stack);

            BorderRenderer.renderStyledTooltip(
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
}