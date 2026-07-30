package com.vomiter.survivorsbutchercraft.common.recipe;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;

public interface IButcherRecipe {
    NonNullList<CompoundChanceResult> getResults(int stage);
    Ingredient getButcheringTool(int stage);
    AnimatedRecipeItemUse getButcheringToolStage(int stage);

}
