package com.vomiter.survivorsbutchercraft.data.loot;

import net.minecraft.data.loot.LootTableSubProvider;

public abstract class SBMeatHookLootTables implements LootTableSubProvider {
    /*
    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> out) {
        for (Carcass carcass : Carcass.values()) {
            for (MeatHookStage stage : MeatHookStage.values()) {
                if(stage.equals(MeatHookStage.HOOK)) continue;

                // main drops
                var mainDrops = carcass.dropsFor(stage);
                var supportDrops = carcass.dropsForSupport(stage);
                var trivialDrops = carcass.dropsForTrivial(stage);

                if(!stage.equals(MeatHookStage.BUTCHER) && mainDrops.isEmpty() && supportDrops.isEmpty() && trivialDrops.isEmpty()){
                    SurvivorsButchercraft.LOGGER.warn("Meat hook loot table for {} at stage {} is completely empty!", carcass, stage);
                    continue;
                }

                var main = LootTable.lootTable();
                if (!mainDrops.isEmpty()) {
                    mainDrops.forEach(dropSpec -> main.withPool(LootPools.poolOnce(dropSpec)));
                } else if (stage.equals(MeatHookStage.BUTCHER)) {
                    main.withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootTableReference.lootTableReference(
                                    Helpers.id("tfc", "entities/" + carcass.serializedName())
                            ))
                    );
                }
                if (!supportDrops.isEmpty()) {
                    main.withPool(MeatHookLootHelper.includeSupport(stage, carcass));
                }
                if (!trivialDrops.isEmpty()) {
                    main.withPool(MeatHookLootHelper.includeTrivial(stage, carcass));
                }

                out.accept(MeatHookLootHelper.mainTable(carcass, stage), main);

                // support & trivial tables：有內容再產
                if (!supportDrops.isEmpty()) {
                    var supportLoot = LootTable.lootTable();
                    supportDrops.forEach(dropSpec -> supportLoot.withPool(LootPools.poolOnce(dropSpec)));
                    out.accept(
                            MeatHookLootHelper.supportTable(carcass, stage),
                            supportLoot
                    );
                }

                if (!trivialDrops.isEmpty()) {
                    var trivialLoot = LootTable.lootTable();
                    trivialDrops.forEach(dropSpec -> trivialLoot.withPool(LootPools.poolOnce(dropSpec)));

                    out.accept(
                            MeatHookLootHelper.trivialTable(carcass, stage),
                            trivialLoot
                    );
                }
            }
        }
    }

     */
}

