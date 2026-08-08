package com.vomiter.survivorsbutchercraft.adapter;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockBlockEntity;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.IButcherBlock;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomButcherBlockRecipe;
import com.vomiter.survivorsbutchercraft.common.recipe.IButcherRecipe;
import com.vomiter.survivorsbutchercraft.mixin.ButcherBlockEntityAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Optional;

public class ButcherBlockBucketAdapter extends AbstractButcherBucketAdapter<CustomButcherBlockRecipe>{

    private final ButcherBlockBlockEntity meatHook;

    public ButcherBlockBucketAdapter(ButcherBlockBlockEntity meatHook) {
        super(meatHook);
        this.meatHook = meatHook;
    }

    @Override
    int getStage() {
        return meatHook.stage;
    }

    @Override
    void setStage(int i) {
        meatHook.stage = i;
    }

    @Override
    void finishRecipe() {
        meatHook.finishRecipe();
    }

    @Override
    void updateInventory() {
        meatHook.updateInventory();
    }

    @Override
    void dropLoot(AnimatedRecipeItemUse tool, Player player) {
        if (meatHook instanceof ButcherBlockEntityAccessor acc){
            acc.sbtfc$dropLoot(tool, player);
        }
    }

    void setupStage(CustomButcherBlockRecipe recipe, int stage){
        if(meatHook instanceof ButcherBlockEntityAccessor acc){
            acc.sbtfc$setupStage(recipe.getButcherBlockRecipe(), stage);
        }
    }

    boolean isFinalStage(CustomButcherBlockRecipe recipe){
        if(meatHook instanceof ButcherBlockEntityAccessor acc){
            return acc.sbtfc$isFinalStage(recipe.getButcherBlockRecipe());
        }
        return false;
    }

    Optional<CustomButcherBlockRecipe> matchRecipe(){
        if(meatHook instanceof IButcherBlock<?> acc){
            return (Optional<CustomButcherBlockRecipe>) acc.sbtfcInterface$getRecipe().map(RecipeHolder::value);
        }
        return Optional.empty();
    }

    @Override
    void progressRecipe(CustomButcherBlockRecipe recipe, Player player, boolean shouldDropOriginalLoot) {
        if (this.isFinalStage(recipe)) {
            if(shouldDropOriginalLoot) dropLoot(getTool(recipe, getStage()), player);
            finishRecipe();
        } else {
            if(shouldDropOriginalLoot) dropLoot(getTool(recipe, getStage()), player);
            setStage(getStage() + 1);
        }
    }

    @Override
    AnimatedRecipeItemUse getTool(CustomButcherBlockRecipe recipe, int stage) {
        return recipe.getButcherBlockRecipe().tools().get(stage);
    }
}
