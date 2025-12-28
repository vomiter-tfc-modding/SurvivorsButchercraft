package com.vomiter.survivorsbutchercraft.data;

import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.client.BlacklistedModel;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimatedFloat;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimatedFloatVector3;
import com.lance5057.butchercraft.client.rendering.animation.floats.AnimationFloatTransform;
import com.lance5057.butchercraft.data.builders.recipes.MeatHookRecipeBuilder;
import com.lance5057.butchercraft.data.builders.recipes.loottables.MeatHookLoottables;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.core.Carcass;
import com.vomiter.survivorsbutchercraft.core.registry.SBItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

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

    ResourceLocation meatHookId(String path){
        return new ResourceLocation(SurvivorsButchercraft.MODID, "meathook/" + path);
    }


    ResourceLocation id(String path){
        return new ResourceLocation(SurvivorsButchercraft.MODID, path);
    }

    ResourceLocation id(String namespace, String path){
        return new ResourceLocation(namespace, path);
    }


    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        MeatHookRecipeBuilder.shapedRecipe(SBItems.CARCASSES.get(Carcass.YAK).get())
                .tool(Ingredient.of(Items.BUCKET), 1, true, MeatHookLoottables.BLOOD_BUCKET,
                        standardModel(id("")),
                        standardHookToolModel(Items.BUCKET))

                .tool(Ingredient.of(Items.BUCKET), 1, true, MeatHookLoottables.BLOOD_BUCKET,
                        standardModel(meatHookId("yak")),
                        standardHookToolModel(Items.BUCKET))
                .tool(Ingredient.of(Items.BUCKET), 1, true, MeatHookLoottables.BLOOD_BUCKET,
                        standardModel(meatHookId("yak")),
                        standardHookToolModel(Items.BUCKET))
                .tool(Ingredient.of(ButchercraftItems.SKINNING_KNIFE.get()), 12, true, MeatHookLoottables.SKIN_COW,
                        standardModel(meatHookId("yak")),
                        standardHookToolModel(ButchercraftItems.SKINNING_KNIFE.get()))
                .tool(Ingredient.of(ButchercraftItems.BONE_SAW.get()), 12, true, MeatHookLoottables.BISECT_COW,
                        standardModel(meatHookId("yak_skinned")),
                        standardHookToolModel(ButchercraftItems.BONE_SAW.get()))
                .tool(Ingredient.of(ButchercraftItems.GUT_KNIFE.get()), 12, true, MeatHookLoottables.DISEMBOWEL_COW,
                        standardModel(meatHookId("yak_gutted")),
                        standardHookToolModel(ButchercraftItems.GUT_KNIFE.get()))
                .tool(Ingredient.of(ButchercraftItems.BUTCHER_KNIFE.get()), 12, true, MeatHookLoottables.BUTCHER_COW,
                        standardModel(meatHookId("yak_bisected")),
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
    }
}
