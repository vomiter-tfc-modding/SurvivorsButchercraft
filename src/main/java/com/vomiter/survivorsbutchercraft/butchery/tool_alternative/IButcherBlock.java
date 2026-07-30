package com.vomiter.survivorsbutchercraft.butchery.tool_alternative;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Optional;

public interface IButcherBlock {
    Ingredient sbtfcInterface$getCurTool();
    ItemStack sbtfcInterface$getInserted();
    int sbtfcInterface$getStage();
    Optional<? extends Recipe<?>> sbtfcInterface$matchRecipe();
    void sbtfcInterface$dropLoot(AnimatedRecipeItemUse recipeToolsIn, Player player);

}
