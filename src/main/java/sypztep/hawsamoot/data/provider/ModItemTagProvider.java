package sypztep.hawsamoot.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryWrapper;
import sypztep.hawsamoot.common.tag.ModItemTags;

import java.util.concurrent.CompletableFuture;


public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
	public ModItemTagProvider(FabricDataOutput output) {
		super(output, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup));
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		getOrCreateTagBuilder(ModItemTags.RARITY_COMMON);
		getOrCreateTagBuilder(ModItemTags.RARITY_UNCOMMON);
		getOrCreateTagBuilder(ModItemTags.RARITY_RARE);
		getOrCreateTagBuilder(ModItemTags.RARITY_EPIC);
		getOrCreateTagBuilder(ModItemTags.RARITY_LEGENDARY);
		getOrCreateTagBuilder(ModItemTags.RARITY_MYTHIC);
		getOrCreateTagBuilder(ModItemTags.RARITY_CELESTIAL);
	}
}
