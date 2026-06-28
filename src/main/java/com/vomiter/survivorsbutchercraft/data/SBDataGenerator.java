package com.vomiter.survivorsbutchercraft.data;

import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.data.bbmodel.BbmodelExportProvider;
import com.vomiter.survivorsbutchercraft.data.tags.SBTagProviders;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class SBDataGenerator {
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(true, new SBRecipesProvider(output));
        generator.addProvider(true, new SBBlockStatesProvider(output, existingFileHelper));
        generator.addProvider(true, new SBLootTableProvider(output));
        new SBTagProviders(event);
        var foodProvider = new SDFoodDataProvider(output, SurvivorsButchercraft.MODID);
        SBFoodData.saveFoodData(foodProvider);
        generator.addProvider(true, foodProvider);
        generator.addProvider(true, new BbmodelExportProvider(output));
        generator.addProvider(true, new SBLangProvider(output));
    }
}
