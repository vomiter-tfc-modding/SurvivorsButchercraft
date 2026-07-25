package com.vomiter.survivorsbutchercraft.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;

public class ToolTierGetter {
    public static Tier get(Item item){
        if (item instanceof TieredItem tieredItem){
            return tieredItem.getTier();
        }
        return null;
    }
}
