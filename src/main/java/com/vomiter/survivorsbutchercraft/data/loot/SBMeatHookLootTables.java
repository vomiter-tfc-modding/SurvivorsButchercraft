package com.vomiter.survivorsbutchercraft.data.loot;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.core.Carcass;
import com.vomiter.survivorsbutchercraft.core.MeatHookStage;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class SBMeatHookLootTables implements LootTableSubProvider {
    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> out) {
        for (Carcass carcass : Carcass.values()) {
            for (MeatHookStage stage : MeatHookStage.values()) {
                if(stage.equals(MeatHookStage.HOOK)) continue;

                // main drops
                var mainDrops = carcass.dropsFor(stage);
                var supportDrops = carcass.dropsForSupport(stage);
                var trivialDrops = carcass.dropsForTrivial(stage);
                if(mainDrops.isEmpty() && supportDrops.isEmpty() && trivialDrops.isEmpty()){
                    SurvivorsButchercraft.LOGGER.warn("Meat hook loot table for {} at stage {} is completely empty!", carcass, stage);
                    continue;
                }

                var main = LootTable.lootTable();
                if (!mainDrops.isEmpty()) {
                    main.withPool(LootPools.poolOnce(mainDrops));
                }
                if (!supportDrops.isEmpty()) {
                    main.withPool(MeatHookLootHelper.includeSupport(stage, carcass));
                }
                if (!trivialDrops.isEmpty()) {
                    main.withPool(MeatHookLootHelper.includeTrivial(stage, carcass));
                }
                if (carcass == Carcass.YAK && stage == MeatHookStage.BISECT) {
                    SurvivorsButchercraft.LOGGER.warn(
                            "[DATAGEN] YAK/BISECT main={}, support={}, trivial={}",
                            mainDrops.size(), supportDrops.size(), trivialDrops.size()
                    );
                }


                out.accept(MeatHookLootHelper.mainTable(carcass, stage), main);

                // support & trivial tables：有內容再產
                if (!supportDrops.isEmpty()) {
                    if (carcass == Carcass.YAK && stage == MeatHookStage.BISECT) {
                        SurvivorsButchercraft.LOGGER.warn("[DATAGEN] WRITING support table for YAK/BISECT");
                    }
                    out.accept(
                            MeatHookLootHelper.supportTable(carcass, stage),
                            LootTable.lootTable().withPool(
                                    LootPools.poolOnce(supportDrops
                                    )
                            )
                    );
                }

                if (!trivialDrops.isEmpty()) {
                    out.accept(
                            MeatHookLootHelper.trivialTable(carcass, stage),
                            LootTable.lootTable().withPool(
                                    LootPools.poolOnce(trivialDrops)
                            )
                    );
                }
            }
        }
    }
}

