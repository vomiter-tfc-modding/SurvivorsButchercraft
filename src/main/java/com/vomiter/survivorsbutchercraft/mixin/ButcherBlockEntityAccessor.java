package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockBlockEntity;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(value = ButcherBlockBlockEntity.class, remap = false)
public interface ButcherBlockEntityAccessor {
    @Invoker("isFinalStage")
    boolean sbtfc$isFinalStage(ButcherBlockRecipe r);

    @Invoker("matchRecipe")
    Optional<RecipeHolder<ButcherBlockRecipe>> sbtfc$matchRecipe();

    @Invoker("setupStage")
    void sbtfc$setupStage(ButcherBlockRecipe recipe, int stage);

    @Invoker("dropLoot")
    void sbtfc$dropLoot(AnimatedRecipeItemUse tool, Player player);

    @Accessor("curTool")
    Ingredient sbtfc$getCurTool();

}
