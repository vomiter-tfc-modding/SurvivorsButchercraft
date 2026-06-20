package com.vomiter.survivorsbutchercraft;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Helpers {

    public static ResourceLocation id(String path){
        return id(SurvivorsButchercraft.MODID, path);
    }

    public static ResourceLocation id(String namespace, String path){
        return new ResourceLocation(namespace, path);
    }

    public static void addOrMerge(ObjectArrayList<ItemStack> list, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }

        ItemStack remaining = stack.copy();

        for (ItemStack existing : list) {
            if (!ItemStack.isSameItemSameTags(existing, remaining)) {
                continue;
            }

            int space = existing.getMaxStackSize() - existing.getCount();

            if (space <= 0) {
                continue;
            }

            int moved = Math.min(space, remaining.getCount());
            existing.grow(moved);
            remaining.shrink(moved);

            if (remaining.isEmpty()) {
                return;
            }
        }

        list.add(remaining);
    }
}
