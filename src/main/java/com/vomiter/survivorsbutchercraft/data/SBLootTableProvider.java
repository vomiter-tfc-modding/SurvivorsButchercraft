package com.vomiter.survivorsbutchercraft.data;

import com.vomiter.survivorsbutchercraft.data.loot.ButcherHeadAndHideBlockLootTables;
import com.vomiter.survivorsbutchercraft.data.loot.SBButcherBlockLootTables;
import com.vomiter.survivorsbutchercraft.data.loot.SBMeatHookLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SBLootTableProvider extends LootTableProvider {
    public SBLootTableProvider(PackOutput output) {
        super(output, Set.of(),
                List.of(
                    new SubProviderEntry(SBMeatHookLootTables::new, LootContextParamSets.ALL_PARAMS),
                    new SubProviderEntry(SBButcherBlockLootTables::new, LootContextParamSets.ALL_PARAMS),
                    new SubProviderEntry(ButcherHeadAndHideBlockLootTables::new, LootContextParamSets.BLOCK)
                )
        );
    }

    protected void validate(@NotNull Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationcontext) {
        /*
        * empty. Just to avoid vanilla datagen complaining about missing loot tables.
         */
    }
}
