package com.vomiter.survivorsbutchercraft.butchery.convert;

import com.vomiter.survivorsbutchercraft.util.WeightedPool;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record ResultConvert(Ingredient from, ItemStack to, int weight, boolean keep) implements WeightedPool.WeightedEntry {
}
