package com.vomiter.survivorsbutchercraft.data;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.core.Carcass;
import com.vomiter.survivorsbutchercraft.core.registry.SBBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SBBlockStatesProvider extends BlockStateProvider {
    public SBBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SurvivorsButchercraft.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Carcass carcass : Carcass.values()) {
            horizontalBlock(
                    SBBlocks.HIDE_CARPETS.get(carcass).get(),
                    models().getExistingFile(modLoc("block/yak_hide_carpet"))
            );
        }
    }
}
