package com.vomiter.survivorsbutchercraft.data.tags;

import com.vomiter.survivorsbutchercraft.Helpers;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SBTags {
    public static class Items{
        static TagKey<Item> createTFC(String path){
            return ItemTags.create(Helpers.id("tfc", path));
        }

        static TagKey<Item> create(String path){
            return ItemTags.create(Helpers.id(path));
        }
        public static TagKey<Item> SKINNING_TOOLS = create("skinning_tools");
        public static TagKey<Item> BEHEADING_TOOLS = create("beheading_tools");
        public static TagKey<Item> GUTTING_TOOLS = create("gutting_tools");
        public static TagKey<Item> BUTCHERING_TOOLS = create("butchering_tools");
        public static TagKey<Item> BUTCHERY_SKIP_LOOT = create("butchery_skip_loot");
        public static TagKey<Item> BUTCHER_RAW_MEATS = create("foods/butchercraft_raw_meats");
        public static TagKey<Item> BUTCHER_SCRAPABLE = create("butcher_scrapable");
        public static TagKey<Item> LINKED_SAUSAGE = create("linked_sausages");
        public static TagKey<Item> LARGE_CARCASS = create("large_carcasses");
        public static TagKey<Item> TALLOW_INGREDIENT = create("tallow_ingredients");
    }
}
