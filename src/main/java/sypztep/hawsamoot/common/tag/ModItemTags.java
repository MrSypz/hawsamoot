package sypztep.hawsamoot.common.tag;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import sypztep.hawsamoot.Hawsamoot;

public class ModItemTags {
    public static final TagKey<Item> RARITY_COMMON = TagKey.of(RegistryKeys.ITEM, Hawsamoot.id("rarity_common"));
    public static final TagKey<Item> RARITY_UNCOMMON = TagKey.of(RegistryKeys.ITEM, Hawsamoot.id("rarity_uncommon"));
    public static final TagKey<Item> RARITY_RARE = TagKey.of(RegistryKeys.ITEM, Hawsamoot.id("rarity_rare"));
    public static final TagKey<Item> RARITY_EPIC = TagKey.of(RegistryKeys.ITEM, Hawsamoot.id("rarity_epic"));
    public static final TagKey<Item> RARITY_LEGENDARY = TagKey.of(RegistryKeys.ITEM, Hawsamoot.id("rarity_legendary"));
    public static final TagKey<Item> RARITY_MYTHIC = TagKey.of(RegistryKeys.ITEM, Hawsamoot.id("rarity_mythic"));
    public static final TagKey<Item> RARITY_CELESTIAL = TagKey.of(RegistryKeys.ITEM, Hawsamoot.id("rarity_celestial"));
}
