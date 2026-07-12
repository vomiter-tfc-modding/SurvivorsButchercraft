package com.vomiter.survivorsbutchercraft.data.tags;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SBTagProviders {
    DataGenerator generator;
    PackOutput output;
    CompletableFuture<HolderLookup.Provider> lookupProvider;
    ExistingFileHelper helper;
    public SBTagProviders(GatherDataEvent event){
        generator = event.getGenerator();
        output = generator.getPackOutput();
        lookupProvider = event.getLookupProvider();
        helper = event.getExistingFileHelper();

        var blockTags = new BlockTags();
        var itemTags = new ItemTags(blockTags);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), itemTags);

    }


    class BlockTags extends BlockTagsProvider{

        public BlockTags() {
            super(output, lookupProvider, SurvivorsButchercraft.MODID, helper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {

        }
    }

    class ItemTags extends ItemTagsProvider {
        public ItemTags(BlockTags blockTags) {
            super(output, lookupProvider, blockTags.contentsGetter(), SurvivorsButchercraft.MODID, helper);
        }
        static TagKey<Item> create(ResourceLocation id){
            return TagKey.create(
                    Registries.ITEM,
                    id
            );
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void addTags(HolderLookup.@NotNull Provider p_256380_) {

            SBItems.HIDES.values().forEach(h ->
                    tag(SBTags.Items.BUTCHER_SCRAPABLE).add(h.get())
            );

            MeatMap.entries.values().forEach(m -> m.values().forEach(
                    item -> {
                        tag(SBTags.Items.BUTCHER_RAW_MEATS).add(item);
                    }
            ));
            tag(SBTags.Items.BUTCHER_RAW_MEATS).add(
                    ButchercraftItems.LUNG.get(),
                    ButchercraftItems.HEART.get(),
                    ButchercraftItems.STOMACH.get(),
                    ButchercraftItems.TRIPE.get(),
                    ButchercraftItems.KIDNEY.get(),
                    ButchercraftItems.BLOOD_SAUSAGE.get(),
                    ButchercraftItems.SAUSAGE.get()
            );

            tag(SBTags.Items.BUTCHERY_SKIP_LOOT)
                    .addOptionalTag(SBTags.Items.createTFC("raw_hides"))
                    .addOptionalTag(SBTags.Items.createTFC("sheepskin_hides"));

            tag(SBTags.Items.BEHEADING_TOOLS)
                    .add(ButchercraftItems.BONE_SAW.get())
                    .addOptionalTags(
                            SBTags.Items.createTFC("saws"),
                            SBTags.Items.createTFC("axes")
                    );
            tag(SBTags.Items.GUTTING_TOOLS)
                    .add(ButchercraftItems.GUT_KNIFE.get())
                    .addOptionalTags(SBTags.Items.createTFC("knives"));
            tag(SBTags.Items.SKINNING_TOOLS)
                    .add(ButchercraftItems.SKINNING_KNIFE.get())
                    .addOptionalTags(
                            SBTags.Items.createTFC("knives"),
                            SBTags.Items.createTFC("shears")
                    );
            tag(SBTags.Items.BUTCHERING_TOOLS)
                    .add(ButchercraftItems.BUTCHER_KNIFE.get())
                    .addOptionalTags(
                            SBTags.Items.createTFC("knives"),
                            SBTags.Items.createTFC("axes")
                    );
        }
    }


}
