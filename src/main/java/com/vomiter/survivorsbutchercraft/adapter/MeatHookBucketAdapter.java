package com.vomiter.survivorsbutchercraft.adapter;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.vomiter.survivorsbutchercraft.butchery.tool_alternative.IButcherBlock;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomMeatHookRecipe;
import com.vomiter.survivorsbutchercraft.mixin.MeatHookBlockEntityAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Optional;

public class MeatHookBucketAdapter extends AbstractButcherBucketAdapter<CustomMeatHookRecipe>{

    private final MeatHookBlockEntity meatHook;

    public MeatHookBucketAdapter(MeatHookBlockEntity meatHook) {
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
        if(meatHook instanceof MeatHookBlockEntityAccessor acc){
            acc.sbtfc$dropLoot(tool, player);
        }
    }

    void setupStage(CustomMeatHookRecipe recipe, int stage){
        if(meatHook instanceof MeatHookBlockEntityAccessor acc){
            acc.sbtfc$setupStage(recipe.getHookRecipe(), stage);
        }
    }

    boolean isFinalStage(CustomMeatHookRecipe recipe){
        if(meatHook instanceof MeatHookBlockEntityAccessor acc){
            return acc.sbtfc$isFinalStage(recipe.getHookRecipe());
        }
        return false;
    }

    Optional<CustomMeatHookRecipe> matchRecipe(){
        if(meatHook instanceof IButcherBlock<?> acc){
            return (Optional<CustomMeatHookRecipe>) acc.sbtfcInterface$getRecipe().map(RecipeHolder::value);
        }
        return Optional.empty();
    }

    @Override
    void progressRecipe(CustomMeatHookRecipe recipe, Player player, boolean shouldDropOriginalLoot) {
        if (this.isFinalStage(recipe)) {
            if(shouldDropOriginalLoot) dropLoot(getTool(recipe, getStage()), player);
            finishRecipe();
        } else {
            if(shouldDropOriginalLoot) dropLoot(getTool(recipe, getStage()), player);
            setStage(getStage() + 1);
        }
    }

    @Override
    AnimatedRecipeItemUse getTool(CustomMeatHookRecipe recipe, int stage) {
        return recipe.getHookRecipe().tools().get(stage);
    }
}
