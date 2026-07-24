package com.vomiter.survivorsbutchercraft.util;

import net.dries007.tfc.common.TFCTiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class ThreadLocalFlags {
    public static ThreadLocal<Boolean> dropLootForButchering = ThreadLocal.withInitial(() -> false);
    public static ThreadLocal<ItemStack> carcass = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
    public static ThreadLocal<ItemStack> carcassRendering = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
    public static ThreadLocal<Tier> tierThreadLocal = ThreadLocal.withInitial(() -> TFCTiers.METAMORPHIC);
}
