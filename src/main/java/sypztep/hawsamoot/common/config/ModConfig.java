package sypztep.hawsamoot.common.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.util.Formatting;
import sypztep.hawsamoot.Hawsamoot;

@Config(name = Hawsamoot.MODID)
public class ModConfig implements ConfigData {
    // Stack Size Module
    @ConfigEntry.Category("stack_size")
    @ConfigEntry.Gui.TransitiveObject
    public StackSizeModule stackSizeModule = new StackSizeModule();

    // Merge Module
    @ConfigEntry.Category("merge")
    @ConfigEntry.Gui.TransitiveObject
    public MergeModule mergeModule = new MergeModule();

    // Client Module
    @ConfigEntry.Category("client")
    @ConfigEntry.Gui.TransitiveObject
    public ClientModule clientModule = new ClientModule();

    // Stack Size Configuration
    public static class StackSizeModule {
        @ConfigEntry.Gui.Tooltip
        public boolean enableStackSizeIncrease = true;

        @ConfigEntry.Gui.RequiresRestart
        @ConfigEntry.BoundedDiscrete(min = 1, max = 64 * 128)
        public int maxStackSize = 1024;
    }

    // Merge Radius Configuration
    public static class MergeModule {
        @ConfigEntry.Gui.Tooltip
        @ConfigEntry.Gui.PrefixText
        public boolean enableLargerMergeRadius = true;

        @ConfigEntry.Gui.RequiresRestart
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10)
        public double mergeRadius = 2.0;
    }

    // Client-Side Configuration
    public static class ClientModule {
        // Custom Name Submodule
        @ConfigEntry.Gui.CollapsibleObject()
        public CustomNameModule customNameModule = new CustomNameModule();

        // Merge Effects Submodule
        @ConfigEntry.Gui.CollapsibleObject()
        public MergeEffectsModule mergeEffectsModule = new MergeEffectsModule();

        // Visual Effects Submodule
        @ConfigEntry.Gui.CollapsibleObject()
        public VisualEffectsModule visualEffectsModule = new VisualEffectsModule();

        // Custom Name Configurations
        public static class CustomNameModule {
            @ConfigEntry.Gui.Tooltip
            public boolean enableCustomItemNames = true;

            @ConfigEntry.BoundedDiscrete(min = -50, max = 50)
            public float yOffset = 10f;

            // Use the custom TextColor enum
            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public TextColor prefixFormatting = TextColor.GOLD;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public TextColor countFormatting = TextColor.RED;

            @ConfigEntry.Gui.Tooltip
            public boolean countBold = true;

            @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.DROPDOWN)
            public TextColor nameFormatting = TextColor.WHITE;

            @ConfigEntry.Gui.Tooltip
            public String prefixText = ">";
        }

        // Merge Effects Configurations
        public static class MergeEffectsModule {
            @ConfigEntry.Gui.Tooltip
            public boolean enableMergeEffects = true;
        }

        // Visual Effects Configurations
        public static class VisualEffectsModule {
            @ConfigEntry.Gui.Tooltip
            public boolean enableVisualEffects = true;

            @ConfigEntry.Gui.CollapsibleObject()
            public BeamEffectModule beamEffectModule = new BeamEffectModule();

            @ConfigEntry.Gui.CollapsibleObject()
            public GlowEffectModule glowEffectModule = new GlowEffectModule();

            @ConfigEntry.Gui.Tooltip
            public boolean enableEnhancedText = true;

            // Beam Effect Configurations
            public static class BeamEffectModule {
                @ConfigEntry.Gui.Tooltip
                public boolean enableBeamEffect = true;

                @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
                public int beamAnimationDuration = 40;

                @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
                public float fadeDistance = 8f;

                @ConfigEntry.BoundedDiscrete(min = 1, max = 20)
                public float maxFadeDistance = 10f;

                @ConfigEntry.BoundedDiscrete(min = 0, max = 5)
                public float beamMaxHeight = 2.0f;

                @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
                public float beamWidth = 0.2f;

                @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
                public float beamAlpha = 0.5f;

                @ConfigEntry.BoundedDiscrete(min = -1, max = 1)
                public float innerRotationSpeed = -0.25f;

                @ConfigEntry.BoundedDiscrete(min = -1, max = 1)
                public float outerRotationSpeed = 0.15f;
            }

            // Glow Effect Configurations
            public static class GlowEffectModule {
                @ConfigEntry.Gui.Tooltip
                public boolean enableGlowEffect = true;

                @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
                public int glowAnimationDuration = 40;

                @ConfigEntry.BoundedDiscrete(min = 1, max = 200)
                public float pulsePeriod = 80f;

                @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
                public float pulseAmp = 0.1f;

                @ConfigEntry.BoundedDiscrete(min = 0, max = 5)
                public float glowSize = 1.0f;

                @ConfigEntry.BoundedDiscrete(min = 0, max = 1)
                public float glowAlpha = 0.5f;
            }
        }
    }
    // Create a custom enum for formatting options
    public enum TextColor {
        BLACK("Black", Formatting.BLACK),
        DARK_BLUE("Dark Blue", Formatting.DARK_BLUE),
        DARK_GREEN("Dark Green", Formatting.DARK_GREEN),
        DARK_AQUA("Dark Aqua", Formatting.DARK_AQUA),
        DARK_RED("Dark Red", Formatting.DARK_RED),
        DARK_PURPLE("Dark Purple", Formatting.DARK_PURPLE),
        GOLD("Gold", Formatting.GOLD),
        GRAY("Gray", Formatting.GRAY),
        DARK_GRAY("Dark Gray", Formatting.DARK_GRAY),
        BLUE("Blue", Formatting.BLUE),
        GREEN("Green", Formatting.GREEN),
        AQUA("Aqua", Formatting.AQUA),
        RED("Red", Formatting.RED),
        LIGHT_PURPLE("Light Purple", Formatting.LIGHT_PURPLE),
        YELLOW("Yellow", Formatting.YELLOW),
        WHITE("White", Formatting.WHITE);

        private final String displayName;
        private final Formatting formatting;

        TextColor(String displayName, Formatting formatting) {
            this.displayName = displayName;
            this.formatting = formatting;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Formatting getFormatting() {
            return formatting;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}