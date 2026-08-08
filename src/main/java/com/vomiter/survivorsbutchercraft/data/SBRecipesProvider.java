package com.vomiter.survivorsbutchercraft.data;

import com.lance5057.butchercraft.Butchercraft;
import com.lance5057.butchercraft.ButchercraftFluids;
import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.client.BlacklistedModel;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimatedFloat;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimatedFloatVector3;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimationFloatTransform;
import com.lance5057.butchercraft.data.builders.recipes.loottables.ButcherBlockLootTables;
import com.lance5057.butchercraft.data.builders.recipes.loottables.MeatHookLoottables;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.butchery.carcass.MeatHookStage;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.common.recipe.CompoundChanceResult;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomButcherRecipeBuilder;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SBRecipesProvider extends RecipeProvider {
    public SBRecipesProvider(PackOutput p_248933_, CompletableFuture<HolderLookup.Provider> provider) {
        super(p_248933_, provider);
    }

    BlacklistedModel standardModel(ResourceLocation rl) {
        return new BlacklistedModel(rl, true, (new AnimationFloatTransform()).setLocation((new AnimatedFloatVector3()).setX(new AnimatedFloat(0.0F))));
    }

    BlacklistedModel standardHookToolModel(Item i) {
        return new BlacklistedModel(i, (new AnimationFloatTransform()).setScale((new AnimatedFloatVector3()).setAll(new AnimatedFloat(0.5F))).setRotation((new AnimatedFloatVector3()).setZ(new AnimatedFloat(-45.0F, 45.0F, 0.0F, 0.05F, true, true))).setLocation((new AnimatedFloatVector3()).setX(new AnimatedFloat(8.0F, 0.0F)).setY(new AnimatedFloat(24.0F, 0.0F)).setZ(new AnimatedFloat(12.0F, 0.0F))));
    }

    BlacklistedModel hideModel(ResourceLocation rl) {
        return new BlacklistedModel(rl, true, (new AnimationFloatTransform()).setLocation((new AnimatedFloatVector3()).setY(new AnimatedFloat(12.0F, 0.0F))));
    }

    BlacklistedModel layFlatModel(Item rl) {
        return new BlacklistedModel(rl, (new AnimationFloatTransform()).setLocation(new AnimatedFloatVector3(new AnimatedFloat(8.0F), new AnimatedFloat(1.0F), new AnimatedFloat(8.0F))).setRotation((new AnimatedFloatVector3()).setX(new AnimatedFloat(-90.0F))).setScale(new AnimatedFloatVector3(new AnimatedFloat(1.0F), new AnimatedFloat(1.0F), new AnimatedFloat(1.0F))));
    }

    BlacklistedModel standardButcherBlockModel(ResourceLocation rl) {
        return new BlacklistedModel(rl, true, (new AnimationFloatTransform()).setLocation((new AnimatedFloatVector3()).setX(new AnimatedFloat(0.0F))));
    }

    BlacklistedModel standardButcherBlockToolModel(Item i, float yOffset) {
        return new BlacklistedModel(i, (new AnimationFloatTransform()).setRotation((new AnimatedFloatVector3()).setZ(new AnimatedFloat(-45.0F, 45.0F, 0.0F, 0.05F, true, true))).setLocation((new AnimatedFloatVector3()).setX(new AnimatedFloat(8.0F, 0.0F)).setY(new AnimatedFloat(10.0F + yOffset, 0.0F)).setZ(new AnimatedFloat(8.0F, 0.0F))).setScale((new AnimatedFloatVector3()).setAll(new AnimatedFloat(0.5F))));
    }

    static ResourceLocation meatHookId(String path){
        return Helpers.id("meathook/" + path);
    }

    private void buildHeadRecipes(Carcass carcass, RecipeOutput consumer){
        var head = SBItems.HEADS.get(carcass);
        var skull = SBItems.SKULLS.get(carcass);
        if (carcass.equals(Carcass.GOAT)){
            CuttingBoardRecipeBuilder.cuttingRecipe(
                            Ingredient.of(skull.get()),
                            Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "hammers"))),
                            Items.BONE, 2
                    ).addResultWithChance(Items.BONE_MEAL,  0.25f, 4)
                    .addResult(TFCItems.GOAT_HORN.get(), 2)
                    .save(consumer, Helpers.id("cutting/" + skull.getId().getPath()));

        } else {
            CuttingBoardRecipeBuilder.cuttingRecipe(
                            Ingredient.of(skull.get()),
                            Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "hammers"))),
                            Items.BONE, 2
                    ).addResultWithChance(Items.BONE_MEAL,  0.25f, 4)
                    .save(consumer, Helpers.id("cutting/" + skull.getId().getPath()));
        }
        new CustomButcherRecipeBuilder()
                .carcass(head.get())
                .tool(
                        Ingredient.of(SBTags.Items.GUTTING_TOOLS),
                        16,
                        true,
                        standardModel(ResourceLocation.fromNamespaceAndPath(head.getId().getNamespace(), "meathook/" + head.getId().getPath())),
                        standardButcherBlockToolModel(ButchercraftItems.GUT_KNIFE.get(), 0)
                )
                .resultStage(
                        new CompoundChanceResult(skull.get().getDefaultInstance(), 1),
                        new CompoundChanceResult(ButchercraftItems.BRAIN.get().getDefaultInstance(), 1),
                        new CompoundChanceResult(ButchercraftItems.EYEBALL.get().getDefaultInstance().copyWithCount(2), 1),
                        new CompoundChanceResult(ButchercraftItems.SINEW.get(), 4, 0.5f)
                ).saveButcherBlock(consumer, Helpers.id("butcherblock/" + head.getId().getPath()));

        if (carcass.hasMaleHead()){
            var headMale = SBItems.HEADS_MALE.get(carcass);
            var skullMale = SBItems.SKULLS_MALE.get(carcass);
            CuttingBoardRecipeBuilder.cuttingRecipe(
                            Ingredient.of(skullMale.get()),
                            Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("tfc", "hammers"))),
                            Items.BONE, 2
                    ).addResultWithChance(Items.BONE_MEAL, 0.25f, 4)
                    .save(consumer, Helpers.id("cutting/" + skullMale.getId().getPath()));

            new CustomButcherRecipeBuilder()
                    .carcass(head.get())
                    .tool(
                            Ingredient.of(SBTags.Items.GUTTING_TOOLS),
                            16,
                            true,
                            standardModel(ResourceLocation.fromNamespaceAndPath(headMale.getId().getNamespace(), "meathook/" + head.getId().getPath())),
                            standardButcherBlockToolModel(ButchercraftItems.GUT_KNIFE.get(), 0)
                    )
                    .resultStage(
                            new CompoundChanceResult(skullMale.get().getDefaultInstance(), 1),
                            new CompoundChanceResult(ButchercraftItems.BRAIN.get().getDefaultInstance(), 1),
                            new CompoundChanceResult(ButchercraftItems.EYEBALL.get().getDefaultInstance().copyWithCount(2), 1),
                            new CompoundChanceResult(ButchercraftItems.SINEW.get(), 4, 0.5f)
                    ).saveButcherBlock(consumer, Helpers.id("butcherblock/" + headMale.getId().getPath()));
        }
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput consumer) {

        CustomButcherRecipeBuilder.builder()
                .carcass(ButchercraftItems.BLOOD_SAUSAGE_LINKED.get())
                .tool(
                        Ingredient.of(SBTags.Items.BUTCHERING_TOOLS),
                        8,
                        true,
                        standardButcherBlockToolModel(ButchercraftItems.BUTCHER_KNIFE.get(), 0)
                )
                .resultStage(new CompoundChanceResult(ButchercraftItems.BLOOD_SAUSAGE.get(), 8, 1))
                .saveButcherBlock(consumer, Helpers.id("butcherblock/blood_sausage"));

        CustomButcherRecipeBuilder.builder()
                .carcass(ButchercraftItems.SAUSAGE_LINKED.get())
                .tool(
                        Ingredient.of(SBTags.Items.BUTCHERING_TOOLS),
                        8,
                        true,
                        standardButcherBlockToolModel(ButchercraftItems.BUTCHER_KNIFE.get(), 0)
                )
                .resultStage(new CompoundChanceResult(ButchercraftItems.SAUSAGE.get(), 8, 1))
                .saveButcherBlock(consumer, Helpers.id("butcherblock/sausage"));


        for (Metal metal : Metal.values()) {
            if(!metal.allParts()) continue;
            ShapedRecipeBuilder
                    .shaped(RecipeCategory.MISC, SBItems.MEAT_HOOKS.get(metal).get())
                    .pattern("RR")
                    .pattern("CC")
                    .define('R', TFCItems.METAL_ITEMS.get(metal).get(Metal.ItemType.ROD).get())
                    .define('C', TFCBlocks.METALS.get(metal).get(Metal.BlockType.CHAIN).get())
                    .unlockedBy("have_butcher_knife", InventoryChangeTrigger.TriggerInstance.hasItems(
                            SBItems.BUTCHER_KNIVES.values().stream().map(Supplier::get).toArray(Item[]::new)
                    ))
                    .save(consumer);
        }

        new CustomButcherRecipeBuilder().carcass(ButchercraftItems.TRIPE.get())
                .tool(
                        Ingredient.of(Items.WATER_BUCKET),
                        1,
                        true,
                        this.layFlatModel(ButchercraftItems.TRIPE.get()),
                        standardButcherBlockToolModel(Items.WATER_BUCKET, 0)
                )
                .resultStage(CompoundChanceResult.EMPTY)
                .tool(
                        Ingredient.of(SBTags.Items.SKINNING_TOOLS),
                        4,
                        true,
                        this.layFlatModel(ButchercraftItems.TRIPE.get()),
                        standardButcherBlockToolModel(ButchercraftItems.SKINNING_KNIFE.get(), 0)
                )
                .resultStage(CompoundChanceResult.EMPTY)
                .tool(
                        Ingredient.of(TFCItems.POWDERS.get(Powder.SALT).get()),
                        4,
                        true,
                        this.layFlatModel(ButchercraftItems.TRIPE.get()),
                        standardButcherBlockToolModel(TFCItems.POWDERS.get(Powder.SALT).get(), 0)
                )
                .resultStage(new CompoundChanceResult(ButchercraftItems.CASING.get().getDefaultInstance(), 1))
                .saveButcherBlock(consumer, Helpers.id("butcherblock/casing"));

        for (MeatType meatType : MeatType.values()) {
            CustomButcherRecipeBuilder.builder().carcass(MeatMap.get(meatType, MeatProduct.ORDINARY))
                    .tool(
                            Ingredient.of(SBTags.Items.BUTCHERING_TOOLS),
                            1,
                            true,
                            this.layFlatModel(MeatMap.get(meatType, MeatProduct.ORDINARY)),
                            standardButcherBlockToolModel(ButchercraftItems.BUTCHER_KNIFE.get(), 0)
                    )
                    .resultStage(new CompoundChanceResult(MeatMap.get(meatType, MeatProduct.CUBED), 2, 1))
                    .saveButcherBlock(consumer, Helpers.id("butcherblock/" + meatType.name().toLowerCase(Locale.ROOT) + "/ordinary_to_cubed"));
            CustomButcherRecipeBuilder.builder().carcass(MeatMap.get(meatType, MeatProduct.ROAST))
                    .tool(
                            Ingredient.of(SBTags.Items.BUTCHERING_TOOLS),
                            1,
                            true,
                            this.layFlatModel(MeatMap.get(meatType, MeatProduct.ROAST)),
                            standardButcherBlockToolModel(ButchercraftItems.BUTCHER_KNIFE.get(), 0)
                    )
                    .resultStage(new CompoundChanceResult(MeatMap.get(meatType, MeatProduct.ORDINARY), 2, 1))
                    .saveButcherBlock(consumer, Helpers.id("butcherblock/" + meatType.name().toLowerCase(Locale.ROOT) + "/roast_to_ordinary"));

        }

        for (Carcass carcass : Carcass.values()) {
            buildHeadRecipes(carcass, consumer);

            CustomButcherRecipeBuilder meatHookRecipeBuilder = new CustomButcherRecipeBuilder().carcass(
                    Ingredient.of(carcass.carcassItem())
            );

            for (int i = 0; i < carcass.bloodBucket(); i++) {
                meatHookRecipeBuilder.tool(
                        Ingredient.of(Items.BUCKET),
                        1,
                        true,
                        standardModel(meatHookId(carcass.serializedName() + "/" + MeatHookStage.HOOK.pp)),
                        standardHookToolModel(Items.BUCKET)
                ).resultStage(new CompoundChanceResult(new FluidStack(ButchercraftFluids.BLOOD.FLUID.get(), FluidType.BUCKET_VOLUME)));
            }
            for (MeatHookStage meatHookStage : MeatHookStage.values()) {
                if(meatHookStage == MeatHookStage.HOOK) continue;
                var loot = meatHookStage.equals(MeatHookStage.BUTCHER)?
                        Helpers.id("tfc", "entities/" + carcass.serializedName()):
                        ButcherBlockLootTables.EMPTY.location();

                meatHookRecipeBuilder.tool(
                        carcass.toolFor(meatHookStage),
                        carcass.workCountFor(meatHookStage),
                        true,
                        loot,
                        standardModel(meatHookId(carcass.serializedName() + "/" + meatHookStage.previousStep())),
                                standardHookToolModel(meatHookStage.iconicTool()
                        )
                ).resultStage(
                        Stream.concat(
                                Stream.concat(
                                        carcass.dropsFor(meatHookStage).stream(),
                                        carcass.dropsForSupport(meatHookStage).stream()
                                        ),
                                carcass.dropsForTrivial(meatHookStage).stream()
                        ).toList()
                );
            }

            meatHookRecipeBuilder.saveHook(consumer, meatHookId(carcass.serializedName()));
        }
    }
}
