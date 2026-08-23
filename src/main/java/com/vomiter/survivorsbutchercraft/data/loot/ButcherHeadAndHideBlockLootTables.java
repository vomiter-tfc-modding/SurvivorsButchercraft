package com.vomiter.survivorsbutchercraft.data.loot;

import com.lance5057.butchercraft.workstations.hook.MeatHookBlock;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.common.registry.SBBlocks;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ButcherHeadAndHideBlockLootTables extends BlockLootSubProvider {

    public ButcherHeadAndHideBlockLootTables(HolderLookup.Provider registries) {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        SBBlocks.MEAT_HOOKS.values().stream().forEach(hook -> {
            add(
                    hook.get(),
                    LootTable.lootTable().withPool(
                            applyExplosionCondition(hook.get(), LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(hook.get()))
                                    .when(new LootItemBlockStatePropertyCondition.Builder(hook.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(MeatHookBlock.DUMMY, 0)))
                            )
                    )
            );
        });

        for (Carcass carcass : Carcass.values()) {
            DeferredHolder<Block, ? extends Block> hide = SBBlocks.HIDE_CARPETS.get(carcass);
            if(hide != null){
                dropSelf(hide.get());
            }

            DeferredHolder<Block, ? extends Block> head = SBBlocks.HEADS.get(carcass);
            DeferredHolder<Block, ? extends Block> wallHead = SBBlocks.WALL_HEADS.get(carcass);
            DeferredHolder<Block, ? extends Block> skull = SBBlocks.SKULLS.get(carcass);
            DeferredHolder<Block, ? extends Block> wallSkull = SBBlocks.WALL_SKULLS.get(carcass);
            dropSelf(head.get());
            dropOther(wallHead.get(), head.get());
            dropSelf(skull.get());
            dropOther(wallSkull.get(), skull.get());


            if (carcass.hasMaleHead()) {
                DeferredHolder<Block, ? extends Block> maleHead = SBBlocks.HEADS_MALE.get(carcass);
                DeferredHolder<Block, ? extends Block> maleWallHead = SBBlocks.WALL_HEADS_MALE.get(carcass);
                DeferredHolder<Block, ? extends Block> maleSkull = SBBlocks.SKULLS_MALE.get(carcass);
                DeferredHolder<Block, ? extends Block> maleWallSkull = SBBlocks.WALL_SKULLS_MALE.get(carcass);
                dropSelf(maleHead.get());
                dropOther(maleWallHead.get(), maleHead.get());
                dropSelf(maleSkull.get());
                dropOther(maleWallSkull.get(), maleSkull.get());
            }
        }
    }

    private void dropOther(Block block, Block droppedBlock) {
        add(block, createSingleItemTable(droppedBlock));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        Set<Block> blocks = new HashSet<>();

        SBBlocks.MEAT_HOOKS.values().stream()
                        .map(DeferredHolder::get)
                                .forEach(blocks::add);

        SBBlocks.HIDE_CARPETS.values().stream()
                .map(DeferredHolder::get)
                .forEach(blocks::add);

        SBBlocks.HEADS.values().stream()
                .map(DeferredHolder::get)
                .forEach(blocks::add);

        SBBlocks.WALL_HEADS.values().stream()
                .map(DeferredHolder::get)
                .forEach(blocks::add);

        SBBlocks.HEADS_MALE.values().stream()
                .map(DeferredHolder::get)
                .forEach(blocks::add);

        SBBlocks.WALL_HEADS_MALE.values().stream()
                .map(DeferredHolder::get)
                .forEach(blocks::add);

        SBBlocks.SKULLS.values().stream()
                .map(DeferredHolder::get)
                .forEach(blocks::add);

        SBBlocks.WALL_SKULLS.values().stream()
                .map(DeferredHolder::get)
                .forEach(blocks::add);

        SBBlocks.SKULLS_MALE.values().stream()
                .map(DeferredHolder::get)
                .forEach(blocks::add);

        SBBlocks.WALL_SKULLS_MALE.values().stream()
                .map(DeferredHolder::get)
                .forEach(blocks::add);


        return blocks;
    }
}