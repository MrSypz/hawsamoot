package sypztep.hawsamoot.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "en_us", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translate) {
        // Main Mod Configuration Title
        translate.add("text.autoconfig.hawsamoot.title", "Hawsamoot Mod Configuration");

        // Configuration Categories
        translate.add("text.autoconfig.hawsamoot.category.stack_size", "Stack Size");
        translate.add("text.autoconfig.hawsamoot.category.merge", "Item Merge");
        translate.add("text.autoconfig.hawsamoot.category.client", "Client Settings");

        // Stack Size Module
        translate.add("text.autoconfig.hawsamoot.option.stackSizeModule", "Stack Size Module");
        translate.add("text.autoconfig.hawsamoot.option.stackSizeModule.enableStackSizeIncrease", "Enable Stack Size Increase");
        translate.add("text.autoconfig.hawsamoot.option.stackSizeModule.enableStackSizeIncrease.@Tooltip", "Allows increasing the maximum stack size for items");
        translate.add("text.autoconfig.hawsamoot.option.stackSizeModule.maxStackSize", "Maximum Stack Size");
        translate.add("text.autoconfig.hawsamoot.option.stackSizeModule.maxStackSize.@Tooltip", "Set the maximum number of items that can stack together");

        // Merge Module
        translate.add("text.autoconfig.hawsamoot.option.mergeModule", "Item Merge Module");
        translate.add("text.autoconfig.hawsamoot.option.mergeModule.enableLargerMergeRadius", "Enable Larger Merge Radius");
        translate.add("text.autoconfig.hawsamoot.option.mergeModule.enableLargerMergeRadius.@Tooltip", "Increases the radius for item entity merging");
        translate.add("text.autoconfig.hawsamoot.option.mergeModule.mergeRadius", "Merge Radius");
        translate.add("text.autoconfig.hawsamoot.option.mergeModule.mergeRadius.@Tooltip", "The radius within which item entities will merge");

        // Client Module
        translate.add("text.autoconfig.hawsamoot.option.clientModule", "Client Settings Module");

        // Client Module - Custom Name
        translate.add("text.autoconfig.hawsamoot.option.clientModule.customNameModule", "Custom Item Names");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.customNameModule.enableCustomItemNames", "Enable Custom Item Names");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.customNameModule.enableCustomItemNames.@Tooltip", "Allows customization of item name display");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.customNameModule.yOffset", "Name Y-Offset");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.customNameModule.yOffset.@Tooltip", "Vertical offset for item name display");

        // Client Module - Merge Effects
        translate.add("text.autoconfig.hawsamoot.option.clientModule.mergeEffectsModule", "Merge Effects");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.mergeEffectsModule.enableMergeEffects", "Enable Merge Effects");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.mergeEffectsModule.enableMergeEffects.@Tooltip", "Enables visual effects when items merge");

        // Client Module - Visual Effects
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule", "Visual Effects");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.enableVisualEffects", "Enable Visual Effects");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.enableVisualEffects.@Tooltip", "Enables advanced visual effects for item interactions");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.enableEnhancedText", "Enable Enhanced Text");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.enableEnhancedText.@Tooltip", "Improves text rendering for item names and descriptions");

        // Beam Effect Module
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule", "Beam Effects");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.enableBeamEffect", "Enable Beam Effect");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.enableBeamEffect.@Tooltip", "Adds visual beam effects when items interact");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.beamAnimationDuration", "Beam Animation Duration");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.beamAnimationDuration.@Tooltip", "Duration of the beam animation in ticks");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.fadeDistance", "Beam Fade Distance");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.fadeDistance.@Tooltip", "Distance at which the beam starts to fade");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.maxFadeDistance", "Max Beam Fade Distance");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.maxFadeDistance.@Tooltip", "Maximum distance for beam fading");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.beamMaxHeight", "Beam Maximum Height");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.beamMaxHeight.@Tooltip", "Maximum height of the beam effect");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.beamWidth", "Beam Width");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.beamWidth.@Tooltip", "Width of the beam effect");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.beamAlpha", "Beam Transparency");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.beamAlpha.@Tooltip", "Transparency level of the beam effect");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.innerRotationSpeed", "Inner Beam Rotation Speed");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.innerRotationSpeed.@Tooltip", "Rotation speed of the inner beam");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.outerRotationSpeed", "Outer Beam Rotation Speed");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.beamEffectModule.outerRotationSpeed.@Tooltip", "Rotation speed of the outer beam");

        // Glow Effect Module
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule", "Glow Effects");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.enableGlowEffect", "Enable Glow Effect");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.enableGlowEffect.@Tooltip", "Adds glowing visual effects to items");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.glowAnimationDuration", "Glow Animation Duration");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.glowAnimationDuration.@Tooltip", "Duration of the glow animation in ticks");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.glowSize", "Glow Size");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.glowSize.@Tooltip", "Size of the glow effect");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.glowAlpha", "Glow Transparency");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.glowAlpha.@Tooltip", "Transparency level of the glow effect");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.pulsePeriod", "Glow Pulse Period");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.pulsePeriod.@Tooltip", "Period of the glow pulsing effect");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.pulseAmp", "Glow Pulse Amplitude");
        translate.add("text.autoconfig.hawsamoot.option.clientModule.visualEffectsModule.glowEffectModule.pulseAmp.@Tooltip", "Amplitude of the glow pulsing effect");
    }
}