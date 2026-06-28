package com.vomiter.survivorsbutchercraft.data.loot;

import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.common.registry.SBBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ButcherHeadAndHideBlockLootTables extends BlockLootSubProvider {
    public ButcherHeadAndHideBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        for (Carcass carcass : Carcass.values()) {
            RegistryObject<Block> hide = SBBlocks.HIDE_CARPETS.get(carcass);
            if(hide != null){
                dropSelf(hide.get());
            }

            RegistryObject<Block> head = SBBlocks.HEADS.get(carcass);
            RegistryObject<Block> wallHead = SBBlocks.WALL_HEADS.get(carcass);

            if (head != null) {
                dropSelf(head.get());
            }

            if (wallHead != null && head != null) {
                dropOther(wallHead.get(), head.get());
            }

            if (carcass.hasMaleHead()) {
                RegistryObject<Block> maleHead = SBBlocks.HEADS_MALE.get(carcass);
                RegistryObject<Block> maleWallHead = SBBlocks.WALL_HEADS_MALE.get(carcass);

                if (maleHead != null) {
                    dropSelf(maleHead.get());
                }

                if (maleWallHead != null && maleHead != null) {
                    dropOther(maleWallHead.get(), maleHead.get());
                }
            }
        }
    }

    private void dropOther(Block block, Block droppedBlock) {
        add(block, createSingleItemTable(droppedBlock));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        Set<Block> blocks = new HashSet<>();

        SBBlocks.HIDE_CARPETS.values().stream()
                .map(RegistryObject::get)
                .forEach(blocks::add);

        SBBlocks.HEADS.values().stream()
                .map(RegistryObject::get)
                .forEach(blocks::add);

        SBBlocks.WALL_HEADS.values().stream()
                .map(RegistryObject::get)
                .forEach(blocks::add);

        SBBlocks.HEADS_MALE.values().stream()
                .map(RegistryObject::get)
                .forEach(blocks::add);

        SBBlocks.WALL_HEADS_MALE.values().stream()
                .map(RegistryObject::get)
                .forEach(blocks::add);

        return blocks;
    }
}