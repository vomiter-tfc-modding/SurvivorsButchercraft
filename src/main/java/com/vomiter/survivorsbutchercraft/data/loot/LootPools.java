package com.vomiter.survivorsbutchercraft.data.loot;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;

public final class LootPools {
    private LootPools() {}

    public static LootPool.Builder poolOnce(List<DropSpec> specs) {
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1));
        for (DropSpec spec : specs) {
            var e = spec.entry();
            for (LootItemFunction.Builder function : spec.functions()) {
                e.apply(function);
            }
            pool.add(e);
        }
        return pool;
    }
}
