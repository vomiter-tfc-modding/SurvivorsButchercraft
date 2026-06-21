package com.vomiter.survivorsbutchercraft.util;

import net.minecraft.world.item.ItemStack;

public class ThreadLocalFlags {
    public static ThreadLocal<Boolean> dropLootForButchering = ThreadLocal.withInitial(() -> false);
    public static ThreadLocal<ItemStack> carcass = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
}
