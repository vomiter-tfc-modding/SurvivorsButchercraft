package com.vomiter.survivorsbutchercraft.data.tags;

import com.lance5057.butchercraft.ButchercraftItems;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
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
