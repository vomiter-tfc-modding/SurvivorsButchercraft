package com.vomiter.survivorsbutchercraft.mixin.compat;

import com.lance5057.butchercraft.integration.jei.categories.ButcherBlockRecipeCategory;
import com.lance5057.butchercraft.workstations.butcherblock.ButcherBlockRecipe;
import com.llamalad7.mixinextras.sugar.Local;
import com.vomiter.survivorsbutchercraft.common.recipe.CompoundChanceResult;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomButcherBlockRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Collectors;

@Mixin(value = ButcherBlockRecipeCategory.class, remap = false)
public class ButcherBlockRecipeCategoryMixin {
    @Inject(method = "setRecipe(Lmezz/jei/api/gui/builder/IRecipeLayoutBuilder;" +
            "Lcom/lance5057/butchercraft/workstations/butcherblock/ButcherBlockRecipe;" +
            "Lmezz/jei/api/recipe/IFocusGroup;)V",
            at = @At("TAIL"),
    require = 0)
    private void sbtfc$setRecipe(
            IRecipeLayoutBuilder builder,
            ButcherBlockRecipe recipe,
            IFocusGroup focuses,
            CallbackInfo ci,
            @Local(name = "c") int c,
            @Local(name = "placementW") int placementW,
            @Local(name = "placementH") int placementH
    ){
        int offset = 2;
        int width = 16 + offset;
        int height = 16 + offset;

        if(recipe instanceof CustomButcherBlockRecipe custom){
            int stages = custom.getRecipeToolsIn().size();
            for (int i = 0; i < stages; i++) {
                for(CompoundChanceResult result : custom.getResults(i).stream().filter(chanceResult -> !chanceResult.isEmpty()).collect(Collectors.toSet())) {

                    var slot = builder.addSlot(
                            RecipeIngredientRole.OUTPUT,
                            1 + placementW,
                            73 + placementH + 18);

                    if (!result.hasFluid()) slot.addItemStack(result.getStack())
                            .addRichTooltipCallback((view, tooltip) -> {
                                if(result.getChance() >= 1) return;
                                tooltip.add(
                                        Component.literal("chance: ")
                                                .append(Component.literal(String.valueOf(result.getChance() * 100f)))
                                                .append(Component.literal("%"))
                                                .withStyle(ChatFormatting.GOLD));
                            });
                    else {
                        slot.addFluidStack(result.getFluid().getFluid(), result.getFluid().getAmount());
                    }


                    placementW += width;
                    ++c;
                    if (c > 7) {
                        placementH += height;
                        placementW = 0;
                        c = 0;
                    }
                }
            }

        }

    }
}
