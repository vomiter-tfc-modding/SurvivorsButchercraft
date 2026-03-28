package com.vomiter.survivorsbutchercraft.mixin;

import com.lance5057.butchercraft.client.BlacklistedModel;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.lance5057.butchercraft.workstations.hook.MeatHookBlockEntity;
import com.lance5057.butchercraft.workstations.hook.MeatHookRenderer;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.vomiter.survivorsbutchercraft.client.CarcassRenderHelper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = MeatHookRenderer.class, remap = false)
public class MeatHookRendererMixin {

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/lance5057/butchercraft/workstations/hook/MeatHookBlockEntity;getCurrentTool()Ljava/util/Optional;"
            )
    )
    private Optional<AnimatedRecipeItemUse> sb$wrapCurrentTool(Optional<AnimatedRecipeItemUse> original, MeatHookBlockEntity pBlockEntity) {
        if (original.isEmpty()) {
            return original;
        }

        ItemStack carcass = pBlockEntity.getInsertedItem();
        if (carcass.isEmpty()) {
            return original;
        }

        AnimatedRecipeItemUse base = original.get();

        BlacklistedModel[] resolvedModels = base.model.stream()
                .map(model -> CarcassRenderHelper.resolveVariant(model, carcass))
                .toArray(BlacklistedModel[]::new);

        AnimatedRecipeItemUse wrapped = new AnimatedRecipeItemUse(base, resolvedModels);
        return Optional.of(wrapped);
    }
}