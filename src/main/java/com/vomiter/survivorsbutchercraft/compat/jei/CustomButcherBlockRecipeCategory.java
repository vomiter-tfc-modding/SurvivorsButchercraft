package com.vomiter.survivorsbutchercraft.compat.jei;

import com.lance5057.butchercraft.ButchercraftItems;
import com.lance5057.butchercraft.workstations.bases.recipes.AnimatedRecipeItemUse;
import com.vomiter.survivorsbutchercraft.SurvivorsButchercraft;
import com.vomiter.survivorsbutchercraft.common.recipe.CompoundChanceResult;
import com.vomiter.survivorsbutchercraft.common.recipe.CustomButcherBlockRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class CustomButcherBlockRecipeCategory implements IRecipeCategory<CustomButcherBlockRecipe> {
    public static final RecipeType<CustomButcherBlockRecipe> TYPE = RecipeType.create(SurvivorsButchercraft.MODID, "butcher_block", CustomButcherBlockRecipe.class);
    private final Component localizedName = Component.translatable("Butchercraft.jei.butcherblock");
    private final IDrawable icon;

    public CustomButcherBlockRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack((ItemLike)ButchercraftItems.BUTCHER_BLOCK_BLOCK_ITEM.get()));
    }

    public RecipeType<CustomButcherBlockRecipe> getRecipeType() {
        return TYPE;
    }

    public Component getTitle() {
        return this.localizedName;
    }

    public int getWidth() {
        return 144;
    }

    public int getHeight() {
        return 144;
    }

    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, CustomButcherBlockRecipe recipe, IFocusGroup focuses) {
        int count = recipe.getButcherBlockRecipe().tools().size();
        int offset = 2;
        int width = 16 + offset;
        int placementH = 0;
        int height = 16 + offset;
        int placementW = 0;
        int c = 0;
        builder.addSlot(RecipeIngredientRole.INPUT, this.getWidth() / 2 - 8, 40).addIngredients(recipe.getButcherBlockRecipe().carcass());

        for(AnimatedRecipeItemUse a : recipe.getButcherBlockRecipe().tools()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 1 + placementW, 1 + placementH).addIngredients(a.tool());
            placementW += width;
            ++c;
            if (c > 7) {
                placementH += height;
                placementW = 0;
                c = 0;
            }
        }

        c = 0;
        placementW = 0;
        placementH = 0;

        for (int i = 0; i < count; i++) {
            var result = recipe.getResults(i);
            for (CompoundChanceResult compoundChanceResult : result) {
                if (compoundChanceResult.hasItem()) builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + placementW, 73 + placementH + 18)
                        .addItemStack(compoundChanceResult.getStack());
                if (compoundChanceResult.hasFluid()) {
                    builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + placementW, 73 + placementH + 18)
                            .addFluidStack(compoundChanceResult.getFluid().getFluid(), compoundChanceResult.getFluid().getAmount());
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

    public void draw(CustomButcherBlockRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(ResourceLocation.fromNamespaceAndPath("butchercraft", "textures/gui/jei.png"), 0, 0, 108, 78, 144, 144);
    }
}
