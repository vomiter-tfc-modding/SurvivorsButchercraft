package com.vomiter.survivorsbutchercraft.data;

import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.client.BlacklistedModel;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimatedFloat;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimatedFloatVector3;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimationFloatTransform;
import com.lance5057.butchercraft.data.builders.recipes.ButcherBlockRecipeBuilder;
import com.vomiter.survivorsbutchercraft.Helpers;
import com.vomiter.survivorsbutchercraft.butchery.carcass.Carcass;
import com.vomiter.survivorsbutchercraft.butchery.carcass.DefaultMammalCarcassProfile;
import com.vomiter.survivorsbutchercraft.butchery.carcass.MeatHookStage;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatMap;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatProduct;
import com.vomiter.survivorsbutchercraft.butchery.meat.MeatType;
import com.vomiter.survivorsbutchercraft.common.registry.SBItems;
import com.vomiter.survivorsbutchercraft.data.loot.DropSpec;
import com.vomiter.survivorsbutchercraft.data.loot.MeatHookLootHelper;
import com.vomiter.survivorsbutchercraft.data.loot.SBButcherBlockLootTables;
import com.vomiter.survivorsbutchercraft.data.recipe_builder.MeatHookRecipeBuilderAlt;
import com.vomiter.survivorsbutchercraft.data.tags.SBTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SBRecipesProvider extends RecipeProvider {
    public SBRecipesProvider(PackOutput p_248933_) {
        super(p_248933_);
    }

    BlacklistedModel standardModel(ResourceLocation rl) {
        return new BlacklistedModel(rl, null, true,
                new AnimationFloatTransform().setLocation(new AnimatedFloatVector3().setX(new AnimatedFloat(0))));
    }

    BlacklistedModel standardHookToolModel(Item i) {
        return new BlacklistedModel(i,
                new AnimationFloatTransform().setScale(new AnimatedFloatVector3().setAll(new AnimatedFloat(0.5f)))
                        .setRotation(new AnimatedFloatVector3().setZ(new AnimatedFloat(-45, 45, 0, 0.05f, true, true)))
                        .setLocation(new AnimatedFloatVector3().setX(new AnimatedFloat(8, 0))
                                .setY(new AnimatedFloat(24, 0)).setZ(new AnimatedFloat(12, 0))));
    }

    BlacklistedModel hideModel(ResourceLocation rl) {
        return new BlacklistedModel(rl, null, true,
                new AnimationFloatTransform().setLocation(new AnimatedFloatVector3().setY(new AnimatedFloat(12, 0))));
    }

    BlacklistedModel layFlatModel(Item rl) {
        return new BlacklistedModel(rl, new AnimationFloatTransform()
                .setLocation(
                        new AnimatedFloatVector3(new AnimatedFloat(8), new AnimatedFloat(1f), new AnimatedFloat(8)))
                .setRotation(new AnimatedFloatVector3().setX(new AnimatedFloat(-90)))
                .setScale(new AnimatedFloatVector3(new AnimatedFloat(1), new AnimatedFloat(1), new AnimatedFloat(1))));
    }

    BlacklistedModel standardButcherBlockModel(ResourceLocation rl) {
        return new BlacklistedModel(rl, null, true,
                new AnimationFloatTransform().setLocation(new AnimatedFloatVector3().setX(new AnimatedFloat(0))));
    }

    BlacklistedModel standardButcherBlockToolModel(Item i) {
        return new BlacklistedModel(i,
                new AnimationFloatTransform()
                        .setRotation(new AnimatedFloatVector3().setZ(new AnimatedFloat(-45, 45, 0, 0.05f, true, true)))
                        .setLocation(new AnimatedFloatVector3().setX(new AnimatedFloat(8, 0))
                                .setY(new AnimatedFloat(8, 0)).setZ(new AnimatedFloat(8, 0)))
                        .setScale(new AnimatedFloatVector3().setAll(new AnimatedFloat(0.5f))));
    }

    static ResourceLocation meatHookId(String path){
        return Helpers.id("meathook/" + path);
    }


    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        for (Metal.Default metal : Metal.Default.values()) {
            if(!metal.hasTools()) continue;
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

        Stream.concat(SBItems.HEADS.values().stream(), SBItems.HEADS_MALE.values().stream())
                .forEach(head -> {
                    if(head.equals(SBItems.HEADS.get(Carcass.GOAT))){
                        ButcherBlockRecipeBuilder.shapedRecipe(head.get())
                                .tool(
                                        Ingredient.of(SBTags.Items.GUTTING_TOOLS),
                                        16,
                                        true,
                                        SBButcherBlockLootTables.GOAT_HEAD,
                                        layFlatModel(head.get()),
                                        standardButcherBlockToolModel(ButchercraftItems.GUT_KNIFE.get())
                                )
                                .JEIIngredient(Ingredient.of(ButchercraftItems.BRAIN.get()))
                                .JEIIngredient(Ingredient.of(ButchercraftItems.EYEBALL.get()))
                                .JEIIngredient(Ingredient.of(Items.BONE))
                                .JEIIngredient(Ingredient.of(TFCItems.GOAT_HORN.get()))
                                .save(consumer, Helpers.id("butcherblock/" + head.getId().getPath()));
                        return;
                    }
            ButcherBlockRecipeBuilder.shapedRecipe(head.get())
                    .tool(
                            Ingredient.of(SBTags.Items.GUTTING_TOOLS),
                            16,
                            true,
                            SBButcherBlockLootTables.BRAIN,
                            layFlatModel(head.get()),
                            standardButcherBlockToolModel(ButchercraftItems.GUT_KNIFE.get())
                    )
                    .JEIIngredient(Ingredient.of(ButchercraftItems.BRAIN.get()))
                    .JEIIngredient(Ingredient.of(ButchercraftItems.EYEBALL.get()))
                    .JEIIngredient(Ingredient.of(Items.BONE))
                    .save(consumer, Helpers.id("butcherblock/" + head.getId().getPath()));
        });

        ButcherBlockRecipeBuilder.shapedRecipe(ButchercraftItems.TRIPE.get())
                .tool(
                        Ingredient.of(Items.WATER_BUCKET),
                        1,
                        true,
                        SBButcherBlockLootTables.EMPTY,
                        this.layFlatModel(ButchercraftItems.TRIPE.get()),
                        standardButcherBlockToolModel(Items.WATER_BUCKET)
                )
                .tool(
                        Ingredient.of(SBTags.Items.SKINNING_TOOLS),
                        4,
                        true,
                        SBButcherBlockLootTables.EMPTY,
                        this.layFlatModel(ButchercraftItems.TRIPE.get()),
                        standardButcherBlockToolModel(ButchercraftItems.SKINNING_KNIFE.get())
                )
                .tool(
                        Ingredient.of(TFCItems.POWDERS.get(Powder.SALT).get()),
                        4,
                        true,
                        SBButcherBlockLootTables.CASING,
                        this.layFlatModel(ButchercraftItems.TRIPE.get()),
                        standardButcherBlockToolModel(TFCItems.POWDERS.get(Powder.SALT).get())
                )
                .JEIIngredient(Ingredient.of(ButchercraftItems.CASING.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.FAT.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.SINEW.get()))
                .save(consumer, Helpers.id("butcherblock/casing"));

        for (MeatType meatType : MeatType.values()) {
            ButcherBlockRecipeBuilder.shapedRecipe(MeatMap.get(meatType, MeatProduct.ORDINARY))
                    .tool(
                            Ingredient.of(SBTags.Items.BUTCHERING_TOOLS),
                            1,
                            true,
                            SBButcherBlockLootTables.ORDINARY2CUBED.get(meatType),
                            this.layFlatModel(MeatMap.get(meatType, MeatProduct.ORDINARY)),
                            standardButcherBlockToolModel(ButchercraftItems.BUTCHER_KNIFE.get())
                    )
                    .save(consumer, Helpers.id("butcherblock/" + meatType.name().toLowerCase(Locale.ROOT) + "/ordinary_to_cubed"));
            ButcherBlockRecipeBuilder.shapedRecipe(MeatMap.get(meatType, MeatProduct.ROAST))
                    .tool(
                            Ingredient.of(SBTags.Items.BUTCHERING_TOOLS),
                            1,
                            true,
                            SBButcherBlockLootTables.ROAST2ORDINARY.get(meatType),
                            this.layFlatModel(MeatMap.get(meatType, MeatProduct.ROAST)),
                            standardButcherBlockToolModel(ButchercraftItems.BUTCHER_KNIFE.get())
                    )
                    .save(consumer, Helpers.id("butcherblock/" + meatType.name().toLowerCase(Locale.ROOT) + "/roast_to_ordinary"));

        }

        for (Carcass carcass : Carcass.values()) {
            List<DropSpec> dropSpecs = new ArrayList<>();

            MeatHookRecipeBuilderAlt meatHookRecipeBuilder = MeatHookRecipeBuilderAlt.shapedRecipe(
                    Ingredient.of(carcass.carcassItem())
                    //TFCIngredientHelper.getNotRotten(Ingredient.of(carcass.carcassItem()))
            );
            for (int i = 0; i < carcass.bloodBucket(); i++) {
                meatHookRecipeBuilder.tool(
                        Ingredient.of(Items.BUCKET),
                        1,
                        true,
                        SBButcherBlockLootTables.EMPTY,
                        standardModel(meatHookId(carcass.serializedName() + "/" + MeatHookStage.HOOK.pp)),
                        standardHookToolModel(Items.BUCKET)
                        );
            }
            for (MeatHookStage meatHookStage : MeatHookStage.values()) {
                if(meatHookStage == MeatHookStage.HOOK) continue;
                meatHookRecipeBuilder.tool(
                        carcass.toolFor(meatHookStage),
                        carcass.workCountFor(meatHookStage),
                        true,
                        MeatHookLootHelper.mainTable(carcass, meatHookStage),
                        standardModel(meatHookId(carcass.serializedName() + "/" + meatHookStage.previousStep())),
                        standardHookToolModel(meatHookStage.iconicTool())
                );
                var mainDrops = carcass.dropsFor(meatHookStage);
                var supportDrops = carcass.dropsForSupport(meatHookStage);
                var trivialDrops = carcass.dropsForTrivial(meatHookStage);
                dropSpecs.addAll(mainDrops);
                dropSpecs.addAll(supportDrops);
                dropSpecs.addAll(trivialDrops);
            }

            var resultSet = dropSpecs.stream().map(DropSpec::item).collect(Collectors.toSet());
            if (carcass.getProfile() instanceof DefaultMammalCarcassProfile defaultMammalCarcassProfile){
                resultSet.add(MeatMap.get(defaultMammalCarcassProfile.getMeatType(), MeatProduct.ORDINARY));
            }
            if(carcass.bloodBucket() > 0) resultSet.add(ButchercraftItems.BLOOD_FLUID_BUCKET.get());
            resultSet.add(Items.BONE);
            if (carcass.hasMaleHead()) resultSet.add(SBItems.HEADS_MALE.get(carcass).get());
            resultSet.forEach(item -> {
                meatHookRecipeBuilder.JEIIngredient(Ingredient.of(item));
            });

            meatHookRecipeBuilder.save(consumer, meatHookId(carcass.serializedName()));
        }

        /*
        MeatHookRecipeBuilderAltAlt.shapedRecipe(SBItems.CARCASSES.get(Carcass.YAK).get())
                .tool(Ingredient.of(Items.BUCKET), 1, true, MeatHookLoottables.BLOOD_BUCKET,
                        standardModel(meatHookId("yak/hooked")),
                        standardHookToolModel(Items.BUCKET))
                .tool(Ingredient.of(Items.BUCKET), 1, true, MeatHookLoottables.BLOOD_BUCKET,
                        standardModel(meatHookId("yak/hooked")),
                        standardHookToolModel(Items.BUCKET))
                .tool(Ingredient.of(Items.BUCKET), 1, true, MeatHookLoottables.BLOOD_BUCKET,
                        standardModel(meatHookId("yak/hooked")),
                        standardHookToolModel(Items.BUCKET))
                .tool(Ingredient.of(ButchercraftItems.SKINNING_KNIFE.get()), 12, true, MeatHookLoottables.SKIN_COW,
                        standardModel(meatHookId("yak/hooked")),
                        standardHookToolModel(ButchercraftItems.SKINNING_KNIFE.get()))
                .tool(Ingredient.of(ButchercraftItems.BONE_SAW.get()), 12, true, MeatHookLoottables.BISECT_COW,
                        standardModel(meatHookId("yak/skinned")),
                        standardHookToolModel(ButchercraftItems.BONE_SAW.get()))
                .tool(Ingredient.of(ButchercraftItems.GUT_KNIFE.get()), 12, true, MeatHookLoottables.DISEMBOWEL_COW,
                        standardModel(meatHookId("yak/gutted")),
                        standardHookToolModel(ButchercraftItems.GUT_KNIFE.get()))
                .tool(Ingredient.of(ButchercraftItems.BUTCHER_KNIFE.get()), 12, true, MeatHookLoottables.BUTCHER_COW,
                        standardModel(meatHookId("yak/bisected")),
                        standardHookToolModel(ButchercraftItems.BUTCHER_KNIFE.get()))

                .JEIIngredient(Ingredient.of(ButchercraftItems.BLOOD_FLUID_BUCKET.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.HEART.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.KIDNEY.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.LIVER.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.STOMACH.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.LUNG.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.TRIPE.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.COW_HEAD_ITEM.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.BEEF_SCRAPS.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.BEEF_STEW_MEAT.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.OXTAIL.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.COW_HIDE.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.LEATHER_SCRAP.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.FAT.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.SINEW.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.BEEF_RIBS.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.BEEF_ROAST.get()))
                .JEIIngredient(Ingredient.of(ButchercraftItems.CUBED_BEEF.get()))
                .JEIIngredient(Ingredient.of(Items.BEEF)).JEIIngredient(Ingredient.of(Items.BONE))
                .save(consumer, meatHookId("yak"));

         */
    }
}
