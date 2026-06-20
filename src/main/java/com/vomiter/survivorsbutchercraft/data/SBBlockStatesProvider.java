package com.vomiter.survivorsbutchercraft.data;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.common.block.SkullLikeBlock;
import com.vomiter.survivorsbutchercraft.common.block.WallSkullLikeBlock;
import com.vomiter.survivorsbutchercraft.common.registry.SBBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

public class SBBlockStatesProvider extends BlockStateProvider {
    public SBBlockStatesProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, SurvivorsButchercraft.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Carcass carcass : Carcass.values()) {
            horizontalBlock(
                    SBBlocks.HIDE_CARPETS.get(carcass).get(),
                    models().getExistingFile(modLoc("block/hide_carpet/" + carcass.serializedName()))
            );

            skullRotationStates(
                    SBBlocks.HEADS.get(carcass).get(),
                    modLoc("block/head/" + carcass.serializedName())           // model: models/block/skull/yak.json
            );

            // === skull-like: 牆上頭 yak_wall_head ===
            wallSkullFacingStates(
                    SBBlocks.WALL_HEADS.get(carcass).get(),
                    modLoc("block/head/" + carcass.serializedName())      // model: models/block/skull/yak_wall_head.json
            );

        }
    }

    private void skullRotationStates(Block block, ResourceLocation model) {
        VariantBlockStateBuilder b = getVariantBuilder(block);
        var mf = models().getExistingFile(model);

        for (int rot = 0; rot < 16; rot++) {
            b.partialState().with(SkullLikeBlock.ROTATION, rot)
                    .modelForState()
                    .modelFile(mf)
                    .addModel();
        }
    }

    private void wallSkullFacingStates(Block block, ResourceLocation model) {
        VariantBlockStateBuilder b = getVariantBuilder(block);
        var mf = models().getExistingFile(model);

        for (var dir : Arrays.asList(
                net.minecraft.core.Direction.NORTH,
                net.minecraft.core.Direction.EAST,
                net.minecraft.core.Direction.SOUTH,
                net.minecraft.core.Direction.WEST
        )) {
            int y = ((int) dir.toYRot()) % 360; // SOUTH=0, WEST=90, NORTH=180, EAST=270

            b.partialState().with(WallSkullLikeBlock.FACING, dir)
                    .modelForState()
                    .modelFile(mf)
                    .rotationY((y + 180) % 360)
                    .addModel();
        }
    }

}
