package com.vomiter.survivorsbutchercraft.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Optional;

public class EnchantmentUtil {
    public static Holder<Enchantment> get(ResourceKey<Enchantment> key){
        return Optional.ofNullable(ServerLifecycleHooks.getCurrentServer()).map(server -> server.overworld().registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(key)).orElse(null);
    }
}
