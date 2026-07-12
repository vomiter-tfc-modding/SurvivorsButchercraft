package com.vomiter.survivorsbutchercraft.adapter;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockBlockEntity;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.vomiter.survivorsbutchercraft.mixin.ButcherBlockEntityAccessor;
import com.vomiter.survivorsbutchercraft.mixin.MeatHookBlockEntityAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;

public class ButcherBlockBucketAdapter extends AbstractButcherBucketAdapter<ButcherBlockRecipe>{

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

    void setupStage(ButcherBlockRecipe recipe, int stage){
        if(meatHook instanceof ButcherBlockEntityAccessor acc){
            acc.sbtfc$setupStage(recipe, stage);
        }
    }

    boolean isFinalStage(ButcherBlockRecipe recipe){
        if(meatHook instanceof ButcherBlockEntityAccessor acc){
            return acc.sbtfc$isFinalStage(recipe);
        }
        return false;
    }

    Optional<ButcherBlockRecipe> matchRecipe(){
        if(meatHook instanceof ButcherBlockEntityAccessor acc){
            return acc.sbtfc$matchRecipe();
        }
        return Optional.empty();
    }

    @Override
    AnimatedRecipeItemUse getTool(ButcherBlockRecipe recipe, int stage) {
        return recipe.getRecipeToolsIn().get(stage);
    }
}
