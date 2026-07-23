package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(value = MeatHookBlockEntity.class, remap = false)
public interface MeatHookBlockEntityAccessor {
    @Invoker("isFinalStage")
    boolean sbtfc$isFinalStage(HookRecipe r);

    @Invoker("matchRecipe")
    Optional<HookRecipe> sbtfc$matchRecipe();

    @Invoker("setupStage")
    void sbtfc$setupStage(HookRecipe recipe, int stage);

    @Invoker("dropLoot")
    void sbtfc$dropLoot(AnimatedRecipeItemUse tool, Player player);

    @Accessor("curTool")
    Ingredient sbtfc$getCurTool();
}
