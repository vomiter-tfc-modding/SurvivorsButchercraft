package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.workstations.hook.HookRecipe;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(MeatHookBlockEntity.class)
public interface MeatHookBlockEntityAccessor {
    @Invoker("isFinalStage")
    boolean sbtfc$isFinalStage(HookRecipe r);

    @Invoker("matchRecipe")
    Optional<HookRecipe> sbtfc$matchRecipe();

    @Invoker("setupStage")
    void sbtfc$setupStage(HookRecipe recipe, int stage);
}
