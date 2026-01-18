package com.vomiter.survivorsbutchercraft.data;

import com.vomiter.survivorsbutchercraft.data.loot.SBMeatHookLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class SBLootTableProvider extends LootTableProvider {
    public SBLootTableProvider(PackOutput output) {
        super(output, Set.of(),
                List.of(
                    new SubProviderEntry(SBMeatHookLootTables::new, LootContextParamSets.ALL_PARAMS)
                )
        );
    }
}
