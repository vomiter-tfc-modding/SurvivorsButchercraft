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
    }
}
