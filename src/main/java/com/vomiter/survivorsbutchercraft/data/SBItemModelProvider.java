package com.vomiter.survivorsbutchercraft.data;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import net.dries007.tfc.util.Metal;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;

public class SBItemModelProvider extends ItemModelProvider {
    public SBItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SurvivorsButchercraft.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Metal.Default metal : Metal.Default.values()) {
            if (!metal.hasTools()) continue;
            List.of(
                    "butcher_knife",
                    "skinning_knife",
                    "bonesaw",
                    "gut_knife"
            ).forEach(s -> {
                generatedItem("metal/" + s + "_head/" + metal.getSerializedName());
                if(metal.equals(Metal.Default.WROUGHT_IRON)) return;
                handheldItem("metal/" + s + "/" + metal.getSerializedName());
            });

        }
    }

    private void generatedItem(String path) {
        trackTexture(path);
        singleTexture(
                "item/" + path,
                mcLoc("item/generated"),
                "layer0",
                modLoc("item/" + path)
        );
    }

    private void handheldItem(String path) {
        trackTexture(path);
        singleTexture(
                "item/" + path,
                mcLoc("item/handheld"),
                "layer0",
                modLoc("item/" + path)
        );
    }

    private void trackTexture(String pathNoExt) {
        existingFileHelper.trackGenerated(
                modLoc("item/" + pathNoExt),
                PackType.CLIENT_RESOURCES,
                ".png",
                "textures"
        );
    }

}